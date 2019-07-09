package com.importExpress.pojo;

import lombok.Data;

@Data
public class CouponRedisBean {

	private String id;//卷码

	private String count;//卷数量
	
	private String leftCount; //剩余数量
	
	private String describe;//描述
	
	private String value;//
	
	private String from;//领取开始时间
	
	private String to; //领取截止时间
	
	private String type; //优惠卷类型 1-满减卷
	
	private String valid = "1";//是否有效 1-有效

	public CouponRedisBean() {
		super();
	}

	public CouponRedisBean(String id, String count, String leftCount, String describe, String value, String from,
			String to, String type, String valid) {
		super();
		this.id = id;
		this.count = count;
		this.leftCount = leftCount;
		this.describe = describe;
		this.value = value;
		this.from = from.substring(0, from.length() - 3);
		this.to = to.substring(0, to.length() - 3);
		this.type = type;
		this.valid = valid;
	}
	
	
}
