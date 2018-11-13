package com.importExpress.pojo;

public class OnlineGoodsStatistic {
    private int typeinTotal;
    private int onlineTotal;
    private int isEditTotal;

    public int getTypeinTotal() {
        return typeinTotal;
    }

    public void setTypeinTotal(int typeinTotal) {
        this.typeinTotal = typeinTotal;
    }

    public int getOnlineTotal() {
        return onlineTotal;
    }

    public void setOnlineTotal(int onlineTotal) {
        this.onlineTotal = onlineTotal;
    }

    public int getIsEditTotal() {
        return isEditTotal;
    }

    public void setIsEditTotal(int isEditTotal) {
        this.isEditTotal = isEditTotal;
    }

    @Override
    public String toString() {
        return "OnlineGoodsStatistic{" +
                "typeinTotal=" + typeinTotal +
                ", onlineTotal=" + onlineTotal +
                ", isEditTotal=" + isEditTotal +
                '}';
    }
}
