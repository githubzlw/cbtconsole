package com.cbt.parse.driver;

import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.service.GoodsBean;

import java.util.ArrayList;

/**引擎
 * @author abc
 *flag:1-yiwugou,2-eelly,3-wholesale,4-aliespress,5-hotsale,
 *6-goodsdata_expand,7-1688
 */
public class DriverInterface {
	private  LocalSDriver localDriver = new LocalSDriver();
	private  OneSixExpressDriver oneDriver = new OneSixExpressDriver();
	
	/**搜索商品
	 * flag:1-yiwugou,2-eelly,3-wholesale,
	 * 4-aliespress,5-hotsale,6-goodsdata_expand,
	 * 7-1688
	 * @return
	 */
	public  ArrayList<SearchGoods> search(int flag,String key,String minprice,String maxprice,String minq,String maxq,String srt,String cid,String page,String pid){
		if(flag==5){
			return localDriver.hotWordsSearchDriver(key);
		}else if(flag==6){
			return localDriver.searchDriver(key, minprice, maxprice, minq, maxq, page, srt, cid,pid);
		}else if(flag==7){
			return oneDriver.searchDriver(key,srt, minprice, maxprice, minq, maxq, cid,pid,page);
		}
		return null;
	}

	/**商店商品
	 * flag:1-yiwugou,2-eelly,3-wholesale,4-aliespress,7-1688
	 * @return
	 */
	public  ArrayList<SearchGoods> store(String sid,int flag,String page){
		if(flag==7){
			return oneDriver.storeDriver(sid,page);
		}
		return null;
	}
	
	/**商品单页信息
	 * flag:1-yiwugou,2-eelly,3-wholesale,4-aliespress,5-hotsale,7-1688
	 * @return
	 */
	public GoodsBean goods(String url, String pid, int flag){
		if(flag==5){
			return localDriver.hotWordsGoodsDriver(url, pid);
		}else if(flag==7){
			return oneDriver.goodsDriver(url, pid);
		}
		return null;
	}
	
}
