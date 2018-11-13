package com.cbt.report.ctrl;

import com.cbt.bean.CustomOnlineGoodsBean;
import com.cbt.bean.TypeBean;
import com.cbt.customer.dao.IShopUrlDao;
import com.cbt.customer.dao.ShopUrlDaoImpl;
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

import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @ClassName ShopGoodsToOnline
 * @Description 店铺商品上线
 * @author Jxw
 * @date 2018年2月27日
 */
public class ShopGoodsToOnline {
	private static final Log LOG = LogFactory.getLog(ShopGoodsToOnline.class);
	//private static final String SHOWURL = "http://192.168.1.100:8765/editimg/shopimg/";
	//private static final String SHOWURL = "http://192.168.1.219:8765/editimg/shopimg/";
	private static final String SHOWURL = "http://img1.import-express.com/importcsvimg/shopimg/";
	private String chineseChar = "([\\一-\\龥]+)";

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
		IShopUrlDao shopDao = new ShopUrlDaoImpl();
		List<CustomOnlineGoodsBean> goodsList = shopDao.queryReadyDealGoods(shopId);

		if (goodsList == null || goodsList.size() == 0) {
			System.err.println("当前无清理好的数据!!");
			json.setOk(true);
		} else {
			shopDao.updateShopState(shopId, 1);
			System.err.println("当前已经清理的数据数量：" + goodsList.size());
			for (int i = 0; i < goodsList.size(); i++) {
				// 图片已经下载的
				if (goodsList.get(i).getImgDownFlag() > 0) {
					syncSingleGoodsToOnline(shopDao, goodsList.get(i), shopId);
				} else {
					// 图片未下载的

					boolean isSuccess = false;
					int downFlag = 0;
					while (!isSuccess && downFlag < 3) {
						downFlag++;
						isSuccess = downloadNetImg(goodsList.get(i));
					}
					// isSuccess = downloadNetImg(goodsList.get(i));
					if (isSuccess) {
						updateDownloadImgGoods(shopDao, goodsList.get(i), shopId);
					} else {
						shopDao.updateGoodsImgError(goodsList.get(i));
						System.err.println("--pid:" + goodsList.get(i).getPid() + " downloadNetImg error!!!");
					}
				}
			}
		}
		System.out.println("publicGoodsToOnline end!!!");
		return json;
	}

	private boolean syncSingleGoodsToOnline(IShopUrlDao shopDao, CustomOnlineGoodsBean goods, String shopId) {

		boolean isSync = false;
		CustomOnlineGoodsBean nwgoods = shopDao.queryGoodsByShopIdAndPid(shopId, goods.getPid());
		if (nwgoods == null) {
			System.err.println("pid:" + goods.getPid() + " 查询失败");
		} else {
			isSync = false;
			int syncCount = 0;
			// 重试2次
			while (!isSync && syncCount < 3) {
				syncCount++;
				if(!(nwgoods.getEninfo() == null || "".equals(nwgoods.getEninfo()) || nwgoods.getEninfo().length() < 15)){
					nwgoods.setIsShowDetImgFlag(1);
				}
				isSync = shopDao.syncSingleGoodsToOnline(nwgoods, shopId);
			}
			if (isSync) {
				shopDao.updateShopState(shopId, 2);
				System.err.println("--pid:" + goods.getPid() + " insert success");
			} else {
				shopDao.updateShopState(shopId, 3);
				System.err.println("--pid:" + goods.getPid() + " insert error!!!");
			}
		}
		return isSync;
	}

	/**
	 * 
	 * @Title downloadNetImg
	 * @Description 下载网络图片
	 * @param goods
	 * @return JsonResult
	 */
	private boolean downloadNetImg(CustomOnlineGoodsBean goods) {
		String localPath = goods.getLocalPath() == null ? "" : goods.getLocalPath();
		boolean isSuccess = false;
		// 分类别下载goods中的图片
		// 1.搜索图，格式是200x200.jpg替换.jpg
		String fullImg = localPath + goods.getCustomMainImage();
		String mainImg = fullImg;
		String mainFileSuffix = fullImg;
		// 取出后缀名称
		int mainLastIndexOf = mainImg.lastIndexOf("/");
		if (mainLastIndexOf > -1) {
			mainFileSuffix = mainImg.substring(mainLastIndexOf + 1).replace(".400x400", "");
			if (mainFileSuffix.indexOf(".220x220.") == -1) {
				mainFileSuffix = mainFileSuffix.replace(".jpg", ".220x220.jpg");
			}
		}
		if (!downImgToService(fullImg,goods.getPid() + "/"+ mainFileSuffix)) {
			// 下载不成功
			System.err.println("PID:" + goods.getPid() + ",CustomMainImage[" + fullImg + "] 下载失败");
			LOG.error("PID:" + goods.getPid() + ",CustomMainImage[" + fullImg + "] 下载失败");
			return false;
		} else {
			goods.setCustomMainImage(goods.getPid() + "/" + mainFileSuffix);
			goods.setRemotPath(SHOWURL);
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
				typeImg = localPath + tyImg.getImg();
				// 判断规格是否有图片
				if (!(tyImg.getImg() == null || "".equals(tyImg.getImg().trim()) || tyImg.getImg().length() < 5)) {
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
							typeSuffix60 = typeImg.substring(typeIndexOf + 1);
							if (typeSuffix60.indexOf(".60x60.") == -1) {
								typeSuffix60 = typeSuffix60.replace(".jpg", ".60x60.jpg");
							}
							typeSuffix400 = typeImg.substring(typeIndexOf + 1);
							if (typeSuffix400.indexOf(".60x60.") > -1) {
								typeSuffix400 = typeSuffix400.replace(".60x60.", ".400x400.");
							}
							if (typeSuffix400.indexOf(".400x400.") == -1) {
								typeSuffix400 = typeSuffix400.replace(".jpg", ".400x400.jpg");
							}
						}
						// 下载网络图片400x400
						// 图片服务器下载ali图片
						String tempImg400 = tyImg.getImg();
						if (tempImg400.indexOf(".60x60.") > -1) {
							tempImg400 = tempImg400.replace(".60x60.", ".400x400.");
						}
						if (tempImg400.indexOf(".400x400.") == -1) {
							tempImg400 = tempImg400.replace(".jpg", ".400x400.jpg");
						}
						if (!downImgToService(tempImg400,goods.getPid() + "/"+ typeSuffix400)) {
							// 下载不成功
							System.err.println(
									"PID:" + goods.getPid() + ",规格图400x400["  + tempImg400 + "]下载失败");
							LOG.error("PID:" + goods.getPid() + ",规格图400x400[" + tempImg400 + "]下载失败");
							return false;
						} else {
							// 图片服务器下载ali图片
							String tempImg60 = tyImg.getImg();
							if (tempImg60.indexOf(".60x60.") == -1) {
								tempImg60 = tempImg60.replace(".jpg", ".60x60.jpg");
							}
							if (!downImgToService(tempImg60,goods.getPid() + "/"+ typeSuffix60)) {
								// 下载不成功
								System.err.println(
										"PID:" + goods.getPid() + ",规格图60x60[" + tempImg60 + "]下载失败");
								LOG.error("PID:" + goods.getPid() + ",规格图60x60[" + tempImg60 + "]下载失败");
								return false;
							} else {
								tyImg.setImg(goods.getPid() + "/" + typeSuffix60);
								goods.setRemotPath(SHOWURL);
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
		List<String> imgs = deal1688GoodsImg(goods, localPath);
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
						windowSuffix60 = windowSuffix60.substring(windowIndexOf + 1);
						if (windowSuffix60.indexOf(".60x60.") == -1) {
							windowSuffix60 = windowSuffix60.replace(".jpg", ".60x60.jpg");
						}
						windowSuffix400 = windowSuffix400.substring(windowIndexOf + 1);
						if (windowSuffix400.indexOf(".60x60.") > -1) {
							windowSuffix400 = windowSuffix400.replace(".60x60.", ".400x400.");
						}
						if (windowSuffix400.indexOf(".400x400.") == -1) {
							windowSuffix400 = windowSuffix400.replace(".jpg", ".400x400.jpg");
						}
					}
					// 下载网络图片400x400
					String tempWindowImg400 = windowImg;
					if (tempWindowImg400.indexOf(".60x60.") > -1) {
						tempWindowImg400 = tempWindowImg400.replace(".60x60.", ".400x400.");
					}
					if (tempWindowImg400.indexOf(".400x400.") == -1) {
						tempWindowImg400 = tempWindowImg400.replace(".jpg", ".400x400.jpg");
					}
					if (!downImgToService(tempWindowImg400,goods.getPid() + "/"+ windowSuffix400)) {
						// 下载不成功
						System.err.println("PID:" + goods.getPid() + ",橱窗图400x400[" + tempWindowImg400 + "]下载失败");
						LOG.error("PID:" + goods.getPid() + ",橱窗图[" + tempWindowImg400 + "]400x400下载失败");
						return false;
					} else {
						String tempWindowImg60 = windowImg;
						if (tempWindowImg60.indexOf(".60x60.") == -1) {
							tempWindowImg60 = tempWindowImg60.replace(".jpg", ".60x60.jpg");
						}
						if (!downImgToService(tempWindowImg60,goods.getPid() + "/"+ windowSuffix60)) {
							// 下载不成功
							System.err.println("PID:" + goods.getPid() + ",橱窗图60x60[" + tempWindowImg60 + "]下载失败");
							LOG.error("PID:" + goods.getPid() + ",橱窗图60x60[" + tempWindowImg60 + "]下载失败");
							return false;
						} else {
							newImgs.add(goods.getPid() + "/" + windowSuffix60);
							goods.setRemotPath(SHOWURL);
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
					if (!downImgToService(localPath + infoImg,goods.getPid() + "/desc"+ infoFileSuffix)) {
						// 下载不成功
						System.err.println("PID:" + goods.getPid() + ",infoImg[" + localPath + infoImg + "]下载失败");
						LOG.error("PID:" + goods.getPid() + ",infoImg[" + localPath + infoImg + "]下载失败");
						return false;
					} else {
						imEl.attr("src", goods.getPid() + "/desc" + infoFileSuffix);
					}
				}
			}
			if (isSuccess) {
				// 更新成功重新赋值详情图数据
				goods.setEninfo(htmlDoc.html());
				goods.setRemotPath(SHOWURL);
			} else {
				return false;
			}
		}

		return isSuccess;
	}

	// 处理1688商品的规格图片数据
	private List<String> deal1688GoodsImg(CustomOnlineGoodsBean cgbean, String localPath) {

		List<String> imgList = new ArrayList<String>();
		// 图片
		String img = cgbean.getImg();
		if (StringUtils.isNotBlank(img)) {
			img = img.replace("[", "").replace("]", "").trim();
			String[] imgs = img.split(",\\s*");

			for (int i = 0; i < imgs.length; i++) {
				if (!imgs[i].isEmpty()) {
					// imgList.add(remotPath + imgs[i].replace(".60x60.jpg",
					// ""));
					// 统一路径，下面代码屏蔽
					if (imgs[i].indexOf("http://") > -1 || imgs[i].indexOf("https://") > -1) {
						imgList.add(imgs[i]);
					} else {
						imgList.add(localPath + imgs[i]);
					}
				}
			}
		}
		return imgList;
	}

	private boolean updateDownloadImgGoods(IShopUrlDao shopDao, CustomOnlineGoodsBean goods, String shopId) {
		boolean isUpdate = false;
		int updateFlag = 0;
		while (!isUpdate && updateFlag < 3) {
			updateFlag++;
			isUpdate = shopDao.updateGoodsImg(goods);
		}
		if (isUpdate) {
			syncSingleGoodsToOnline(shopDao, goods, shopId);
		} else {
			System.err.println("--pid:" + goods.getPid() + " update Img error!!!");
		}
		return isUpdate;
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
									typeBean.setImg(cgbean.getLocalPath() + tem);
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
	
	/**
	 * 
	 * @Title downImgToService 
	 * @Description 本地图片下载到图片服务器
	 * @param localFileName
	 * @param remoteFileName
	 * @return
	 * @return boolean
	 */
	private boolean downImgToService(String localFileName,String remoteFileName){
		String url = ContentConfig.SHOP_GOODS_IMG_DOWN + "imgUrl=" + localFileName + "&newImgName=" + remoteFileName;
		String resultJson = DownloadMain.getContentClient(url, null);
		//失败，重试一次
		if("0".equals(resultJson)){
			resultJson = DownloadMain.getContentClient(url, null);
			if("0".equals(resultJson)){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}

}
