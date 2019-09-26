package com.dfy.utils

import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.LinearSVCModel
import org.apache.spark.ml.feature.{HashingTF, IDF}
import org.apache.spark.sql.SparkSession

object SVM {
  def svm(string:String): String = {
    val conf = new SparkConf().setMaster("local").setAppName("SA")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    spark.sparkContext.setLogLevel("WARN") ///日志级别

    import spark.implicits._
    val data=Seq((string.split(" ").filter(!_.equals(",")).filter(!_.equals(" ")),1)).toDF("words","value")
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
}
