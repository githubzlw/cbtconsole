package com.importExpress.pojo;

public class KeyWordBean {
    private String keyword;
    private int adminId;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "KeyWordBean{" +
                "keyword='" + keyword + '\'' +
                ", adminId=" + adminId +
                '}';
    }
}
