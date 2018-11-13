package com.cbt.email.email;

//java邮件客户端
 
import java.util.Date;
import java.util.List;

public class App_email {
 
    private String title; // 主题
    private String fromaddr; // 发件人
    private String toaddr; // 收件人
    private String acctoaddr; // 抄送
    private String kind="no"; // 类型
    private String sta = "收件箱"; // 状态
    private Date senddate = new Date(); // 发送(接收)时间
    private String mailbody; // 正文
    private List<App_emailfile> app_emailfiles;
 
    /** [集合] */
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getFromaddr() {
        return fromaddr;
    }
 
    public void setFromaddr(String fromaddr) {
        this.fromaddr = fromaddr;
    }
 
    public String getToaddr() {
        return toaddr;
    }
 
    public void setToaddr(String toaddr) {
        this.toaddr = toaddr;
    }
 
    public String getAcctoaddr() {
        return acctoaddr;
    }
 
    public void setAcctoaddr(String acctoaddr) {
        this.acctoaddr = acctoaddr;
    }
 
    public String getKind() {
        return kind;
    }
 
    public void setKind(String kind) {
        this.kind = kind;
    }
 
    public String getSta() {
        return sta;
    }
 
    public void setSta(String sta) {
        this.sta = sta;
    }
 
    public Date getSenddate() {
        return senddate;
    }
 
    public void setSenddate(Date senddate) {
        this.senddate = senddate;
    }
 
    public String getMailbody() {
        return mailbody;
    }
 
    public void setMailbody(String mailbody) {
        this.mailbody = mailbody;
    }
 
    public List<App_emailfile> getApp_emailfiles() {
        return app_emailfiles;
    }
 
    public void setApp_emailfiles(List<App_emailfile> app_emailfiles) {
        this.app_emailfiles = app_emailfiles;
    }

	@Override
	public String toString() {
		return "App_email [title=" + title + ", fromaddr=" + fromaddr
				+ ", toaddr=" + toaddr + ", acctoaddr=" + acctoaddr + ", kind="
				+ kind + ", sta=" + sta + ", senddate=" + senddate
				+ ", mailbody=" + mailbody + ", app_emailfiles="
				+ app_emailfiles + "]";
	}
 
 
}
 
