package com.cbt.customer.translate;

import java.util.ArrayList;
import java.util.HashMap;

public class GoodsHelp {
		private String title;//网页title
		private String pName;//商品名称
		private String sName;//卖家名称（可能为公司名）
		private String pPriceUnit;//货币单位
		private String pGoodsUnit;//商品单位
		private String minOrder;//最小订单
		private String maxOrder;//最大订单
		private String category;//商品类别
		private ArrayList<String> pcolor;//商品颜色
		private String pFreight;//运费
		private HashMap<String, String> pInfo;//商品详细信息
		private ArrayList<String> pDetail;//商品详细信息
		
		
		public ArrayList<String> getpDetail() {
			return pDetail;
		}
		public void setpDetail(ArrayList<String> pDetail) {
			this.pDetail = pDetail;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getpPriceUnit() {
			return pPriceUnit;
		}
		public void setpPriceUnit(String pPriceUnit) {
			this.pPriceUnit = pPriceUnit;
		}
		public String getpName() {
			return pName;
		}
		public void setpName(String pName) {
			this.pName = pName;
		}
		public String getsName() {
			return sName;
		}
		public void setsName(String sName) {
			this.sName = sName;
		}
		public String getMinOrder() {
			return minOrder;
		}
		public void setMinOrder(String minOrder) {
			this.minOrder = minOrder;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		
		public ArrayList<String> getPcolor() {
			return pcolor;
		}
		public void setPcolor(ArrayList<String> pcolor) {
			this.pcolor = pcolor;
		}
		public HashMap<String, String> getpInfo() {
			return pInfo;
		}
		public void setpInfo(HashMap<String, String> pInfo) {
			this.pInfo = pInfo;
		}
		public String getpFreight() {
			return pFreight;
		}
		public void setpFreight(String sCost) {
			this.pFreight = sCost;
		}
		
		public String getMaxOrder() {
			return maxOrder;
		}
		public void setMaxOrder(String maxOrder) {
			this.maxOrder = maxOrder;
		}
		
		public void setpGoodsUnit(String pGoodsUnit) {
			this.pGoodsUnit = pGoodsUnit;
		}
		public String getpGoodsUnit() {
			return pGoodsUnit;
		}
		@Override
		public String toString() {
			return "GoodsHelp [title=" + title + ", pName=" + pName
					+ ", sName=" + sName + ", pPriceUnit=" + pPriceUnit
					+ ", pGoodsUnit=" + pGoodsUnit + ", minOrder=" + minOrder
					+ ", maxOrder=" + maxOrder + ", category=" + category
					+ ", pcolor=" + pcolor + ", pFreight=" + pFreight
					+ ", pInfo=" + pInfo + ", pDetail=" + pDetail + "]";
		}
		 
	}

