package com.cbt.website.thread;

import com.cbt.parse.service.DownloadMain;

import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName ShopGoodsDealThred
 * @Description 商品数据发布上线线程
 * @author zlw
 * @date 2018年4月28日
 */
public class CoreGoodsSyncThread implements Runnable {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CoreGoodsSyncThread.class);
	// private static final String ACCESSUTL = "http://127.0.0.1:8080/checkimage/clear/shopGoods?shopId=";
	//private static final String ACCESSUTL = "http://192.168.1.31:8080/checkimage/sync/shopGoodsSync?shopId=";
	private static final String ACCESSUTL = "http://192.168.1.31:8088/syncGoodsToOnline/sync/coreGoodsSync?sourceTbl=";
//	private static final String ACCESSUTL = "http://192.168.1.55:8080/syncGoodsToOnline/sync/coreGoodsSync?sourceTbl=";

	private String sourceTbl;
	private String saveTbl;

	public CoreGoodsSyncThread(String sourceTbl,String saveTbl) {
		super();
//		this.shopId = shopId;
		this.sourceTbl = sourceTbl;
		this.saveTbl = saveTbl;
	}

	@Override
	public void run() {
		try {
			String url = ACCESSUTL + this.sourceTbl +"&saveTbl="+this.saveTbl;
			String resultJson = DownloadMain.getContentClient(url, null);
			LOG.info("saveTbl:" + this.saveTbl + ",result:[" + resultJson + "]");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("saveTbl:" + this.saveTbl + ",coreGoodsSync error:" + e.getMessage());
			LOG.error("saveTbl:" + this.saveTbl + ",coreGoodsSync error:" + e.getMessage());
		}

	}

}
