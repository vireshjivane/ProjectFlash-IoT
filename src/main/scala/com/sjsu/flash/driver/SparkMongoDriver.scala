package com.sjsu.flash.driver

import com.mongodb.casbah.{WriteConcern => MongodbWriteConcern}
import com.sjsu.flash.configuraiton.SparkConfiguration
import com.stratio.datasource._
import com.stratio.datasource.mongodb._
import com.stratio.datasource.mongodb.schema._
import com.stratio.datasource.mongodb.writer._
import com.stratio.datasource.mongodb.config._
import com.stratio.datasource.mongodb.config.MongodbConfig._
import org.apache.spark.sql.SQLContext
import com.stratio.datasource.util.Config._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils

/**
  * Created by vjivane on 5/5/16.
  */
object SparkMongoDriver {

  def main(args: Array[String]) {

    val builder = MongodbConfigBuilder(Map(Host -> List("52.11.76.17:27017"), Database -> "flash", Collection ->"raw", SamplingRatio -> 1.0, WriteConcern -> "normal"))
    val readConfig = builder.build()

    SparkConfiguration.initializeSpark("mongo","local[4]")

    val sc = SparkConfiguration.getConfiguredSpark

    val sqlContext = new SQLContext(sc)

    val mongoRDD = sqlContext.fromMongoDB(readConfig)
    mongoRDD.registerTempTable("raw")
    //.foreach(row => println(row.get(4)))

      val parsedData = sqlContext.sql("SELECT * FROM raw").toDF().map { row =>

      LabeledPoint(row.get(1).toString.toDouble,
        MLUtils.appendBias(Vectors.dense(Array(row.get(2).toString.toDouble, row.get(3).toString.toDouble, row.get(6).toString.toDouble))))}.cache()


    parsedData.foreach(println)

  }



}
