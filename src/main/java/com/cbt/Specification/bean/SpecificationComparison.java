package com.cbt.Specification.bean;

import java.io.Serializable;

public class SpecificationComparison implements Serializable {

	private static final long serialVersionUID = -110110991L;
	private String aliSpecificationEnName;// AliExpress规格中文名
	private String aliSpecificationChName;// AliExpress规格英文名
	private String aliAttributeEnName;// AliExpress规格属性中文名
	private String aliAttributeChName;// AliExpress规格属性英文名
	private String specificationChName1688;// 1688规格中文名
	private String attributeChName1688;// 1688规格属性中文名
	private boolean isAttributeMatch = false;// AliExpress规格属性是否匹配1688规格属性

	public String getAliSpecificationEnName() {
		return aliSpecificationEnName;
	}

	public void setAliSpecificationEnName(String aliSpecificationEnName) {
		this.aliSpecificationEnName = aliSpecificationEnName;
	}

	public String getAliSpecificationChName() {
		return aliSpecificationChName;
	}

	public void setAliSpecificationChName(String aliSpecificationChName) {
		this.aliSpecificationChName = aliSpecificationChName;
	}

	public String getAliAttributeEnName() {
		return aliAttributeEnName;
	}

	public void setAliAttributeEnName(String aliAttributeEnName) {
		this.aliAttributeEnName = aliAttributeEnName;
	}

	public String getAliAttributeChName() {
		return aliAttributeChName;
	}

	public void setAliAttributeChName(String aliAttributeChName) {
		this.aliAttributeChName = aliAttributeChName;
	}

	public String getSpecificationChName1688() {
		return specificationChName1688;
	}

	public void setSpecificationChName1688(String specificationChName1688) {
		this.specificationChName1688 = specificationChName1688;
	}

	public String getAttributeChName1688() {
		return attributeChName1688;
	}

	public void setAttributeChName1688(String attributeChName1688) {
		this.attributeChName1688 = attributeChName1688;
	}

	public boolean isAttributeMatch() {
		return isAttributeMatch;
	}

	public void setAttributeMatch(boolean isAttributeMatch) {
		this.isAttributeMatch = isAttributeMatch;
	}

	@Override
	public String toString() {
		return "SpecificationComparison [aliSpecificationEnName=" + aliSpecificationEnName + ", aliSpecificationChName="
				+ aliSpecificationChName + ", aliAttributeEnName=" + aliAttributeEnName + ", aliAttributeChName="
				+ aliAttributeChName + ", specificationChName1688=" + specificationChName1688 + ", attributeChName1688="
				+ attributeChName1688 + ", isAttributeMatch=" + isAttributeMatch + "]";
	}

}
