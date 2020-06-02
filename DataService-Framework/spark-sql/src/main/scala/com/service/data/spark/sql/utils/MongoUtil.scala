package com.service.data.spark.sql.utils

import com.mongodb.client.{MongoCollection, MongoDatabase}
import com.mongodb.client.model.{UpdateManyModel, UpdateOptions, WriteModel}
import com.mongodb.spark._
import com.mongodb.spark.config.{ReadConfig, WriteConfig}
import com.mongodb.spark.rdd.MongoRDD
import com.mongodb.spark.sql.MongoMapFunctions
import com.service.data.commons.property.ServiceProperty
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Column, DataFrame, Row, SparkSession}
import org.bson.conversions.Bson
import org.bson.{BsonDocument, Document}

import scala.collection.JavaConversions._
import scala.reflect.ClassTag

/**
  * @author 伍鲜
  *
  *         MongoDB工具类
  */
object MongoUtil {
  /**
    * 加载MongoDB中的数据，会直接加载所有数据
    *
    * @param collection MongoDB的集合名称，目标集合名称
    * @param spark      SparkSession
    * @return 加载所有数据后形成的DataFrame
    */
  def loadFromMongoDB(collection: String)(implicit spark: SparkSession): DataFrame = {
    spark.sparkContext.loadFromMongoDB(ReadConfig(Map("collection" -> collection), Some(ReadConfig(spark)))).toDF()
  }

  /**
    * 根据DataFrame中主键数据加载MongoDB中的相同主键的数据，会直接加载所有数据
    *
    * @param collection MongoDB的集合名称，目标集合名称
    * @param df         DataFrame，根据给定的DataFrame中的主键数据过滤MongoDB中的数据
    * @param keys       主键字段，与MongoDB中记录进行关联的数据主键字段
    * @param spark      SparkSession
    * @return 加载所有数据后形成的DataFrame
    */
  def loadFromMongoDB(collection: String, df: DataFrame, keys: Seq[String])(implicit spark: SparkSession): DataFrame = {
    spark.sparkContext.loadFromMongoDB(ReadConfig(Map("collection" -> collection), Some(ReadConfig(spark))))
      .withPipeline(Seq(Document.parse("{ $match: { $or: " + df.select(keys.map(new Column(_)): _*).toJSON.collectAsList() + "}}")))
      .toDF()
  }

  /**
    * 加载MongoDB中的数据，会直接加载所有数据
    *
    * @param url        MongDB的url
    * @param database   库名
    * @param collection 集合名
    * @param spark
    * @return 加载所有数据后形成的DataFrame
    */
  def readFromMongoDB(url: String = ServiceProperty.properties.getOrElse("spark.mongodb.input.uri", ""),
                      database: String = ServiceProperty.properties.getOrElse("spark.mongodb.input.database", ""),
                      collection: String = ServiceProperty.properties.getOrElse("spark.mongodb.input.collection", ""))
                     (implicit spark: SparkSession): DataFrame = {
    spark.read.format("com.mongodb.spark.sql.DefaultSource")
      .option("url", url)
      .option("database", database)
      .option("collection", collection)
      .load()
  }

  /**
    * 加载MongoRDD，会直接加载所有数据
    *
    * @param readConfig
    * @param sparkSession
    * @return 加载所有数据后形成的RDD
    */
  def toBsonDocumentRDD[T <: Bson :ClassTag](readConfig: ReadConfig, pipeline: Seq[Bson] = Seq())(implicit sparkSession: SparkSession): MongoRDD[T] = {
    MongoSpark.builder()
      .sparkSession(sparkSession)
      .connector(MongoConnector(readConfig))
      .readConfig(readConfig)
      .pipeline(pipeline)
      .build()
      .toRDD[T]()
  }

  def genDocumentByRow(row: Row, cols: Seq[String]): Document = {
    val doc = new Document()
    cols.foreach(col => {
      doc.put(col, row.get(row.fieldIndex(col)))
    })
    doc
  }

  def genDocumentByDocument(row: BsonDocument, cols: Seq[String]): Document = {
    val doc = new Document()
    cols.foreach(col => {
      doc.put(col, row.get(col))
    })
    doc
  }

  /**
    * 将DataFrame转换为RDD[BsonDocument]
    *
    * @param df
    * @param extendedBsonTypes
    * @return
    */
  def convertToDocument(df: DataFrame, extendedBsonTypes: Boolean): RDD[BsonDocument] = {
    convertToDocument(df.rdd, df.schema, extendedBsonTypes)
  }

