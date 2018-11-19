package com.importExpress.pojo;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class OnlineGoodsCheck {
    private int id;
    private String pid;
    private String shopId;
    private String catid;
    private String categoryName;
    private String remotePath;
    private String localPath;
    private String mainImg;
    private String imgs;
    private String imgShow;
    private String eninfo;
    private String eninfoShow1;
    private String eninfoShow2;
    private String eninfoShow3;
    private String eninfoShow4;
    private int clickNum;
    private int startNum;
    private int limitNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
        if (StringUtils.isNotBlank(imgs)) {
            String[] imgList = imgs.split(",");
            this.imgShow = imgList[0].replace("[", "");
            imgList = null;
        }
    }

    public String getEninfo() {
        return eninfo;
    }

    public void setEninfo(String eninfo) {
        this.eninfo = eninfo;
        if (StringUtils.isNotBlank(eninfo)) {
            Document nwDoc = Jsoup.parseBodyFragment(eninfo);
            Elements imgEls = nwDoc.getElementsByTag("img");
            if (imgEls.size() > 0) {
                if (imgEls.size() >= 1) {
                    this.eninfoShow1 = imgEls.get(0).attr("src");
                }
                if (imgEls.size() >= 2) {
                    this.eninfoShow2 = imgEls.get(1).attr("src");
                }
                if (imgEls.size() >= 3) {
                    this.eninfoShow3 = imgEls.get(2).attr("src");
                }
                if (imgEls.size() >= 4) {
                    this.eninfoShow4 = imgEls.get(3).attr("src");
                }
            }
            imgEls.clear();
            nwDoc = null;
        }
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public String getImgShow() {
        return imgShow;
    }

    public void setImgShow(String imgShow) {
        this.imgShow = imgShow;
    }

    public String getEninfoShow1() {
        return eninfoShow1;
    }

    public void setEninfoShow1(String eninfoShow1) {
        this.eninfoShow1 = eninfoShow1;
    }

    public String getEninfoShow2() {
        return eninfoShow2;
    }

    public void setEninfoShow2(String eninfoShow2) {
        this.eninfoShow2 = eninfoShow2;
    }

    public String getEninfoShow3() {
        return eninfoShow3;
    }

    public void setEninfoShow3(String eninfoShow3) {
        this.eninfoShow3 = eninfoShow3;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }


    public String getEninfoShow4() {
        return eninfoShow4;
    }

    public void setEninfoShow4(String eninfoShow4) {
        this.eninfoShow4 = eninfoShow4;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    @Override
    public String toString() {
        return "OnlineGoodsCheck{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", shopId='" + shopId + '\'' +
                ", catid='" + catid + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", remotePath='" + remotePath + '\'' +
                ", localPath='" + localPath + '\'' +
                ", mainImg='" + mainImg + '\'' +
                ", imgs='" + imgs + '\'' +
                ", imgShow='" + imgShow + '\'' +
                ", eninfo='" + eninfo + '\'' +
                ", clickNum=" + clickNum +
                '}';
    }
}
