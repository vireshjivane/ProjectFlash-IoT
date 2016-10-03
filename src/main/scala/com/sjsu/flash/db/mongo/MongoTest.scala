package com.sjsu.flash.db.mongo

import java.util

import com.sjsu.flash.utils.DateParser
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

object MongoTest extends App{

  MongoProcessing.pushAlert("cpu",98,"4.5.6.7",DateParser.parse("2016-04-16T05:43:37.941Z"))

  Thread.sleep(2000)
}