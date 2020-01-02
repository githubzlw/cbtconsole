package com.cbt.website.bean;

import java.io.Serializable;

public class ShopManagerPojo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String shop_id;//店铺ID
	
	private String shop_name;//店铺名称
	
	private String shop_url;//店铺链接
	
	private String admuser;//操作人
	
	private String createtime;//店铺插入时间
	
	private String updatetime;//最后修改时间
	
	private String remark;//店铺状态  0：自动禁用 1：自动全免 2人工解禁 3 人工全免   4系统无法判断
	
	private String flag;//店铺产品是否下载完成  0：未完成  1已完成
	
	private String system_evaluation;//系统是否判断过该店铺   0未判断 1判断
	
	private String operation;
	
	private String imgs;
	
	private String goods_pid;
	
	private String goods_name;
	
	private String goods_p_url;
	
	private String status;
	
	private String antiKey;//反关键词
	private int antiId;//反关键词id
	

	public int getAntiId() {
		return antiId;
	}

	public void setAntiId(int antiId) {
		this.antiId = antiId;
	}

	public String getAntiKey() {
		return antiKey;
	}

	public void setAntiKey(String antiKey) {
		this.antiKey = antiKey;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	private String enName;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private String keyword;
	private String category;
	private String shopId;
	private String orderid;
	private String username;
	private String totalprice;
	private String orderdate;

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	private String minPrice;
	private String maxPrice;
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_p_url() {
		return goods_p_url;
	}

	public void setGoods_p_url(String goods_p_url) {
		this.goods_p_url = goods_p_url;
	}

	
	public String getGoods_pid() {
		return goods_pid;
	}

	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_url() {
		return shop_url;
	}

	public void setShop_url(String shop_url) {
		this.shop_url = shop_url;
	}

	public String getAdmuser() {
		return admuser;
	}

	public void setAdmuser(String admuser) {
		this.admuser = admuser;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSystem_evaluation() {
		return system_evaluation;
	}

	public void setSystem_evaluation(String system_evaluation) {
		this.system_evaluation = system_evaluation;
	}

}
