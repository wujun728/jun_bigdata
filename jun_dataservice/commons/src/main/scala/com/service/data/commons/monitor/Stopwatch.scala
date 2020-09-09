package com.service.data.commons.monitor

class Stopwatch {
  private val start = System.currentTimeMillis()

  override def toString: String = s"${System.currentTimeMillis() - start} ms."
}
