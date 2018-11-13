package com.cbt.Specification.service;

import com.cbt.Specification.bean.SpecificationComparison;
import com.cbt.Specification.dao.ComparisonMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("ComparisonService")
public class ComparisonServiceImpl implements ComparisonService {

	@Resource
    ComparisonMapper comparisonMapper;

	@Override
	public List<SpecificationComparison> queryChoiceByAliCid(String aliCid, String[] specificationNames,
			String[] attributeNames) {
		return comparisonMapper.queryChoiceByAliCid(aliCid, specificationNames, attributeNames);
	}

	@Override
	public List<SpecificationComparison> queryDetailByAliCid(String aliCid) {
		return comparisonMapper.queryDetailByAliCid(aliCid);
	}

}
