package com.cbt.warehouse.pojo;

public class orderJson {
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
	private Object rows1;
	/**
	 * 查询总数
	 */
	private int total;
	
	/**
	 * 底部统计
	 */
	private Object footer;

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

	public Object getRows1() {
		return rows1;
	}

	public void setRows1(Object rows1) {
		this.rows1 = rows1;
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
}
