package com.importExpress.pojo;

import lombok.Data;

import java.util.Date;

@Data
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

    private Integer shareFlag;//是否用于社交分享：0-默认值 用于关联客户；1-用于社交分享；
    private String shareUrl;

	// 创建时间
	private Date createtime;

	//
	private String mqlog;

	private Integer websiteType;  // 关联用户发送邮件网站名

    private Integer site;//优惠卷所在网站

    public TabCouponNew() {
		super();
	}

	public TabCouponNew(String id, Integer count, Integer leftCount, String describe, String value, Date from, Date to,
			int type, int valid, Integer userid, Integer shareFlag, Integer websiteType, Integer site) {
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
		this.shareFlag = shareFlag;
		this.websiteType = websiteType;
		this.site = site;
	}
	
}