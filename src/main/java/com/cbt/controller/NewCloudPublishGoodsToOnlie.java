package com.cbt.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.service.NewCloudGoodsService;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.NewFtpUtil;
import com.cbt.website.util.JsonResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewCloudPublishGoodsToOnlie extends Thread {
	private static final Log LOG = LogFactory.getLog(NewCloudPublishGoodsToOnlie.class);

	private String pid;
	private NewCloudGoodsService newCloudGoodsService;
	private FtpConfig ftpConfig;

	public NewCloudPublishGoodsToOnlie(String pid, NewCloudGoodsService newCloudGoodsService, FtpConfig ftpConfig) {
		super();
		this.pid = pid;
		this.newCloudGoodsService = newCloudGoodsService;
		this.ftpConfig = ftpConfig;
	}

	@Override
	public void run() {

		List<String> imgList = new ArrayList<String>();

		try {

			LOG.info("Pid : " + pid + " Execute Start");

			// 获取配置文件信息
			if (ftpConfig == null) {
				ftpConfig = GetConfigureInfo.getFtpConfig();
			}
			String localShowPath = ftpConfig.getLocalShowPath();
			String remoteShowPath = ftpConfig.getRemoteShowPath();

			// 根据pid获取商品信息
			CustomGoodsPublish goods = newCloudGoodsService.getGoods(pid, 0);
			// 判断是否处于发布中的状态
			if (goods.getGoodsState() != 1) {

				// 设置商品处于发布中的状态
				int updateState = newCloudGoodsService.updateGoodsState(pid, 1);
				if (updateState > 0) {
					// 提取远程保存路径
					String remotepath = goods.getRemotpath();

					// 获取橱窗图的img List集合
					List<String> windowImgs = deal1688GoodsImg(goods.getImg(), goods.getRemotpath());
					// 抽取含有本地上传的图片数据
					if (windowImgs.size() > 0) {
						for (int i = 0; i < windowImgs.size(); i++) {
							String wdImg = windowImgs.get(i);
							if (wdImg == null || "".equals(wdImg)) {
								continue;
							} else if (wdImg.indexOf(localShowPath) > -1) {
								imgList.add(wdImg);
								// 上面小图60x60的，下面大图400x400的
								imgList.add(wdImg.replace("60x60", "400x400"));
								// 替换本地路径为远程路径
								windowImgs.set(i, wdImg.replace(localShowPath, remoteShowPath));
							}
						}
						// 重新生成橱窗图的数据保存bean中
						goods.setImg(windowImgs.toString().replace(remotepath, ""));
					}

					// 详情数据的获取和解析img数据
					Document nwDoc = Jsoup.parseBodyFragment(goods.getEninfo());
					Elements imgEls = nwDoc.getElementsByTag("img");
					if (imgEls.size() > 0) {
						for (Element imel : imgEls) {
							String imgUrl = imel.attr("src");
							if (imgUrl == null || "".equals(imgUrl)) {
								continue;
							} else if (imgUrl.indexOf(localShowPath) > -1) {
								imgList.add(imgUrl);
								// 替换本地路径为远程路径
								imel.attr("src", imgUrl.replace(localShowPath, remoteShowPath));
							}
						}
						goods.setEninfo(nwDoc.html().replace(remotepath, ""));
					}

					// 判断需要上传的图片，执行上传逻辑
					if (imgList.size() > 0) {
						boolean isSuccess = true;
						// 循环单独上传图片
						for (String imgUrl : imgList) {
							// 得到图片服务器FTP后部分保存全路径
							String remoteSavePath = imgUrl.replace(localShowPath, "");
							System.err.println("imgUrl:" + imgUrl + ",remoteSavePath:" + remoteSavePath);
							// 本地图片全路径
							String localImgPath = ftpConfig.getLocalDiskPath() + remoteSavePath;

							File imgFile = new File(localImgPath);
							if (imgFile.exists()) {
								
								boolean isSc = false;
								JsonResult json = new JsonResult();
								json.setOk(false);
								// 重试5次
								int count = 0;
								while (!(json.isOk() || count > 5)) {
									count++;
									json = NewFtpUtil.uploadFileToRemoteSSM(remoteSavePath, localImgPath,
											ftpConfig);
									if (json.isOk()) {
										isSc = true;
										break;
									}else{
										isSc = false;
									}
								}
								if (isSc) {
									continue;
								} else {
									isSuccess = false;
									System.err.println("this pid:" + pid + "," + localImgPath + " upload error,"
											+ json.getMessage());
									LOG.error("this pid:" + pid + "," + localImgPath + " upload error,"
											+ json.getMessage());
									break;
								}
							} else {
								System.err.println("this pid:" + pid + ",file:" + localImgPath + " is not exists");
								LOG.error("this pid:" + pid + ",file:" + localImgPath + " is not exists");
								/*isSuccess = false;
								break;*/
							}
						}
						if (isSuccess) {
							newCloudGoodsService.publish(goods);
							newCloudGoodsService.updateGoodsState(pid, 4);
						} else {
							newCloudGoodsService.updateGoodsState(pid, 3);
						}
					} else {
						newCloudGoodsService.publish(goods);
						newCloudGoodsService.updateGoodsState(pid, 4);
					}
				} else {
					LOG.error("this pid:" + pid + " update goodsstate 1 error!");
				}
			} else {
				LOG.warn("UploadImgToOnlie pid:" + pid + " is uploading!");
			}
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("UploadImgToOnlie error:" + e.getMessage());
			newCloudGoodsService.updateGoodsState(pid, 3);
		}
		LOG.info("Pid : " + pid + " Execute End");
	}

	// 处理1688商品的规格图片数据
	private List<String> deal1688GoodsImg(String img, String remotPath) {

		List<String> imgList = new ArrayList<String>();

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
