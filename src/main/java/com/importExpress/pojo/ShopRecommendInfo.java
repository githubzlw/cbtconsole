package com.importExpress.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ShopRecommendInfo {
    private Integer id;
    private String shopId;
    private String coverImg;
    private String coverPid;
    private Integer sort;
    private Integer isOn;
    private String createTime;
	private int createAdminId;
	private String updateTime;
	private int updateAdminId;
	private List<ShopRecommendGoods> goodsList;
	private String pidList;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"shopId\":\"")
                .append(shopId).append('\"');
        sb.append(",\"coverImg\":\"")
                .append(coverImg).append('\"');
        sb.append(",\"coverPid\":\"")
                .append(coverPid).append('\"');
        sb.append(",\"sort\":")
                .append(sort);
        sb.append(",\"goodsList\":")
                .append(goodsList.toString());
        sb.append('}');
        return sb.toString();
    }
}
