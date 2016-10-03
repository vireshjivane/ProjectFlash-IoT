package com.sjsu.flash.streamprocessor

import java.util

import com.sjsu.flash.alerts.Email
import com.sjsu.flash.db.elasticsearch.Elastic
import com.sjsu.flash.db.mongo.MongoProcessing
import com.sjsu.flash.properties.PropertiesManager
import com.sjsu.flash.utils.DateParser
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

import scala.concurrent.{ExecutionContext, Future}

object StreamProcessor {

  Elastic.initializeElastic(PropertiesManager.elasticsearch_host, PropertiesManager.elasticsearch_cluster, "flash", "metrics")
  Email.initializeEmailSession()

  def process(message: String): Unit = {
    pushToElasticSearchAndValidateAlerts(message)
    MongoProcessing.pushRecord(message)
  }

  def pushToElasticSearchAndValidateAlerts(message: String): Unit = {
    import ExecutionContext.Implicits.global
    Future {
      val parser = new JSONParser()
      val json = parser.parse(message).asInstanceOf[JSONObject]
      val document = new util.HashMap[String, Object]()
      val ip = json.get("ip").toString
      val cpu = new Integer(json.get("cpu").toString)
      val memory = new Integer(json.get("memory").toString)
      val timestamp = json.get("@timestamp").toString
      validateForAlerts(ip, cpu, memory,timestamp)
      document.put("ip", ip)
      document.put("cpu", cpu)
      document.put("memory", memory)
      document.put("@timestamp", timestamp)
      Elastic.insertDocument(json.get("ip").toString)(document)
    }
  }

  def validateForAlerts(ip: String, cpu: Integer, memory: Integer, timestamp: String) = {
    import ExecutionContext.Implicits.global
    Future {

//      println(s"\n\n\n\n\n\n\n\n\n\n\n\n\n#######################################################\n\n LOG: Current value of CPU Max Threshold is : ${PropertiesManager.cpu_max_threshold} \n")
//      println(s" LOG: Current value of Memory Max Threshold is : ${PropertiesManager.memory_max_threshold} \n\n#######################################################\n\n\n\n\n\n\n\n\n\n\n\n\n")

      cpu match {
        case value: Integer if value >= PropertiesManager.cpu_max_threshold => {
          Email.sendEmail("vireshjivane5@gmail.com", "admin", s"Alert: CPU High [ $cpu ] for $ip", "")
          MongoProcessing.pushAlert("cpu",cpu,ip,DateParser.parse(timestamp))
        }
        case value: Integer if value < PropertiesManager.cpu_min_threshold => {
          Email.sendEmail("vireshjivane5@gmail.com", "admin", s"Alert: CPU Low [ $cpu ] for $ip", "")
//          MongoProcessing.pushAlert("cpu",cpu,ip,DateParser.parse(timestamp))
        }
        case _ =>
      }
      memory match {
        case value: Integer if value >= PropertiesManager.memory_max_threshold => {
          Email.sendEmail("vireshjivane5@gmail.com", "admin", s"Alert: Memory High [ $memory ] for $ip", "")
          MongoProcessing.pushAlert("memory",memory,ip,DateParser.parse(timestamp))
        }
        case value: Integer if value < PropertiesManager.memory_min_threshold => {
          Email.sendEmail("vireshjivane5@gmail.com", "admin", s"Alert: Memory Low [ $memory ] for $ip", "")
//          MongoProcessing.pushAlert("cpu",cpu,ip,DateParser.parse(timestamp))
        }
        case _ =>
      }
    }
  }
}
