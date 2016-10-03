package com.sjsu.flash.db.mongo

import com.mongodb.casbah.commons.MongoDBObject
import com.sjsu.flash.utils.DateParser
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import com.mongodb.casbah.Imports._

import scala.concurrent.{ExecutionContext, Future}

object MongoProcessing {

  MongoConfigurator.initializeClient()
  var rawCollection = MongoConfigurator.getCollection("flash", "raw")
  var hourlyCollection = MongoConfigurator.getCollection("flash", "hourly")
  var dailyCollection = MongoConfigurator.getCollection("flash", "daily")
  var monthlyCollection = MongoConfigurator.getCollection("flash", "monthly")
  var alertsCollection = MongoConfigurator.getCollection("flash", "alerts")

  val yes = true
  val no = false

  def pushRecord(message:String): Unit = {
    import ExecutionContext.Implicits.global
    Future {
      val parser = new JSONParser()
      val json = parser.parse(message).asInstanceOf[JSONObject]
      val ip = json.get("ip").toString
      val cpu = new Integer(json.get("cpu").toString)
      val memory = new Integer(json.get("memory").toString)
      val rec = DateParser.parse(json.get("@timestamp").toString)
      pushMinuteRecord(ip,cpu,memory,rec)
      pushHourRecord(ip,cpu,memory,rec)
      pushDailyRecord(ip,cpu,memory,rec)
      pushMonthlyRecord(ip,cpu,memory,rec)
    }
  }

  def pushMinuteRecord(ip: String, cpu:Integer, memory: Integer, rec: (Int, Int, Int, Int, Int, Int)) = {
    rawCollection.insert(MongoDBObject("ip" -> s"$ip",
      "cpu" -> cpu, "memory" -> memory,
      "year" -> rec._1, "month" -> rec._2, "day" -> rec._3,
      "hour" -> rec._4, "minute" -> rec._5, "second" -> rec._6))
  }

  def pushHourRecord(ip: String, cpu: Int, memory: Int, rec: (Int, Int, Int, Int, Int, Int)) = {
    hourlyCollection.update(MongoDBObject("ip" -> s"$ip", "year" -> rec._1, "month" -> rec._2, "day" -> rec._3, "hour" -> rec._4), $inc("cpu" -> cpu, "memory" -> memory, "write_count" -> 1), yes, no)
  }

  def pushDailyRecord(ip: String, cpu: Int, memory: Int, rec: (Int, Int, Int, Int, Int, Int)) = {
    dailyCollection.update(MongoDBObject("ip" -> s"$ip", "year" -> rec._1, "month" -> rec._2, "day" -> rec._3), $inc("cpu" -> cpu, "memory" -> memory, "write_count" -> 1), yes, no)
  }

  def pushMonthlyRecord(ip: String, cpu: Int, memory: Int, rec: (Int, Int, Int, Int, Int, Int)) = {
    monthlyCollection.update(MongoDBObject("ip" -> s"$ip", "year" -> rec._1, "month" -> rec._2), $inc("cpu" -> cpu, "memory" -> memory, "write_count" -> 1), yes, no)
  }

  def pushAlert(alertType: String, value: Int, ip: String, rec: (Int, Int, Int, Int, Int, Int)) = {
    alertsCollection.insert(MongoDBObject("type" -> s"$alertType", "value" -> value, "ip" -> s"$ip", "year" -> rec._1, "month" -> rec._2, "day" -> rec._3, "hour" -> rec._4, "minute" -> rec._5, "second" -> rec._6))
  }
}
