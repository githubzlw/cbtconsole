package com.cbt.bean;

/**
 * 规格数据
 * 
 * @author abc
 *
 */
public class TypeBean {
	private String type;// 规格类型
	private String value;// 规格值
	private String img;// 图片
	private String id;// id
	private String lableType;
	private String typeId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null) {
			value = value.replaceAll("\"", "").replaceAll("'", "");
		}
		this.value = value;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getLableType() {
		return lableType;
	}

	public void setLableType(String lableType) {
		this.lableType = lableType;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

//	@Override
//	public String toString() {
//		return "{\"type\":\"" + type + "\", \"value\":\"" + value + "\", \"img\":\"" + img + "\", \"id\":\"" + id
//				+ "\", \"lableType\":\"" + lableType + "\", \"typeId\":\"" + typeId + "\"}";
//	}
	
	

	public TypeBean(String type, String value, String img, String id) {
		super();
		this.type = type;
		this.value = value;
		this.img = img;
		this.id = id;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", type=" + type + ", value=" + value + ", img=" + img + "]";
	}

	public TypeBean(String type, String value, String id) {
		super();
		this.type = type;
		this.value = value;
		this.id = id;
	}

	public TypeBean() {
		super();
	}

}
