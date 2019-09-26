import java.io.File

import org.apache.spark.ml.classification.{DecisionTreeClassifier, LinearSVC, NaiveBayes, NaiveBayesModel}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{HashingTF, IDF}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

import scala.util.Random

object textClassify {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("SA")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    spark.sparkContext.setLogLevel("WARN") ///日志级别
    import spark.implicits._
    val rand = new Random()
    val neg = spark.read.textFile("/root/data/neg.txt").map(
      line => {
        (line.split(" ").filter(!_.equals(" ")).filter(!_.equals(":")).filter(!_.equals(",")).filter(!_.equals(".")),
          0, rand.nextDouble())
      }).toDF("words", "value", "random")
    val pos = spark.read.textFile("/root/data/pos.txt").map(
      line => {
        (line.split(" ").filter(!_.equals(" ")).filter(!_.equals(":")).filter(!_.equals(",")).filter(!_.equals(".")), 1, rand.nextDouble())
      }).toDF("words", "value", "random")

    val data = neg.union(pos).sort("random")
    //data.show(false)
    //println(neg.count(),data.count())//合并
    println(neg.count(), data.count())
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
//    val Array(training, test) = transformedData
//      .randomSplit(Array(0.8, 0.2))
    //    val svm = new LinearSVC().setMaxIter(20).setRegParam(0.1)
    //      .setFeaturesCol("tfidf").setLabelCol("value")
    //    val model = svm.fit(training)
    //    val result = model.transform(test)
    //    result.show()
    //    model.save("/root/model/svm")
      val bayes=NaiveBayesModel.load("/root/model/model1")
    //根据抽取到的文本特征，使用分类器进行分类，这是一个二分类问题
    //分类器是可替换的
//    val bayes = new NaiveBayes()
//      .setFeaturesCol("tfidf") //X
//      .setLabelCol("value") //y
//      .fit(training)
//    bayes.save("/root/model/model1")

    val result = bayes.transform(transformedData) //交叉验证
    result.show(false)

    //对模型的准确率进行评估
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("value")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(result)
    println(s"""accuracy is $accuracy""")

  }

}
