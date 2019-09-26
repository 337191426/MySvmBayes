package com.dfy.controller

import com.dfy.utils.{BAYESClassify, ResultVOUtil, SVM}
import org.springframework.web.bind.annotation._


@RestController
class ScalaHelloBoot {

  @RequestMapping(value = Array("/sayScalaHello"),method = Array(RequestMethod.GET))
  def sayScalaHello() = {
    "Hello Scala Boot...."
  }
  @RequestMapping(value = Array("/bayes"), method = Array(RequestMethod.GET))
  @ResponseBody
  def bayes(words:String) = {
//    BAYESClassify.close()
    ResultVOUtil.success(BAYESClassify.bayes(words))  // Scala调用已有的Java代码
  }

  @RequestMapping(value = Array("/svm"), method = Array(RequestMethod.GET))
  @ResponseBody
  def svm(words:String) = {
//    SVM.close()
    ResultVOUtil.success(SVM.svm(words))  // Scala调用已有的Java代码
  }

//  @RequestMapping(value = Array("/bayes1"), method = Array(RequestMethod.GET))
//  @ResponseBody
//  def bayes1(words:String) = {
//    ResultVOUtil.success("abcd@0.5")  // Scala调用已有的Java代码
//  }
}
