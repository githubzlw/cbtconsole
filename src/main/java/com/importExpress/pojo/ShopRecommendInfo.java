package com.importExpress.pojo;

import lombok.Data;

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
}
