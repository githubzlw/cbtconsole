package com.cbt.pojo;

import java.util.Date;

public class Syncdatainfo {
    private Integer id;

    private String sqlstr;

    private Date creattime;

    private Integer flag;

    private String remaek;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSqlstr() {
        return sqlstr;
    }

    public void setSqlstr(String sqlstr) {
        this.sqlstr = sqlstr == null ? null : sqlstr.trim();
    }

    public Date getCreattime() {
        return creattime;
    }

    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getRemaek() {
        return remaek;
    }

    public void setRemaek(String remaek) {
        this.remaek = remaek == null ? null : remaek.trim();
    }
}