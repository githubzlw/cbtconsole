package com.importExpress.pojo;

/**
 * 对标商品相关统计
 */
public class AliBenchmarkingStatistic {

    private int benchmarkingTotalNum;//我们完成对标的数量
    private float onlineRate;//我们上线的百分比
    private int onlineNum;//上线总数
    private float isEditedRate;//我们人为编辑过的百分比
    private int editedNum;//上线总数
    private int freightFreeNum;//我们最终 “免邮” 的数量
    private int moqNum;//MOQ大于 1 的数量
    private int adminId;
    private String adminName;
    private int noBenchmarksCount;//非对标上线产品数量
    public int getNoBenchmarksCount() {
        return noBenchmarksCount;
    }

    public void setNoBenchmarksCount(int noBenchmarksCount) {
        this.noBenchmarksCount = noBenchmarksCount;
    }



    public int getBenchmarkingTotalNum() {
        return benchmarkingTotalNum;
    }

    public void setBenchmarkingTotalNum(int benchmarkingTotalNum) {
        this.benchmarkingTotalNum = benchmarkingTotalNum;
    }

    public float getOnlineRate() {
        return onlineRate;
    }

    public void setOnlineRate(float onlineRate) {
        this.onlineRate = onlineRate;
    }

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }

    public float getIsEditedRate() {
        return isEditedRate;
    }

    public void setIsEditedRate(float isEditedRate) {
        this.isEditedRate = isEditedRate;
    }

    public int getEditedNum() {
        return editedNum;
    }

    public void setEditedNum(int editedNum) {
        this.editedNum = editedNum;
    }

    public int getFreightFreeNum() {
        return freightFreeNum;
    }

    public void setFreightFreeNum(int freightFreeNum) {
        this.freightFreeNum = freightFreeNum;
    }

    public int getMoqNum() {
        return moqNum;
    }

    public void setMoqNum(int moqNum) {
        this.moqNum = moqNum;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "AliBenchmarkingStatistic{" +
                "benchmarkingTotalNum=" + benchmarkingTotalNum +
                ", onlineRate=" + onlineRate +
                ", onlineNum=" + onlineNum +
                ", isEditedRate=" + isEditedRate +
                ", editedNum=" + editedNum +
                ", freightFreeNum=" + freightFreeNum +
                ", moqNum=" + moqNum +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                '}';
    }
}
