package com.dfy.utils

import java.io.File

import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.NaiveBayesModel
import org.apache.spark.ml.feature.{HashingTF, IDF}
import org.apache.spark.sql.SparkSession

import scala.util.Random

object BAYESClassify {
  def bayes(string:String): String = {
    val conf = new SparkConf().setMaster("local").setAppName("SA")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    spark.sparkContext.setLogLevel("WARN") ///日志级别
    val str=spark.sparkContext.parallelize(Array(string))
    str.saveAsTextFile("/root/data/text")
    import spark.implicits._
    val rand = new Random()
    val data = spark.read.textFile("/root/data/text0.txt").map(
      line => {
        (line.split(" ").filter(!_.equals(" ")).filter(!_.equals(":")).filter(!_.equals(",")).filter(!_.equals(".")),
          0, rand.nextDouble())
      }).toDF("words", "value", "random")
    deleteDir(new File("/root/data/text"))
    //文本特征抽取
    val hashingTf = new HashingTF()
      .setInputCol("words")
      .setOutputCol("hashing")
      .transform(data)
    val idfModel = new IDF()
      .setInputCol("hashing")
      .setOutputCol("tfidf")
      .fit(hashingTf)
    val transformedData = idfModel
      .transform(hashingTf)
    val model=NaiveBayesModel.load("/root/model/bayes")
    val result = model.transform(transformedData)
    result.show(false)
    result.map(_.getAs("rawPrediction").toString()).collect()(0).concat("@").concat(result.map(_.getAs("prediction")
     .toString).collect()(0))
  }

  def deleteDir(dir: File): Unit = {
    val files = dir.listFiles()
    files.foreach(f => {
      if (f.isDirectory) {
        deleteDir(f)
      } else {
        f.delete()
        println("delete file " + f.getAbsolutePath)
      }
    })
    dir.delete()
    println("delete dir " + dir.getAbsolutePath)
  }
}
