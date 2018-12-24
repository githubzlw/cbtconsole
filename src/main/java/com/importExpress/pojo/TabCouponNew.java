package com.importExpress.pojo;

import java.util.Date;

public class TabCouponNew {

	// 折扣卷码
	private String id;

	// 可用
	private Integer count = 0;

	// 剩余可用
	private Integer leftCount;

	// 描述
	private String describe;

	// 折扣卷值
	private String value;

	// 领取开始时间
	private Date from;

	// 领取结束时间
	private Date to;

	// 折扣卷类型 1-满减卷；2-折扣卷；3-代金卷；
	private int type = 0;

	// 是否有效 0-无效；1-生效；
	private int valid = 0;

	// 创建用户 id
	private Integer userid;

	// 创建用户名
	private String username;

	private String userids;//折扣卷关联的用户id

    public String getUserids() {
        return userids;
    }

    public void setUserids(String userids) {
        this.userids = userids;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// 创建时间
	private Date createtime;

	//
	private String mqlog;
	
	public String getMqlog() {
		return mqlog;
	}

	public void setMqlog(String mqlog) {
		this.mqlog = mqlog;
	}

	public String getId() {
		return this.id;
	};

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCount() {
		return this.count;
	};

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getLeftCount() {
		return this.leftCount;
	};

	public void setLeftCount(Integer leftCount) {
		this.leftCount = leftCount;
	}

	public String getDescribe() {
		return this.describe;
	};

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getValue() {
		return this.value;
	};

	public void setValue(String value) {
		this.value = value;
	}

	public Date getFrom() {
		return this.from;
	};

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return this.to;
	};

	public void setTo(Date to) {
		this.to = to;
	}

	public int getType() {
		return this.type;
	};

	public void setType(int type) {
		this.type = type;
	}

	public int getValid() {
		return this.valid;
	};

	public void setValid(int valid) {
		this.valid = valid;
	}

	public Integer getUserid() {
		return this.userid;
	};

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Date getCreatetime() {
		return this.createtime;
	};

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public TabCouponNew() {
		super();
	}

	public TabCouponNew(String id, Integer count, Integer leftCount, String describe, String value, Date from, Date to,
			int type, int valid, Integer userid) {
		super();
		this.id = id;
		this.count = count;
		this.leftCount = leftCount;
		this.describe = describe;
		this.value = value;
		this.from = from;
		this.to = to;
		this.type = type;
		this.valid = valid;
		this.userid = userid;
	}
	
}