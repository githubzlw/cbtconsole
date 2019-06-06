package com.importExpress.pojo;

import lombok.Data;

import java.util.List;

@Data
public class HotEventsInfo {
    private Integer id;
    private String link;
    private String imgUrl;
    private String childName1;
    private String childLink1;
    private String childName2;
    private String childLink2;
    private String childName3;
    private String childLink3;
    private int isOn = -1;
    private Integer adminId;
    private String createTime;
    private String updateTime;

    private List<HotEventsGoods> goodsList;
}
