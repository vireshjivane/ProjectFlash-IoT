package com.sjsu.flash.db.elasticsearch

import java.io.PrintWriter
import java.util

import com.sjsu.flash.utils.DateParser
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

object ElasticTest extends App{


//
//  Elastic.initializeElastic("54.218.29.243","elasticsearch","logstash-2016.04.24","logs")
//  //Elastic.createIndex("flash","metrics")
//  Elastic.getDocuments(10,"logstash-2016.04.24","logs").foreach(println)

  Elastic.initializeElastic("54.218.29.243","elasticsearch","flash","metrics")
  //Elastic.createIndex("flash","metrics")
  val data = Elastic.getDocuments(10,"logstash-2016.04.24","logs")

  val writer = new PrintWriter("iot-data-latest.csv", "UTF-8")
  data.foreach(entry => writer.println(format(entry)))
  writer.close()


  def format(data: String) ={

    val parser = new JSONParser()
    val json = parser.parse(data).asInstanceOf[JSONObject]
    val inTemp = json.get("inTemp").toString
    val inHumidity = new Integer(json.get("inHumidity").toString)
    val outTemp = new Integer(json.get("outTemp").toString)
    val timestamp = json.get("@timestamp").toString

    val dateData = DateParser.parse(timestamp)

    val sb = new StringBuilder

    sb.append(dateData._1 + "," + dateData._2 + "," + dateData._3 + "," + dateData._4 + "," + dateData._5 + ",")
    sb.append(inTemp.toInt + "," + inHumidity.toInt + "," + outTemp.toInt + ",")
    sb.append(dateData._1 + "" + dateData._2 + "" + dateData._3 + "" + dateData._4 + "" + dateData._5)

   sb.toString()
  }





}
