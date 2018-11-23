package com.importExpress.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qiqing
 * Date :17/07/25
 * 购物车商品暂时被弃用的信息实体（不再作为购物车数据保存在redis中）：将精简的部分保存在goods_car_unused表
 * */
public class GoodsCarSaveTableBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5253208867242485827L;
	
	private int userid;
	private int deposit_rate;//折扣率
	private int freight_free;//免运费
	private double freight_upgrade;//升级运费额外增加的费用
	private double extra_freight;//额外运费
	private int goodsdata_id;//商品ID，不懂这个是什么鬼
	private int isFeight;//是否升级运费 1-
	private int isValid=1;//是否失效
	private int isshipping_promote;//是否运输方式是免费升级
	private int isvolume;//是否用体积算运费1-体积重，0-重量
	private String norm_least1;// 最小定量 
	private String norm_most;// 最大定量
	private int offPrice;//优惠折扣-
	private String pWprice;//批发价格
	private double pay_price;//
	private String preferential;//优惠申请的分组号
	private int preshopping;//是否优惠商品
	private String feeprice;//运费（被免去的运费）---现存商品是否只存在非免邮，商品存在免邮价格=1,否则为0（商品批价格是免邮价格=1,否则为0）
	private double shipping_cost = 0;//用户选择的运输方式对应的金额
	private String shipping_express;//运输方式
	private int source_url;//来源网址,1->1688网址->feeprice免邮转非免邮价格减去该字段,否则计算
	private double theproductfrieght;//分担运费
	private String true_shipping;//运费备注
	private double url_number;//同一链接不同规格数量
	private double url_sumprice;//同一链接不同规格总价格
	private String weight;//单个产品重量
	private String free_shopping_company;//免邮快递公司
	private String goods_catid;//商品最小类别ID
	private int cartNumber;//购物车数量
	private String guId;//商品url,type等计算得到的id,可以理解为购物车商品唯一性标志
	private Date createtime;//商品加入购物车时间
	private String userCookieid;
	
	public String getUserCookieid() {
		return userCookieid;
	}
	public void setUserCookieid(String userCookieid) {
		this.userCookieid = userCookieid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getGuId() {
		return guId;
	}
	public void setGuId(String guId) {
		this.guId = guId;
	}
	
	public int getDeposit_rate() {
		return deposit_rate;
	}
	public void setDeposit_rate(int deposit_rate) {
		this.deposit_rate = deposit_rate;
	}
	public int getFreight_free() {
		return freight_free;
	}
	public void setFreight_free(int freight_free) {
		this.freight_free = freight_free;
	}
	public double getFreight_upgrade() {
		return freight_upgrade;
	}
	public void setFreight_upgrade(double freight_upgrade) {
		this.freight_upgrade = freight_upgrade;
	}

	public double getExtra_freight() {
		return extra_freight;
	}
	public void setExtra_freight(double extra_freight) {
		this.extra_freight = extra_freight;
	}
	public int getGoodsdata_id() {
		return goodsdata_id;
	}
	public void setGoodsdata_id(int goodsdata_id) {
		this.goodsdata_id = goodsdata_id;
	}

	public int getIsFeight() {
		return isFeight;
	}
	public void setIsFeight(int isFeight) {
		this.isFeight = isFeight;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
	public int getIsshipping_promote() {
		return isshipping_promote;
	}
	public void setIsshipping_promote(int isshipping_promote) {
		this.isshipping_promote = isshipping_promote;
	}
	public int getIsvolume() {
		return isvolume;
	}
	public void setIsvolume(int isvolume) {
		this.isvolume = isvolume;
	}
	public String getNorm_least1() {
		return norm_least1;
	}
	public void setNorm_least1(String norm_least1) {
		this.norm_least1 = norm_least1;
	}
	public String getNorm_most() {
		return norm_most;
	}
	public void setNorm_most(String norm_most) {
		this.norm_most = norm_most;
	}
	public int getOffPrice() {
		return offPrice;
	}
	public void setOffPrice(int offPrice) {
		this.offPrice = offPrice;
	}
	public String getpWprice() {
		return pWprice;
	}
	public void setpWprice(String pWprice) {
		this.pWprice = pWprice;
	}
	public double getPay_price() {
		return pay_price;
	}
	public void setPay_price(double pay_price) {
		this.pay_price = pay_price;
	}
	public String getPreferential() {
		return preferential;
	}
	public void setPreferential(String preferential) {
		this.preferential = preferential;
	}
	public int getPreshopping() {
		return preshopping;
	}
	public void setPreshopping(int preshopping) {
		this.preshopping = preshopping;
	}
	public String getFeeprice() {
		return feeprice;
	}
	public void setFeeprice(String feeprice) {
		this.feeprice = feeprice;
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
	public int getSource_url() {
		return source_url;
	}
	public void setSource_url(int source_url) {
		this.source_url = source_url;
	}
	public double getTheproductfrieght() {
		return theproductfrieght;
	}
	public void setTheproductfrieght(double theproductfrieght) {
		this.theproductfrieght = theproductfrieght;
	}
	public String getTrue_shipping() {
		return true_shipping;
	}
	public void setTrue_shipping(String true_shipping) {
		this.true_shipping = true_shipping;
	}
	public double getUrl_number() {
		return url_number;
	}
	public void setUrl_number(double url_number) {
		this.url_number = url_number;
	}
	public double getUrl_sumprice() {
		return url_sumprice;
	}
	public void setUrl_sumprice(double url_sumprice) {
		this.url_sumprice = url_sumprice;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getFree_shopping_company() {
		return free_shopping_company;
	}
	public void setFree_shopping_company(String free_shopping_company) {
		this.free_shopping_company = free_shopping_company;
	}
	public String getGoods_catid() {
		return goods_catid;
	}
	public void setGoods_catid(String goods_catid) {
		this.goods_catid = goods_catid;
	}
	public int getCartNumber() {
		return cartNumber;
	}
	public void setCartNumber(int cartNumber) {
		this.cartNumber = cartNumber;
	}
}
