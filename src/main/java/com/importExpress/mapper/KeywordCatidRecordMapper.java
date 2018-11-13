package com.importExpress.mapper;

import com.importExpress.pojo.KeywordCatidRecordBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface KeywordCatidRecordMapper {
	
	public List<KeywordCatidRecordBean> getList(@Param("keyword") String keyword, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

	public Integer getCount(@Param("keyword") String keyword);

	public int addKeyword(KeywordCatidRecordBean bean);

	public KeywordCatidRecordBean getDetail(@Param("id") Integer id);

	public int updateKeyword(KeywordCatidRecordBean bean);

	public int delKeyword(@Param("id") Integer id);

	public int updateSyn(@Param("id") Integer id, @Param("issyn") Integer issyn);

	public List<Map<String,Object>> getKeyword1s();

	public int getCountByKeyword(@Param("keyword") String keyword);

	public int getCountByKeyword1(@Param("keyword") String keyword, @Param("id") Integer id);
	
	public List<Map<String,Object>> getCategorys();

}
