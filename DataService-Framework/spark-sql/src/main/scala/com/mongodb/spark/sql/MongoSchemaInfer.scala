package com.mongodb.spark.sql

import java.util

import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql.types.{BsonCompatibility, ConflictType, SkipFieldType}
import com.service.data.commons.logs.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.analysis.TypeCoercion
import org.apache.spark.sql.types._
import org.bson._

import scala.collection.JavaConverters._

/**
  * @author 伍鲜
  *
  *         MongoSchemaInfer MongoDB数据的Schema推断
  */
object MongoSchemaInfer extends Logging {
  /**
    * 根据指定的RDD，推断RDD的Schema信息
    *
    * @param rdd
    * @param readConfig
    * @return RDD的Schema信息StructType
    */
  def apply(rdd: RDD[BsonDocument], readConfig: ReadConfig): StructType = {
    val rootType: DataType = rdd
      .map(getSchemaFromDocument(_, readConfig))
      .treeAggregate[DataType](StructType(Seq()))(
      compatibleType(_, _, readConfig, nested = false),
      compatibleType(_, _, readConfig, nested = false)
    )
    canonicalizeType(rootType) match {
      case Some(st: StructType) => st
      case _ => StructType(Seq()) // canonicalizeType erases all empty structs, including the only one we want to keep
    }
  }

  private val compatibleDecimalTypes = Seq(ByteType, ShortType, IntegerType, LongType, FloatType, DoubleType)
  private val ByteDecimal = DecimalType(3, 0)
  private val ShortDecimal = DecimalType(5, 0)
  private val IntDecimal = DecimalType(10, 0)
  private val LongDecimal = DecimalType(20, 0)
  private val FloatDecimal = DecimalType(14, 7)
  private val DoubleDecimal = DecimalType(30, 15)

  def getSchemaFromDocument(document: BsonDocument, readConfig: ReadConfig): StructType = {
    val fields = new util.ArrayList[StructField]()
    document.entrySet.asScala.foreach(kv => fields.add(DataTypes.createStructField(kv.getKey, getDataType(kv.getValue, readConfig), true)))
    DataTypes.createStructType(fields)
  }

  def getDataType(bsonValue: BsonValue, readConfig: ReadConfig): DataType = {
    bsonValue.getBsonType match {
      case BsonType.NULL => DataTypes.NullType
      case BsonType.ARRAY => getSchemaFromArray(bsonValue.asArray().asScala, readConfig)
      case BsonType.BINARY => BsonCompatibility.Binary.getDataType(bsonValue.asBinary())
      case BsonType.BOOLEAN => DataTypes.BooleanType
      case BsonType.DATE_TIME => DataTypes.TimestampType
      case BsonType.DOCUMENT => getSchemaFromDocument(bsonValue.asDocument(), readConfig)
      case BsonType.DOUBLE => DataTypes.DoubleType
      case BsonType.INT32 => DataTypes.IntegerType
      case BsonType.INT64 => DataTypes.LongType
      case BsonType.STRING => DataTypes.StringType
      case BsonType.OBJECT_ID => BsonCompatibility.ObjectId.structType
      case BsonType.TIMESTAMP => BsonCompatibility.Timestamp.structType
      case BsonType.MIN_KEY => BsonCompatibility.MinKey.structType
      case BsonType.MAX_KEY => BsonCompatibility.MaxKey.structType
      case BsonType.JAVASCRIPT => BsonCompatibility.JavaScript.structType
      case BsonType.JAVASCRIPT_WITH_SCOPE => BsonCompatibility.JavaScriptWithScope.structType
      case BsonType.REGULAR_EXPRESSION => BsonCompatibility.RegularExpression.structType
      case BsonType.UNDEFINED => BsonCompatibility.Undefined.structType
      case BsonType.SYMBOL => BsonCompatibility.Symbol.structType
      case BsonType.DB_POINTER => BsonCompatibility.DbPointer.structType
      case BsonType.DECIMAL128 =>
        val bigDecimalValue = bsonValue.asDecimal128().decimal128Value().bigDecimalValue()
        val precision = Integer.max(bigDecimalValue.precision(), bigDecimalValue.scale())
        DataTypes.createDecimalType(precision, bigDecimalValue.scale())
      case _ => ConflictType
    }
  }

  def getSchemaFromArray(bsonArray: Seq[BsonValue], readConfig: ReadConfig): DataType = {
    val arrayTypes: Seq[BsonType] = bsonArray.map(_.getBsonType).distinct
    arrayTypes.length match {
      case 0 => SkipFieldType
      case 1 if Seq(BsonType.ARRAY, BsonType.DOCUMENT).contains(arrayTypes.head) => getCompatibleArraySchema(bsonArray, readConfig)
      case 1 => DataTypes.createArrayType(getDataType(bsonArray.head, readConfig), true)
      case _ => getCompatibleArraySchema(bsonArray, readConfig)
    }
  }

  def getCompatibleArraySchema(bsonArray: Seq[BsonValue], readConfig: ReadConfig): DataType = {
    var arrayType: Option[DataType] = Some(SkipFieldType)
    bsonArray.takeWhile({
      case (bsonValue: BsonNull) => true
      case (bsonValue: BsonValue) =>
        val previous: Option[DataType] = arrayType
        arrayType = Some(getDataType(bsonValue, readConfig))
        if (previous.nonEmpty && arrayType != previous) arrayType = Some(compatibleType(arrayType.get, previous.get, readConfig))
        !arrayType.contains(ConflictType)
    })
    arrayType.get match {
      case SkipFieldType => SkipFieldType
      case dataType => DataTypes.createArrayType(dataType, true)
    }
  }

