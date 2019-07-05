package com.importExpress.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ShopRecommendInfo {
    private Integer id;
    private String shopId;
    private String coverImg;
    private String coverPid;
    private String coverOnlineUrl;
    private String coverPrice;
    private Integer sort;
    private Integer isOn;
    private String createTime;
    private int createAdminId;
    private String updateTime;
    private int updateAdminId;
    private List<ShopRecommendGoods> goodsList;
    private String pidList;
    private String shopUrl;
    private String shopName;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"shopId\":\"")
                .append(shopId).append('\"');
        sb.append(",\"shopUrl\":\"")
                .append(shopUrl).append('\"');
        sb.append(",\"shopName\":\"")
                .append(shopName).append('\"');
        sb.append(",\"coverImg\":\"")
                .append(coverImg).append('\"');
        sb.append(",\"coverPid\":\"")
                .append(coverPid).append('\"');
        sb.append(",\"coverPrice\":\"")
                .append(coverPrice).append('\"');
        sb.append(",\"coverOnlineUrl\":\"")
                .append(coverOnlineUrl).append('\"');
        sb.append(",\"sort\":")
                .append(sort);
        sb.append(",\"goodsList\":")
                .append(goodsList.toString());
        sb.append('}');
        return sb.toString();
    }
}
