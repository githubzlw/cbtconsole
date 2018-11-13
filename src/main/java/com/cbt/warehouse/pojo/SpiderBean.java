package com.cbt.warehouse.pojo;

import java.io.Serializable;

/**
 * 购物车信息
 */
public class SpiderBean implements Serializable, Comparable<SpiderBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3798638565309501899L;
	
	private int id;
	private int carId;// 购物车的ID，不存储数据库
	private String guId;// 购物车的ID，不存储数据库
	private String itemId;// 网站中的商品ID
	private String shopId;// 网站中的商店ID
	private int userId;
	private String sessionId;
	private String name;
	private String url;
	private String seller;// 店家名称
	private String price;// 单价
	private int number;// 数量
	private double freight;// 运费
	private String img_url;
	private String types;//规格
	private String remark;
	private String norm_most;// 最大定量
	private String norm_least;// 最小定量 
	private String norm_least1;// 最小定量 
	private String delivery_time;// 交期
	private double total_price;// 某一商店的购买总价
	private String pWprice;//批发价格
	private int cartNumber;//购物车数量
	private String true_shipping;//运费备注
	private int freight_free;//免运费
	private String perWeight;//单位重量+重量单位
	private String seilUnit;//重量计算单位
	private String goodsUnit;//购买商品单位
	private String bulk_volume;//总体积
	private String total_weight;//总重量
	private String width;//体积
	private String weight;//单个产品重量
	private String createtime;//加入时间
	private String goods_email;
	private String free_shopping_company;//免邮快递公司
	private String free_sc_days;//免邮对应快递公司的运输时间
	private int goodsdata_id;//商品ID
	private String preferential;//优惠申请的分组号
	private int deposit_rate;//折扣率
	private String feeprice;//运费（被免去的运费）---现存商品是否只存在非免邮，商品存在免邮价格=1,否则为0（商品批价格是免邮价格=1,否则为0）
	private int state;//购物车状态
	private String currency;//货币单位
	private int goods_class;//商品类别
	private String img_type;//选择的类别折扣
	private double extra_freight;//额外运费
	private int source_url;//来源网址,1->1688网址->feeprice免邮转非免邮价格减去该字段,否则计算
	private int preshopping;//是否优惠商品
	private String goods_catid;//商品最小类别ID
	private double method_feight;//美国运费
	private int isshipping_promote;//是否运输方式是免费升级
	private String goods_url;
	private double url_number;//同一链接不同规格数量
	private double url_sumprice;//同一链接不同规格总价格
	private double price1;//根据数量变化的非免邮价格，平摊非免邮价格
	private double price2;//价格2->原始一件商品的邮费
	private double price3;//价格3->原始工厂价-用来后台修改工厂价，保留原始值的
	private double theproductfrieght;//分担运费
	private double notfreeprice;//非免邮价格
	private double freeprice;//免邮价格
	private int isvolume;//是否用体积算运费1-体积重，0-重量
	private double firstprice;//第一次加入购物车价格>1-22改为原始免邮价
	private int firstnumber;//首次加购物车数量
	private String international_time;//国际处理时间
	private double shipping_cost = 0;//用户选择的运输方式对应的金额
	private String shipping_express;//运输方式
	private double pay_price;//运输方式
	private int countryId;//国家id
	private int offPrice;//优惠折扣-
	private double freight_upgrade;//升级运费额外增加的费用
	private int isFeight;//是否升级运费
	private String aliPosttime;//ali原始运输交期时间
	private int isBattery;//商品是否带电，0-不带电，1带电
	
	public double getFreight_upgrade() {
		return freight_upgrade;
	}

	public void setFreight_upgrade(double freight_upgrade) {
		this.freight_upgrade = freight_upgrade;
	}

	public int getIsFeight() {
		return isFeight;
	}

	public void setIsFeight(int isFeight) {
		this.isFeight = isFeight;
	}
	
	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public double getPay_price() {
		return pay_price;
	}

	public void setPay_price(double pay_price) {
		this.pay_price = pay_price;
	}

	public String getInternational_time() {
		return international_time;
	}

	public void setInternational_time(String international_time) {
		this.international_time = international_time;
	}

	public double getShipping_cost() {
		return shipping_cost;
	}

	public void setShipping_cost(double shipping_cost) {
		this.shipping_cost = shipping_cost;
	}

	public String getShipping_express() {
		return shipping_express;
	}

	public void setShipping_express(String shipping_express) {
		this.shipping_express = shipping_express;
	}

	public String getGoods_url() {
		return goods_url;
	}

	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}

	public String getPerWeight() {
		return perWeight;
	}

	public void setPerWeight(String perWeight) {
		this.perWeight = perWeight;
	}

	public String getSeilUnit() {
		return seilUnit;
	}

	public void setSeilUnit(String seilUnit) {
		this.seilUnit = seilUnit;
	}

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public String getBulk_volume() {
		return bulk_volume;
	}

	public void setBulk_volume(String bulk_volume) {
		this.bulk_volume = bulk_volume;
	}

	public SpiderBean() {
		super();
	}
 
	public SpiderBean(int id, int carId, String itemId, String shopId,
			int userId, String name, String url, String seller, String price,
			int number, double freight, String img_url, String remark, String norm_most, String norm_least,
			String delivery_time,String true_shipping) {
		super();
		this.id = id;
		this.carId = carId;
		this.itemId = itemId;
		this.shopId = shopId;
		this.userId = userId;
		this.name = name;
		this.url = url;
		this.seller = seller;
		this.price = price;
		this.number = number;
		this.freight = freight;
		this.img_url = img_url;
		this.remark = remark;
		this.norm_most = norm_most;
		this.norm_least = norm_least;
		this.delivery_time = delivery_time;
		this.true_shipping = true_shipping;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"carId\":\"%s\", \"itemId\":\"%s\", \"shopId\":\"%s\", \"userId\":\"%s\", \"name\":\"%s\", \"url\":\"%s\", \"seller\":\"%s\", \"price\":\"%s\", \"number\":\"%s\", \"freight\":\"%s\", \"img_url\":\"%s\", \"remark\":\"%s\", \"norm_most\":\"%s\", \"norm_least\":\"%s\", \"delivery_time\":\"%s\", \"total_price\":\"%s\", \"pWprice\":\"%s\",\"freight_free\":%d}",
						id, carId, itemId, shopId, userId, name, url, seller,
						price, number, freight, img_url, remark,
						norm_most, norm_least, delivery_time, total_price, pWprice,freight_free);
	}

	public int getCarId() {
		return carId;
	}
	
	public void setTrue_shipping(String true_shipping) {
		this.true_shipping = true_shipping;
	}
	public String getTrue_shipping() {
		return true_shipping;
	}
	
	public String getNorm_most() {
		return norm_most;
	}
	public void setCartNumber(int cartNumber) {
		this.cartNumber = cartNumber;
	}
	public int getCartNumber() {
		return cartNumber;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSessionId() {
		return sessionId;
	}

	public void setNorm_most(String norm_most) {
		this.norm_most = norm_most;
	}

	public String getNorm_least() {
		return norm_least;
	}

	public void setNorm_least(String norm_least) {
		this.norm_least = norm_least;
	}

	public String getDelivery_time() {
		return delivery_time;
	}

	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopId() {
		return shopId;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getFreight() {
		return freight;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public double getTotal_price() {
		if (total_price == 0) {
			if(price !=null){
				price = price.replaceAll(",", "");
			}else{
				
			}
			return Double.parseDouble(price);
		}
		return total_price;
	}
	public void setpWprice(String pWprice) {
		this.pWprice = pWprice;
	}
	
	public String getpWprice() {
		return pWprice;
	}
	
	public int getFreight_free() {
		return freight_free;
	}
	public void setFreight_free(int freight_free) {
		this.freight_free = freight_free;
	}
	public String getTotal_weight() {
		return total_weight;
	}
	public void setTotal_weight(String total_weight) {
		this.total_weight = total_weight;
	}
	
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getCreatetime() {
		return createtime;
	}
	
	public void setNorm_least1(String norm_least1) {
		this.norm_least1 = norm_least1;
	}
	public String getNorm_least1() {
		return norm_least1;
	}
	public void setGoods_email(String goods_email) {
		this.goods_email = goods_email;
	}
	public String getGoods_email() {
		return goods_email;
	}
	
	public void setFree_shopping_company(String free_shopping_company) {
		this.free_shopping_company = free_shopping_company;
	}
	public String getFree_shopping_company() {
		return free_shopping_company;
	}
	
	public int getGoodsdata_id() {
		return goodsdata_id;
	}
	public void setGoodsdata_id(int goodsdata_id) {
		this.goodsdata_id = goodsdata_id;
	}
	
	public void setFree_sc_days(String free_sc_days) {
		this.free_sc_days = free_sc_days;
	}
	public String getFree_sc_days() {
		return free_sc_days;
	}
	
	public String getPreferential() {
		return preferential;
	}

	public void setPreferential(String preferential) {
		this.preferential = preferential;
	}

	@Override
	public int compareTo(SpiderBean o) {
		if(this.shopId!=null){
			if (this.shopId.compareTo(o.shopId) == 0) {
				return Integer.valueOf(id).compareTo(Integer.valueOf(o.id));
			} else {
				return this.shopId.compareTo(o.shopId);
			}
		}
		return 0;
	}

	public int getDeposit_rate() {
		return deposit_rate;
	}

	public void setDeposit_rate(int deposit_rate) {
		this.deposit_rate = deposit_rate;
	}
	
	public void setGuId(String guId) {
		this.guId = guId;
	}
	public String getGuId() {
		return guId;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getTypes() {
		return types;
	}
	public void setFeeprice(String feeprice) {
		this.feeprice = feeprice;
	}
	public String getFeeprice() {
		return feeprice;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrency() {
		return currency;
	}
	public int getGoods_class() {
		return goods_class;
	}
	public void setGoods_class(int goods_class) {
		this.goods_class = goods_class;
	}
	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}
	public String getImg_type() {
		return img_type;
	}
	public double getExtra_freight() {
		return extra_freight;
	}
	public void setExtra_freight(double extra_freight) {
		this.extra_freight = extra_freight;
	}
	public void setSource_url(int source_url) {
		this.source_url = source_url;
	}
	public int getSource_url() {
		return source_url;
	}
	public void setPreshopping(int preshopping) {
		this.preshopping = preshopping;
	}
	public int getPreshopping() {
		return preshopping;
	}
	public void setGoods_catid(String goods_catid) {
		this.goods_catid = goods_catid;
	}
	public String getGoods_catid() {
		return goods_catid;
	}
 
	public void setMethod_feight(double method_feight) {
		this.method_feight = method_feight;
	}
	public double getMethod_feight() {
		return method_feight;
	}
	
	public int getIsshipping_promote() {
		return isshipping_promote;
	}
	public void setIsshipping_promote(int isshipping_promote) {
		this.isshipping_promote = isshipping_promote;
	}
	public void setUrl_number(Double url_number) {
		this.url_number = url_number;
	}
	public double getUrl_number() {
		return url_number;
	}
	public void setUrl_sumprice(Double url_sumprice) {
		this.url_sumprice = url_sumprice;
	}
	public double getUrl_sumprice() {
		return url_sumprice;
	}

	public double getPrice1() {
		return price1;
	}

	public void setPrice1(double price1) {
		this.price1 = price1;
	}

	public double getPrice2() {
		return price2;
	}

	public void setPrice2(double price2) {
		this.price2 = price2;
	}

	public double getPrice3() {
		return price3;
	}

	public void setPrice3(double price3) {
		this.price3 = price3;
	}
	
	public double getTheproductfrieght() {
		return theproductfrieght;
	}

	public void setTheproductfrieght(double theproductfrieght) {
		this.theproductfrieght = theproductfrieght;
	}
	
	public double getNotfreeprice() {
		return notfreeprice;
	}
	public void setNotfreeprice(double notfreeprice) {
		this.notfreeprice = notfreeprice;
	}
	public double getFreeprice() {
		return freeprice;
	}
	public void setFreeprice(double freeprice) {
		this.freeprice = freeprice;
	}
	public void setIsvolume(int isvolume) {
		this.isvolume = isvolume;
	}
	public int getIsvolume() {
		return isvolume;
	}
	public void setFirstprice(double firstprice) {
		this.firstprice = firstprice;
	}
	public double getFirstprice() {
		return firstprice;
	}
	public void setFirstnumber(int firstnumber) {
		this.firstnumber = firstnumber;
	}
	public int getFirstnumber() {
		return firstnumber;
	}
	public void setOffPrice(int offPrice) {
		this.offPrice = offPrice;
	}
	public int getOffPrice() {
		return offPrice;
	}

	public String getAliPosttime() {
		return aliPosttime;
	}

	public void setAliPosttime(String aliPosttime) {
		this.aliPosttime = aliPosttime;
	}

	public void setIsBattery(int isBattery) {
		this.isBattery = isBattery;
	}
	public int getIsBattery() {
		return isBattery;
	}
}
