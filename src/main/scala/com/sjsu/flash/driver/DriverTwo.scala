package com.sjsu.flash.driver

import com.sjsu.flash.configuraiton.SparkConfiguration
import com.sjsu.flash.properties.PropertiesManager
import org.apache.spark.SparkConf
import org.apache.spark.mllib.evaluation.RegressionMetrics
import org.apache.spark.mllib.feature.StandardScaler
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.util.MLUtils

object DriverTwo extends App {

  System.setProperty("archaius.fixedDelayPollingScheduler.delayMills", "500000")
  PropertiesManager.initializePropertiesManager("flash-properties", "flash.config")
  val sparkConf = new SparkConf().setAppName(PropertiesManager.spark_app_name).setMaster(PropertiesManager.spark_app_master)

  SparkConfiguration.initializeSpark("mlapp", "local[4]")
  val sc = SparkConfiguration.getConfiguredSpark
  val data = sc.textFile("dataset-hour-and-minutes-individual.csv")
  val Array(training, test) = data.randomSplit(Array(0.75, 0.25))

  val parsedData = training.map { line =>
    val parts = line.split(',')
    LabeledPoint(parts(0).toDouble, MLUtils.appendBias(Vectors.dense(parts(1).split('|').map(_.toDouble))))}.cache()

  val parsedTestData = test.map { line =>
    val parts = line.split(',')
    LabeledPoint(parts(0).toDouble, MLUtils.appendBias(Vectors.dense(parts(1).split('|').map(_.toDouble))))
  }.cache()

  val scaler = new StandardScaler(withMean = true, withStd = true)
    .fit(parsedData.map(x => x.features))

  val scaledData = parsedData
    .map(x =>
      LabeledPoint(x.label,
        scaler.transform(Vectors.dense(x.features.toArray))))

  val scaledTestData = parsedTestData
    .map(x =>
      LabeledPoint(x.label,
        scaler.transform(Vectors.dense(x.features.toArray))))


  val numIterations = 200

  val algorithm = new LinearRegressionWithSGD()
  algorithm.setIntercept(true)
  algorithm.optimizer
    .setNumIterations(numIterations)

  val model = algorithm.run(scaledData)

  val valuesAndPreds = scaledTestData.map { point =>
    val prediction = model.predict(point.features)
    (point.label.round.toDouble, prediction.round.toDouble)
  }

  println("Metrics: Model Weights => " + model.weights)



  valuesAndPreds.foreach(entry => { println(s"Actual Value: ${entry._1} \t Predicted Value: ${entry._2}")})

  val MSE = valuesAndPreds.map { case (v, p) => math.pow((v - p), 2) }.mean()
  val testMetrics = new RegressionMetrics(valuesAndPreds)
  println("Metrics: Training Dataset Count => " + training.count())
  println("Metrics: Test Dataset Count => " + test.count())
  println("Metrics: Model Intercept => " + model.intercept)
  println("Metrics: RMSE => " + testMetrics.rootMeanSquaredError)
  println("Metrics: MSE => " + testMetrics.meanSquaredError)
  println("Metrics: MAE => " + testMetrics.meanAbsoluteError)
  println("Metrics: R2 => " + testMetrics.r2.abs)
  sc.stop()
}