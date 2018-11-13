package com.importExpress.pojo;

public class GoodsCarconfigWithBLOBs extends GoodsCarconfig {
    private String shopcarshowinfo;

    private String shopcarinfo;

    private String saveforlatershowinfo;

    private String saveforlaterinfo;

    private String buyformecarconfig;

    public String getShopcarshowinfo() {
        return shopcarshowinfo;
    }

    public void setShopcarshowinfo(String shopcarshowinfo) {
        this.shopcarshowinfo = shopcarshowinfo == null ? null : shopcarshowinfo.trim();
    }

    public String getShopcarinfo() {
        return shopcarinfo;
    }

    public void setShopcarinfo(String shopcarinfo) {
        this.shopcarinfo = shopcarinfo == null ? null : shopcarinfo.trim();
    }

    public String getSaveforlatershowinfo() {
        return saveforlatershowinfo;
    }

    public void setSaveforlatershowinfo(String saveforlatershowinfo) {
        this.saveforlatershowinfo = saveforlatershowinfo == null ? null : saveforlatershowinfo.trim();
    }

    public String getSaveforlaterinfo() {
        return saveforlaterinfo;
    }

    public void setSaveforlaterinfo(String saveforlaterinfo) {
        this.saveforlaterinfo = saveforlaterinfo == null ? null : saveforlaterinfo.trim();
    }

    public String getBuyformecarconfig() {
        return buyformecarconfig;
    }

    public void setBuyformecarconfig(String buyformecarconfig) {
        this.buyformecarconfig = buyformecarconfig == null ? null : buyformecarconfig.trim();
    }
}