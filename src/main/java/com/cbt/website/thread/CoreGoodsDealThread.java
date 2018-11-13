package com.cbt.website.thread;

import com.cbt.parse.service.DownloadMain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @ClassName CoreGoodsDealThread
 * @Description 核心商品的数据清洗线程
 * @author zlw
 * @date 2018年4月26日
 */
public class CoreGoodsDealThread implements Runnable {
	private static final Log LOG = LogFactory.getLog(CoreGoodsDealThread.class);
	private static final String ACCESSUTL = "http://192.168.1.31:8080/checkimage/clear/clearGoods?sourceTbl=";
//	private static final String ACCESSUTL = "http://192.168.1.55:8080/checkimage/clear/clearGoods?sourceTbl=";
	

	private String sourceTbl;
	private String saveTbl;

	public CoreGoodsDealThread(String sourceTbl,String saveTbl) {
		super();
		this.sourceTbl = sourceTbl;
		this.saveTbl = saveTbl;
	}

	@Override
	public void run() {
		try {
			String url = ACCESSUTL + this.sourceTbl +"&saveTbl="+this.saveTbl;
			String resultJson = DownloadMain.getContentClient(url, null);
			LOG.info("sourceTbl:" + this.sourceTbl + ",result:[" + resultJson + "]");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("sourceTbl:" + this.sourceTbl + ",ShopGoodsDeal error:" + e.getMessage());
			LOG.error("sourceTbl:" + this.sourceTbl + ",ShopGoodsDeal error:" + e.getMessage());
		}
		

	}

}
