package com.cbt.bean;

import java.util.Date;

public class buyGoods {
        private Integer id;
        private String url;
        private Integer goodId;
        private String category;
        private Integer count;
        private String name;
        private String imgurl;
        private String price;
        private String title;
        private Integer buysum;
        private String mOrder;
        private String pUnit;
        private Date updateTime;
        private Date createTime;
        private Integer valid;
        private Double zongjia;
		private String saleNums;//销售数量
        private String buyprices;//采购价格
        private String buySums;//采购总金额
        private String buycount;//采购总数
        private String goods_pid;
        public String getGoods_pid() {
			return goods_pid;
		}
		public void setGoods_pid(String goods_pid) {
			this.goods_pid = goods_pid;
		}
		public String getSaleNums() {
			return saleNums;
		}
		public void setSaleNums(String saleNums) {
			this.saleNums = saleNums;
		}
		public String getBuyprices() {
			return buyprices;
		}
		public void setBuyprices(String buyprices) {
			this.buyprices = buyprices;
		}
		public String getBuySums() {
			return buySums;
		}
		public void setBuySums(String buySums) {
			this.buySums = buySums;
		}
		public String getBuycount() {
			return buycount;
		}
		public void setBuycount(String buycount) {
			this.buycount = buycount;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public Integer getGoodId() {
			return goodId;
		}
		public void setGoodId(Integer goodId) {
			this.goodId = goodId;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public Integer getCount() {
			return count;
		}
		public void setCount(Integer count) {
			this.count = count;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getImgurl() {
			return imgurl;
		}
		public void setImgurl(String imgurl) {
			this.imgurl = imgurl;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Integer getBuysum() {
			return buysum;
		}
		public void setBuysum(Integer buysum) {
			this.buysum = buysum;
		}
		public String getmOrder() {
			return mOrder;
		}
		public void setmOrder(String mOrder) {
			this.mOrder = mOrder;
		}
		public String getpUnit() {
			return pUnit;
		}
		public void setpUnit(String pUnit) {
			this.pUnit = pUnit;
		}
	    
		public Date getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public Integer getValid() {
			return valid;
		}
		public void setValid(Integer valid) {
			this.valid = valid;
		}
		public Double getZongjia() {
			return zongjia;
		}
		public void setZongjia(Double zongjia) {
			this.zongjia = zongjia;
		}
		
        
}
