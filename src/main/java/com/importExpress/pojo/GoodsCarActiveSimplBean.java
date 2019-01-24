package com.importExpress.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsCarActiveSimplBean implements Serializable{

	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = -8701597635121563713L;
	
	private double categoryDiscountRate;//商品类别折扣率
	private int number;//商品在购物车中的数量 Use
	private String price;//购物车最新价格
	private String price1;//根据数量变化的非免邮价格，平摊非免邮价格
	private double price2;//添加购物车上一次的价格
	private double price3;//price*number
	private String itemId;//产品pid
    private String urlMD5;//产品id和数据来源类型生成的唯一值 Use
    private String bizPriceDiscount;// catid path 类别id树
	private String priceListSize;//判断该商品是否有区间价格的9-13 >0 有  Use
	private String priceList;//该商品的价格区间  Use
	private int groupBuyId;//团购编号，也是团购标识，团购商品不执行降价逻辑:0非团购商品;>0团购商品
	private double gbPrice;//加入购物车时团购价格
	private String comparePrices;//ali的价格 或者是我们造的假数据
	private int shopCount;//店铺商品数量
	private String sessionId;//针对未登录用户，添加购物车时，存入goods_car表，关联sessionId，这块还保留的原因：登陆前后，购物车商品合并时需要用到
	private String types;//商品规格
	private double price4;//购物车划掉的价格，产品单页中的start price
	private String remark;//客户填写的购物车商品备注
	private String skuid_1688;//1688那边的规格id，用来与sku表做关联，从而能校验购物车数据
	private String spec_id;//1688那边的  规格唯一标识，用来做自动下单的
}
