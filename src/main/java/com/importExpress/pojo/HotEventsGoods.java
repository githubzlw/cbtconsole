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
    private String unit;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"eventsId\":")
                .append(eventsId);
        sb.append(",\"pid\":\"")
                .append(pid).append('\"');
        sb.append(",\"onlineUrl\":\"")
                .append(onlineUrl).append('\"');
        sb.append(",\"mainImg\":\"")
                .append(mainImg).append('\"');
        sb.append(",\"enName\":\"")
                .append(enName).append('\"');
        sb.append(",\"price\":\"")
                .append(price).append('\"');
        sb.append(",\"unit\":\"")
                .append(unit).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
