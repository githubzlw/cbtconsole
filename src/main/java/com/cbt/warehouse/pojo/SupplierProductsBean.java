package com.cbt.warehouse.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商产品对应的打分
 * @ClassName SupplierProducts 
 * @Description TODO
 * @author Administrator
 * @date 2018年2月13日 下午2:52:36
 */
public class SupplierProductsBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String shopId;//工厂
	private String goodsPid;//产品id
	private String goodsImg;//产品图片
	private double quality;//产品评分
	private double service;//服务评分
	private String remarks;//备注
	private String createtime;
	private int userId;
	private String userName;
	
	private String goodsUrl;//产品url
	private Date updateTime;

	public String getQuestion_content() {
		return question_content;
	}

	public void setQuestion_content(String question_content) {
		this.question_content = question_content;
	}

	public String getReply_content() {
		return reply_content;
	}

	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

	private String question_content;
	private String reply_content;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String type;
	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}


	public String getRemotpath() {
		return remotpath;
	}

	public void setRemotpath(String remotpath) {
		this.remotpath = remotpath;
	}

	public String getCustom_main_image() {
		return custom_main_image;
	}

	public void setCustom_main_image(String custom_main_image) {
		this.custom_main_image = custom_main_image;
	}

	private String remotpath;
	private String custom_main_image;
	
	
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getGoodsUrl() {
		return goodsUrl;
	}
	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	
	public String getGoodsPid() {
		return goodsPid;
	}
	public void setGoodsPid(String goodsPid) {
		this.goodsPid = goodsPid;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public double getQuality() {
		return quality;
	}
	public void setQuality(double quality) {
		this.quality = quality;
	}
	public double getService() {
		return service;
	}
	public void setService(double service) {
		this.service = service;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "SupplierProductsBean [id=" + id + ", shopId=" + shopId
				+ ", goodsPid=" + goodsPid + ", goodsImg=" + goodsImg
				+ ", quality=" + quality + ", service=" + service
				+ ", remarks=" + remarks + "]";
	}
	
}
