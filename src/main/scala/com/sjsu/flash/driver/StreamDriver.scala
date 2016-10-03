package com.sjsu.flash.driver

import com.sjsu.flash.configuraiton.SparkConfiguration
import com.sjsu.flash.properties.PropertiesManager
import com.sjsu.flash.streamprocessor.StreamProcessor
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamDriver extends App {

  System.setProperty("archaius.fixedDelayPollingScheduler.delayMills","5000")
  PropertiesManager.initializePropertiesManager("flash-properties", "flash.config")
  val sparkConf = new SparkConf().setAppName(PropertiesManager.spark_app_name).setMaster(PropertiesManager.spark_app_master)
  val ssc = new StreamingContext(sparkConf, Seconds(PropertiesManager.kafka_polling))

  val lines = KafkaUtils.createStream(ssc, PropertiesManager.kafka_broker, PropertiesManager.kafka_receivers_group, PropertiesManager.kafka_topics).map(_._2)
  lines.foreachRDD({
    entry => entry.foreach({message =>
    println(message)
    StreamProcessor.process(message)
    })
  })
  val sc = SparkConfiguration.getConfiguredSpark

  println("Exceuting batch...Start")
//  sc.textFile(getClass.getResource("/Users/vjivane/Desktop/01-Int-elligentsia/blink-stars-project-repo/blink-stars/Flash/flash.config").toString).
//    flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey(_ + _).foreach(println)
  println("Exceuting batch...Complete")

  ssc.start()
  ssc.awaitTermination()



}