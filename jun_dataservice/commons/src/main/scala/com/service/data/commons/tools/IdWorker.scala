package com.service.data.commons.tools

/**
  * @author 伍鲜
  *
  *         Twitter的分布式自增ID算法Snowflake
  */
class IdWorker {
  private var workerId = 0L
  private var datacenterId = 0L
  private var sequence = 0L

  private val twepoch = 1288834974657L

  private val workerIdBits = 5L
  private val datacenterIdBits = 5L
  private val maxWorkerId = -1L ^ (-1L << workerIdBits)
  private val maxDatacenterId = -1L ^ (-1L << datacenterIdBits)
  private val sequenceBits = 12L

  private val workerIdShift = sequenceBits
  private val datacenterIdShift = sequenceBits + workerIdBits
  private val timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits
  private val sequenceMask = -1L ^ (-1L << sequenceBits)

  private var lastTimestamp = -1L

  def this(workerId: Long, datacenterId: Long) {
    this()
    if (workerId > maxWorkerId || workerId < 0) throw new IllegalArgumentException(s"worker Id can't be greater than ${maxWorkerId} or less than 0")
    if (datacenterId > maxDatacenterId || datacenterId < 0) throw new IllegalArgumentException(s"datacenter Id can't be greater than ${maxDatacenterId} or less than 0")
    this.workerId = workerId
    this.datacenterId = datacenterId
  }

  def nextId(): Long = {
    var timestamp = timeGen
    if (timestamp < lastTimestamp) throw new RuntimeException(s"Clock moved backwards.  Refusing to generate id for ${lastTimestamp - timestamp} milliseconds")
    if (lastTimestamp == timestamp) {
      sequence = (sequence + 1) & sequenceMask
      if (sequence == 0) timestamp = tilNextMillis(lastTimestamp)
    } else {
      sequence = 0L
    }
    lastTimestamp = timestamp
    ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence
  }

  protected def tilNextMillis(lastTimestamp: Long): Long = {
    var timestamp = timeGen
    while ( {
      timestamp <= lastTimestamp
    }) timestamp = timeGen
    timestamp
  }

  protected def timeGen: Long = System.currentTimeMillis
}
