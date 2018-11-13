package com.cbt.website.quartz;

import com.cbt.bean.CustomOnlineGoodsBean;
import com.cbt.bean.TypeBean;
import com.cbt.dao.SameTypeGoodsDao;
import com.cbt.dao.impl.SameTypeGoodsDaoImpl;
import com.cbt.parse.service.DownloadMain;
import com.cbt.util.ContentConfig;
import com.cbt.website.util.JsonResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @ClassName SameTypeGoodsJob
 * @Description 同款商品定时任务
 * @author Jxw
 * @date 2018年1月23日
 */
public class SameTypeGoodsJob implements Job {
	private static final Log LOG = LogFactory.getLog(SameTypeGoodsJob.class);
	
	//private static final String SHOWURL = "http://192.168.1.100:8765/editimg/sametype/";
	//private static final String SHOWURL = "http://192.168.1.29:8765/editimg/sametype/";
	private static final String SHOWURL = "http://img1.import-express.com/importcsvimg/sametype/";
	
	//private static final String CLEARURL = "http://192.168.1.100:8080/checkimage/clear/sameGoods";
	private static final String CLEARURL = "http://192.168.1.27:9115/checkimage/clear/sameGoods";
	//private static final String CLEARURL = "http://192.168.1.29:9115/checkimage/clear/sameGoods";
	
	
	private String chineseChar = "([\\一-\\龥]+)";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("SameTypeGoodsJob begin "  + new Date().toLocaleString());
		LOG.info("SameTypeGoodsJob begin " + new Date().toLocaleString());
		
		//远程调用商品数据清洗方法
		String resultJson = DownloadMain.getContentClient(CLEARURL, null);
		System.out.println("resultJson:" + resultJson);
		LOG.info("resultJson:" + resultJson);
		
