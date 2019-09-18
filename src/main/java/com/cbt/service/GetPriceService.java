package com.cbt.service;

import com.cbt.controller.GetPriceController;
import com.cbt.pojo.ImgPojo;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.warehouse.dao.GetPriceMapper;
import com.importExpress.utli.ImgDownByOkHttpUtils;
import com.importExpress.utli.OKHttpUtils;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GetPriceService {
    /*
    @Autowired
    private GetPriceMapper getPriceMapper;
    private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();
    private static final String OCR_URL = "http://192.168.1.251:5000/photo";
    private static final Log LOG = LogFactory.getLog(GetPriceController.class);

    public List<List<String>> FindAllImgUrl(int categoryId, int page) {
        List<ImgPojo> list = this.getPriceMapper.FindAllImgUrl(categoryId, page * 20);
        List<String> imglist = new ArrayList<>();
        List<List<String>> imgStrlist = new ArrayList<>();
        for (ImgPojo imgUrl : list) {
            Pattern p = Pattern.compile("[s][r][c][=][\"][^\"]+[\"]"); //匹配数字
            Matcher m = p.matcher(imgUrl.getEnInfo());
            while (true) {
                if (m.find()) {
                    String img = m.group();
                    if (img.split("=")[1].replaceAll("\"", "").indexOf("http") > -1) {
                        imglist.add(img.split("=")[1].replaceAll("\"", "").replaceAll("https", "http"));
                        int count=this.getPriceMapper.FindImgUrlByurl(img.split("=")[1].replaceAll("\"", "").replaceAll("https", "http"));
                        if (count>0){
                            imglist.remove(imglist.size()-1);
                        }
                    } else {
                        imglist.add(imgUrl.getRemotPath().replaceAll("https", "http") + img.split("=")[1].replaceAll("\"", ""));
                        int count=this.getPriceMapper.FindImgUrlByurl(imgUrl.getRemotPath().replaceAll("https", "http") + img.split("=")[1].replaceAll("\"", ""));
                        if (count>0){
                            imglist.remove(imglist.size()-1);
                        }
                    }
                    if (imglist.size() > 3) {
                        List<String> imglist2 = new ArrayList<>();
                        imglist2.add(imglist.get(0));
                        imglist2.add(imglist.get(1));
                        imglist2.add(imglist.get(2));
                        imglist2.add(imglist.get(3));
                        imgStrlist.add(imglist2);
                        imglist.clear();
                    }
                } else {
                    break;
                }
            }//结果22、33

        }
        imgStrlist.add(imglist);
        return imgStrlist;
    }

    public List<ImgPojo> FindCategory() {
        List<ImgPojo> Category = this.getPriceMapper.FindCategory();
        return Category;
    }

    public int FindAllImgUrlCount(int categoryId) {
        int totalpage = 0;
        try {
            int count = this.getPriceMapper.FindAllImgUrlCount(categoryId);
            totalpage = ((count - 1) / 20) + 1;
        } catch (Exception e) {
            totalpage = 0;
        }


        return totalpage;
    }

    public Boolean AddSizeChartList(List<String> sizeChartList) {
        try {
            for (String sizeChart : sizeChartList) {
                this.getPriceMapper.AddSizeChartList(sizeChart);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<List<String>> FindAllTranslationImg(int page) {
        List<List<String>> urllist=new ArrayList<>();
        for (int i=0;i<4;i++) {
            List<String> imgs = this.getPriceMapper.FindAllTranslationImg(page * 20 + i * 5);
            if (imgs.size()>0) {
                urllist.add(imgs);
            }
        }
        return urllist;
    }

    public int FindAllTranslationImgCount() {
        int totalpage = 0;
        try {
            int count = this.getPriceMapper.FindAllTranslationImgCount();
            totalpage = ((count - 1) / 20) + 1;
        } catch (Exception e) {
            totalpage = 0;
        }
        return totalpage;
    }

    public Boolean delImgurlByList(List<String> sizeChartList) {
        Boolean bo=true;
        try {
        for (String url:sizeChartList){
            this.getPriceMapper.delImgurlByList(url);
        }
        } catch (Exception e) {
            bo = false;
        }
        return bo;
    }
    public static String makeFileName(String filename) { // 2.jpg
        // 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
        return UUID.randomUUID().toString() + "_" + filename;
    }

    public String changeChineseImgToEnglishImg(String imgUrl) {
       String img="";
        try {
            // 下载图片到本地
            if (ftpConfig == null) {
                ftpConfig = GetConfigureInfo.getFtpConfig();
            }
            String suffixName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
            String prePath = ftpConfig.getLocalDiskPath() + "importimg/desc/";
            String pidEnInfoFile = prePath + suffixName;
            boolean isDown = ImgDownByOkHttpUtils.downFromImgServiceWithApache(imgUrl, pidEnInfoFile);
            // 重试一次
            if (!isDown) {
                isDown = ImgDownByOkHttpUtils.downFromImgServiceWithApache(imgUrl, pidEnInfoFile);
            }
            File imgFile = new File(pidEnInfoFile);
            if (isDown && imgFile.exists() && imgFile.isFile()) {
                // 调用替换中文图片到英文图片的接口
                OkHttpClient client = OKHttpUtils.getClientInstence();

                String imageType = "image/jpg";
                okhttp3.RequestBody fileBody = okhttp3.RequestBody.create(MediaType.parse(imageType), imgFile);
                MultipartBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", imgFile.getName(), fileBody)
                        .build();*/
//                Request okHttpRequest = new Request.Builder().addHeader("Accept", "*/*").addHeader("Connection", "close")
//                        .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
//                        .post(body)
//                        .url(OCR_URL)
//                        .build();

                /* Response okHttpResponse = client.newCall(okHttpRequest).execute();
                if (okHttpResponse.isSuccessful()) {
                    // 本地生成新的文件
                    Random random = new Random();
                    String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));

                    String changeLocalFilePath = prePath + suffixName;

                    BASE64Decoder decoder = new BASE64Decoder();
                    FileUtils.writeByteArrayToFile(new File(changeLocalFilePath), decoder.decodeBuffer(okHttpResponse.body().byteStream()));

                    File checkFile = new File(changeLocalFilePath);
                    if (checkFile.exists() && checkFile.isFile()) {
                        img=ftpConfig.getLocalShowPath() + "importimg/desc/" + suffixName;
//                        img= "/xyz/data/cbtconsole/cbtimg/editimg/importimg/desc/" + saveFilename + fileSuffix;
//                        this.getPriceMapper.addImg();
                    } else {
                        img="获取新文件失败";
                    }
                } else {
                    img="调用转换接口失败";
                }
            } else {
                img="下载图片失败,请重试";
                return img;
            }


        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("changeChineseImgToEnglishImg", e);
            img="changeChineseImgToEnglishImg error:" + e.getMessage();
        }
        return img;
    }*/
}


