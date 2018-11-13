package com.cbt.test.catch_Page;

import com.cbt.customer.dao.IShopUrlDao;
import com.cbt.customer.dao.ShopUrlDaoImpl;
import com.cbt.parse.service.DownloadMain;

import java.util.List;

public class ShopGoodsBatchClear {
	private static final String ACCESSUTL = "http://192.168.1.27:9115/checkimage/clear/shopGoods?shopId=";

	public static void main(String[] args) {
		IShopUrlDao shopDao = new ShopUrlDaoImpl();
		List<String> shopIds = shopDao.queryErrorClearShopList();
		if (shopIds.size() > 0) {
			for (String shopId : shopIds) {
				String url = ACCESSUTL + shopId;
				try {
					String resultJson = DownloadMain.getContentClient(url, null);
					System.err.println("shopId:" + shopId + ",resultJson:" + resultJson);
					Thread.sleep(1500);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("shopId:" + shopId + ",ShopGoodsDeal error:" + e.getMessage());
				}			
			}
		}	
	}
}
