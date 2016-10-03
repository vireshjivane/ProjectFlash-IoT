//package com.sjsu.flash.driver
//
//import com.mongodb.casbah.MongoClient
//import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.sql.SQLContext
//
//
//trait MongoEnvironmentConfig {
//  val Database = "highschool"
//  val Collection = "students"
//  val MongoHost = "127.0.0.1"
//  val MongoPort = 27017
//  val MongoProvider = "com.stratio.datasource.mongodb"
//}
//
//object MongoExampleFunctions {
//
//  def withSQLContext(block: SQLContext => Unit) = {
//
//    val sparkConf = new SparkConf().
//      setAppName("MongoDFExample").
//      setMaster("local[4]")
//
//    val sc = new SparkContext(sparkConf)
//    try {
//      val sqlContext = new SQLContext(sc)
//      block(sqlContext)
//    } finally {
//      sc.stop()
//    }
//
//  }
//
//  def prepareEnvironment(): MongoClient = {
//    val mongoClient = MongoClient(MongoHost, MongoPort)
//    populateTable(mongoClient)
//    mongoClient
//  }
//
//  def cleanEnvironment(mongoClient: MongoClient) = {
//    cleanData(mongoClient)
//    mongoClient.close()
//  }
//
//  private def populateTable(client: MongoClient): Unit = {
//
//    val collection = client(Database)(Collection)
//    for (a <- 1 to 10) {
//      collection.insert {
//        MongoDBObject("id" -> a.toString,
//          "age" -> (10 + a),
//          "description" -> s"description $a",
//          "enrolled" -> (a % 2 == 0),
//          "name" -> s"Name $a"
//        )
//      }
//    }
//
//    collection.update(QueryBuilder.start("age").greaterThan(14).get, MongoDBObject(("$set", MongoDBObject(("optionalField", true)))), multi = true)
//    collection.update(QueryBuilder.start("age").is(14).get, MongoDBObject(("$set", MongoDBObject(("fieldWithSubDoc", MongoDBObject(("subDoc", MongoDBList("foo", "bar"))))))))
//  }
//
//  private def cleanData(client: MongoClient): Unit = {
//    val collection = client(Database)(Collection)
//    collection.dropCollection()
//  }
//}