package com.cbt.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.parse.service.DownloadMain;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.NewFtpUtil;
import com.cbt.website.util.JsonResult;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PublishGoodsToOnlie extends Thread {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PublishGoodsToOnlie.class);
    /**
     * 图片服务器选择主图路径
     */
    private static final String IMG_SERVICE_CHOOSE_MAIN_IMG_URL = "http://104.247.194.50:8080/cloudimginterface/selectSearchImg/editedGoodsMainImg?";
    // private static final String IMG_SERVICE_CHOOSE_MAIN_IMG_URL = "http://127.0.0.1:8765/cloudimginterface/selectSearchImg/editedGoodsMainImg?";

    private static final String SERVICE_LOCAL_PATH = "/usr/local/goodsimg";
    private static final String SERVICE_SHOW_URL_1 = "http://img.import-express.com";
    private static final String SERVICE_SHOW_URL_2 = "http://img1.import-express.com";
    private static final String SERVICE_SHOW_URL_3 = "https://img.import-express.com";
    private static final String SERVICE_SHOW_URL_4 = "https://img1.import-express.com";

    private String pid;
    private CustomGoodsService customGoodsService;
    private FtpConfig ftpConfig;
    private int isUpdateImg;

    public PublishGoodsToOnlie(String pid, CustomGoodsService customGoodsService, FtpConfig ftpConfig, int isUpdateImg) {
        super();
        this.pid = pid;
        this.customGoodsService = customGoodsService;
        this.ftpConfig = ftpConfig;
        this.isUpdateImg = isUpdateImg;
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
            CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pid, 0);
            // 判断是否处于发布中的状态
            if (goods.getGoodsState() != 1) {

                // 设置商品处于发布中的状态
                int updateState = customGoodsService.updateGoodsState(pid, 1);
                if (updateState > 0) {
                    String firstImg = "";
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
                        // 获取第一张图片数据的大图
                        firstImg = windowImgs.get(0).replace(".60x60", ".400x400");
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
                                    json = NewFtpUtil.uploadFileToRemoteSSM(remoteSavePath, localImgPath, ftpConfig);
                                    if (json.isOk()) {
                                        isSc = true;
                                        break;
                                    } else {
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
                                isSuccess = false;
                                break;
                            }
                        }
                        if (isSuccess) {
                            customGoodsService.publish(goods);
                            customGoodsService.updateGoodsState(pid, 4);
                        } else {
                            customGoodsService.updateGoodsState(pid, 3);
                        }
                    } else {
                        customGoodsService.publish(goods);
                        customGoodsService.updateGoodsState(pid, 4);
                    }

                    if (isUpdateImg > 0) {
                        // 进行搜索图设置，取橱窗图的第一张大图数据
                        String resultStr = "";
                        int count = 0;
                        String localPathByRemote = "";
                        if (remotepath.contains(SERVICE_SHOW_URL_1)) {
                            localPathByRemote = remotepath.replace(SERVICE_SHOW_URL_1, SERVICE_LOCAL_PATH);
                        } else if (remotepath.contains(SERVICE_SHOW_URL_2)) {
                            localPathByRemote = remotepath.replace(SERVICE_SHOW_URL_2, SERVICE_LOCAL_PATH);
                        } else if (remotepath.contains(SERVICE_SHOW_URL_3)) {
                            localPathByRemote = remotepath.replace(SERVICE_SHOW_URL_3, SERVICE_LOCAL_PATH);
                        } else if (remotepath.contains(SERVICE_SHOW_URL_4)) {
                            localPathByRemote = remotepath.replace(SERVICE_SHOW_URL_4, SERVICE_LOCAL_PATH);
                        }
                        String oldMainImg = localPathByRemote + goods.getShowMainImage();
                        // 防止出现是上传的第一张图片数据
                        String tempFirstImg = "";
                        if (firstImg.contains("http://") || firstImg.contains("https://")) {
                            if (firstImg.contains(SERVICE_SHOW_URL_1)) {
                                tempFirstImg = firstImg.replace(SERVICE_SHOW_URL_1, SERVICE_LOCAL_PATH);
                            } else if (firstImg.contains(SERVICE_SHOW_URL_2)) {
                                tempFirstImg = firstImg.replace(SERVICE_SHOW_URL_2, SERVICE_LOCAL_PATH);
                            } else if (firstImg.contains(SERVICE_SHOW_URL_3)) {
                                tempFirstImg = firstImg.replace(SERVICE_SHOW_URL_3, SERVICE_LOCAL_PATH);
                            } else if (firstImg.contains(SERVICE_SHOW_URL_4)) {
                                tempFirstImg = firstImg.replace(SERVICE_SHOW_URL_4, SERVICE_LOCAL_PATH);
                            }
                        } else {
                            tempFirstImg = localPathByRemote + firstImg;
                        }

                        String url = IMG_SERVICE_CHOOSE_MAIN_IMG_URL + "oldMainImg=" + oldMainImg + "&firstImg=" + tempFirstImg;
                        while (!("1".equals(resultStr) || count > 3)) {
                            count++;
                            resultStr = DownloadMain.getContentClient(url, null);
                        }
                        if (!"1".equals(resultStr)) {
                            LOG.error("this pid:" + pid + ",选择设置主图失败");
                        }
                    }
                } else {
                    LOG.error("this pid:" + pid + " update goodsstate error!");
                }
            } else {
                LOG.warn("UploadImgToOnlie pid:" + pid + " is uploading!");
            }
        } catch (Exception e) {
            e.getStackTrace();
            LOG.error("UploadImgToOnlie error:" + e.getMessage());
            customGoodsService.updateGoodsState(pid, 3);
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
