package com.sjsu.flash.properties


/**
  * Created by vjivane on 4/14/16.
  */
object PropertyManagerTest extends App {

  System.setProperty("archaius.fixedDelayPollingScheduler.delayMills","10000")

 PropertiesManager.initializePropertiesManager("flash-properties", "flash.config")
  for (i <- 0 to 100 by 1) {
    println("\n############################################\n")
    println(s"Start => $i")
    println(s"CPU Max = > ${PropertiesManager.cpu_max_threshold}")
    println(s"CPU Min = > ${PropertiesManager.cpu_min_threshold}")
    println(s"Memory Max = > ${PropertiesManager.memory_max_threshold}")
    println(s"Memory Min = > ${PropertiesManager.memory_min_threshold}")

    println(PropertiesManager.elasticsearch_cluster)
    println(PropertiesManager.elasticsearch_host)

    println(PropertiesManager.mongodb_host)
    println(PropertiesManager.mongodb_port)
    println(PropertiesManager.mongodb_db)
    println(PropertiesManager.mongodb_collection)

    println(s"Complete => $i")
    println("\n############################################\n")
    Thread.sleep(10000)
  }
}










//  val parser = new JSONParser()
//  val json = parser.parse("{\"timestamp\": \"2016-04-14T19:31:41.000Z\",\"cpu\":89, \"memory\":90}").asInstanceOf[JSONObject]
//  val cpu = json.get("cpu").toString
//  val memory = json.get("memory").toString
//  var timesStamp = json.get("timestamp").toString
//
//  if (timesStamp.contains("T")) timesStamp = timesStamp.replace('T', ' ')
//  if (timesStamp.contains("Z")) timesStamp = timesStamp.replace("Z", "+0000")
//
//  val document = new util.HashMap[String, Object]()
//  document.put("cpu", new Integer(cpu))
//  document.put("memory", new Integer(memory))
//
//  val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
//  val date = format.parse(timesStamp)
//
//
//
//  println(date)
//
//  //document.put("timestamp", new Date(timesStamp))
//
//  println(s"cpu =>$cpu \t memory =>$memory")


