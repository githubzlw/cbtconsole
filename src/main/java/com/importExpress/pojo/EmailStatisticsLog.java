package com.importExpress.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 统计发送日志
 * @author chenlun
 *
 */
public class EmailStatisticsLog implements Serializable {
    private String id;
    
    private String emailStatisticsLogId;

    private String sendEmail;

    private String receiveEmail;

    private String orderNo;
    
    private String emailType;
    
    private String sendStatus;

    private String title;

    private String errorLog;

    private Date createDate;

    private String content;
    
    private String denseEmail;//密送人
    
    private String copyEmail;//抄送人

    private static final long serialVersionUID = 1L;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail == null ? null : sendEmail.trim();
    }

    public String getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(String receiveEmail) {
        this.receiveEmail = receiveEmail == null ? null : receiveEmail.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog == null ? null : errorLog.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getEmailStatisticsLogId() {
		return emailStatisticsLogId;
	}

	public void setEmailStatisticsLogId(String emailStatisticsLogId) {
		this.emailStatisticsLogId = emailStatisticsLogId;
	}

	public String getDenseEmail() {
		return denseEmail;
	}

	public void setDenseEmail(String denseEmail) {
		this.denseEmail = denseEmail;
	}

	public String getCopyEmail() {
		return copyEmail;
	}

	public void setCopyEmail(String copyEmail) {
		this.copyEmail = copyEmail;
	}
    
    
}