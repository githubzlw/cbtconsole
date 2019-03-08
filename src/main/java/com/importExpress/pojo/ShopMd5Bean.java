package com.importExpress.pojo;

import com.cbt.util.GoodsInfoUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ShopMd5Bean {
    private String shopId;
    private String pid;
    private int md5Num;
    private String shopName;
    private String md5Img;
    private String imgShow;
    private String md5Val;
    private int isMark;
    private int isDelete;
    private int startNum;
	private int limitNum;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getMd5Num() {
        return md5Num;
    }

    public void setMd5Num(int md5Num) {
        this.md5Num = md5Num;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMd5Img() {
        return md5Img;
    }

    public void setMd5Img(String md5Img) {
        this.md5Img = md5Img;
        if(StringUtils.isNotBlank(md5Img)){
            this.imgShow = GoodsInfoUtils.changeLocalPathToRemotePath(md5Img);
        }
    }

    public String getImgShow() {
        return imgShow;
    }

    public void setImgShow(String imgShow) {
        this.imgShow = imgShow;
    }

    public String getMd5Val() {
        return md5Val;
    }

    public void setMd5Val(String md5Val) {
        this.md5Val = md5Val;
    }

    public int getIsMark() {
        return isMark;
    }

    public void setIsMark(int isMark) {
        this.isMark = isMark;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"shopId\":\"")
                .append(shopId).append('\"');
        sb.append(",\"md5Num\":")
                .append(md5Num);
        sb.append(",\"shopName\":\"")
                .append(shopName).append('\"');
        sb.append(",\"md5Img\":\"")
                .append(md5Img).append('\"');
        sb.append(",\"md5Val\":\"")
                .append(md5Val).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
