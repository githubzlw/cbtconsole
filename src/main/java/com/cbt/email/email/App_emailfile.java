package com.cbt.email.email;
 
/**
 *
 *
 * @author ZOUQH
 * 邮件附件实体
 *
 */
 
public class App_emailfile {
 
    private App_email app_email;
    private String name;        //名称
    private String url;     //URL
    private String kind;        //类型
    private String status="启用";        //状态
     
    public App_email getApp_email() {
        return app_email;
    }
    public void setApp_email(App_email app_email) {
        this.app_email = app_email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getKind() {
        return kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
 
}



