package com.importExpress.service;

import com.importExpress.pojo.KeywordCatidRecordBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface KeywordCatidRecordService {

	public Map<String,Object> getList(String keyword, Integer currentPage, Integer pageSize);

	public int addKeyword(KeywordCatidRecordBean bean);

	public KeywordCatidRecordBean getDetail(Integer id);

	public int updateKeyword(KeywordCatidRecordBean bean);

	public int delKeyword(@Param("id") Integer id);

	public int updateSyn(@Param("id") Integer id, @Param("issyn") Integer issyn);

	public List<Map<String,Object>> getKeyword1s();

	public int getCountByKeyword(String keyword);

	public int getCountByKeyword1(String keyword, Integer id);
	
	public List<Map<String,Object>> getCategorys();
	
}
