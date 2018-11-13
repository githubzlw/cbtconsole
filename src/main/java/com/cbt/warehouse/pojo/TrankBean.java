package com.cbt.warehouse.pojo;

import java.util.List;

//跟踪运输方式
public class TrankBean {

	private  int id ;
	private  String  orderid ;
	private  String  company; //运输方式
	private  String  expressno; //运单号
	private  int  flag  ; // 1.超过十天没有签收  2.超过三天没有更新状态
	private  String  createtime ; 
	private  String  remarks ;// 包含多个订单
	private  String  admName ; // 销售人员
	private  String  admId;    //销售id
	private  String  context ; //物流站点信息
	private  String  bz;  //备注
	
	private  String  transportcompany ; //运输公司
	
	private  List<TrankBean> tList ;
	private  String   errorInfo  ;  //错误信息提醒
	
	
	
	
	public String getAdmId() {
		return admId;
	}
	public void setAdmId(String admId) {
		this.admId = admId;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	public List<TrankBean> gettList() {
		return tList;
	}
	public void settList(List<TrankBean> tList) {
		this.tList = tList;
	}
	public String getExpressno() {
		return expressno;
	}
	public void setExpressno(String expressno) {
		this.expressno = expressno;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAdmName() {
		return admName;
	}
	public void setAdmName(String admName) {
		this.admName = admName;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}

	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTransportcompany() {
		return transportcompany;
	}
	public void setTransportcompany(String transportcompany) {
		this.transportcompany = transportcompany;
	}
	
	
}
