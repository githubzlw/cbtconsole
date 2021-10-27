package com.cbt.pojo;

public class EntypeBen {
    private String type;
    private String value;
    private String img;
    private String id;
    private String lableType;
    private String sell;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLableType() {
        return lableType;
    }

    public void setLableType(String lableType) {
        this.lableType = lableType;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"value\":\"")
                .append(value).append('\"');
        sb.append(",\"img\":\"")
                .append(img).append('\"');
        sb.append(",\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"lableType\":\"")
                .append(lableType).append('\"');
        sb.append(",\"sell\":\"")
                .append(sell).append('\"');
        sb.append('}');
        return sb.toString();
    }


    public EntypeBen() {
        super();
    }
}
