package com.cbt.website.util;

/**
 * 通用返回对象
 * @author jack.luo
 */
public class CommonResult {

    /**
     *成功
     */
    public static final int SUCCESS = 200;

    /**
     *失败
     */
    public static final int FAILED = 500;


    private int code;

    private String message;

    private Object data;

    

    public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	

	public CommonResult(int code) {
		super();
		this.code = code;
	}

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


}
