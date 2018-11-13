package com.cbt.Specification.bean;

import java.io.Serializable;

public class SpecificationTranslation implements Serializable {

	private static final long serialVersionUID = -99115823L;
	private int id;
	private String chName;// 中文名
	private String enName;// 英文名
	private int type;// 规格类型：1.选择区，2.详情区
	private String productCategoryId;// 物品类别id

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	@Override
	public String toString() {
		return "SpecificationTranslation [id=" + id + ", chName=" + chName + ", enName=" + enName + ", type=" + type
				+ ", productCategoryId=" + productCategoryId + "]";
	}

}
