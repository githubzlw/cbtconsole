package com.cbt.warehouse.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 工厂打分
 * @ClassName SupplierScoring 
 * @Description TODO
 * @author Administrator
 * @date 2018年2月13日 上午10:59:03
 */
public class SupplierScoringBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String shopId;//工厂
	private String shopUrl;//
	private double qualityAvg;//质量平均分
	private double serviceAvg;//服务平均分
	private String level;//级别 :两个平均分都大于四为优质  0:黑名单,1:合作过的供应商,3,优选供应商
	
	private String address;//供应商所在的城市
	private int inventoryAgreement;//是否有库存;==0没有,1有
	private int returnDays;//人为可修改的供应商科退货天数
	private Date createTime;
	private Date updateTime;
	private String category;
	private int noSupplier;
	private int sell;  // 店铺内销量
	/**
	 * 店铺售卖金额
	 */
	private double shopPrice;

	public double getShopPrice() {
		return shopPrice;
	}

	public void setShopPrice(double shopPrice) {
		this.shopPrice = shopPrice;
	}

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public int getNoSupplier() {
		return noSupplier;
	}

	public void setNoSupplier(int noSupplier) {
		this.noSupplier = noSupplier;
	}


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	private String shopType;
	private String authorizedFlag;

	public String getAuthorizedFlag() {
		return authorizedFlag;
	}

	public void setAuthorizedFlag(String authorizedFlag) {
		this.authorizedFlag = authorizedFlag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String type;
	public int getAllcounts() {
		return allcounts;
	}

	public void setAllcounts(int allcounts) {
		this.allcounts = allcounts;
	}

	private int allcounts;
	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	private int counts;
	
	
	
	
	public int getReturnDays() {
		return returnDays;
	}
	public void setReturnDays(int returnDays) {
		this.returnDays = returnDays;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getInventoryAgreement() {
		return inventoryAgreement;
	}
	public void setInventoryAgreement(int inventoryAgreement) {
		this.inventoryAgreement = inventoryAgreement;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public String getShopUrl() {
		return shopUrl;
	}
	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}
	public double getQualityAvg() {
		return qualityAvg;
	}
	public void setQualityAvg(double qualityAvg) {
		this.qualityAvg = qualityAvg;
	}
	public double getServiceAvg() {
		return serviceAvg;
	}
	public void setServiceAvg(double serviceAvg) {
		this.serviceAvg = serviceAvg;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	
}
