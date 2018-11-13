package com.cbt.warehouse.pojo;

public class HotEvaluation {
    private int id;
    private String goodsPid;
    private String skuId;
    private String content;
    private int userId;
    private int evaluationLevel;
    private int serviceLevel;
    private String createTime;
    private String evaluationTime;
    private int adminId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsPid() {
        return goodsPid;
    }

    public void setGoodsPid(String goodsPid) {
        this.goodsPid = goodsPid;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEvaluationLevel() {
        return evaluationLevel;
    }

    public void setEvaluationLevel(int evaluationLevel) {
        this.evaluationLevel = evaluationLevel;
    }

    public int getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(int serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }


    @Override
    public String toString() {
        return "HotEvaluation{" +
                "id=" + id +
                ", goodsPid='" + goodsPid + '\'' +
                ", skuId='" + skuId + '\'' +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", evaluationLevel=" + evaluationLevel +
                ", serviceLevel=" + serviceLevel +
                ", createTime='" + createTime + '\'' +
                ", evaluationTime='" + evaluationTime + '\'' +
                ", adminId=" + adminId +
                '}';
    }
}
