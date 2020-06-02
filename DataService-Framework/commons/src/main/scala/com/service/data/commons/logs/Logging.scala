package com.service.data.commons.logs

import org.slf4j.{Logger, LoggerFactory}

/**
  * @author 伍鲜
  *
  *         日志处理
  */
trait Logging {

  lazy val logger: Logger = LoggerFactory.getLogger(getClass.getName.stripSuffix("$"))

  def trace(msg: => String): Unit = if (logger.isTraceEnabled) logger.trace(msg)

  def trace(msg: => String, err: Throwable): Unit = if (logger.isTraceEnabled) logger.trace(msg, err)

  def debug(msg: => String): Unit = if (logger.isDebugEnabled) logger.debug(msg)

  def debug(msg: => String, err: Throwable): Unit = if (logger.isDebugEnabled) logger.debug(msg, err)

  def info(msg: => String): Unit = if (logger.isInfoEnabled) logger.info(msg)

  def info(msg: => String, err: Throwable): Unit = if (logger.isInfoEnabled) logger.info(msg, err)

  def warn(msg: => String): Unit = if (logger.isWarnEnabled) logger.warn(msg)

  def warn(msg: => String, err: Throwable): Unit = if (logger.isWarnEnabled) logger.warn(msg, err)

  def error(msg: => String): Unit = if (logger.isErrorEnabled) logger.error(msg)

  def error(msg: => String, err: Throwable): Unit = if (logger.isErrorEnabled) logger.error(msg, err)
}