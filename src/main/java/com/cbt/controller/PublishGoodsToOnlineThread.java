package com.cbt.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import com.cbt.parse.service.ImgDownload;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.GoodsInfoUtils;
import com.cbt.website.util.UploadByOkHttp;
import com.importExpress.utli.ImageCompressionByNoteJs;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishGoodsToOnlineThread extends Thread {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PublishGoodsToOnlineThread.class);
    /**
     * 图片服务器选择主图路径
     */
    private static final String IMG_SERVICE_CHOOSE_MAIN_IMG_URL = "http://104.247.194.50:8080/cloudimginterface/selectSearchImg/editedGoodsMainImg?";
    // private static final String IMG_SERVICE_CHOOSE_MAIN_IMG_URL = "http://127.0.0.1:8765/cloudimginterface/selectSearchImg/editedGoodsMainImg?";

    private String pid;
    private CustomGoodsService customGoodsService;
    private FtpConfig ftpConfig;
    private int isUpdateImg;
    private CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();

    public PublishGoodsToOnlineThread(String pid, CustomGoodsService customGoodsService, FtpConfig ftpConfig, int isUpdateImg) {
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
            goods.setIsUpdateImg(isUpdateImg);
            // 判断是否处于发布中的状态
            if (goods.getGoodsState() != 1) {

                // 设置商品处于发布中的状态
                int updateState = customGoodsService.updateGoodsState(pid, 1);
                if (updateState > 0) {
                    String firstImg = "";
                    // 提取远程保存路径
                    String remotepath = goods.getRemotpath();

                    // 获取橱窗图的img List集合
                    List<String> windowImgs = GoodsInfoUtils.deal1688GoodsImg(goods.getImg(), goods.getRemotpath());
                    // 抽取含有本地上传的图片数据
                    if (windowImgs.size() > 0) {
                        List<String> tempImgs = new ArrayList<>();
                        for (int i = 0; i < windowImgs.size(); i++) {
                            String wdImg = windowImgs.get(i);
                            if (StringUtils.isBlank(wdImg)) {
                                continue;
                            } else if (wdImg.contains(localShowPath)) {
                                // 判断图片是否存在，不存在删除
                                if (checkIsExistsLocalImg(wdImg.replace(localShowPath, ftpConfig.getLocalDiskPath()))) {
                                    imgList.add(wdImg);
                                    // 上面小图60x60的，下面大图400x400的
                                    imgList.add(wdImg.replace("60x60", "400x400"));
                                    // 替换本地路径为远程路径
                                    tempImgs.add(wdImg.replace(localShowPath, remoteShowPath));
                                } else {
                                    // 本地文件不存的，删除数据
                                    windowImgs.set(i, "");
                                }
                            }else if(wdImg.contains("192.168.1")){
                                // 清空原来服务器上传的图片数据，原因：图片路劲对应服务器本地路劲已经失效，无法再同步到服务器
                                windowImgs.set(i, "");
                            }else{
                                tempImgs.add(wdImg);
                            }
                        }
                        // 重新生成橱窗图的数据保存bean中
                        goods.setImg(tempImgs.toString().replace(remotepath, ""));
                        // 获取第一张图片数据的大图
                        firstImg = tempImgs.get(0).replace(".60x60", ".400x400");
                        goods.setShowMainImage(firstImg);
                    }

                    // 详情数据的获取和解析img数据
                    Document nwDoc = Jsoup.parseBodyFragment(goods.getEninfo());
                    Elements imgEls = nwDoc.getElementsByTag("img");
                    if (imgEls.size() > 0) {
                        for (Element imel : imgEls) {
                            String imgUrl = imel.attr("src");
                            if (StringUtils.isBlank(imgUrl)) {
                                continue;
                            } else if (imgUrl.contains(localShowPath)) {
                                if (checkIsExistsLocalImg(imgUrl.replace(localShowPath, ftpConfig.getLocalDiskPath()))) {
                                    imgList.add(imgUrl);
                                    // 替换本地路径为远程路径
                                    imel.attr("src", imgUrl.replace(localShowPath, remoteShowPath));
                                } else {
                                    // 本地文件不存在的，移除
                                    imel.remove();
                                }
                            }else if(imgUrl.contains("192.168.1")){
                                // 判断本地路径非当前配置的上传图片地址，移除数据
                                imel.remove();
                            }
                        }
                        goods.setEninfo(nwDoc.html().replace(remotepath, ""));
                    }

                    // 判断需要上传的图片，执行上传逻辑
                    if (imgList.size() > 0) {
                        boolean isSuccess = true;
                        // 使用批量上传文件代码
                        Map<String, String> uploadMap = new HashMap<>();
                        // 循环单独上传图片
                        for (String imgUrl : imgList) {
                            // 得到图片服务器FTP后部分保存全路径
                            String remoteSavePath = imgUrl.replace(localShowPath, "");
                            String remoteSavePreFile =  FtpConfig.REMOTE_LOCAL_PATH + remoteSavePath.substring(0,remoteSavePath.lastIndexOf("/"));
                            System.err.println("imgUrl:" + imgUrl + ",remoteSavePreFile:" + remoteSavePreFile);
                            // 本地图片全路径
                            String localImgPath = ftpConfig.getLocalDiskPath() + remoteSavePath;
                            File imgFile = new File(localImgPath);
                            if (imgFile.exists()) {
                                uploadMap.put(localImgPath,remoteSavePreFile);
                                /*boolean isSc = GoodsInfoUtils.uploadFileToRemoteSSM(pid, remoteSavePath, localImgPath, ftpConfig);
                                if (isSc) {
                                    continue;
                                } else {
                                    isSuccess = false;
                                    break;
                                }*/
                            } else {
                                System.err.println("this pid:" + pid + ",file:" + localImgPath + " is not exists");
                                LOG.error("this pid:" + pid + ",file:" + localImgPath + " is not exists");
                                isSuccess = false;
                                break;
                            }
                        }
                        //批量上传
                        if(isSuccess){
                            isSuccess = UploadByOkHttp.doUpload(uploadMap);
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

                    // isUpdateImg = 1;
                    if (isUpdateImg > 0) {
                        // 下载需要的图片到本地
                        // 新的主图名称
                        String downImgName = goods.getShowMainImage().substring(goods.getShowMainImage().lastIndexOf("/"));
                        // 图片下载本地路径名称
                        String localDownImgPre = ftpConfig.getLocalDiskPath() + pid + "/edit";
                        String localDownImg = localDownImgPre + downImgName.replace(".220x220", ".400x400");
                        boolean isSuccess = ImgDownload.downFromImgService(goods.getShowMainImage(), localDownImg);
                        System.err.println("down[" + goods.getShowMainImage() + "] to [" + localDownImg + "]");
                        if (isSuccess) {
                            System.err.println("localDownImg:" + localDownImg + ",success!!");
                            //压缩图片 220x200 285x285 285x380
                            boolean isCompress;
                            boolean isCompress1 = ImageCompressionByNoteJs.compressByOkHttp(localDownImg, 2);
                            boolean isCompress2 = ImageCompressionByNoteJs.compressByOkHttp(localDownImg, 3);
                            boolean isCompress3 = ImageCompressionByNoteJs.compressByOkHttp(localDownImg, 4);
                            isCompress = isCompress1 && isCompress2 && isCompress3;
                            // 压缩成功后，上传图片
                            if (isCompress) {
                                System.err.println("Compress:[" + localDownImg  + "] 285x285,285x380,img220x220 success");
                                String destPath = GoodsInfoUtils.changeRemotePathToLocal(remotepath + pid);
                                //上传
                                File upFile = new File(localDownImgPre);
                                if (upFile.exists() && upFile.isDirectory()) {
                                    boolean isUpload;
                                    isUpload = UploadByOkHttp.uploadFileBatch(upFile, destPath);
                                    if (isUpload) {
                                        System.err.println("this pid:" + pid + ",上传产品主图成功");
                                    } else {
                                        // 重试一次
                                        isUpload = UploadByOkHttp.uploadFileBatch(upFile, destPath);
                                        if (isUpload) {
                                            System.err.println("this pid:" + pid + ",上传产品主图成功");
                                        } else {
                                            System.err.println("this pid:" + pid + ",上传产品主图失败");
                                            isSuccess = false;
                                        }
                                    }
                                } else {
                                    System.err.println("this pid:" + pid + ",下载图片文件夹[" + localDownImgPre + "] 不存在----");
                                    LOG.error("this pid:" + pid + ",下载图片文件夹[" + localDownImgPre + "] 不存在----");
                                    isSuccess = false;
                                }
                            } else {
                                System.err.println("this pid:" + pid + ",压缩img [" + localDownImg + "] error----");
                                LOG.error("this pid:" + pid + ",压缩img [" + localDownImg + "] error----");
                                isSuccess = false;
                            }
                        } else {
                            LOG.error("this pid:" + pid + ",下载图片失败,无法设置主图");
                        }
                        if (!isSuccess) {
                            customGoodsService.updateGoodsState(pid, 3);
                        }
                    }
                } else {
                    LOG.error("this pid:" + pid + " update goodsstate error!");
                }
            } else {
                LOG.warn("UploadImgToOnline pid:" + pid + " is uploading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("UploadImgToOnline error:" + e.getMessage());
            System.err.println("UploadImgToOnline error:" + e.getMessage());
            customGoodsService.updateGoodsState(pid, 3);
        }
        LOG.info("Pid : " + pid + " Execute End");
    }

    private boolean checkIsExistsLocalImg(String fileName){
        File file = new File(fileName);
        return file.exists() && file.isFile();
    }



}
