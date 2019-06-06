package com.importExpress.pojo;

import lombok.Data;

@Data
public class HotEventsGoods {
    private Integer id;
    private Integer eventsId;
    private String pid;
    private int isOn = -1;
    private Integer adminId;
    private String createTime;
    private String updateTime;
    private String onlineUrl;
    private String mainImg;
    private String enName;
    private String price;
}
