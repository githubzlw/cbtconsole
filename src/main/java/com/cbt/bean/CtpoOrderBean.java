package com.cbt.bean;

import java.util.List;

/**
 * 确认价格中的订单 bean
 * @author Administrator
 */
public class CtpoOrderBean {
	private int id;//订单 表中 id
	private int goodId;//订单中 商品id
	private String orderNo;//订单号
	private String deliveryTime;//交期时间
	private int yourOrder;//定量
	private String goodsName;//商品名称
	private String goodsPrice;//商品价格 单价
	private String goodsRemark;//商品备注
	private String goodsfreight;//国内运费
	private String goodsImg;//商品图片
	private String goodsUrl;//商品链接 
//	private String productCost;//订单总价格
//	private String productCostTotal;//订单支付总价格
	private List<String> change1;//价格变动
	private List<String> change2;//交期变动
	private List<String> change3;//最新定量
	private List<String> change7;//最新国内运费
	private String change4;//取消
	private boolean isCancel;//取消 true  没有失效 false
	private List<String> change5;//交流信息
	private int purchase_state;//是否确认货源
	private String currency;//货币单位
	private double discount_amount;//优惠金额
	private String img_type;//商品类型选择图片
	private String types;//商品类别
	private double discount_ratio;//混批优惠折扣
	private String state;//主单状态
	private int ropType;//商品问题类型:4.后台取消商品采购,基本上只用4
	private double share_discount; //社交分享折扣金额
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getRopType() {
		return ropType;
	}
	public void setRopType(int ropType) {
		this.ropType = ropType;
	}
	public double getShare_discount() {
		return share_discount;
	}
	public void setShare_discount(double share_discount) {
		this.share_discount = share_discount;
	}
	public CtpoOrderBean(){}
	public CtpoOrderBean(String orderNo,int goodId){
		this.orderNo=orderNo;
		this.goodId=goodId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGoodId() {
		return goodId;
	}
	public void setGoodId(int goodId) {
		this.goodId = goodId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public int getYourOrder() {
		return yourOrder;
	}
	public void setYourOrder(int yourOrder) {
		this.yourOrder = yourOrder;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public String getGoodsUrl() {
		return goodsUrl;
	}
	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public List<String> getChange1() {
		return change1;
	}
	public void setChange1(List<String> change1) {
		this.change1 = change1;
	}
	public List<String> getChange2() {
		return change2;
	}
	public void setChange2(List<String> change2) {
		this.change2 = change2;
	}
	public List<String> getChange3() {
		return change3;
	}
	public void setChange3(List<String> change3) {
		this.change3 = change3;
	}
	public String getChange4() {
		return change4;
	}
	public void setChange4(String change4) {
		this.change4 = change4;
	}
	public boolean isCancel() {
		return isCancel;
	}
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
	public List<String> getChange5() {
		return change5;
	}
	public void setChange5(List<String> change5) {
		this.change5 = change5;
	}
	public void setGoodsRemark(String goodsRemark) {
		this.goodsRemark = goodsRemark;
	}
	public String getGoodsRemark() {
		return goodsRemark;
	}
	public void setChange7(List<String> change7) {
		this.change7 = change7;
	}
	public List<String> getChange7() {
		return change7;
	}
	public void setGoodsfreight(String goodsfreight) {
		this.goodsfreight = goodsfreight;
	}
	public String getGoodsfreight() {
		return goodsfreight;
	}
	public void setPurchase_state(int purchase_state) {
		this.purchase_state = purchase_state;
	}
	public int getPurchase_state() {
		return purchase_state;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrency() {
		return currency;
	}
	public void setDiscount_amount(double discount_amount) {
		this.discount_amount = discount_amount;
	}
	public double getDiscount_amount() {
		return discount_amount;
	}
	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}
	public String getImg_type() {
		return img_type;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getTypes() {
		return types;
	}
	public void setDiscount_ratio(double discount_ratio) {
		this.discount_ratio = discount_ratio;
	}
	public double getDiscount_ratio() {
		return discount_ratio;
	}
}
