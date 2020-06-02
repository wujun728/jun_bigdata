package com.service.data.spark.sql.utils

import com.service.data.commons.property.ServiceProperty
import com.service.data.commons.utils.CommUtil
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import redis.clients.jedis.{HostAndPort, JedisCluster, JedisPool, JedisPoolConfig, JedisSentinelPool}

import scala.collection.JavaConversions._

/**
  * @author 伍鲜
  *
  *         Redis工具类
  */
object RedisUtil {
  lazy val jedisPoolConfig = {
    val config = new JedisPoolConfig
    config.setMinIdle(5)
    config.setMaxIdle(50)
    config.setMaxTotal(500)
    config.setMaxWaitMillis(5000)
    config
  }

  /**
    * 单机
    */
  lazy val jedisPool: JedisPool = {
    if (CommUtil.isNotEmpty(ServiceProperty.properties.get("spark.redis.password").getOrElse(""))) {
      new JedisPool(jedisPoolConfig,
        ServiceProperty.properties.get("spark.redis.host").get,
        ServiceProperty.properties.get("spark.redis.port").get.toInt,
        ServiceProperty.properties.get("spark.redis.timeout").getOrElse("5000").toInt,
        ServiceProperty.properties.get("spark.redis.password").getOrElse(""))
    } else {
      new JedisPool(jedisPoolConfig,
        ServiceProperty.properties.get("spark.redis.host").get,
        ServiceProperty.properties.get("spark.redis.port").get.toInt,
        ServiceProperty.properties.get("spark.redis.timeout").getOrElse("5000").toInt)
    }
  }

  lazy val jedisClient = jedisPool.getResource

  /**
    * 哨兵
    */
  lazy val jedisSentinelPool: JedisSentinelPool = {
    if (CommUtil.isNotEmpty(ServiceProperty.properties.get("spark.redis.password").getOrElse(""))) {
      new JedisSentinelPool(
        ServiceProperty.properties.get("spark.redis.sentinel.master").get,
        ServiceProperty.properties.get("spark.redis.sentinel.sentinels").get.split(",", -1).toSet[String],
        jedisPoolConfig,
        ServiceProperty.properties.get("spark.redis.timeout").getOrElse("5000").toInt,
        ServiceProperty.properties.get("spark.redis.password").getOrElse(""))
    } else {
      new JedisSentinelPool(
        ServiceProperty.properties.get("spark.redis.sentinel.master").get,
        ServiceProperty.properties.get("spark.redis.sentinel.sentinels").get.split(",", -1).toSet[String],
        jedisPoolConfig,
        ServiceProperty.properties.get("spark.redis.timeout").getOrElse("5000").toInt)
    }
  }

  lazy val jedisSentinelClient = jedisSentinelPool.getResource

  /**
    * 集群
    */
  lazy val jedisCluster: JedisCluster = {
    new JedisCluster(ServiceProperty.properties.get("spark.redis.cluster.hosts").get.split(",", -1).map(x => new HostAndPort(x.split(":")(0), x.split(":")(1).toInt)).toSet,
      ServiceProperty.properties.get("spark.redis.timeout").getOrElse("5000").toInt,
      jedisPoolConfig
    )
  }

  /**
    * 加载Redis中的数据。加载Redis中key为："${tableName}:${keyColumn.value}"的所有数据形成一个DataFrame
    *
    * @param tableName 表名称，即Redis的key前缀
    * @param keyColumn 主键字段，即Redis的key后缀
    * @param spark
    * @return
    */
  def loadFromRedis(tableName: String, keyColumn: String)(implicit spark: SparkSession): DataFrame = {
    spark.read
      .format("org.apache.spark.sql.redis")
      .option("table", tableName)
      .option("key.column", keyColumn)
      .load()
  }

  /**
    * 写入数据到Redis。将DataFrame中的每一行数据转成Hash存放到Redis，Redis的key为："${tableName}:df(keyColumn)"，类型为Hash
    *
    * @param df        需要写入的数据
    * @param tableName 表名称，即Redis的key前缀
    * @param keyColumn 主键字段，即Redis的key后缀
    * @param mode      写入方式
    * @param spark
    */
  def writeToRedis(df: DataFrame, tableName: String, keyColumn: String, mode: SaveMode)(implicit spark: SparkSession): Unit = {
    df.write
      .format("org.apache.spark.sql.redis")
      .option("table", tableName)
      .option("key.column", keyColumn)
      .mode(mode)
      .save()
  }
}