package com.cbt.util;

/**搜索+产品配置常量参数
 * @author Administrator
 *
 */
public class SearchConfig {
	/**
	 * 搜索页面每页产品数量
	 */
	public static final int PAGE_SIZE = 60;
	
	/**
	 * 搜索排序:销量排序
	 */
	public static final String SEARCH_SORT_ORDER_DESC = "order-desc";
	/**
	 * 搜索排序:价格降序
	 */
	public static final String SEARCH_SORT_PRICE_DESC = "bbPrice-desc";
	/**
	 * 搜索排序:价格升序
	 */
	public static final String SEARCH_SORT_PRICE_ASC = "bbPrice-asc";
	/**
	 * 默认网站
	 */
	public static final String SEARCH_WEBSITE = "a";
	/**
	 * 默认搜索接口
	 */
	public static final String SEARCH_SOURCE = "goodslist";
	
	/**
	 * 货源检查:有货
	 */
	public static final int GOODS_STOCK_ON = 1;
	/**
	 * 货源检查:无货
	 */
	public static final int GOODS_STOCK_OFF = 0;
	/**
	 * 产品单页统一国内订单处理时间为5天
	 */
	public static final String PROCESS_TIME = "5";
	/**
	 * 产品免邮
	 */
	public static final String GOODS_FREE = "1";
	/**
	 * 产品数据来源--数据库表sql
	 */
	public static final String DATA_SOURCE_SQL = "sql";
	/**
	 * 产品数据来源--在线抓取web
	 */
	public static final String DATA_SOURCE_WEB = "web";
	/**
	 * 产品下架标志:产品单页可显示基本数据，但不能购买
	 */
	public static final int GOODS_VALID_STATE_AVALIBLE = 0;
	/**
	 * 产品下架标志:产品单页显示Oops, the product has been taken off shelf
	 */
	public static final int GOODS_VALID_STATE_OFF = 6;
	/**
	 * 产品上架
	 */
	public static final int GOODS_VALID_STATE_ON = 1;
	/**
	 * 永不下架
	 */
	public static final int GOODS_VALID_STATE_LONG = 2;
	/**
	 * 产品无法运输
	 */
	public static final int GOODS_VALID_STATE_NOT_SHIPPING = 4;
	/**
	 * 产品默认售卖单位
	 */
	public static final String GOODS_UNIT = "piece";
	/**
	 * 产品默认货币单位
	 */
	public static final String CURRENCY = "$";
	/**
	 * 默认快递
	 */
	public static final String SHIPPING_METHOD = "ePacket";
	/**
	 * 默认快递时间
	 */
	public static final String SHIPPING_TIME = "9-15";
	
	/**
	 * 产品类型:aliexpress商品
	 */
	public static final String ALIEXPRESS_PRODUCT = "A";
	/**
	 * 产品类型:亚马逊商品
	 */
	public static final String AMAZON_PRODUCT = "M";
	
	/**
	 * 产品类型:自定义上传商品
	 */
	public static final String UPLOAD_PRODUCT = "D";
	
	/**
	 * 产品类型:新品云商品
	 */
	public static final String NEWCLOUD_PRODUCT = "N";
	
	/**
	 * 产品类型:图片搜索商品
	 */
	public static final String IMAGE_SEARCH_PRODUCT = "I";
	
	/**
	 * 产品类型:WHOLESALER商品
	 */
	public static final String WHOLESALER_PRODUCT = "W";
	
	
	/**
	 * 产品类型:货源替换产品
	 */
	public static final String CHANGE_PRODUCT = "T";
	
	/**
	 * 产品类型:销售生成订单用商品
	 */
	public static final String IMPORT_EXPRESS_PRODUCT = "E";
	
	/**
	 * 产品单页:0-aliexpress 1-货源替换商品 2-1688上传商品，3-亚马逊商品  4-图片搜索商品 5-新品云
	 */
	public static final int IS_ALIEXPRESS_SOURCE = 0;
	public static final int IS_CHANGE_SOURCE = 1;
	public static final int IS_UPLOAD_SOURCE = 2;
	public static final int IS_AMAZON_SOURCE = 3;
	public static final int IS_IMAGE_SOURCE = 4;
	public static final int IS_NEWCLOUD_SOURCE = 5;
//	/**
//	 *搜索页面:商品类型:0-aliexpress  1-1688 2-amzon
//	 */
//	public static final int IS_ALIEXPRESS_CUSTOM = 0;
//	public static final int IS_UPLOAD_CUSTOM = 1;
//	public static final int IS_AMAZON_CUSTOM = 2;
	
	
	
	
}
