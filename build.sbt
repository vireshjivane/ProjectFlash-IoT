import sbt.Keys._

name := "Flash"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.10.64",
  "org.apache.spark" % "spark-core_2.10" % "1.6.1",
  "org.apache.spark" % "spark-bagel_2.10" % "1.6.1",
  "commons-io" % "commons-io" % "2.4",
  "org.apache.logging.log4j" % "log4j-core" % "2.5",
  "org.apache.logging.log4j" % "log4j-api" % "2.5",
  "org.elasticsearch" % "elasticsearch-spark_2.10" % "2.2.0",
  "org.mongodb" %% "casbah" % "2.6.0",
  "org.reactivemongo" %% "reactivemongo" % "0.11.10",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.1",
  "org.apache.spark" % "spark-streaming_2.10" % "1.6.1",
  "com.sksamuel.elastic4s" % "elastic4s" % "0.90.2.8",
  "org.elasticsearch" % "elasticsearch" % "2.3.1",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "javax.mail" % "mail" % "1.4",
  "com.netflix.archaius" % "archaius-aws" % "0.6.3",
  "org.apache.spark" % "spark-mllib_2.10" % "1.6.1",
//  "com.stratio" % "spark-mongodb" % "0.8.0",
  "com.stratio.datasource" % "spark-mongodb_2.10" % "0.11.1",
  "org.apache.spark" % "spark-sql_2.10" % "1.6.1")

dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
)


