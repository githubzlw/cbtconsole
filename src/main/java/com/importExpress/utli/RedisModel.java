package com.importExpress.utli;

public class RedisModel {
    /**
     * 1对应客户key值删除，2客户信息全部删除
     */
    private String type = "1";
    private String[] userid;

    public RedisModel(String[] userid) {
        this.userid = userid;
    }

    public RedisModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getUserid() {
        return userid;
    }

    public void setUserid(String[] userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "RedisModel{" +
                "type='" + type + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
