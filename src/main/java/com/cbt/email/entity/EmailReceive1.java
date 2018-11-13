package com.cbt.email.entity;

import java.io.Serializable;

public class EmailReceive1 implements Serializable {
    private Integer id;

    private String saleName;

    private String cname;

    private String createTime;

    private String sendTime;

    private String title;

    private String content;

    private String customerEmail;

    private String ccEmail;

    private String orderid;

    private Integer include;

    private Integer userid;

    private Integer customerId;
    
  //邮件状态  0表示未删除  1表示已删除
    private Integer isdeleted;
    //邮件收件
    private Integer mailformat;
    
  private Integer start;
    
    private Integer end;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName == null ? null : saleName.trim();
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime == null ? null : sendTime.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail == null ? null : customerEmail.trim();
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail == null ? null : ccEmail.trim();
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public Integer getInclude() {
        return include;
    }

    public void setInclude(Integer include) {
        this.include = include;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

	public Integer getIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(Integer isdeleted) {
		this.isdeleted = isdeleted;
	}

	public Integer getMailformat() {
		return mailformat;
	}

	public void setMailformat(Integer mailformat) {
		this.mailformat = mailformat;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "EmailReceive1 [id=" + id + ", saleName=" + saleName
				+ ", cname=" + cname + ", createTime=" + createTime
				+ ", sendTime=" + sendTime + ", title=" + title + ", content="
				+ content + ", customerEmail=" + customerEmail + ", ccEmail="
				+ ccEmail + ", orderid=" + orderid + ", include=" + include
				+ ", userid=" + userid + ", customerId=" + customerId
				+ ", isdeleted=" + isdeleted + ", mailformat=" + mailformat
				+ ", start=" + start + ", end=" + end + "]";
	}
	
    
}