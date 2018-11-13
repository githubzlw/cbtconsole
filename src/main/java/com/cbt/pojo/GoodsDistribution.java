package com.cbt.pojo;

import java.sql.Timestamp;

public class GoodsDistribution {
   private Integer id;
   
  private String orderid;
   
   private int odid;
   
   private String goodsid;
   
   private int admuserid;
   
   private Timestamp createTime;
   
   private String distributionid;
   
   private String goodsdataid;
   
   private String carUrl;
   
   private String goodscatid;
   
   private String goods_pid;

   public String getGoods_pid() {
	return goods_pid;
}

public void setGoods_pid(String goods_pid) {
	this.goods_pid = goods_pid;
}

public String getGoodscatid() {
	return goodscatid;
}

public void setGoodscatid(String goodscatid) {
	this.goodscatid = goodscatid;
}

public String getCarUrl() {
	return carUrl;
}

public void setCarUrl(String carUrl) {
	this.carUrl = carUrl;
}

public Timestamp getCreateTime() {
	return createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	   
	 
	   
	   public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getOrderid() {
		return orderid;
	}
	
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
	public int getOdid() {
		return odid;
	}
	
	public void setOdid(int odid) {
		this.odid = odid;
	}
	
	public String getGoodsid() {
		return goodsid;
	}
	
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	
	public int getAdmuserid() {
		return admuserid;
	}
	
	public void setAdmuserid(int admuserid) {
		this.admuserid = admuserid;
	}
	
	
	public String getDistributionid() {
		return distributionid;
	}
	
	public void setDistributionid(String distributionid) {
		this.distributionid = distributionid;
	}
	
	public String getGoodsdataid() {
		return goodsdataid;
	}
	
	public void setGoodsdataid(String goodsdataid) {
		this.goodsdataid = goodsdataid;
	}

}
