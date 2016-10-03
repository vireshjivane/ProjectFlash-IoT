package com.sjsu.flash.alerts

/**
  * Created by vjivane on 4/13/16.
  */
object EmailTest extends App {

  println("Start...")

  Email.initializeEmailSession()
  Email.sendEmail("vireshjivane5@gmail.com","Admin","Flash Infrastructure Alert","This is a perminutedata-spark-format-large-minute-t-test.csv email from system. Thanks")

  println("Complete...")

}
