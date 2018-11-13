package com.cbt.bean;

import java.util.List;

public class SkuAttr {
	private SkuAttrBean skuAttrBean;//sku数据
	private List<TypeBean> typeList;//对应的规格组合
	
	public SkuAttrBean getSkuAttrBean() {
		return skuAttrBean;
	}

	public void setSkuAttrBean(SkuAttrBean skuAttrBean) {
		this.skuAttrBean = skuAttrBean;
	}


	public List<TypeBean> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<TypeBean> typeList) {
		this.typeList = typeList;
	}

	@Override
	public String toString() {
		return String.format("{\"skuAttrBean\":\"%s\", \"typeList\":\"%s\"}",
				skuAttrBean, typeList);
	}


}
