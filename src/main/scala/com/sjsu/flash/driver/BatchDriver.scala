package com.sjsu.flash.driver

import org.apache.spark.{SparkConf, SparkContext}

object BatchDriver extends App {

  val sparkConf = new SparkConf().setAppName("mlapp").setMaster("local[4]")
  val sc = new SparkContext(sparkConf)










}