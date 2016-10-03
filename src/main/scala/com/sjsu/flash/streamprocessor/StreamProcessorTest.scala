package com.sjsu.flash.streamprocessor

import com.sjsu.flash.properties.PropertiesManager

/**
  * Created by vjivane on 4/16/16.
  */
object StreamProcessorTest extends App {
  println("Start")
  PropertiesManager.initializePropertiesManager("flash-properties", "flash.config")
  val message = "{\"@timestamp\":\"2016-04-16T05:42:42.939Z\",\"ip\":\"172.31.53.71\",\"cpu\":92,\"memory\":21}"
  StreamProcessor.process(message)
  Thread.sleep(2000)
  println("Complete")
}
