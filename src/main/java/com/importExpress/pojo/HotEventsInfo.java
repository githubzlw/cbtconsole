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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"link\":\"")
                .append(link).append('\"');
        sb.append(",\"imgUrl\":\"")
                .append(imgUrl).append('\"');
        sb.append(",\"childName1\":\"")
                .append(childName1).append('\"');
        sb.append(",\"childLink1\":\"")
                .append(childLink1).append('\"');
        sb.append(",\"childName2\":\"")
                .append(childName2).append('\"');
        sb.append(",\"childLink2\":\"")
                .append(childLink2).append('\"');
        sb.append(",\"childName3\":\"")
                .append(childName3).append('\"');
        sb.append(",\"childLink3\":\"")
                .append(childLink3).append('\"');
        sb.append(",\"goodsList\":");
        if(goodsList == null || goodsList.size() == 0){
            sb.append("[]");
        }else{
             sb.append(goodsList.toString());
        }
        sb.append('}');
        return sb.toString();
    }
}
