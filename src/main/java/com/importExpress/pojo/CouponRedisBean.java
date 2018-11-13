package com.importExpress.pojo;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(String leftCount) {
		this.leftCount = leftCount;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "CouponRedisBean [id=" + id + ", count=" + count + ", leftCount=" + leftCount + ", describe=" + describe
				+ ", value=" + value + ", from=" + from + ", to=" + to + ", type=" + type + ", valid=" + valid + "]";
	}

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
		this.from = from;
		this.to = to;
		this.type = type;
		this.valid = valid;
	}
	
	
}
