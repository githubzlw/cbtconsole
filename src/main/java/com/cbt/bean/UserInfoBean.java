package com.cbt.bean;

public class UserInfoBean {
    private Integer id;

    private String name;

    private String pass;

    private String nicename;

    private Double availableM;

    private Double applicableCredit;

    private String email;

    private String picture;

    private Integer activationstate;

    private String activationcode;

    private String activationtime;

    private String activationpasstime;

    private String activationpasscode;

    private String createtime;

    private Integer acThere;

    private String currency;

    private String adminname;

    private String businessname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass == null ? null : pass.trim();
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename == null ? null : nicename.trim();
    }

    public Double getAvailableM() {
        return availableM;
    }

    public void setAvailableM(Double availableM) {
        this.availableM = availableM;
    }

    public Double getApplicableCredit() {
        return applicableCredit;
    }

    public void setApplicableCredit(Double applicableCredit) {
        this.applicableCredit = applicableCredit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }

    public Integer getActivationstate() {
        return activationstate;
    }

    public void setActivationstate(Integer activationstate) {
        this.activationstate = activationstate;
    }

    public String getActivationcode() {
        return activationcode;
    }

    public void setActivationcode(String activationcode) {
        this.activationcode = activationcode == null ? null : activationcode.trim();
    }

    public String getActivationtime() {
        return activationtime;
    }

    public void setActivationtime(String activationtime) {
        this.activationtime = activationtime == null ? null : activationtime.trim();
    }

    public String getActivationpasstime() {
        return activationpasstime;
    }

    public void setActivationpasstime(String activationpasstime) {
        this.activationpasstime = activationpasstime == null ? null : activationpasstime.trim();
    }

    public String getActivationpasscode() {
        return activationpasscode;
    }

    public void setActivationpasscode(String activationpasscode) {
        this.activationpasscode = activationpasscode == null ? null : activationpasscode.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public Integer getAcThere() {
        return acThere;
    }

    public void setAcThere(Integer acThere) {
        this.acThere = acThere;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname == null ? null : adminname.trim();
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname == null ? null : businessname.trim();
    }
}