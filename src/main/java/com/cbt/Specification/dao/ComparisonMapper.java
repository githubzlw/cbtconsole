package com.cbt.Specification.dao;

import com.cbt.Specification.bean.SpecificationComparison;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ComparisonMapper {

	/**
	 * 根据AliExpress的cid、规格信息查询选择区的数据
	 * 
	 * @param aliCid
	 * @param type
	 * @param specificationNames
	 * @param attributeNames
	 * @return
	 */
	List<SpecificationComparison> queryChoiceByAliCid(@Param("aliCid") String aliCid,
                                                      @Param("specificationNames") String[] specificationNames, @Param("attributeNames") String[] attributeNames);

	/**
	 * 根据AliExpress的cid查询详情区的数据
	 * 
	 * @param aliCid
	 * @return
	 */
	List<SpecificationComparison> queryDetailByAliCid(@Param("aliCid") String aliCid);

}
