package com.sjsu.flash.driver

import com.sjsu.flash.configuraiton.SparkConfiguration
import com.sjsu.flash.properties.PropertiesManager
import org.apache.spark.SparkConf
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors


object DriverThree extends App {

  //System.setProperty("archaius.fixedDelayPollingScheduler.delayMills","500000")
  PropertiesManager.initializePropertiesManager("flash-properties", "flash.config")
  val sparkConf = new SparkConf().setAppName(PropertiesManager.spark_app_name).setMaster(PropertiesManager.spark_app_master)

  SparkConfiguration.initializeSpark("mlapp","local[4]")
  val sc = SparkConfiguration.getConfiguredSpark
  val data = sc.textFile("/Users/vjivane/spark/data/mllib/ridge-data/lpsa.data")
  val parsedData = data.map { line =>
    val parts = line.split(',')
    LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
  }

  // Building the model
  val numIterations = 100
  val model = LinearRegressionWithSGD.train(parsedData, numIterations)

  // Evaluate model on training examples and compute training error
  val valuesAndPreds = parsedData.map { point =>
    val prediction = model.predict(point.features)
    (point.label, prediction)
  }

  valuesAndPreds.foreach(println)

  val MSE = valuesAndPreds.map{case(v, p) => math.pow((v - p), 2)}.mean()
  println("training Mean Squared Error = " + MSE)

}