package com.sjsu.flash.utils

import java.io.PrintWriter

import scala.io.Source

/**
  * Created by vjivane on 4/25/16.
  */
object DataPreparator extends App{

  val writer = new PrintWriter("dataset-hour-and-minutes-individual.csv", "UTF-8")
  Source.fromFile("/Users/vjivane/Desktop/01-Int-elligentsia/blink-stars-project-repo/blink-stars/Flash/iot-data.csv").getLines().foreach(entry => formatandwrite(entry))
  writer.close()

  def formatandwrite(line: String) ={
    val values = line.split(",")
    writer.println(values(5)+","+ values(6)+"|"+values(7)+"|"+values(3) + "|" + values(4))
  }
}


//year,month,day,hour,minute,in_temp,in_humudity,out_temp,timestamp

//year,month,day,hour,minute,in_temp,in_humudity,out_temp,timestamp