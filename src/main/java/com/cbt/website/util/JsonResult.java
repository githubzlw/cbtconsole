package com.cbt.website.util;

import java.io.Serializable;

/**
 * ajax json 返回值包装对象
 *
 * @author Tommy
 */
public class JsonResult implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 671162142354945640L;
    /**
     * 操作结果（成功或失败）
     */
    boolean ok = true;
    /**
     * 操作提示信息
     */
    String message;
    /**
     * 备注
     */
    String comment;
    /**
     * 数据
     */
    Object data;
    Object rows;

    long total;

    String batch;// 批次
    Object allData;

    public static JsonResult success(Object data, int total) {
        JsonResult json = new JsonResult();
        json.setOk(true);
        json.setData(data);
        json.setTotal((long) total);
        return json;
    }

    public static JsonResult success(Object data) {
        return success(data, 0);
    }


    public static JsonResult error(String message) {
        JsonResult json = new JsonResult();
        json.setOk(false);
        json.setMessage(message);
        return json;
    }


    public void setErrorInfo(String message) {
        this.ok = false;
        this.message = message;
    }

    public void setSuccess(String message) {
        this.ok = true;
        this.message = message;
    }


    public void setSuccess(Object rows, long total) {
        this.ok = true;
        this.rows = rows;
        this.total = total;
    }

    public Object getAllData() {
        return allData;
    }

    public void setAllData(Object allData) {
        this.allData = allData;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

}
