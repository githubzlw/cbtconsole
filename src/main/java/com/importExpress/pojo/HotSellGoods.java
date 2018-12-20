package com.importExpress.pojo;

public class HotSellGoods {
	private String show_name;// 热卖产品显示名称
	private String selling_price;// 1pc价格
	private String wholesale_price_1;// Wholesale显示的价格
	private String wholesale_price_2;// 5pc显示的价格
	private String wholesale_price_3;// 显示wholesale的价格
	private String goods_img;
	private String hot_selling_id;
	private String goods_import_url;// 本网站的商品链接
	private String goods_url;// 商品ali链接
	private String goods_pid;
	private String goods_price;// 产品搜索页的价格 源自custom_benchmark_ready表wprice
	private String price_show;// 用于最终在首页页面显示的价格
	private String goods_unit;// 用于最终在首页页面显示的单位
	private String rangePrice;// 产品搜索页的价格 源自custom_benchmark_ready表range_price
	private String price1688;// 源自custom_benchmark_ready表price
	private String moq;// 最小订量
	private String asin_code;// 亚马逊asin码
	private String amazon_price;// 亚马逊价格
	private String categoryId;
	private double profit_margin;

	//折扣信息
	private int discountId;
	private double discountPercentage;
	private String discountBeginTime;
	private String discountEndTime;
	private int discountSort;

	//评价信息
	private int evaluationId;
	private String evaluationSkuId;
	private int evaluationUserId;
	private String evaluationContent;
	private int evaluationLevel;
	private int evaluationServiceLevel;
	private String evaluationTime;

	private int isNewCloud;

	private String feeprice;
	private int isSoldFlag;
	private String maxPrice;
	private String minPrice;

	public String getPrice1688() {
		return price1688;
	}

	public void setPrice1688(String price1688) {
		this.price1688 = price1688;
	}

	public String getAmazon_price() {
		return amazon_price;
	}

	public void setAmazon_price(String amazon_price) {
		this.amazon_price = amazon_price;
	}

	public String getMoq() {
		return moq;
	}

	public void setMoq(String moq) {
		this.moq = moq;
	}

	public String getAsin_code() {
		return asin_code;
	}

	public void setAsin_code(String asin_code) {
		this.asin_code = asin_code;
	}

	public String getRangePrice() {
		return rangePrice;
	}

	public void setRangePrice(String rangePrice) {
		this.rangePrice = rangePrice;
	}

	public String getGoods_unit() {
		return goods_unit;
	}

	public void setGoods_unit(String goods_unit) {
		this.goods_unit = goods_unit;
	}

	public String getPrice_show() {
		return price_show;
	}

	public void setPrice_show(String price_show) {
		this.price_show = price_show;
	}

