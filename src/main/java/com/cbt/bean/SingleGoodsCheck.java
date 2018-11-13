package com.cbt.bean;

import org.apache.commons.lang3.StringUtils;

public class SingleGoodsCheck {
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
    private String kjEninfo;
    private String eninfoShow1;
    private String eninfoShow2;
    private String eninfoShow3;
    private int isPass = -1;
    private int isUpdate = -1;
    private int startNum;
    private int limitNum;
    private String mainImgSet;
    private int imgCheck ;
    private String shopMainImg;
    private int shopCheck = -1;

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
        this.shopMainImg = this.shopMainImg.replace(this.localPath,this.remotePath);
        if(this.mainImg.equals(this.shopMainImg)){
            this.shopCheck = 0;
        }
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
        if(StringUtils.isNotBlank(imgs)){
            String[] imgList =  imgs.split(";");
            this.imgShow = imgList[0];
            imgList = null;
        }
    }

    public String getKjEninfo() {
        return kjEninfo;
    }

    public void setKjEninfo(String kjEninfo) {
        this.kjEninfo = kjEninfo;
        if(StringUtils.isNotBlank(kjEninfo)){
            String[] eninfoList =  kjEninfo.split("><");
            if(eninfoList[0].contains("<")){
                this.eninfoShow1 =  eninfoList[0].replace("<img src=",this.remotePath).replace("\"","");
            }else{
                this.eninfoShow1 = eninfoList[0].replace("img src=",this.remotePath).replace("\"","");
            }
            if(eninfoList.length > 1){
                if(eninfoList[1].contains("<")){
                    this.eninfoShow2 = eninfoList[1].replace("<img src=", this.remotePath).replace("\"","");
                }else{
                    this.eninfoShow2 = eninfoList[1].replace("img src=", this.remotePath).replace("\"","");
                }
            }
            if(eninfoList.length > 2){
                if(eninfoList[2].contains("<")){
                    this.eninfoShow3 = eninfoList[2].replace("<img src=", this.remotePath).replace("\"","");
                }else{
                    this.eninfoShow3 = eninfoList[2].replace("img src=", this.remotePath).replace("\"","");
                }
            }
            if(StringUtils.isNotBlank(this.mainImgSet)){
                this.mainImgSet = this.mainImgSet.replace(this.localPath,this.remotePath);
                if(this.eninfoShow1.equals(this.mainImgSet)){
                    this.imgCheck = 1;
                }else if(this.eninfoShow2.equals(this.mainImgSet)){
                    this.imgCheck = 2;
                }else if(this.eninfoShow3.equals(this.mainImgSet)){
                    this.imgCheck = 3;
                }
            }
            if(StringUtils.isNotBlank(this.shopMainImg)){
                this.shopMainImg = this.shopMainImg.replace(this.localPath,this.remotePath);
                if(this.eninfoShow1.equals(this.shopMainImg)){
                    this.shopCheck = 1;
                }else if(this.eninfoShow2.equals(this.shopMainImg)){
                    this.shopCheck = 2;
                }else if(this.eninfoShow3.equals(this.shopMainImg)){
                    this.shopCheck = 3;
                }
            }
            eninfoList = null;
        }
    }

    public int getIsPass() {
        return isPass;
    }

    public void setIsPass(int isPass) {
        this.isPass = isPass;
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

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getMainImgSet() {
        return mainImgSet;
    }

    public void setMainImgSet(String mainImgSet) {
        this.mainImgSet = mainImgSet;
    }

    public int getImgCheck() {
        return imgCheck;
    }

    public void setImgCheck(int imgCheck) {
        this.imgCheck = imgCheck;
    }

    public String getShopMainImg() {
        return shopMainImg;
    }

    public void setShopMainImg(String shopMainImg) {
        this.shopMainImg = shopMainImg;
    }

    public int getShopCheck() {
        return shopCheck;
    }

    public void setShopCheck(int shopCheck) {
        this.shopCheck = shopCheck;
    }

    @Override
    public String toString() {
        return "SingleGoodsCheck{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", remotePath='" + remotePath + '\'' +
                ", localPath='" + localPath + '\'' +
                ", mainImg='" + mainImg + '\'' +
                ", imgs='" + imgs + '\'' +
                ", kjEninfo='" + kjEninfo + '\'' +
                ", isPass=" + isPass +
                '}';
    }
}
