package com.importExpress.pojo;

import java.util.Date;

public class StandardGoodsFormDataPO {

	private Integer id;
	// 1688商品id
	private String pid;

	private String wprice;

	private String feeprice;

	// 价格区间
	private String rangePrice;

	private String onlinePrice;

	private String onlineAliPrice;

	// ali产品id
	private String aliPid;

	// ali价格
	private String aliPrice;

	// 1
	private long valid;

	private Date createtime;

	private Integer flag; //'0-默认值；1-优势商品；2-劣势商品；'
	
	private Integer bmFlag; //'0-默认值；1-优势商品；2-劣势商品；'
	
	public Integer getBmFlag() {
		return bmFlag;
	}

	public void setBmFlag(Integer bmFlag) {
		this.bmFlag = bmFlag;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getWprice() {
		return wprice;
	}

	public void setWprice(String wprice) {
		this.wprice = wprice;
	}

	public String getFeeprice() {
		return feeprice;
	}

	public void setFeeprice(String feeprice) {
		this.feeprice = feeprice;
	}

	public String getRangePrice() {
		return rangePrice;
	}

	public void setRangePrice(String rangePrice) {
		this.rangePrice = rangePrice;
	}

	public String getOnlinePrice() {
		return onlinePrice;
	}

	public void setOnlinePrice(String onlinePrice) {
		this.onlinePrice = onlinePrice;
	}

	public String getOnlineAliPrice() {
		return onlineAliPrice;
	}

	public void setOnlineAliPrice(String onlineAliPrice) {
		this.onlineAliPrice = onlineAliPrice;
	}

	public String getAliPid() {
		return aliPid;
	}

	public void setAliPid(String aliPid) {
		this.aliPid = aliPid;
	}

	public String getAliPrice() {
		return aliPrice;
	}

	public void setAliPrice(String aliPrice) {
		this.aliPrice = aliPrice;
	}

	public long getValid() {
		return valid;
	}

	public void setValid(long valid) {
		this.valid = valid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "StandardGoodsFormDataPO [id=" + id + ", pid=" + pid + ", wprice=" + wprice + ", feeprice=" + feeprice
				+ ", rangePrice=" + rangePrice + ", onlinePrice=" + onlinePrice + ", onlineAliPrice=" + onlineAliPrice
				+ ", aliPid=" + aliPid + ", aliPrice=" + aliPrice + ", valid=" + valid + ", createtime=" + createtime
				+ ", flag=" + flag + "]";
	}

}