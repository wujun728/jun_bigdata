package com.service.data.spark.sql.paging

import com.mongodb.client.MongoCollection
import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.{MongoConnector, MongoSpark}
import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.bson.Document
import org.bson.types.ObjectId

import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer

/**
  * @author 伍鲜
  *
  *         MongoSpark分页加载MongoDB的数据
  *         不支持自定义排序，因为：
  *         1、如果数据量过大，需要给排序字段创建索引；
  *         2、自定义排序不利于分页边界的确定。
  */
@deprecated("该方法分页，在MongoDB是多Partition的情况下需要特别注意，容易导致多个Task并行读取数据，造成分页下界无法准确确定，漏读数据。")
class SparkMongoPagingBySpark private() {
  /**
    * 分页大小，默认10000
    */
  @BeanProperty var pageSize: Int = 10000
  /**
    * MongoSpark ReadConfig
    */
  @BeanProperty var readConfig: ReadConfig = _

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

  /**
    * SparkContext
    */
  private var sparkContext: SparkContext = _

  /**
    * 空Document
    */
  private val emptyDocument: Document = new Document()

  def this(spark: SparkSession) {
    this()
    this.sparkContext = spark.sparkContext
    this.readConfig = ReadConfig(spark)
  }

  def this(sparkSession: SparkSession, collection: String) {
    this()
    this.sparkContext = sparkSession.sparkContext
    this.readConfig = ReadConfig(Map(ReadConfig.collectionNameProperty -> collection), Some(ReadConfig(sparkSession)))
  }

  def this(sparkSession: SparkSession, database: String, collection: String) {
    this()
    this.sparkContext = sparkSession.sparkContext
    this.readConfig = ReadConfig(Map(ReadConfig.databaseNameProperty -> database, ReadConfig.collectionNameProperty -> collection), Some(ReadConfig(sparkSession)))
  }

  def this(sparkContext: SparkContext) {
    this()
    this.sparkContext = sparkContext
    this.readConfig = ReadConfig(sparkContext)
  }

  def this(sparkContext: SparkContext, collection: String) {
    this()
    this.sparkContext = sparkContext
    this.readConfig = ReadConfig(Map(ReadConfig.collectionNameProperty -> collection), Some(ReadConfig(sparkContext)))
  }

  def this(sparkContext: SparkContext, database: String, collection: String) {
    this()
    this.sparkContext = sparkContext
    this.readConfig = ReadConfig(Map(ReadConfig.databaseNameProperty -> database, ReadConfig.collectionNameProperty -> collection), Some(ReadConfig(sparkContext)))
  }

  /**
    * 构建查询条件
    *
    * @param minimumObjectId
    * @return
    */
  private def buildQueryDocument(minimumObjectId: String): Document = {
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

    if (queryBuffer.size > 0) {
      Document.parse("""{ $match : { $and : [""" + queryBuffer.map(_.toJson()).mkString(",") + """] } }""")
    } else {
      emptyDocument
    }
  }

  /**
    * 判断是否还有下一页数据
    *
    * @return
    */
  def hasNextPage(): Boolean = {
    val pipelineBuffer = new ArrayBuffer[Document]()
    // 根据查询条件
    val queryBson = buildQueryDocument(minimumObjectId)
    if (queryBson.size() > 0) {
      pipelineBuffer.append(queryBson)
    }
    // 仅查询ID字段
    pipelineBuffer.append(Document.parse("""{ $project : { "_id" : 1 } }"""))
    // ID字段升序排序
    pipelineBuffer.append(Document.parse("""{ $sort : { "_id" : 1 } }"""))
    // 仅返回一条数据
    pipelineBuffer.append(Document.parse("""{ $limit : 1 }"""))
    // 仅判断有无
    MongoSpark.load(sparkContext, readConfig).withPipeline(pipelineBuffer).count() > 0
  }

  /**
    * 获取下一页数据
    *
    * @return
    */
  def nextPage(): DataFrame = {
    val pipelineBuffer = new ArrayBuffer[Document]()
    // 根据查询条件
    val queryBson = buildQueryDocument(minimumObjectId)
    if (queryBson.size() > 0) {
      pipelineBuffer.append(queryBson)
    }
    // 仅查询指定字段
    if (projectFields != null && projectFields.size > 0) {
      pipelineBuffer.append(Document.parse("{ $project : { " + projectFields.map(x => s"${x} : 1").mkString(",") + " }}"))
    }
    // ID字段升序排序
    pipelineBuffer.append(Document.parse("""{ $sort : { "_id" : 1 } }"""))
    // 仅返回指定条数
    pipelineBuffer.append(Document.parse("{ $limit : " + pageSize + " }"))

    // 根据条件加载数据
    val rdd = MongoSpark.load(sparkContext, readConfig).withPipeline(pipelineBuffer)

    // 记录一下当前分页的ID上界，也就是下一个分页的下界
    minimumObjectId = rdd.map(_.get("_id").asInstanceOf[ObjectId].toHexString).max()

    // 返回当前分页的数据
    rdd.toDF()
  }
}
