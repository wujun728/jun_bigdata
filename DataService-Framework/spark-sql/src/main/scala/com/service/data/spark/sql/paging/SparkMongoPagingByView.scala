package com.service.data.spark.sql.paging

import com.mongodb.client.MongoCollection
import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql.MongoMapFunctions
import com.mongodb.spark.{MongoConnector, MongoSpark}
import com.service.data.commons.utils.CommUtil
import com.service.data.spark.sql.utils.MongoUtil
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.bson.types.ObjectId
import org.bson.{BsonDocument, BsonObjectId, Document}

import scala.beans.BeanProperty
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Try

/**
  * @author 伍鲜
  *
  *         MongoSpark分页加载MongoDB的数据
  *         不支持自定义排序，因为：
  *         1、如果数据量过大，需要给排序字段创建索引；
  *         2、自定义排序不利于分页边界的确定。
  */
class SparkMongoPagingByView private() {
  /**
    * SparkSession
    */
  private var sparkSession: SparkSession = _

  /**
    * 空Document
    */
  private val emptyDocument: Document = new Document()
  private val objectDocument: Document = new Document("_id", 1)
  private val viewSuffix = "PageTempView"

  /**
    * MongoSpark ReadConfig
    */
  @BeanProperty var readConfig: ReadConfig = _
  private var viewConfig: ReadConfig = _

  /**
    * 分页大小，默认10000
    */
  @BeanProperty var pageSize: Int = 10000

  /**
    * 查询条件
    *
    * 例如：
    * 1、根据某个字段值查询：
    *    Document.parse("{key : value}")
    * 或者
    * new Document(key, value)
    * 2、根据多个字段值查询：
    *    Document.parse("{key1 : value1, key2 : value2, ..., keyn : valuen}")
    * 或者
    * new Document(key1, value1).append(key2, value2)....append(keyn, valuen)
    * 3、同时满足多个条件：
    *    Document.parse("{ $and : [{key1 : value1},{key2 : value2}]}")
    * 或者
    *    Document.parse("{ $or : [{key1 : value1},{key2 : value2}]}")
    * 或者
    *    Document.parse("{ $nor : [{key1 : value1},{key2 : value2}]}")
    */
  @BeanProperty var queryDocument: Document = new Document()

  /**
    * 筛选字段：仅返回需要的字段
    */
  @BeanProperty var projectFields: Seq[String] = Seq()

  /**
    * 当前分页的objectId下限（不包含）
    */
  @BeanProperty var minimumObjectId: String = _

  def this(sparkSession: SparkSession) {
    this()
    this.sparkSession = sparkSession
    this.readConfig = ReadConfig(sparkSession)
    this.viewConfig = ReadConfig(Map(ReadConfig.collectionNameProperty -> s"${readConfig.collectionName}${viewSuffix}", ReadConfig.partitionerProperty -> "MongoSinglePartitioner"), Some(ReadConfig(sparkSession)))
  }

  def this(sparkSession: SparkSession, collection: String) {
    this()
    this.sparkSession = sparkSession
    this.readConfig = ReadConfig(Map(ReadConfig.collectionNameProperty -> collection), Some(ReadConfig(sparkSession)))
    this.viewConfig = ReadConfig(Map(ReadConfig.collectionNameProperty -> s"${collection}${viewSuffix}", ReadConfig.partitionerProperty -> "MongoSinglePartitioner"), Some(ReadConfig(sparkSession)))
  }

  def this(sparkSession: SparkSession, database: String, collection: String) {
    this()
    this.sparkSession = sparkSession
    this.readConfig = ReadConfig(Map(ReadConfig.databaseNameProperty -> database, ReadConfig.collectionNameProperty -> collection), Some(ReadConfig(sparkSession)))
    this.viewConfig = ReadConfig(Map(ReadConfig.collectionNameProperty -> s"${collection}${viewSuffix}", ReadConfig.partitionerProperty -> "MongoSinglePartitioner"), Some(ReadConfig(sparkSession)))
  }

  /**
    * 构建查询条件的 ArrayBuffer
    *
    * @param minimumObjectId
    * @return
    */
  private def buildQueryDocumentBuffer(minimumObjectId: String): ArrayBuffer[Document] = {
    val queryBuffer = new ArrayBuffer[Document]()
    if (minimumObjectId != null) {
      queryBuffer.append(Document.parse("""{ "_id" : { $gt : ObjectId("""" + minimumObjectId + """")}}"""))
    } else {
      MongoConnector(readConfig).withCollectionDo(readConfig, { collection: MongoCollection[Document] =>
        queryBuffer.append(Document.parse("""{ "_id" : { $gte : ObjectId("""" + collection.find().sort(new Document("_id", 1)).limit(1).iterator().next().get("_id").asInstanceOf[ObjectId].toHexString + """")}}"""))
      })
    }
    if (queryDocument != null && queryDocument.size() > 0) {
      queryBuffer.append(queryDocument)
    }
    queryBuffer
  }

  /**
    * 构建筛选条件
    *
    * @return
    */
  private def buildProjectionDocument(): Document = {
    if (CommUtil.isNotEmpty(projectFields)) {
      val projection = new Document("_id", 1)
      projectFields.foreach(projection.put(_, 1))
      projection
    } else {
      emptyDocument
    }
  }

  /**
    * 获取下一页数据
    *
    * @return
    */
  def nextPage(): DataFrame = {
    val pipelineBuffer = new ArrayBuffer[Document]()
    // 根据查询条件
    val queryBuffer = buildQueryDocumentBuffer(minimumObjectId)
    if (queryBuffer.size > 0) {
      pipelineBuffer.append(Document.parse("""{ $match : { $and : [""" + queryBuffer.map(_.toJson()).mkString(",") + """] } }"""))
    }
    // 仅查询指定字段
    if (projectFields != null && projectFields.size > 0) {
      pipelineBuffer.append(new Document("$project", buildProjectionDocument()))
    }
    // ID字段升序排序
    pipelineBuffer.append(Document.parse("""{ $sort : { "_id" : 1 } }"""))
    // 仅返回指定条数
    pipelineBuffer.append(Document.parse("{ $limit : " + pageSize + " }"))

    MongoUtil.createView(readConfig, viewConfig.collectionName, pipelineBuffer)
    val rdd = MongoSpark.load(sparkSession.sparkContext, viewConfig)

    Try(minimumObjectId = rdd.map(_.get("_id").asInstanceOf[ObjectId].toHexString).max())

    rdd.toDF()
  }
}
