package com.importExpress.pojo;

import java.util.List;

import lombok.Data;

@Data
public class Product {
	/**
	 * 商品id
	 */
	private String id;
	/**
	 * 商品链接
	 */
	private String url;
	/**
	 * 商品名
	 */
	private String name;
	/**
	 * 商品图片
	 */
	private String image;
	/**
	 * 商品最小订单
	 */
	private String minOrder;
	/**
	 * 已经售出数量
	 */
	private String sold;
	/**
	 * 订量单位
	 */
	private String moqUnit;
	/**
	 *价格后面单位
	 */
	private String priceUnit;
	/**
	 * 订量单位2  100 pieces/lot
	 */
	private String goodsUnit;
	/**
	 * 产品重量
	 */
	private String weight;
	/**
	 * 价格
	 */
	private String price;

	/**
	 * 产品类别
	 */
	private String category;

	/**
	 * 多批量批发价格
	 */
	private List<Price> wholesalePrice;

	/**
	 *多批量批发价格-非免邮中间价格
	 */
	private String wholesaleMiddlePrice;

	/**
	 * 货币单位
	 */
	private String currencySymbol = "USD";
	/**
	 * 商品id
	 */
	private String shopId;

	/**
	 * 1-有视频  0-无视频
	 */
	private int isVideo;//是否有视频

	/**
	 * 商品库存标识  0没有库存  1有库存  hot
	 */
	private int isStock;
}