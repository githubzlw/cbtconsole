package com.importExpress.pojo;

public class FollowLogBean {
    private Integer id;
    private Integer userId;
    private String userEmail;
    private Integer adminId;
    private String adminName;
    private String followCode;
    private String createTime;
    private String userIp;

    private int limitNum;
    private int startNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getFollowCode() {
        return followCode;
    }

    public void setFollowCode(String followCode) {
        this.followCode = followCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"userId\":")
                .append(userId);
        sb.append(",\"userEmail\":\"")
                .append(userEmail).append('\"');
        sb.append(",\"adminId\":")
                .append(adminId);
        sb.append(",\"adminName\":\"")
                .append(adminName).append('\"');
        sb.append(",\"followCode\":\"")
                .append(followCode).append('\"');
        sb.append(",\"createTime\":\"")
                .append(createTime).append('\"');
        sb.append(",\"limitNum\":")
                .append(limitNum);
        sb.append(",\"startNum\":")
                .append(startNum);
        sb.append('}');
        return sb.toString();
    }
}