  /**
    * 将RDD[Row]转换为RDD[BsonDocument]
    *
    * @param rdd
    * @param schema
    * @param extendedBsonTypes
    * @return
    */
  def convertToDocument(rdd: RDD[Row], schema: StructType, extendedBsonTypes: Boolean): RDD[BsonDocument] = {
    val mapper = MongoMapFunctions.rowToDocumentMapper(schema, extendedBsonTypes)
    rdd.map(row => mapper(row))
  }

  /**
    * 创建视图
    *
    * @param readConfig
    * @param viewName
    * @param pipeline
    */
  def createView(readConfig: ReadConfig, viewName: String, pipeline: Seq[_ <: Bson]): Unit = {
    MongoConnector(readConfig.asOptions).withDatabaseDo(readConfig, { database: MongoDatabase =>
      database.getCollection(viewName).drop()
      database.createView(viewName, readConfig.collectionName, pipeline)
    })
  }

  /**
    * 删除集合
    *
    * @param collection
    */
  def dropCollection(collection: String)(implicit spark: SparkSession): Unit = {
    MongoConnector(spark.sparkContext).withDatabaseDo(ReadConfig(spark), { database: MongoDatabase =>
      database.getCollection(collection).drop()
    })
  }

  /**
    * 保存数据到MongoDB
    *
    * @param collection MongoDB的集合名称，目标集合名称
    * @param df         DataFrame，需要写入MongoDB的源数据
    * @param spark
    */
  def saveToMongoDB(collection: String, df: DataFrame)(implicit spark: SparkSession): Unit = {
    MongoSpark.save(df, WriteConfig(Map("collection" -> collection), Some(WriteConfig(spark))))
  }

  /**
    * 更新插入到MongoDB
    *
    * @param collection MongoDB的集合名称，目标集合名称
    * @param df         DataFrame，需要写入MongoDB的源数据
    * @param keys       主键字段，与MongoDB中记录进行关联的数据主键字段
    * @param cols       普通字段，需要写入MongoDB的其他字段
    * @param spark
    */
  def upsertToMongoDB(collection: String, df: DataFrame, keys: Seq[String], cols: Seq[String])(implicit spark: SparkSession): Unit = {
    val rdd = df.rdd.map(row => {
      val keyDoc = genDocumentByRow(row, keys)
      val colDoc = genDocumentByRow(row, cols)
      new UpdateManyModel[Document](keyDoc, new Document("$set", colDoc), new UpdateOptions().upsert(true))
    })
    bulkWriteMongoDB(rdd, WriteConfig(Map("collection" -> collection), Some(WriteConfig(spark))))
  }

  /**
    * Push数据到MongoDB的Array中
    *
    * @param collection MongoDB的集合名称，目标集合名称
    * @param df         DataFrame，需要写入MongoDB的源数据
    * @param keys       主键字段，与MongoDB中记录进行关联的数据主键字段
    * @param pushCols   普通字段，Array中要存放的字段
    * @param pushKey    Push的Array的字段名称
    * @param spark
    */
  def pushToMongoDB(collection: String, df: DataFrame, keys: Seq[String], pushCols: Seq[String], pushKey: String)(implicit spark: SparkSession): Unit = {
    val rdd = df.rdd.map(row => {
      val keyDoc = genDocumentByRow(row, keys)
      val pushDoc = genDocumentByRow(row, pushCols)

      new UpdateManyModel[Document](keyDoc, new Document("$push", pushDoc))
    })
    bulkWriteMongoDB(rdd, WriteConfig(Map("collection" -> collection), Some(WriteConfig(spark))))
  }

  def bulkWriteMongoDB[D: ClassTag](collection: String, rdd: RDD[_ <: WriteModel[D]])(implicit spark: SparkSession): Unit = {
    bulkWriteMongoDB(rdd, WriteConfig(Map("collection" -> collection), Some(WriteConfig(spark))))
  }

  def bulkWriteMongoDB[D: ClassTag](rdd: RDD[_ <: WriteModel[D]], writeConfig: WriteConfig)(implicit spark: SparkSession): Unit = {
    rdd.foreachPartition(x => {
      if (x.nonEmpty) {
        MongoConnector(writeConfig.asOptions).withCollectionDo(writeConfig, { collection: MongoCollection[D] =>
          x.grouped(ServiceProperty.properties.getOrElse("spark.mongodb.bulkwrite.batch.size", "1024").toInt).foreach(batch => {
            collection.bulkWrite(batch.toList)
          })
        })
      }
    })
  }
}
