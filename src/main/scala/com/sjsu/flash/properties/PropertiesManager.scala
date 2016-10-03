package com.sjsu.flash.properties

import com.netflix.config._
import com.netflix.config.sources.S3ConfigurationSource
import com.sjsu.flash.s3.S3

object PropertiesManager {

  def initializePropertiesManager(bucket: String, key: String): Unit = {
    S3.initializeS3Client()
    var s3ConfigurationClient: Option[S3ConfigurationSource] = None
    s3ConfigurationClient = Some(new S3ConfigurationSource(S3.s3Client.get, bucket, key))
    ConfigurationManager.install(new DynamicConfiguration(s3ConfigurationClient.get, new FixedDelayPollingScheduler()))
    println("Initialized CPU =>" + DynamicPropertyFactory.getInstance().getStringProperty("cpu.max.threshold",null).getValue)
    println("Initialized CPU =>" + DynamicPropertyFactory.getInstance().getStringProperty("memory.max.threshold",null).getValue)
  }

  def f(key: String) = DynamicPropertyFactory.getInstance().getStringProperty(key, null).getValue

  def spark_app_name = f("spark.app.name")

  def spark_app_master = f("spark.app.master")

  def kafka_broker = f("kafka.broker")

  def kafka_receivers_group = f("kafka.receivers.group")

  def kafka_topics = f("kafka.topics").split(",").map((_, kafka_parallelism)).toMap

  def kafka_parallelism = f("kafka.parallelism").toInt

  def kafka_polling   = f("kafka.polling").toInt

  def cpu_max_threshold = f("cpu.max.threshold").toInt

  def memory_max_threshold = f("memory.max.threshold").toInt

  def cpu_min_threshold = f("cpu.min.threshold").toInt

  def memory_min_threshold = f("memory.min.threshold").toInt

  def elasticsearch_host = f("elasticsearch.host")

  def elasticsearch_cluster = f("elasticsearch.cluster")

  def mongodb_host = f("mongodb.host")

  def mongodb_port = f("mongodb.port")

  def mongodb_db = f("mongodb.db")

  def mongodb_collection = f("mongodb.collection")
}