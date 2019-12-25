package com.cbt.website.util;

import java.io.Serializable;

public class EasyUiJsonResult implements Serializable {

	private static final long serialVersionUID = 889900237520L;

	/**
	 * 操作提示信息
	 */
	private String message;
	/**
	 * 是否成功
	 */
	private boolean success;

	/**
	 * 数据实体
	 */
	private Object rows;
	/**
	 * 查询总数
	 */
	private int total;
	
	/**
	 * 底部统计
	 */
	private Object footer;


	public void setSuccess(Object rows, int total){
		this.success = true;
		this.rows = rows;
		this.total = total;
	}

	public void setError(String message){
		this.success = false;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	

	public Object getFooter() {
		return footer;
	}

	public void setFooter(Object footer) {
		this.footer = footer;
	}

	@Override
	public String toString() {
		return "EasyUiJsonResult [message=" + message + ", success=" + success + ", total=" + total + "]";
	}

}
