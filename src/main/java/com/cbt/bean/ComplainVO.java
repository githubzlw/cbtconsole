package com.cbt.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class ComplainVO extends Complain {
	//ComplainFile
	private int cfid;
	private int cfcomplainid;
	private String cfimgUrl;
	
	//ComplainChat
	private int ccchatid;
	private int ccchaAdminId;
	private String ccchatAdmin;
	private String ccchatText;
	private Date ccchatTime;
	private int ccflag;
	
	private String admName; //销售管理
	
	private int counts;//未回复客户投诉个数
	private int customSum;//客户的消息数量
	private int salerSum;//销售的消息数量

	//Refund
	private int rid;
	private double rappcount;
	private String rcurrency;
	protected String rcurrencyShow;
	private String imgUrl;
	
	private List<String> orderIdList;
	
	private List<Map<String,String>> disputeList;//申诉集合
	
	public List<Map<String, String>> getDisputeList() {
		return disputeList;
	}

	public void setDisputeList(List<Map<String, String>> disputeList) {
		this.disputeList = disputeList;
	}


	public List<String> getOrderIdList() {
		return orderIdList;
	}

	public void setOrderIdList(List<String> orderIdList) {
		this.orderIdList = orderIdList;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	public int getCustomSum() {
		return customSum;
	}

	public void setCustomSum(int customSum) {
		this.customSum = customSum;
	}

	public int getSalerSum() {
		return salerSum;
	}

	public void setSalerSum(int salerSum) {
		this.salerSum = salerSum;
	}

	public int getCcchaAdminId() {
		return ccchaAdminId;
	}

	public void setCcchaAdminId(int ccchaAdminId) {
		this.ccchaAdminId = ccchaAdminId;
	}

	public int getCcchatid() {
		return ccchatid;
	}

	public Date getCcchatTime() {
		return ccchatTime;
	}

	public void setCcchatTime(Date ccchatTime) {
		this.ccchatTime = ccchatTime;
	}

	public void setCcchatid(int ccchatid) {
		this.ccchatid = ccchatid;
	}
	

	public String getCcchatText() {
		return ccchatText;
	}

	public void setCcchatText(String ccchatText) {
		this.ccchatText = ccchatText;
	}

	public int getCfid() {
		return cfid;
	}

	public void setCfid(int cfid) {
		this.cfid = cfid;
	}

	public int getCfcomplainid() {
		return cfcomplainid;
	}

	public void setCfcomplainid(int cfcomplainid) {
		this.cfcomplainid = cfcomplainid;
	}

	public String getCfimgUrl() {
		return cfimgUrl;
	}

	public void setCfimgUrl(String cfimgUrl) {
		this.cfimgUrl = cfimgUrl;
	}

	public String getCcchatAdmin() {
		return ccchatAdmin;
	}

	public void setCcchatAdmin(String ccchatAdmin) {
		this.ccchatAdmin = ccchatAdmin;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public double getRappcount() {
		return rappcount;
	}

	public void setRappcount(double rappcount) {
		this.rappcount = rappcount;
	}

	public String getRcurrency() {
		return rcurrency;
	}

	public void setRcurrency(String rcurrency) {
		this.rcurrency = rcurrency;
	}

	public String getRcurrencyShow() {
		return rcurrencyShow;
	}

	public void setRcurrencyShow(String rcurrencyShow) {
		this.rcurrencyShow = rcurrencyShow;
	}

	public int getCcflag() {
		return ccflag;
	}

	public void setCcflag(int ccflag) {
		this.ccflag = ccflag;
	}

	public String getAdmName() {
		return admName;
	}

	public void setAdmName(String admName) {
		this.admName = admName;
	}

	
	
}
