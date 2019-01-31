package com.importExpress.pojo;

public class KjPidBean {
    private String pid;
    private String imgUrl;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"pid\":\"")
                .append(pid).append('\"');
        sb.append(",\"imgUrl\":\"")
                .append(imgUrl).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
