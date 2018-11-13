package com.cbt.Specification.bean;

import java.io.Serializable;

public class SpecificationMapping  implements Serializable{
	
	private static final long serialVersionUID = 66779235L;
	private int id;
	private String chName;// 中文名
	private String enName;// 英文名
	private String productCategoryId;// 物品类别id
	private String specificationId;// 规格id

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

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(String specificationId) {
		this.specificationId = specificationId;
	}

	@Override
	public String toString() {
		return "SpecificationMapping [id=" + id + ", chName=" + chName + ", enName=" + enName + ", productCategoryId="
				+ productCategoryId + ", specificationId=" + specificationId + "]";
	}

}
