package com.importExpress.pojo;

public class CouponUserRedisBean {

	private String id;//卷码

	private String to; //领取截止时间

    private String userid;//关联的用户id

	private String type = "2"; //优惠卷类型 2-满减卷 关联用户

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public CouponUserRedisBean() {
    }

    public CouponUserRedisBean(String id, String to, String userid) {
        this.id = id;
        this.to = to.substring(0, to.length() - 3);
        this.userid = userid;
    }
}