  def compatibleType(t1: DataType, t2: DataType, readConfig: ReadConfig, nested: Boolean = true): DataType = {
    val dataType = TypeCoercion.findTightestCommonType(t1, t2).getOrElse {
      // t1 or t2 is a StructType, ArrayType, or an unexpected type.
      (t1, t2) match {
        // Double support larger range than fixed decimal, DecimalType.Maximum should be enough
        // in most case, also have better precision.
        case (DoubleType, _: DecimalType) | (_: DecimalType, DoubleType) =>
          DoubleType

        case (t1: DecimalType, t2: DecimalType) =>
          val scale = math.max(t1.scale, t2.scale)
          val range = math.max(t1.precision - t1.scale, t2.precision - t2.scale)
          if (range + scale > 38) {
            // DecimalType can't support precision > 38
            DoubleType
          } else {
            DecimalType(range + scale, scale)
          }

        case (StructType(fields1), StructType(fields2)) => compatibleStructType(fields1, fields2, readConfig)
        case (MapType(keyType1, valueType1, valueContainsNull1), MapType(keyType2, valueType2, valueContainsNull2)) =>
          MapType(
            compatibleType(keyType1, keyType2, readConfig),
            compatibleType(valueType1, valueType2, readConfig),
            valueContainsNull1 || valueContainsNull2
          )

        case (StructType(fields), mapType: MapType) => appendStructToMap(fields, mapType, readConfig)
        case (mapType: MapType, StructType(fields)) => appendStructToMap(fields, mapType, readConfig)

        case (ArrayType(elementType1, containsNull1), ArrayType(elementType2, containsNull2)) =>
          ArrayType(compatibleType(elementType1, elementType2, readConfig), containsNull1 || containsNull2)

        // The case that given `DecimalType` is capable of given `IntegralType` is handled in
        // `findTightestCommonTypeOfTwo`. Both cases below will be executed only when
        // the given `DecimalType` is not capable of the given `IntegralType`.
        case (t1: DataType, t2: DecimalType) if compatibleDecimalTypes.contains(t1) => compatibleType(forType(t1), t2, readConfig)
        case (t1: DecimalType, t2: DataType) if compatibleDecimalTypes.contains(t2) => compatibleType(t1, forType(t2), readConfig)

        // SkipFieldType Types
        case (s: SkipFieldType, dataType: DataType) => dataType
        case (dataType: DataType, s: SkipFieldType) => dataType

        // Conflicting types
        case (_, _) => ConflictType
      }
    }
    dataType match {
      case structType: StructType if nested => structTypeToMapType(structType, readConfig)
      case _ => dataType
    }
  }

  def compatibleStructType(fields1: Array[StructField], fields2: Array[StructField], readConfig: ReadConfig): DataType = {
    val newFields = (fields1 ++ fields2).groupBy(field => field.name).map {
      case (name, fieldTypes) =>
        val dataType = fieldTypes.view.map(x => x.dataType).reduce(compatibleType(_, _, readConfig))
        StructField(name, dataType, nullable = true)
    }
    StructType(newFields.toSeq.sortBy(_.name))
  }

  def appendStructToMap(fields: Array[StructField], mapType: MapType, readConfig: ReadConfig): MapType = {
    val valueType = (mapType.valueType +: fields.map(_.dataType)).reduce(compatibleType(_, _, readConfig))
    DataTypes.createMapType(mapType.keyType, valueType, mapType.valueContainsNull)
  }

  def forType(dataType: DataType): DecimalType = dataType match {
    case ByteType => ByteDecimal
    case ShortType => ShortDecimal
    case IntegerType => IntDecimal
    case LongType => LongDecimal
    case FloatType => FloatDecimal
    case DoubleType => DoubleDecimal
  }

  def structTypeToMapType(structType: StructType, readConfig: ReadConfig): DataType = {
    if (readConfig.inferSchemaMapTypesEnabled && structType.fields.length >= readConfig.inferSchemaMapTypesMinimumKeys) {
      structType.fields.map(_.dataType).reduce(compatibleType(_, _, readConfig)) match {
        case ConflictType => structType // ignore conflicting types
        case SkipFieldType => structType // ignore skipfield types
        case dataType: DataType => DataTypes.createMapType(StringType, dataType, true)
        case _ => structType
      }
    } else {
      structType
    }
  }

  def canonicalizeType: DataType => Option[DataType] = {
    case at@ArrayType(elementType, _) => canonicalizeType(elementType).map(dt => at.copy(elementType = dt))
    case StructType(fields) =>
      val canonicalFields = fields.flatMap(field => {
        if (field.name.isEmpty || field.dataType == SkipFieldType) {
          None
        } else {
          if (fieldContainsConflictType(field.dataType)) {
            val start = if (field.dataType.isInstanceOf[ArrayType]) "Array Field" else "Field"
            warn(s"$start '${field.name}' contains conflicting types converting to StringType")
          }
          canonicalizeType(field.dataType).map(dt => field.copy(dataType = dt))
        }
      })
      if (canonicalFields.nonEmpty) {
        Some(StructType(canonicalFields))
      } else {
        // per SPARK-8093: empty structs should be deleted
        None
      }
    case other: ConflictType => Some(StringType)
    case other => Some(other)
  }

  def fieldContainsConflictType(dataType: DataType): Boolean = {
    dataType match {
      case ArrayType(elementType, _) if elementType == ConflictType => true
      case ConflictType => true
      case _ => false
    }
  }
}
