package com.cbt.website.thread;

import com.cbt.parse.service.DownloadMain;

import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName ShopGoodsDealThred
 * @Description 店铺商品的数据清洗线程
 * @author Jxw
 * @date 2018年3月1日
 */
public class ShopGoodsDealThread implements Runnable {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ShopGoodsDealThread.class);
//	private static final String ACCESSUTL = "http://192.168.1.100:8080/checkimage/clear/shopGoods?shopId=";
	// private static final String ACCESSUTL = "http://192.168.1.31:8080/checkimage/clear/shopGoods?shopId=";
	private static final String ACCESSUTL = "http://192.168.1.28:8081/checkimage/clear/shopGoods?shopId=";

	

	private String shopId;

	public ShopGoodsDealThread(String shopId) {
		super();
		this.shopId = shopId;
	}

	@Override
	public void run() {
		try {
			String url = ACCESSUTL + this.shopId;
			String resultJson = DownloadMain.getContentClient(url, null);
			LOG.info("shopId:" + this.shopId + ",result:[" + resultJson + "]");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("shopId:" + this.shopId + ",ShopGoodsDeal error:" + e.getMessage());
			LOG.error("shopId:" + this.shopId + ",ShopGoodsDeal error:" + e.getMessage());
		}
		

	}

}
