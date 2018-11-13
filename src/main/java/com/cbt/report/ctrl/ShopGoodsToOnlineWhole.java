package com.cbt.report.ctrl;

import com.cbt.bean.CustomOnlineGoodsBean;
import com.cbt.customer.dao.IShopUrlDao;
import com.cbt.customer.dao.ShopUrlDaoImpl;
import com.cbt.parse.service.DownloadMain;
import com.cbt.util.ContentConfig;
import com.cbt.website.util.JsonResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName ShopGoodsToOnlineWhole
 * @Description 店铺商品整体压缩文件并上传
 * @author Jxw
 * @date 2018年3月2日
 */
public class ShopGoodsToOnlineWhole {
	private static final Log LOG = LogFactory.getLog(ShopGoodsToOnlineWhole.class);
	private static final String LOCALSHOPZIPPAHT = "http://117.144.21.74:9000/";
	//private static final String SHOWURL = "http://192.168.1.100:8765/editimg/shopimg/";
	private static final String SHOWURL = "http://img1.import-express.com/importcsvimg/shopimg/";

	/**
	 * 
	 * @Title publicGoodsToOnline
	 * @Description 根据shopId发布店铺下清洗好的商品
	 * @param shopId
	 * @return
	 * @return boolean
	 */
	public JsonResult publicGoodsToOnline(String shopId) {
		System.out.println("publicGoodsToOnline begin");
		JsonResult json = new JsonResult();
		//deleteTempZip("shoptemp");
		IShopUrlDao shopDao = new ShopUrlDaoImpl();
		List<CustomOnlineGoodsBean> goodsList = shopDao.queryReadyDealGoods(shopId);

		
		if (goodsList == null || goodsList.size() == 0) {
			System.err.println("当前无清理好的数据!!");
			json.setOk(true);
		} else {

			try {
				shopDao.updateShopState(shopId, 1);
				System.err.println("当前已经清理的数据数量：" + goodsList.size());
				int susCount = 0;
				for (int i = 0; i < goodsList.size(); i++) {
					if (downWholeGoodsImgs(shopDao, shopId, goodsList.get(i).getPid())) {
						//goodsList.get(i).setRemotPath(SHOWURL);						
						if(syncSingleGoodsToOnline(shopDao, goodsList.get(i).getPid(), shopId)){
							susCount ++;
						}
					} else {
						System.err.println("shopId:" + shopId + ",pid:" + goodsList.get(i).getPid() + "down img error");
						LOG.error("shopId:" + shopId + ",pid:" + goodsList.get(i).getPid() + "down img error");
					}

				}
				if(susCount > 0){
					shopDao.updateShopState(shopId, 2);
					//path_catid更新
					shopDao.batchUpdateCatidPath(shopId);
				}else{
					Map<String,Integer> stMap = shopDao.queryShopGoodsSync(shopId);
					if(stMap == null || stMap.size() == 0){
						shopDao.updateShopState(shopId, 0);
					}else{
						 if(stMap.get("total_num") > 0){
							 if(stMap.get("sync_num") > 0){
								 shopDao.updateShopState(shopId, 2);
							 }else{
								 shopDao.updateShopState(shopId, 4);
							 }							
						 }else{
							 shopDao.updateShopState(shopId, 0);
						 }
					}					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				shopDao.updateShopState(shopId, 3);
				System.err.println("shopId:" + shopId + " publicGoodsToOnline error");
				LOG.error("shopId:" + shopId + " publicGoodsToOnline error");		
			}

		}
		System.out.println("publicGoodsToOnline end!!!");
		return json;
	}

	private boolean syncSingleGoodsToOnline(IShopUrlDao shopDao, String pid, String shopId) {

		boolean isSync = false;
		CustomOnlineGoodsBean nwgoods = shopDao.queryGoodsByShopIdAndPid(shopId, pid);
		if (nwgoods == null) {
			System.err.println("pid:" + pid + " 查询失败");
		} else {
			nwgoods.setRemotPath(SHOWURL);
			if (!(nwgoods.getEninfo() == null || "".equals(nwgoods.getEninfo()) || nwgoods.getEninfo().length() < 15)) {
				nwgoods.setIsShowDetImgFlag(1);
			}
			isSync = false;
			int syncCount = 0;
			// 重试2次
			while (!isSync && syncCount < 3) {
				syncCount++;
				isSync = shopDao.syncSingleGoodsToOnline(nwgoods, shopId);
			}
			if (isSync) {
				System.out.println("--pid:" + pid + " insert success");
			} else {
				System.err.println("--pid:" + pid + " insert error!!!");
			}
		}
		return isSync;
	}

	/**
	 * 
	 * @Title downWholeGoodsImgs
	 * @Description 整体pid商品图片打包上传到图片服务器
	 * @param shopDao
	 * @param shopId
	 * @param goodsPid
	 * @return
	 * @return boolean
	 */
	private boolean downWholeGoodsImgs(IShopUrlDao shopDao, String shopId, String goodsPid) {
		boolean isSuccess = false;
		String localPath = shopDao.queryGoodsLocalPath(shopId, goodsPid);
		if (localPath == null || "".equals(localPath)) {
			System.err.println("shopId:" + shopId + ",goodsPid:" + goodsPid + " localPath空值");
			LOG.error("shopId:" + shopId + ",goodsPid:" + goodsPid + " localPath空值");
			return isSuccess;
		} else {
			String usrFile = localPath.endsWith("\\") ? localPath : localPath + "\\";
			String tempPath = usrFile.replace("/", "\\").replace("K:", "k$") + goodsPid;
			String zipPakage = LOCALSHOPZIPPAHT + "shoptemp/temp_" + goodsPid + ".zip";
			String zipLocalPath = "K:\\shopimgzip\\shoptemp\\temp_" + goodsPid + ".zip";
			try {
				// 压缩文件到指定路径
				String command = "cmd /c WINRAR A -ep1 \\\\192.168.1.28\\K$\\shopimgzip\\shoptemp\\temp_" + goodsPid
						+ ".zip  \\\\192.168.1.28\\" + tempPath + " -r";
				try {
					Runtime.getRuntime().exec(command);
				} catch (Exception ex) {
					System.err.println("执行命令：" + command + "错误");
					LOG.error("执行命令：" + command + "错误");
				}
				try {
					// 暂停5秒等待压缩执行完成
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 上传到服务器，并解压缩，返回上传和解压的结果,执行失败重试一次
				boolean isDown = downImgToService(zipPakage, goodsPid);
				if (isDown) {
					isSuccess = true;
				} else {
					isSuccess = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("shopId:" + shopId + ",goodsPid:" + goodsPid + " error: " + e.getMessage());
				LOG.error("shopId:" + shopId + ",goodsPid:" + goodsPid + " error: " + e.getMessage());
				isSuccess = false;
			} finally {
				File fl = new File(zipLocalPath);
				if (fl.exists()) {
					System.out.println("file:" + zipLocalPath + " is exists");
					if (fl.delete()) {
						System.out.println("file:" + zipLocalPath + " delete success!!");
					} else {
						System.err.println("file:" + zipLocalPath + " delete error--");
						LOG.error("file:" + zipLocalPath + " delete error--");
					}
				} else {
					System.err.println("file:" + zipLocalPath + " is not exists");
					LOG.error("file:" + zipLocalPath + " is not exists");
				}
			}
		}
		return isSuccess;
	}

	/**
	 * 
	 * @Title downImgToService
	 * @Description 本地图片下载到图片服务器
	 * @param localFileName
	 * @param remoteFileName
	 * @return
	 * @return boolean
	 */
	private boolean downImgToService(String localFileName, String pid) {
		String url = ContentConfig.SHOP_GOODS_WHOLE_IMG_DOWN + "imgUrl=" + localFileName + "&pid=" + pid;
		String resultJson = "0";
		int count = 0;
		while ("0".equals(resultJson) && count < 3) {
			try {
				resultJson = DownloadMain.getContentClient(url, null);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
				resultJson ="0";
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}finally {
				count++;
			}			
		}
		return "0".equals(resultJson) ? false : true;
	}
	
}
