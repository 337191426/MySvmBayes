package com.dfy.utils;

/**
 * Boot返回值工具类
 */
public class ResultVOUtil {

    public static ResultVO success(String str) {
        ResultVO resultVO = new ResultVO();
        resultVO.setRawPrediction(str.split("@")[0]);
        resultVO.setPrediction(str.split("@")[1]);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }


    public static ResultVO success() {
        return success(null);
    }


    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg("msg");
        return resultVO;
    }

}
