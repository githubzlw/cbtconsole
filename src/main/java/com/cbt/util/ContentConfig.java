package com.cbt.util;

public class ContentConfig {

	//正式图片服务器请求地址
	public static final String DOWNLOAD_URL= "http://img.import-express.com/imgdownload/download?pid=";
	//正式图片服务器请求地址(批量)
	public static final String DOWNLOAD_LIST_URL= "http://img.import-express.com/imgdownload/plist?plist=";
	//正式图片链接
	public static final String IMG_URL= "http://img.import-express.com/importcsvimg/";
	//抓取ali商品项目的路径
	public static final String CRAWL_ALI_URL="http://219.235.6.140:8080/crawlAliGoodsInfo/crawlCtr/getAliGoondsInfo.do?url=";
	
	//抓取ali商品info项目的路径
	public static final String CRAWL_ALI_INFO_URL="http://219.235.6.140:8080/crawlAliGoodsInfo/crawlCtr/getAliInfoUrl.do?infoUrl=";
		
	//图片服务器快照图片下载路径
	public static final String  PICTURE_SERVER_IMG_DOWN ="http://108.61.142.103:8080/cloudimginterface/downLoadCtr/downImg?";
	
	//图片服务器同款1688图片下载路径
	public static final String  PICTURE_SERVER_IMG_1688_DOWN ="http://108.61.142.103:8080/cloudimginterface/downLoadCtr/down1688Img?";
		
	//清理抓取后的程序调用接口
	public static final String  DEAL_1688_GOODS_URL ="http://192.168.1.28:8765/checkimage/clear/sameGoods?";
	
	//图片服务器店铺商品图片下载路径
	public static final String  SHOP_GOODS_IMG_DOWN ="http://108.61.142.103:8080/cloudimginterface/downLoadCtr/downShopImg?";
	
	//图片服务器店铺商品图片打包文件下载路径
	public static final String  SHOP_GOODS_WHOLE_IMG_DOWN ="http://108.61.142.103:8080/cloudimginterface/downLoadCtr/downShopWholeImg?";
	
	//公用的图片服务器商品图片打包文件下载路径
	public static final String  PUBLIC_USE_WHOLE_IMG_DOWN ="http://192.168.1.100:8765/cloudimginterface/downLoadCtr/publicDownWholeImg?";
	
	//品类精研图片上传到服务器
	public static final String  CATEGORY_RESEARCH_IMG_DOWN ="http://108.61.142.103:8080/cloudimginterface/downLoadCtr/publicDownWholeImg?";
	//public static final String  CATEGORY_RESEARCH_IMG_DOWN ="http://127.0.0.1:8080/cloudimginterface/downLoadCtr/downCategoryImg?";
	
	//测试图片服务器请求地址
	//public static final String DOWNLOAD_URL= "http://192.168.1.29:9988/imgdownload/download?pid=";
	//测试图片服务器请求地址(批量)
	//public static final String DOWNLOAD_LIST_URL= "http://192.168.1.29:9988/imgdownload/plist?plist=";
	//测试图片链接
	//public static final String IMG_URL= "http://192.168.1.29:9977/importcsvimg/";
	//抓取ali商品项目的路径
	//public static final String CRAWL_ALI_URL="http://192.168.1.220:8080/crawlAliGoodsInfo/crawlCtr/getAliGoondsInfo.do?url=";
	//测试图片服务器图片下载路径
	//public static final String  PICTURE_SERVER_IMG_DOWN ="http://192.168.1.100:8080/cloudimginterface/downLoadCtr/downImg?";
	
	//图片服务器图片下载路径
	//public static final String  PICTURE_SERVER_IMG_1688_DOWN ="http://192.168.1.100:8765/cloudimginterface/downLoadCtr/down1688Img?";
//	public static final String  PICTURE_SERVER_IMG_1688_DOWN ="http://192.168.1.219:8765/cloudimginterface/downLoadCtr/down1688Img?";
	//清理抓取1688数据后的程序调用接口
	//public static final String  DEAL_1688_GOODS_URL ="http://192.168.1.100:8765/checkimage/clear/sameGoods?";
//	public static final String  DEAL_1688_GOODS_URL ="http://192.168.1.219:8765/checkimage/clear/sameGoods?";
	//图片服务器店铺商品图片下载路径
	//public static final String  SHOP_GOODS_IMG_DOWN ="http://192.168.1.100:8765/cloudimginterface/downLoadCtr/downShopImg?";
	//图片服务器店铺商品图片打包文件下载路径
	//public static final String  SHOP_GOODS_WHOLE_IMG_DOWN ="http://192.168.1.100:8765/cloudimginterface/downLoadCtr/downShopWholeImg?";
	//公用的图片服务器商品图片打包文件下载路径
	//public static final String  PUBLIC_USE_WHOLE_IMG_DOWN ="http://192.168.1.100:8765/cloudimginterface/downLoadCtr/publicDownWholeImg?";
				
	
}
