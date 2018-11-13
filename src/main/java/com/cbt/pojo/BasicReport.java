package com.cbt.pojo;

public class BasicReport {
    private Integer id;

    private String breportName;

    private String breportType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBreportName() {
        return breportName;
    }

    public void setBreportName(String breportName) {
        this.breportName = breportName == null ? null : breportName.trim();
    }

    public String getBreportType() {
        return breportType;
    }

    public void setBreportType(String breportType) {
        this.breportType = breportType == null ? null : breportType.trim();
    }
}