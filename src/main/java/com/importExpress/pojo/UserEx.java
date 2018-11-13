package com.importExpress.pojo;

import java.io.Serializable;

public class UserEx implements Serializable {
    
	private static final long serialVersionUID = 4032623496220503585L;

	private Integer id;

    private Integer userid;

    private String otheremail;

    private String otherphone;

    private String whatsapp;

    private String facebook;

    private String tweater;

    private String kiki;

    private String skype;
    
    public UserEx(){}

    public UserEx(Integer id, Integer userid, String otheremail,
                  String otherphone, String whatsapp, String facebook,
                  String tweater, String kiki, String skype) {
		super();
		this.id = id;
		this.userid = userid;
		this.otheremail = otheremail;
		this.otherphone = otherphone;
		this.whatsapp = whatsapp;
		this.facebook = facebook;
		this.tweater = tweater;
		this.kiki = kiki;
		this.skype = skype;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getOtheremail() {
        return otheremail;
    }

    public void setOtheremail(String otheremail) {
        this.otheremail = otheremail == null ? null : otheremail.trim();
    }

    public String getOtherphone() {
        return otherphone;
    }

    public void setOtherphone(String otherphone) {
        this.otherphone = otherphone == null ? null : otherphone.trim();
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp == null ? null : whatsapp.trim();
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook == null ? null : facebook.trim();
    }

    public String getTweater() {
        return tweater;
    }

    public void setTweater(String tweater) {
        this.tweater = tweater == null ? null : tweater.trim();
    }

    public String getKiki() {
        return kiki;
    }

    public void setKiki(String kiki) {
        this.kiki = kiki == null ? null : kiki.trim();
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype == null ? null : skype.trim();
    }
}