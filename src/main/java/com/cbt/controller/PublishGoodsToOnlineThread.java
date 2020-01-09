package com.cbt.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import com.cbt.parse.service.ImgDownload;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.*;
import com.cbt.website.util.UploadByOkHttp;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.ImageCompressionByNoteJs;
import com.importExpress.utli.OKHttpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class PublishGoodsToOnlineThread implements Callable<Boolean> {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PublishGoodsToOnlineThread.class);

    private CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();
    private static List<String> kidsCatidList = new ArrayList<>();

    private static final String DOWN_IMG_PATH = "/usr/local/downImg/";
    /**
     * 图片服务器选择主图路径
     */
    // private static final String IMG_SERVICE_CHOOSE_MAIN_IMG_URL = "http://104.247.194.50:8080/cloudimginterface/selectSearchImg/editedGoodsMainImg?";
    // private static final String IMG_SERVICE_CHOOSE_MAIN_IMG_URL = "http://127.0.0.1:8765/cloudimginterface/selectSearchImg/editedGoodsMainImg?";

    private String pid;
    private CustomGoodsService customGoodsService;
    private FtpConfig ftpConfig;
    private int isUpdateImg;
    private int adminId;

    // private CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();
    public PublishGoodsToOnlineThread(String pid, CustomGoodsService customGoodsService, FtpConfig ftpConfig, int isUpdateImg, int adminId) {
        super();
        this.pid = pid;
        this.customGoodsService = customGoodsService;
        this.ftpConfig = ftpConfig;
        this.isUpdateImg = isUpdateImg;
        this.adminId = adminId;
    }

    public Boolean call() throws Exception {

        List<String> imgList = new ArrayList<String>();
        boolean executeSu = false;
        try {
            customGoodsService.insertIntoGoodsImgUpLog(pid, "", adminId, "test");
            LOG.info("Pid : " + pid + " Execute Start");
            // 获取配置文件信息
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            String localShowPath = ftpConfig.getLocalShowPath();
            String remoteShowPath = ftpConfig.getRemoteShowPath();
            // 根据pid获取商品信息
            CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pid, 0);
            /*int isKids = 0;
            if (checkIsKidsCatid(goods.getCatid1())) {
                isKids = 1;
            }*/
            int isKids = 0;

            boolean isCheckImg = false;
            if (goods.getValid() == 0 || goods.getValid() == 2) {
                isCheckImg = checkOffLineImg(goods, isKids);
                System.err.println("pid:" + goods.getPid() + ",isCheckImg:" + isCheckImg);
                if (isCheckImg) {
                    executeSu = doPublish(goods, localShowPath, remoteShowPath, imgList, isKids);
                } else {
                    executeSu = false;
                    System.err.println("pid:" + goods.getPid() + ",图片检查异常 --下架");
                    GoodsInfoUpdateOnlineUtil.setOffOnlineByPid(pid, "图片检查异常");
                }
            } else {
                executeSu = doPublish(goods, localShowPath, remoteShowPath, imgList, isKids);
            }
        } catch (Exception e) {
            e.printStackTrace();
            executeSu = false;
            LOG.error("PublishGoodsToOnlineThread pid:" + pid + " error:", e);
            System.err.println("PublishGoodsToOnlineThread pid:" + pid + " error:" + e.getMessage());
            customGoodsService.insertIntoGoodsImgUpLog(pid, "" + imgList.size(), adminId, "PublishGoodsToOnlineThread error:" + e.getMessage());
            customGoodsService.updateGoodsState(pid, 3);
        } finally {
            imgList.clear();
        }
        System.err.println("pid:" + pid + ",executeSu:" + executeSu);
        if (executeSu) {
            customGoodsService.deleteOnlineSync(pid);
        } else {
            System.err.println("pid:" + pid + ",更新失败---");
            customGoodsService.insertIntoOnlineSync(pid);
            GoodsInfoUpdateOnlineUtil.setOffOnlineByPid(pid, "更新失败");
        }
        LOG.info("Pid : " + pid + " Execute End");
        return executeSu;
    }


    private boolean doPublish(CustomGoodsPublish goods, String localShowPath, String remoteShowPath, List<String> imgList, int isKids) {
        boolean executeSu = false;
        if (StringUtils.isNotBlank(goods.getEninfo()) && goods.getEninfo().length() > 20) {
            goods.setIsShowDetImgFlag(1);
        }
        goods.setEntypeNew(ChangeEntypeUtils.getEntypeNew(goods.getEntype(), goods.getSku(), ""));
        goods.setIsUpdateImg(isUpdateImg);
        // 判断是否处于发布中的状态
        if (goods.getGoodsState() != 1) {
            // 设置商品处于发布中的状态
            int updateState = customGoodsService.updateGoodsState(pid, 1);
            if (updateState > 0) {
                // Thread.sleep(35000);
                dealWindowImg(goods, localShowPath, remoteShowPath, imgList);
                dealEninfoImg(goods, localShowPath, remoteShowPath, imgList);

                boolean isSuccess = true;
                // 判断需要上传的图片，执行上传逻辑
                if (imgList.size() > 0) {
                    isSuccess = uploadLocalImg(imgList, localShowPath, isKids);
                }
                if (isSuccess) {
                    // isUpdateImg = 1;
                    if (isUpdateImg > 0) {
                        executeSu = setWindowImgToMainImg(goods, isKids);
                    } else {
                        isSuccess = true;
                        executeSu = true;
                    }
                } else {
                    // 记录上传失败日志
                    customGoodsService.insertIntoGoodsImgUpLog(pid, "批量上传失败,size:" + imgList.size(), adminId, imgList.toString());
                    customGoodsService.updateGoodsState(pid, 3);
                }
                // 判断kids
                if (isSuccess) {
                    // isSuccess = dealKidsImgService(goods);
                }
            } else {
                // 记录上传失败日志
                customGoodsService.insertIntoGoodsImgUpLog(pid, "" + imgList.size(), adminId, "update goodsState error");
                LOG.error("this pid:" + pid + " update goodsstate error!");
            }
        } else {
            LOG.warn("PublishGoodsToOnlineThread pid:" + pid + " is uploading!");
        }
        return executeSu;
    }


    private boolean checkOffLineImg(CustomGoodsPublish goods, int isKids) {
        boolean isSu = false;
        try {
            List<String> allImgList = GoodsInfoUtils.getAllImgList(goods, isKids, 0);
            if (CollectionUtils.isNotEmpty(allImgList)) {
                isSu = downImgAndCheck(allImgList, goods.getPid());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("pid:" + goods.getPid() + ",checkOffLineImg error:", e);
        }
        return isSu;
    }

    /**
     * 下载图片并且检查
     *
     * @param allImgList
     * @param pid
     * @return
     */
    private boolean downImgAndCheck(List<String> allImgList, String pid) {

        String today = DateFormatUtil.formatDateToYearAndMonthString(LocalDateTime.now());
        String filePath = DOWN_IMG_PATH + today + "/" + pid;
        int imgSize = 0;
        boolean isSu = false;
        if (CollectionUtils.isNotEmpty(allImgList)) {
            imgSize = allImgList.size();
            for (String imgUrl : allImgList) {
                if (imgUrl.contains("/desc")) {
                    isSu = ImgDownload.downAndReTry(imgUrl, filePath + "/desc" + imgUrl.substring(imgUrl.lastIndexOf("/")));
                } else {
                    isSu = ImgDownload.downAndReTry(imgUrl, filePath + imgUrl.substring(imgUrl.lastIndexOf("/")));
                }
                if (!isSu) {
                    break;
                }
            }
        }

        if (isSu) {
            // 检查
            File tempFile = new File(filePath);
            int fileCount = 0;
            File[] childFiles = null;
            if (tempFile.exists() && tempFile.isDirectory()) {
                childFiles = tempFile.listFiles();
                for (File childFl : childFiles) {
                    if (!childFl.isDirectory()) {
                        fileCount++;
                    }
                }
                tempFile = new File(filePath + "/desc");
                if (tempFile.exists() && tempFile.isDirectory()) {
                    childFiles = tempFile.listFiles();
                    for (File childFl : childFiles) {
                        if (!childFl.isDirectory()) {
                            fileCount++;
                        }
                    }
                }
            }
            System.err.println("fileCount:" + fileCount + ",imgSize:" + imgSize);
            if (fileCount > 0 && fileCount == imgSize) {
                isSu = true;
            } else {
                isSu = false;
            }
        }
        return isSu;

    }


    private void dealWindowImg(CustomGoodsPublish goods, String localShowPath, String remoteShowPath, List<String> imgList) {
        // 获取橱窗图的img List集合
        String firstImg = "";
        String remotepath = goods.getRemotpath();
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
                        tempImgs.add(wdImg.replace(localShowPath, remoteShowPath).replace(".400x400.", ".60x60."));
                    } else {
                        // 本地文件不存的，删除数据
                        windowImgs.set(i, "");
                    }
                } else if (wdImg.contains("192.168.1")) {
                    // 清空原来服务器上传的图片数据，原因：图片路劲对应服务器本地路劲已经失效，无法再同步到服务器
                    windowImgs.set(i, "");
                } else {
                    tempImgs.add(wdImg.replace(".400x400.", ".60x60."));
                }
            }
            // 重新生成橱窗图的数据保存bean中
            goods.setImg(tempImgs.toString().replace(remotepath, ""));
            // 获取第一张图片数据的大图
            firstImg = tempImgs.get(0).replace(".60x60", ".400x400");
            if (isUpdateImg == 1) {
                goods.setShowMainImage(firstImg);
            }
        }
    }


    private void dealEninfoImg(CustomGoodsPublish goods, String localShowPath, String remoteShowPath, List<String> imgList) {
        // 详情数据的获取和解析img数据
        String remotepath = goods.getRemotpath();
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
                } else if (imgUrl.contains("192.168.1")) {
                    // 判断本地路径非当前配置的上传图片地址，移除数据
                    imel.remove();
                }
            }
            goods.setEninfo(nwDoc.html().replace(remotepath, ""));
        }
    }


    private boolean uploadLocalImg(List<String> imgList, String localShowPath, int isKids) {
        // 使用批量上传文件代码
        boolean isSuccess = true;
        Map<String, String> uploadMap = new HashMap<>();
        // 循环单独上传图片
        for (String imgUrl : imgList) {
            // 得到图片服务器FTP后部分保存全路径
            String remoteSavePath = imgUrl.replace(localShowPath, "");
            String remoteSavePreFile = FtpConfig.REMOTE_LOCAL_PATH + remoteSavePath.substring(0, remoteSavePath.lastIndexOf("/"));
            System.err.println("imgUrl:" + imgUrl + ",remoteSavePreFile:" + remoteSavePreFile);
            // 本地图片全路径
            String localImgPath = ftpConfig.getLocalDiskPath() + remoteSavePath;
            File imgFile = new File(localImgPath);
            if (imgFile.exists()) {
                uploadMap.put(localImgPath, remoteSavePreFile);
            } else {
                System.err.println("this pid:" + pid + ",file:" + localImgPath + " is not exists");
                LOG.error("this pid:" + pid + ",file:" + localImgPath + " is not exists");
                // 记录上传失败日志
                customGoodsService.insertIntoGoodsImgUpLog(pid, localImgPath, adminId, ",file:" + localImgPath + " is not exists");
                isSuccess = false;
                break;
            }
        }
        //批量上传
        if (isSuccess) {
            isSuccess = UploadByOkHttp.doUpload(uploadMap, isKids);
        }
        return isSuccess;
    }


    private boolean setWindowImgToMainImg(CustomGoodsPublish goods, int isKids) {
        boolean executeSu = false;
        boolean isSuccess = false;
        // 下载需要的图片到本地
        // 新的主图名称
        String downImgName = goods.getShowMainImage().substring(goods.getShowMainImage().lastIndexOf("/"));

        // 图片下载本地路径名称
        String localDownImgPre = ftpConfig.getLocalDiskPath() + pid + "/edit";
        String localDownImg = localDownImgPre + downImgName.replace(".220x220", ".400x400");
        deleteFileChild(localDownImgPre);

        String downImgUrl;
        if (isUpdateImg == 2) {
            downImgUrl = goods.getShowMainImage().replace(".220x220", ".400x400");
        } else {
            downImgUrl = goods.getShowMainImage();
        }
        isSuccess = ImgDownload.downFromImgService(downImgUrl, localDownImg);
        if (!isSuccess) {
            // 重新下载一次
            isSuccess = ImgDownload.downFromImgService(downImgUrl, localDownImg);
        }

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
                System.err.println("Compress:[" + localDownImg + "] 285x285,285x380,img220x220 success");
                String destPath = GoodsInfoUtils.changeRemotePathToLocal(goods.getShowMainImage().substring(0, goods.getShowMainImage().lastIndexOf("/")), isKids);
                //上传
                File upFile = new File(localDownImgPre);
                boolean isUpload = false;
                if (upFile.exists() && upFile.isDirectory()) {
                    if (isKids > 0) {
                        isUpload = UploadByOkHttp.uploadFileBatchAll(upFile, destPath);
                        if (!isUpload) {
                            isUpload = UploadByOkHttp.uploadFileBatchAll(upFile, destPath);
                        }
                    } else {
                        isUpload = UploadByOkHttp.uploadFileBatchOld(upFile, destPath);
                        if (!isUpload) {
                            isUpload = UploadByOkHttp.uploadFileBatchOld(upFile, destPath);
                        }
                    }
                    if (isUpload) {
                        System.err.println("this pid:" + pid + ",上传产品主图成功<:<:<:");
                        isSuccess = true;
                        executeSu = true;
                    } else {
                        System.err.println("this pid:" + pid + ",上传产品主图失败");
                        // 记录上传失败日志
                        customGoodsService.insertIntoGoodsImgUpLog(pid, localDownImgPre, adminId, "to " + destPath + "error");
                        isSuccess = false;
                    }
                } else {
                    System.err.println("this pid:" + pid + ",下载图片文件夹[" + localDownImgPre + "] 不存在----");
                    LOG.error("this pid:" + pid + ",下载图片文件夹[" + localDownImgPre + "] 不存在----");
                    // 记录上传失败日志
                    customGoodsService.insertIntoGoodsImgUpLog(pid, localDownImgPre, adminId, "下载图片文件夹[" + localDownImgPre + "] 不存在----");
                    isSuccess = false;
                }
            } else {
                System.err.println("this pid:" + pid + ",压缩img [" + localDownImg + "] error----");
                LOG.error("this pid:" + pid + ",压缩img [" + localDownImg + "] error----");
                // 记录上传失败日志
                customGoodsService.insertIntoGoodsImgUpLog(pid, localDownImgPre, adminId, "压缩img[" + localDownImgPre + "] error----");
                isSuccess = false;
            }
        } else {
            LOG.error("this pid:" + pid + ",下载图片失败,无法设置主图");
            customGoodsService.insertIntoGoodsImgUpLog(pid, localDownImgPre, adminId, "下载图片失败,无法设置主图");
        }
        if (isSuccess) {
            String nwMainImg = goods.getShowMainImage().replace(".400x400.", ".220x220.")
                    .replace(goods.getRemotpath(), "");
            goods.setShowMainImage(nwMainImg);
            System.err.println("nwMainImg:[" + nwMainImg + "]");
            // 上传完成后，27数据更新，提供给刘勇使用
            customGoodsDao.publish(goods, 0);
        } else {
            customGoodsService.updateGoodsState(pid, 3);
        }
        return executeSu;
    }


    private boolean checkIsExistsLocalImg(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.isFile();
    }

    private void deleteFileChild(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isDirectory()) {
            File[] childList = file.listFiles();
            for (File child : childList) {
                child.delete();
            }
            childList = null;
        }
    }


    private boolean checkIsKidsCatid(String catid) {
        boolean isCheck = false;
        if (kidsCatidList == null || kidsCatidList.size() == 0) {
            kidsCatidList = customGoodsService.queryKidsCanUploadCatid();
        }
        for (String tempCatid : kidsCatidList) {
            if (tempCatid.equals(catid)) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }


    private boolean dealKidsImgService(CustomGoodsPublish goods) {
        boolean isSuccess = false;
        boolean isUp = false;
        if (goods.getValid() == 0 && checkIsKidsCatid(goods.getCatid1())) {
            // 如果kids并且下架，则执行图片上传
            isUp = OKHttpUtils.optionGoodsInterface(goods.getPid(), 1, 45, 2);
        } else {
            return true;
        }
        if (!isUp) {
            // 重试一次
            isUp = OKHttpUtils.optionGoodsInterface(goods.getPid(), 1, 45, 2);
        }
        if (!isUp) {
            // 重试一次
            isUp = OKHttpUtils.optionGoodsInterface(goods.getPid(), 1, 45, 2);
        }
        if (isUp) {
            isSuccess = true;
        } else {
            isSuccess = false;
            // 调用接口上传失败
            customGoodsService.updateGoodsState(pid, 3);
            customGoodsService.insertIntoGoodsImgUpLog(pid, "", adminId, ",上传到kids服务器图片失败");
        }
        if (isSuccess) {
            customGoodsService.publish(goods);
            customGoodsService.updateGoodsState(pid, 4);
        }
        return isSuccess;
    }


}
