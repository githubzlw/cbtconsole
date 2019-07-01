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
}
