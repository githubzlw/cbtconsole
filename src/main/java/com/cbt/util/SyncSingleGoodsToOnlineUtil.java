package com.cbt.util;

import com.cbt.bean.CustomOnlineGoodsBean;
import com.cbt.dao.SyncPidToOnlineDao;
import com.cbt.dao.impl.SyncPidToOnlineDaoImpl;
import com.cbt.parse.service.DownloadMain;
import com.cbt.website.util.JsonResult;

import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 
 * @ClassName SyncSingleGoodsToOnlineUtil
 * @Description 单个商品同步线上工具类
 * @author Jxw
 * @date 2018年3月8日
 */
public class SyncSingleGoodsToOnlineUtil {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SyncSingleGoodsToOnlineUtil.class);

	/**
	 * ZIP文件临时存放路径
	 */
	private static final String TEMPLOCALZIPPATH = "K:\\shopimgzip\\temp_";

	/**
	 * ZIP文件网络访问路径
	 */
	private static final String TEMPLOCALZIPPATHSHOWURL = "http://117.144.21.74:9988/";

	/**
	 * 
	 * @Title syncOnile
	 * @Description PID商品上线
	 * @param goodsPid
	 * @param crawlTable
	 *            : 抓取1688商品的原始数据表
	 * @param dealTable
	 *            : 处理后的1688商品数据表
	 * @param imgServiceLocalPath
	 *            : 图片服务器图片保存在服务器的本地全路径
	 * @param imgServiceShowPath
	 *            : 图片服务器图片保存后网络显示的路径
	 * @return 是否执行成功
	 * @return boolean
	 */
	public static JsonResult syncOnile(String goodsPid, String crawlTable, String dealTable, String imgServiceLocalPath,
			String imgServiceShowPath) {
		JsonResult json = new JsonResult();
		// 1.临时文件夹ZIP包的的清理
		deleteTempZip();
		SyncPidToOnlineDao syncDao = new SyncPidToOnlineDaoImpl();
		// 2.查询商品信息
		CustomOnlineGoodsBean goods = syncDao.queryGoodsInfoByPid(goodsPid, dealTable);
		if (goods == null) {
			json.setOk(false);
			json.setMessage("dealTable:" + dealTable + ",goodsPid:" + goodsPid + " 获取商品信息失败");
		} else {
			// 3.整体PID商品图片打包上传到图片服务器
			if (downWholeGoodsImgs(syncDao, goodsPid, crawlTable, dealTable, imgServiceLocalPath)) {
				System.out.println("crawlTable:" + crawlTable + "dealTable:" + dealTable + ",goodsPid:" + goodsPid
						+ "down img success!!");
				// 4.同步商品到线上
				if (syncSingleGoodsToOnline(syncDao, goodsPid, dealTable, imgServiceShowPath)) {
					json.setOk(true);
					json.setMessage("同步商品到线上成功！！");
				} else {
					json.setOk(true);
					json.setMessage("同步商品到线上失败");
				}
			} else {
				System.err.println("crawlTable:" + crawlTable + "dealTable:" + dealTable + ",goodsPid:" + goodsPid
						+ "down img error");
				LOG.error("crawlTable:" + crawlTable + "dealTable:" + dealTable + ",goodsPid:" + goodsPid
						+ "down img error");
				json.setOk(false);
				json.setMessage("crawlTable:" + crawlTable + "dealTable:" + dealTable + ",goodsPid:" + goodsPid
						+ "down img error");
			}
		}
		return json;
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
	private static boolean downWholeGoodsImgs(SyncPidToOnlineDao syncDao, String goodsPid, String crawlTable,
			String dealTable, String imgServiceLocalPath) {
		boolean isSuccess = false;
		String localPath = syncDao.queryGoodsLocalPath(crawlTable, goodsPid);
		if (localPath == null || "".equals(localPath)) {
			System.err.println("crawlTable:" + crawlTable + ",goodsPid:" + goodsPid + " localPath空值");
			LOG.error("crawlTable:" + crawlTable + ",goodsPid:" + goodsPid + " localPath空值");
			return isSuccess;
		} else {
			String usrFile = localPath.endsWith("\\") ? localPath : localPath + "\\";
			String tempPath = usrFile.replace("/", "\\").replace("K:", "k$") + goodsPid;
			String zipPakageUrl = TEMPLOCALZIPPATHSHOWURL + "temp_" + goodsPid + ".zip";
			String zipPakageLocalPath = TEMPLOCALZIPPATH + goodsPid + ".zip";
			try {
				// 压缩文件到指定路径
				String command = "cmd /c WINRAR A -ep1 \\\\192.168.1.28\\K$\\shopimgzip\\temp_" + goodsPid
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
				if (downImgToService(zipPakageUrl, goodsPid, imgServiceLocalPath)) {
					isSuccess = true;
				} else {
					isSuccess = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("dealTable:" + dealTable + ",goodsPid:" + goodsPid + " downWholeGoodsImgs error: "
						+ e.getMessage());
				LOG.error("dealTable:" + dealTable + ",goodsPid:" + goodsPid + " downWholeGoodsImgs error: "
						+ e.getMessage());
				isSuccess = false;
			} finally {
				File fl = new File(zipPakageLocalPath);
				if (fl.exists()) {
					System.out.println("file:" + zipPakageLocalPath + " is exists");
					if (fl.delete()) {
						System.out.println("file:" + zipPakageLocalPath + " delete success!!");
					} else {
						System.err.println("file:" + zipPakageLocalPath + " delete error--");
						LOG.error("file:" + zipPakageLocalPath + " delete error--");
					}
				} else {
					System.err.println("file:" + zipPakageLocalPath + " is not exists");
					LOG.error("file:" + zipPakageLocalPath + " is not exists");
				}
			}
		}
		return isSuccess;
	}

	/**
	 * 
	 * @Title syncSingleGoodsToOnline
	 * @Description 单个商品PID同步线上
	 * @param shopDao
	 * @param pid
	 * @param shopId
	 * @return
	 * @return boolean
	 */
	private static boolean syncSingleGoodsToOnline(SyncPidToOnlineDao syncDao, String goodsPid, String dealTable,
			String imgServiceShowPath) {

		boolean isSync = false;
		CustomOnlineGoodsBean nwgoods = syncDao.queryGoodsByTableAndPid(dealTable, goodsPid);
		if (nwgoods == null) {
			System.err.println("dealTable:" + dealTable + ",goodsPid:" + goodsPid + " 查询失败");
		} else {
			nwgoods.setRemotPath(imgServiceShowPath);
			if (!(nwgoods.getEninfo() == null || "".equals(nwgoods.getEninfo()) || nwgoods.getEninfo().length() < 15)) {
				nwgoods.setIsShowDetImgFlag(1);
			}
			isSync = false;
			int syncCount = 0;
			// 重试2次
			while (!isSync && syncCount < 3) {
				syncCount++;
				isSync = syncDao.syncSingleGoodsToOnline(nwgoods, dealTable);
			}
			if (isSync) {
				System.out.println("--goodsPid:" + goodsPid + " insert success!!!");
			} else {
				System.err.println("--goodsPid:" + goodsPid + " insert error---");
				LOG.error("--goodsPid:" + goodsPid + " insert error---");
			}
		}
		return isSync;
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
	private static boolean downImgToService(String localFileName, String pid, String imgServiceLocalPath) {
		String url = ContentConfig.PUBLIC_USE_WHOLE_IMG_DOWN + "imgUrl=" + localFileName + "&localPath="
				+ imgServiceLocalPath + "&pid=" + pid;
		String resultJson = DownloadMain.getContentClient(url, null);
		int count = 0;
		while ("0".equals(resultJson) && count < 3) {
			resultJson = DownloadMain.getContentClient(url, null);
			count++;
		}
		return "0".equals(resultJson) ? false : true;
	}

	/**
	 * 
	 * @Title deleteTempZip
	 * @Description 临时文件夹ZIP包的的清理
	 * @return void
	 */
	private static void deleteTempZip() {
		try {
			File file = new File("K:\\publicuseimgzip");
			if (file.exists()) {
				File[] chidsFls = file.listFiles();
				if (!(chidsFls == null || chidsFls.length == 0)) {
					for (File tempFl : chidsFls) {
						String abFlPath = tempFl.getAbsolutePath();
						if (abFlPath.contains(".zip")) {
							tempFl.delete();
						}
					}
				}
				chidsFls = null;
			}
		} catch (Exception e) {
			System.err.println("deleteTempZip error:" + e.getMessage());
			LOG.error("deleteTempZip error:", e);
		}
	}

	
	/**
	 * 品类精研打包程序
	 * @Title downCategoryResearchImgs 
	 * @Description TODO
	 * @param object
	 * @param sid
	 * @param object2
	 * @param object3
	 * @param string
	 * @return
	 * @return boolean
	 */
	public static boolean downCategoryResearchImgs(String sid, String imgServiceLocalPath) {
		boolean isSuccess = false;
		String localPath = "K:\\shopimgzip\\research";
		if (localPath == null || "".equals(localPath)) {
			return isSuccess;
		} else {//
			String usrFile = localPath.endsWith("\\") ? localPath : localPath + "\\";
			String tempPath = usrFile.replace("/", "\\").replace("K:", "k$") +  sid;
			String zipPakageUrl = TEMPLOCALZIPPATHSHOWURL + "research/temp_" +  sid + ".zip";
			String zipPakageLocalPath = TEMPLOCALZIPPATH +"research\\temp_"+  sid + ".zip";
			try {
				// 压缩文件到指定路径
				String command = "cmd /c WINRAR A -ep1 \\\\192.168.1.28\\K$\\shopimgzip\\research\\temp_" +  sid
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
				if (downCategoryImgToService(zipPakageUrl, sid, imgServiceLocalPath)) {
					isSuccess = true;
				} else {
					isSuccess = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				isSuccess = false;
			} finally {
				File fl = new File(zipPakageLocalPath);
				if (fl.exists()) {
					System.out.println("file:" + zipPakageLocalPath + " is exists");
					if (fl.delete()) {
						System.out.println("file:" + zipPakageLocalPath + " delete success!!");
					} else {
						System.err.println("file:" + zipPakageLocalPath + " delete error--");
						LOG.error("file:" + zipPakageLocalPath + " delete error--");
					}
				} else {
					System.err.println("file:" + zipPakageLocalPath + " is not exists");
					LOG.error("file:" + zipPakageLocalPath + " is not exists");
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
	private static boolean downCategoryImgToService(String localFileName, String pid, String imgServiceLocalPath) {
		String url = ContentConfig.CATEGORY_RESEARCH_IMG_DOWN + "imgUrl=" + localFileName + "&localPath="
				+ imgServiceLocalPath + "&pid=" + pid;
		String resultJson = DownloadMain.getContentClient(url, null);
		int count = 0;
		while ("0".equals(resultJson) && count < 3) {
			resultJson = DownloadMain.getContentClient(url, null);
			count++;
		}
		return "0".equals(resultJson) ? false : true;
	}

}
