package com.dfy.utils

import java.io.File

import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.LinearSVCModel
import org.apache.spark.ml.feature.{HashingTF, IDF}
import org.apache.spark.sql.SparkSession

import scala.util.Random

object SVM {
  def svm(string:String): String = {
    val conf = new SparkConf().setMaster("local").setAppName("SA")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    spark.sparkContext.setLogLevel("WARN") ///日志级别
    val str=spark.sparkContext.parallelize(Array(string))
    str.saveAsTextFile("/root/data/text.txt")
    import spark.implicits._
    val rand = new Random()
    val data = spark.read.textFile("/root/data/text.txt").map(
      line => {
        (line.split(" ").filter(!_.equals(" ")).filter(!_.equals(":")).filter(!_.equals(",")).filter(!_.equals(".")),
          0, rand.nextDouble())
      }).toDF("words", "value", "random")
    dirDel(new File("/root/data/text.txt"))
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
    val model=LinearSVCModel.load("/root/model/svm")//加载模型
    val result = model.transform(transformedData)
    result.show(false)
    result.map(_.getAs("rawPrediction").toString()).collect()(0).concat("@").concat(result.map(_.getAs("prediction")
      .toString).collect()(0))//返回
  }

  def dirDel(path: File) {
    if (!path.exists())
      return
    else if (path.isFile()) {
      path.delete()
      println(path + ":  文件被删除")
      return
    }
  }
}
