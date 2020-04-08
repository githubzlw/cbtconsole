package com.importExpress.pojo;

import lombok.Data;
import lombok.NonNull;

@Data
public class CommonResult {
	 /**
     *成功
     */
    public static final int SUCCESS = 200;

    /**
     *失败
     */
    public static final int FAILED = 500;


    private @NonNull int code;

    private String message;

    private Object data;

    private Object rows;

    private int total;



    /**
     * 普通成功返回
     *
     */
    public static CommonResult success() {
        CommonResult commonResult = new CommonResult(SUCCESS);
        commonResult.message = "操作成功";
        return commonResult;
    }

    /**
     * 普通成功返回
     *
     * @param data 获取的数据
     */
    public static CommonResult success(Object data) {
        CommonResult commonResult = new CommonResult(SUCCESS);
        commonResult.message = "操作成功";
        commonResult.data = data;
        return commonResult;
    }

    /**
     * 普通成功返回
     */
    public static CommonResult success(String message,Object data) {
        CommonResult commonResult = new CommonResult(SUCCESS);
        commonResult.message = message;
        commonResult.data = data;
        return commonResult;
    }

    /**
     * 普通失败提示信息
     */
    public static CommonResult failed() {
        CommonResult commonResult = new CommonResult(FAILED);
        commonResult.message = "操作失败";
        return commonResult;
    }

    /**
     * 具体失败提示信息
     * @param message
     * @return
     */
    public static CommonResult failed(String message){
        CommonResult commonResult = new CommonResult(FAILED);
        commonResult.message = message;
        return commonResult;
    }

    public CommonResult() {
    }

    public CommonResult(@NonNull int code) {
        this.code = code;
    }
}
