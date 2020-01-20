package com.importExpress.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * *****************************************************************************************
 *
 * @ClassName ResultBean
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2018/11/30 13:18:24
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       13:18:242018/11/30     cjc                       初版
 * ******************************************************************************************
 */
@Data
public class ResultBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int NO_PERMISSION = 2;
    private String msg = "success";
    private int code = SUCCESS;
    private T data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL;
    }
    public ResultBean(int code , T data) {
        super();
        this.code = code;
        this.data = data;
    }
    public ResultBean(String msg, T data, int code) {
        super();
        this.msg = msg;
        this.code = code;
        this.data = data;
    }
}

