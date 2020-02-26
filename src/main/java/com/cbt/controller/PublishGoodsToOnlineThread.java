package com.cbt.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import com.cbt.parse.service.ImgDownload;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.ChangeEntypeUtils;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.GoodsInfoUtils;
import com.cbt.website.util.UploadByOkHttp;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.ImageCompressionByNoteJs;
import com.importExpress.utli.OKHttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class PublishGoodsToOnlineThread implements Callable<Boolean> {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PublishGoodsToOnlineThread.class);

    private CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();
    private static List<String> kidsCatidList = new ArrayList<>();


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
                System.err.println("pid:" + goods.getPid() + ",valid:" + goods.getValid());
                isCheckImg = GoodsInfoUtils.checkOffLineImg(goods, isKids, isUpdateImg);
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

        // 设置商品处于发布中的状态
        // customGoodsService.updateGoodsState(pid, 1);
        // Thread.sleep(35000);
        GoodsInfoUtils.dealWindowImg(goods, localShowPath, remoteShowPath, imgList, ftpConfig, isUpdateImg);
        GoodsInfoUtils.dealEninfoImg(goods, localShowPath, remoteShowPath, imgList, ftpConfig);

        boolean isSuccess = true;
        // 判断需要上传的图片，执行上传逻辑
        if (imgList.size() > 0) {
            isSuccess = uploadLocalImg(imgList, localShowPath, isKids);
        }
        System.err.println("uploadLocalImg size:" + imgList.size() + ",result:" + isSuccess);
        if (isSuccess) {
            // 保存本地数据


            // isUpdateImg = 1;
            if (isUpdateImg > 0) {
                System.err.println("pid" + pid + ",begin setWindowImgToMainImg---");
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
        System.err.println("pid:" + pid + ",executeSu:" + executeSu);
        if (executeSu) {
            customGoodsService.deleteOnlineSync(pid);
            customGoodsService.publish(goods);
            customGoodsService.updateGoodsState(pid, 4);
        } else {
            System.err.println("pid:" + pid + ",处理图片失败---");
            customGoodsService.insertIntoOnlineSync(pid);
            GoodsInfoUpdateOnlineUtil.setOffOnlineByPid(pid, "更新失败");
        }
        return executeSu;
    }


    private boolean uploadLocalImg(List<String> imgList, String localShowPath, int isKids) {
        System.err.println("this pid:" + pid + ",localShowPath:" + localShowPath + " 确认图片是否上传");
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
        if(!goods.getShowMainImage().contains("http")){
            goods.setShowMainImage(goods.getRemotpath() + goods.getShowMainImage());
        }
        String downImgUrl = goods.getShowMainImage().replace(".220x220", ".400x400");
        String downImgName = downImgUrl.substring(downImgUrl.lastIndexOf("/"));

        // 图片下载本地路径名称
        String localDownImgPre = ftpConfig.getLocalDiskPath() + pid + "/edit";
        String localDownImg = localDownImgPre + downImgName.replace(".220x220", ".400x400");
        deleteFileChild(localDownImgPre);

        System.err.println("down[" + downImgUrl + "] to [" + localDownImg + "]");
        isSuccess = ImgDownload.downFromImgService(downImgUrl, localDownImg);
        if (!isSuccess) {
            // 重新下载一次
            isSuccess = ImgDownload.downFromImgService(downImgUrl, localDownImg);
        }
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
            System.err.println("this pid:" + pid + ",下载图片失败,无法设置主图");
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