	public String getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}

	public String getWholesale_price_3() {
		return wholesale_price_3;
	}

	public void setWholesale_price_3(String wholesale_price_3) {
		this.wholesale_price_3 = wholesale_price_3;
	}

	public String getGoods_pid() {
		return goods_pid;
	}

	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}

	public String getGoods_url() {
		return goods_url;
	}

	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}

	public String getGoods_import_url() {
		return goods_import_url;
	}

	public void setGoods_import_url(String goods_import_url) {
		this.goods_import_url = goods_import_url;
	}

	public String getHot_selling_id() {
		return hot_selling_id;
	}

	public void setHot_selling_id(String hot_selling_id) {
		this.hot_selling_id = hot_selling_id;
	}

	public String getShow_name() {
		return show_name;
	}

	public void setShow_name(String show_name) {
		this.show_name = show_name;
	}

	public String getSelling_price() {
		return selling_price;
	}

	public void setSelling_price(String selling_price) {
		this.selling_price = selling_price;
	}

	public String getWholesale_price_1() {
		return wholesale_price_1;
	}

	public void setWholesale_price_1(String wholesale_price_1) {
		this.wholesale_price_1 = wholesale_price_1;
	}

	public String getWholesale_price_2() {
		return wholesale_price_2;
	}

	public void setWholesale_price_2(String wholesale_price_2) {
		this.wholesale_price_2 = wholesale_price_2;
	}

	public String getGoods_img() {
		return goods_img;
	}

	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}

	public int getIsNewCloud() {
		return isNewCloud;
	}

	public void setIsNewCloud(int isNewCloud) {
		this.isNewCloud = isNewCloud;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public double getProfit_margin() {
		return profit_margin;
	}

	public void setProfit_margin(double profit_margin) {
		this.profit_margin = profit_margin;
	}

	public int getDiscountId() {
		return discountId;
	}

	public void setDiscountId(int discountId) {
		this.discountId = discountId;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public String getDiscountBeginTime() {
		return discountBeginTime;
	}

	public void setDiscountBeginTime(String discountBeginTime) {
		this.discountBeginTime = discountBeginTime;
	}

	public String getDiscountEndTime() {
		return discountEndTime;
	}

	public void setDiscountEndTime(String discountEndTime) {
		this.discountEndTime = discountEndTime;
	}

	public int getDiscountSort() {
		return discountSort;
	}

	public void setDiscountSort(int discountSort) {
		this.discountSort = discountSort;
	}

	public int getEvaluationId() {
		return evaluationId;
	}

	public void setEvaluationId(int evaluationId) {
		this.evaluationId = evaluationId;
	}

	public String getEvaluationSkuId() {
		return evaluationSkuId;
	}

	public void setEvaluationSkuId(String evaluationSkuId) {
		this.evaluationSkuId = evaluationSkuId;
	}

	public int getEvaluationUserId() {
		return evaluationUserId;
	}

	public void setEvaluationUserId(int evaluationUserId) {
		this.evaluationUserId = evaluationUserId;
	}

	public String getEvaluationContent() {
		return evaluationContent;
	}

	public void setEvaluationContent(String evaluationContent) {
		this.evaluationContent = evaluationContent;
	}

	public int getEvaluationLevel() {
		return evaluationLevel;
	}

	public void setEvaluationLevel(int evaluationLevel) {
		this.evaluationLevel = evaluationLevel;
	}

	public int getEvaluationServiceLevel() {
		return evaluationServiceLevel;
	}

	public void setEvaluationServiceLevel(int evaluationServiceLevel) {
		this.evaluationServiceLevel = evaluationServiceLevel;
	}

	public String getEvaluationTime() {
		return evaluationTime;
	}

	public void setEvaluationTime(String evaluationTime) {
		this.evaluationTime = evaluationTime;
	}

	public String getFeeprice() {
		return feeprice;
	}

	public void setFeeprice(String feeprice) {
		this.feeprice = feeprice;
	}

	public int getIsSoldFlag() {
		return isSoldFlag;
	}

	public void setIsSoldFlag(int isSoldFlag) {
		this.isSoldFlag = isSoldFlag;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	@Override
	public String toString() {
		return "HotSellGoods{" +
				"show_name='" + show_name + '\'' +
				", selling_price='" + selling_price + '\'' +
				", goods_img='" + goods_img + '\'' +
				", hot_selling_id='" + hot_selling_id + '\'' +
				", goods_url='" + goods_url + '\'' +
				", goods_pid='" + goods_pid + '\'' +
				", goods_price='" + goods_price + '\'' +
				", price_show='" + price_show + '\'' +
				", goods_unit='" + goods_unit + '\'' +
				", rangePrice='" + rangePrice + '\'' +
				", price1688='" + price1688 + '\'' +
				", moq='" + moq + '\'' +
				", asin_code='" + asin_code + '\'' +
				", amazon_price='" + amazon_price + '\'' +
				", categoryId='" + categoryId + '\'' +
				", maxPrice='" + maxPrice + '\'' +
				", profit_margin=" + profit_margin +
				'}';
	}
}
