package com.sjsu.flash.utils

import com.sjsu.flash.configuraiton.SparkConfiguration

object WordCount extends App{
  SparkConfiguration.initializeSpark("flash","local[4]")
  Some(SparkConfiguration.getConfiguredSpark).get.textFile(getClass.getResource("/input.txt").toString).
    flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey(_ + _).foreach(println) }