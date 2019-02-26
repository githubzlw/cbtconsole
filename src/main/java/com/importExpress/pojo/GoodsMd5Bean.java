package com.importExpress.pojo;

public class GoodsMd5Bean {
    private Integer id;
    private String pid;
    private String shopId;
    private String md5;
    private String localPath;
    private String remotePath;

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

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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
        sb.append(",\"md5\":\"")
                .append(md5).append('\"');
        sb.append(",\"localPath\":\"")
                .append(localPath).append('\"');
        sb.append(",\"remotePath\":\"")
                .append(remotePath).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
