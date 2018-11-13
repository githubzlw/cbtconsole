package com.cbt.Specification.service;

import com.cbt.Specification.bean.SpecificationComparison;

import java.util.List;

public interface ComparisonService {

	/**
	 * 根据AliExpress的cid、规格信息查询选择区的数据
	 * 
	 * @param aliCid
	 * @param type
	 * @param specificationNames
	 * @param attributeNames
	 * @return
	 */
	public List<SpecificationComparison> queryChoiceByAliCid(String aliCid, String[] specificationNames,
                                                             String[] attributeNames);

	/**
	 * 根据AliExpress的cid查询详情区的数据
	 * 
	 * @param aliCid
	 * @return
	 */
	public List<SpecificationComparison> queryDetailByAliCid(String aliCid);
}
