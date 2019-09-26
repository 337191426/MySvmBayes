package com.dfy.utils;

import java.io.Serializable;

/**
 * http请求返回的对象
 */

public class ResultVO implements Serializable {


    /**错误码*/
    private Integer code;

    /**提示信息*/
    private String msg;

    /**具体内容*/
    private String prediction;

    private String rawPrediction;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getRawPrediction() {
        return rawPrediction;
    }

    public void setRawPrediction(String rawPrediction) {
        this.rawPrediction = rawPrediction;
    }
}