		//进行数据清洗
		//SameGoodsDealJob();
		//同步部分
		//beginDataReady();
		System.out.println("SameTypeGoodsJob end!!! "  + new Date().toLocaleString());
		LOG.info("SameTypeGoodsJob end!!! "  + new Date().toLocaleString());

	}
	
	
	private void beginDataReady(){
		SameTypeGoodsDao goodsDao = new SameTypeGoodsDaoImpl();
		List<CustomOnlineGoodsBean> goodsList = goodsDao.queryDealGoods();
		JsonResult json = new JsonResult();

		if (goodsList == null || goodsList.size() == 0) {
			System.err.println("当前无清理好的数据!!");
			json.setOk(true);
		} else {
			System.err.println("当前已经清理的数据数量：" + goodsList.size());
			for (int i = 0; i < goodsList.size(); i++) {
				// 图片已经下载的
				if (goodsList.get(i).getImgDownFlag() > 0) {
					syncSingleGoodsToOnline(goodsDao, goodsList.get(i));
				} else {
					// 图片未下载的
					json.setOk(false);
					boolean isSuccess = false;
					int downFlag = 0;
					while (!isSuccess && downFlag < 3) {
						downFlag++;
						isSuccess = downloadNetImg(goodsList.get(i));
					}
					// isSuccess = downloadNetImg(goodsList.get(i));
					if (isSuccess) {
						updateDownloadImgGoods(goodsDao, goodsList.get(i));
					} else {
						goodsDao.updateGoodsImgError(goodsList.get(i));
						System.err.println("--pid:" + goodsList.get(i).getPid() + " downloadNetImg error!!!");
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Title SameGoodsDealJob
	 * @Description 进行数据清洗
	 * @return void
	 */
	private void SameGoodsDealJob() {
		System.out.println("SameGoodsDealJob begin");

		SameTypeGoodsDao goodsDao = new SameTypeGoodsDaoImpl();

		try {
			List<Integer> ids = goodsDao.queryNoDealGoods();
			if (ids.size() > 0) {
				System.out.println((new Date(System.currentTimeMillis())).toString() +  " SameGoodsDealJob ids size：" + ids.size());
				int minId = ids.get(0);
				for (int tempId : ids) {
					if (minId > tempId) {
						minId = tempId;
					}
				}
				int updateCount = goodsDao.batchUpdateDlFlag(ids);
				if (updateCount > 0) {
					// 远程访问处理数据的接口
					DownloadMain.getContentClient(
							ContentConfig.DEAL_1688_GOODS_URL + "stid=" + (minId - 1) + "&size=" + ids.size(), null);
				}
			} else {
				System.err.println((new Date(System.currentTimeMillis())).toString() +  " NoDealGoods size:0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("SameGoodsDealJob error: " + e.getMessage());
			LOG.error("SameGoodsDealJob error: " + e.getMessage());
		}

		System.out.println("SameGoodsDealJob end!!!");
	}

	/**
	 * 
	 * @Title syncSingleGoodsToOnline
	 * @Description 同步到线上
	 * @param goodsDao
	 * @param goods
	 * @return
	 * @return boolean
	 */
	private boolean syncSingleGoodsToOnline(SameTypeGoodsDao goodsDao, CustomOnlineGoodsBean goods) {

		boolean isSync = false;
		CustomOnlineGoodsBean nwgoods = goodsDao.queryGoodsByPid(goods.getPid());
		if (nwgoods == null) {
			System.err.println("pid:" + goods.getPid() + " 查询失败");
		} else {
			if(!(nwgoods.getEninfo() == null || "".equals(nwgoods.getEninfo()) || nwgoods.getEninfo().length() < 15)){
				nwgoods.setIsShowDetImgFlag(1);
			}
			isSync = false;
			int syncCount = 0;
			// 重试2次
			while (!isSync && syncCount < 3) {
				syncCount++;
				isSync = goodsDao.syncSingleGoodsToOnline(nwgoods);
			}
			// isSync = goodsDao.syncSingleGoodsToOnline(nwgoods);
			if (isSync) {				
				System.err.println("--pid:" + goods.getPid() + " insert success");
			} else {
				//goodsDao.updateSameTypeGoodsError(nwgoods.getPid());
				System.err.println("--pid:" + goods.getPid() + " insert error!!!");
			}
		}
		return isSync;
	}

	/**
	 * 
	 * @Title updateDownloadImgGoods
	 * @Description 更新下载的图片信息
	 * @param goodsDao
	 * @param goods
	 * @return
	 * @return boolean
	 */
	private boolean updateDownloadImgGoods(SameTypeGoodsDao goodsDao, CustomOnlineGoodsBean goods) {
		boolean isUpdate = false;
		int updateFlag = 0;
		while (!isUpdate && updateFlag < 3) {
			updateFlag++;
			isUpdate = goodsDao.updateGoodsImg(goods);
		}
		// isUpdate = goodsDao.updateGoodsImg(goods);
		if (isUpdate) {
			syncSingleGoodsToOnline(goodsDao, goods);
		} else {
			System.err.println("--pid:" + goods.getPid() + " update Img error!!!");
		}
		return isUpdate;
	}

	/**
	 * 
	 * @Title downloadNetImg
	 * @Description 下载网络图片
	 * @param goods
	 * @return JsonResult
	 */
	private boolean downloadNetImg(CustomOnlineGoodsBean goods) {
		boolean isSuccess = false;
		// 分类别下载goods中的图片
		// 1.搜索图，格式是200x200.jpg替换.jpg
		String mainImg = goods.getCustomMainImage();
		String mainFileSuffix = goods.getCustomMainImage();
		// 取出后缀名称
		int mainLastIndexOf = mainImg.lastIndexOf("/");
		if (mainLastIndexOf > -1) {
			mainFileSuffix = mainImg.substring(mainLastIndexOf + 1).replace(".400x400", "").replace(".jpg",
					".220x220.jpg");
		}
		// 图片服务器下载ali图片
		String url = ContentConfig.PICTURE_SERVER_IMG_1688_DOWN + "imgUrl=" + goods.getCustomMainImage()
				+ "&newImgName=" + goods.getPid() + "/" + mainFileSuffix;
		String resultJson = DownloadMain.getContentClient(url, null);
		if ("0".equals(resultJson)) {
			// 下载不成功
			System.err.println("PID:" + goods.getPid() + ",CustomMainImage[" + goods.getCustomMainImage() + "] 下载失败");
			LOG.error("PID:" + goods.getPid() + ",CustomMainImage[" + goods.getCustomMainImage() + "] 下载失败");
			return false;
		} else {
			goods.setCustomMainImage(goods.getPid() + "/" + mainFileSuffix);
			goods.setRemotPath(SHOWURL);
			goods.setLocalPath(SHOWURL);
			isSuccess = true;
		}

		// 2.规格图 400x400.jpg替换.jpg和60x60.jpg替换.jpg
		// 规格图bean
		List<TypeBean> typeList = deal1688GoodsType(goods);
		if (typeList.size() > 0) {
			String typeImg = "";
			String typeSuffix60 = "";
			String typeSuffix400 = "";
			int typeIndexOf = -1;
			// 循环下载

			for (TypeBean tyImg : typeList) {
				typeImg = tyImg.getImg();
				// 判断规格是否有图片
				if (!(typeImg == null || "".equals(typeImg.trim()) || typeImg.length() < 5)) {
					// 判断是否是本地上传的图片，是的话单独替换即可
					if (typeImg.contains(SHOWURL)) {
						tyImg.setImg(typeImg.replace(SHOWURL, ""));
					} else {
						// 网络图片进行下面操作
						typeSuffix60 = tyImg.getImg();
						typeSuffix400 = tyImg.getImg();
						// 取出后缀名称
						typeIndexOf = typeImg.lastIndexOf("/");
						if (typeIndexOf > -1) {
							typeSuffix60 = typeImg.substring(typeIndexOf + 1).replace(".jpg", ".60x60.jpg");
							typeSuffix400 = typeImg.substring(typeIndexOf + 1).replace(".jpg", ".400x400.jpg");
						}
						// 下载网络图片400x400
						// 图片服务器下载ali图片
						url = ContentConfig.PICTURE_SERVER_IMG_1688_DOWN + "imgUrl="
								+ tyImg.getImg().replace(".jpg", ".400x400.jpg") + "&newImgName=" + goods.getPid() + "/"
								+ typeSuffix400;
						resultJson = DownloadMain.getContentClient(url, null);
						if ("0".equals(resultJson)) {
							// 下载不成功
							System.err.println("PID:" + goods.getPid() + ",规格图400x400["
									+ tyImg.getImg().replace(".jpg", ".400x400.jpg") + "]下载失败");
							LOG.error("PID:" + goods.getPid() + ",规格图400x400["
									+ tyImg.getImg().replace(".jpg", ".400x400.jpg") + "]下载失败");
							return false;
						} else {
							// 图片服务器下载ali图片
							url = ContentConfig.PICTURE_SERVER_IMG_1688_DOWN + "imgUrl="
									+ tyImg.getImg().replace(".jpg", ".60x60.jpg") + "&newImgName=" + goods.getPid()
									+ "/" + typeSuffix60;
							resultJson = DownloadMain.getContentClient(url, null);
							if ("0".equals(resultJson)) {
								// 下载不成功
								System.err.println("PID:" + goods.getPid() + ",规格图60x60["
										+ tyImg.getImg().replace(".jpg", ".60x60.jpg") + "]下载失败");
								LOG.error("PID:" + goods.getPid() + ",规格图60x60["
										+ tyImg.getImg().replace(".jpg", ".60x60.jpg") + "]下载失败");
								return false;
							} else {
								tyImg.setImg(goods.getPid() + "/" + typeSuffix60);
								goods.setRemotPath(SHOWURL);
								goods.setLocalPath(SHOWURL);
								isSuccess = true;
							}
						}
					}
				}
			}
			if (isSuccess) {
				// 更新成功重新赋值规格数据
				goods.setEntype(typeList.toString());
			} else {
				return false;
			}
		}

		// 3.橱窗图 400x400.jpg替换.jpg和60x60.jpg替换.jpg
		// 橱窗图list集合
		List<String> imgs = deal1688GoodsImg(goods, SHOWURL);
		if (imgs.size() > 0) {

			String windowSuffix60 = "";
			String windowSuffix400 = "";
			int windowIndexOf = -1;
			List<String> newImgs = new ArrayList<String>();
			// 循环下载
			for (String windowImg : imgs) {
				// 判断是否是本地上传的图片，是的话单独替换即可
				if (windowImg.contains(SHOWURL)) {
					newImgs.add(windowImg.replace(SHOWURL, ""));
				} else {
					// 网络图片进行下面操作
					windowSuffix60 = windowImg;
					windowSuffix400 = windowImg;
					// 取出后缀名称
					windowIndexOf = windowImg.lastIndexOf("/");
					if (windowIndexOf > -1) {
						windowSuffix60 = windowSuffix60.substring(windowIndexOf + 1).replace(".jpg", ".60x60.jpg");
						windowSuffix400 = windowSuffix400.substring(windowIndexOf + 1).replace(".jpg", ".400x400.jpg");
					}
					// 下载网络图片400x400
					// 图片服务器下载ali图片
					url = ContentConfig.PICTURE_SERVER_IMG_1688_DOWN + "imgUrl="
							+ windowImg.replace(".jpg", ".400x400.jpg") + "&newImgName=" + goods.getPid() + "/"
							+ windowSuffix400;
					resultJson = DownloadMain.getContentClient(url, null);
					if ("0".equals(resultJson)) {
						// 下载不成功
						System.err.println("PID:" + goods.getPid() + ",橱窗图400x400["
								+ windowImg.replace(".jpg", ".400x400.jpg") + "]下载失败");
						LOG.error("PID:" + goods.getPid() + ",橱窗图[" + windowImg.replace(".jpg", ".400x400.jpg")
								+ "]400x400下载失败");
						return false;
					} else {
						// 图片服务器下载ali图片
						url = ContentConfig.PICTURE_SERVER_IMG_1688_DOWN + "imgUrl="
								+ windowImg.replace(".jpg", ".60x60.jpg") + "&newImgName=" + goods.getPid() + "/"
								+ windowSuffix60;
						resultJson = DownloadMain.getContentClient(url, null);
						if ("0".equals(resultJson)) {
							// 下载不成功
							System.err.println("PID:" + goods.getPid() + ",橱窗图60x60["
									+ windowImg.replace(".jpg", ".60x60.jpg") + "]下载失败");
							LOG.error("PID:" + goods.getPid() + ",橱窗图60x60[" + windowImg.replace(".jpg", ".60x60.jpg")
									+ "]下载失败");
							return false;
						} else {
							newImgs.add(goods.getPid() + "/" + windowSuffix60);
							goods.setRemotPath(SHOWURL);
							goods.setLocalPath(SHOWURL);
							isSuccess = true;
						}
					}
				}
			}
			if (isSuccess) {
				// 更新成功重新赋值橱窗图数据
				goods.setImg(newImgs.toString());
			} else {
				return false;
			}
		}

		// 4.详情图
		Document htmlDoc = Jsoup.parseBodyFragment(goods.getEninfo());
		// 获取所有的 img属性的图片
		Elements infoImgLst = htmlDoc.getElementsByTag("img");
		// 判断含有图片时进行图片下载处理
		if (infoImgLst.size() > 0) {
			for (Element imEl : infoImgLst) {
				// 判断本地图片，直接替换路径
				String infoImg = imEl.attr("src");
				if (infoImg.contains(SHOWURL)) {
					imEl.attr("src", infoImg.replace(SHOWURL, ""));
				} else {
					// 判断网络图片，下载本地
					String infoFileSuffix = infoImg;
					// 取出后缀名称
					int infoLastIndexOf = infoImg.lastIndexOf("/");
					if (infoLastIndexOf > -1) {
						infoFileSuffix = infoFileSuffix.substring(infoLastIndexOf);
					}

					// 图片服务器下载ali图片
					url = ContentConfig.PICTURE_SERVER_IMG_1688_DOWN + "imgUrl=" + infoImg + "&newImgName="
							+ goods.getPid() + "/" + infoFileSuffix;
					resultJson = DownloadMain.getContentClient(url, null);
					if ("0".equals(resultJson)) {
						// 下载不成功
						System.err.println("PID:" + goods.getPid() + ",infoImg[" + infoImg + "]下载失败");
						LOG.error("PID:" + goods.getPid() + ",infoImg[" + infoImg + "]下载失败");
						return false;
					} else {
						imEl.attr("src", goods.getPid() + "/" + infoFileSuffix);
					}
				}
			}
			if (isSuccess) {
				// 更新成功重新赋值详情图数据
				goods.setEninfo(htmlDoc.html());
				goods.setRemotPath(SHOWURL);
				goods.setLocalPath(SHOWURL);
			} else {
				return false;
			}
		}

		return isSuccess;
	}

	// 处理1688商品的规格图片数据
	private List<TypeBean> deal1688GoodsType(CustomOnlineGoodsBean cgbean) {// 规格
		List<TypeBean> typeList = new ArrayList<TypeBean>();
		if (!(cgbean.getEntype() == null || "".equals(cgbean.getEntype()))) {
			Map<String, List<TypeBean>> typeMap = new HashMap<String, List<TypeBean>>();
			String types = cgbean.getEntype();
			// String localPath = cgbean.getLocalpath();
			if (StringUtils.isNotBlank(types) && !StringUtils.equals(types, "[]")) {
				types = types.replace("[[", "[").replace("]]", "]").trim();
				String[] matchStrList = types.split(",\\s*\\[");
				TypeBean typeBean = null;
				String[] tems = null;
				String tem = null;
				for (String str : matchStrList) {
					str = str.replace("[", "").replace("]", "");
					if (str.isEmpty()) {
						continue;
					}
					typeBean = new TypeBean();
					String[] type = str.split(",\\s*");
					for (int j = 0; j < type.length; j++) {
						if (type[j].indexOf("id=") > -1) {
							tems = type[j].split("id=");
							tem = tems.length > 1 ? tems[1] : "";
							typeBean.setId(tem);
						} else if (type[j].indexOf("type=") > -1) {
							tems = type[j].split("type=");
							tem = tems.length > 1 ? tems[1] : "";
							typeBean.setType(tem.replaceAll(chineseChar, ""));
							typeBean.setLableType(tem.replaceAll(chineseChar, ""));
						} else if (type[j].indexOf("value=") > -1) {
							tems = type[j].split("value=");
							tem = tems.length > 1 ? tems[1] : "";
							tem = StringUtils.equals(tem, "null") ? String.valueOf(j) : tem;
							typeBean.setValue(tem.replaceAll(chineseChar, ""));
						} else if (type[j].indexOf("img=") > -1) {
							tems = type[j].split("img=");
							tem = tems.length > 1 ? tems[1] : "";
							tem = tem.endsWith(".jpg") ? tem : "";
							if (StringUtils.isBlank(tem) || StringUtils.equals(tem, "null")) {
								typeBean.setImg("");
							} else {
								if (tem.indexOf("http://") > -1 || tem.indexOf("https://") > -1) {
									typeBean.setImg(tem);
								} else {
									typeBean.setImg(SHOWURL + tem);
								}
							}
						}
					}
					List<TypeBean> list = typeMap.get(typeBean.getType());
					if (list == null) {
						list = new ArrayList<TypeBean>();
					}
					if (StringUtils.isBlank(typeBean.getType())) {
						continue;
					}
					if (StringUtils.isBlank(typeBean.getValue())) {
						typeBean.setType(typeBean.getId());
					}
					list.add(typeBean);
					typeMap.put(typeBean.getType(), list);
				}
				Iterator<Entry<String, List<TypeBean>>> iterator = typeMap.entrySet().iterator();
				while (iterator.hasNext()) {
					typeList.addAll(iterator.next().getValue());
				}
			}
		}
		return typeList;
	}

	// 处理1688商品的规格图片数据
	private List<String> deal1688GoodsImg(CustomOnlineGoodsBean cgbean, String remotPath) {

		List<String> imgList = new ArrayList<String>();
		// 图片
		String img = cgbean.getImg();
		if (StringUtils.isNotBlank(img)) {
			img = img.replace("[", "").replace("]", "").trim();
			String[] imgs = img.split(",\\s*");

			for (int i = 0; i < imgs.length; i++) {
				if (!imgs[i].isEmpty()) {
					if (imgs[i].indexOf("http://") > -1 || imgs[i].indexOf("https://") > -1) {
						imgList.add(imgs[i]);
					} else {
						imgList.add(remotPath + imgs[i]);
					}
				}
			}
		}
		return imgList;
	}

}
