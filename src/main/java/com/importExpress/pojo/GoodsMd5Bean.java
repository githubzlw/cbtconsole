package com.importExpress.pojo;

import com.cbt.util.GoodsInfoUtils;
import org.apache.commons.lang3.StringUtils;

public class GoodsMd5Bean {
    private Integer id;
    private String pid;
    private String shopId;
    private String goodsMd5;
    private String localPath;
    private String remotePath;
    private String imgShow;
    private int valid;
    private int isMark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getGoodsMd5() {
        return goodsMd5;
    }

    public void setGoodsMd5(String goodsMd5) {
        this.goodsMd5 = goodsMd5;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
        if(StringUtils.isNotBlank(remotePath)){
            this.imgShow = GoodsInfoUtils.changeLocalPathToRemotePath(remotePath);
        }
    }

    public String getImgShow() {
        return imgShow;
    }

    public void setImgShow(String imgShow) {
        this.imgShow = imgShow;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getIsMark() {
        return isMark;
    }

    public void setIsMark(int isMark) {
        this.isMark = isMark;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"pid\":\"")
                .append(pid).append('\"');
        sb.append(",\"shopId\":\"")
                .append(shopId).append('\"');
        sb.append(",\"goodsMd5\":\"")
                .append(goodsMd5).append('\"');
        sb.append(",\"localPath\":\"")
                .append(localPath).append('\"');
        sb.append(",\"remotePath\":\"")
                .append(remotePath).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
