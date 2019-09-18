package com.cbt.pojo;

public class ImgPojo {
    private String remotPath;
    private int id;
    private String enInfo;
    private int category_id;
    private String name;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemotPath() {
        return remotPath;
    }

    public void setRemotPath(String remotPath) {
        this.remotPath = remotPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnInfo() {
        return enInfo;
    }

    public void setEnInfo(String enInfo) {
        this.enInfo = enInfo;
    }
}
