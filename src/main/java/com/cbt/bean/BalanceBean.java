package com.cbt.bean;

import java.sql.Date;

public class BalanceBean {
	private Integer userId;//用户id
	private Double money;//金额
	private String remark;//备注
	private String admin;//处理人员
	private Integer type;//类型      0-后台人员奖励补偿   1-paypal申诉
	private Date  createTime;//时间
	private String complainid;//web投诉单号
	private String orderid;//订单号
	private int valid;//状态  0-预处理   1-已处理
	
	private int countTotal;//
	
	
	
	public int getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}

	public BalanceBean() {
		super();
	}

	public BalanceBean(Integer userId, Double money, String remark,
			String admin, Integer type, String complainid, String orderid,
			int valid) {
		super();
		this.userId = userId;
		this.money = money;
		this.remark = remark;
		this.admin = admin;
		this.type = type;
		this.complainid = complainid;
		this.orderid = orderid;
		this.valid = valid;
	}




	public BalanceBean(Integer userId, Double money, String remark, String admin) {
		super();
		this.userId = userId;
		this.money = money;
		this.remark = remark;
		this.admin = admin;
	}
	
	
	
	public BalanceBean(Integer userId, Double money, String remark,
			String admin, String complainid, String orderid) {
		super();
		this.userId = userId;
		this.money = money;
		this.remark = remark;
		this.admin = admin;
		this.complainid = complainid;
		this.orderid = orderid;
	}

	public BalanceBean(Integer userId, Double money, String remark,
			String admin, Integer type) {
		super();
		this.userId = userId;
		this.money = money;
		this.remark = remark;
		this.admin = admin;
		this.type = type;
	}


	public BalanceBean(Integer userId, Double money, String remark,
			String admin, Integer type, Date createTime) {
		super();
		this.userId = userId;
		this.money = money;
		this.remark = remark;
		this.admin = admin;
		this.type = type;
		this.createTime = createTime;
	}



	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public String getComplainid() {
		return complainid;
	}

	public void setComplainid(String complainid) {
		this.complainid = complainid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
	

}
