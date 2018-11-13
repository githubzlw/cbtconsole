package com.importExpress.mapper;

import com.importExpress.pojo.CustomizedBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomizedMapper {

	public int delete(Integer id);

	public int insert(CustomizedBean record);

	public CustomizedBean get(Integer id);
	
	public List<CustomizedBean> getList(Integer id);

	public Map<String, Object> getMap(Integer id);

	public List<Map<String, Object>> getMapList(Integer id);
	
	public int update(CustomizedBean record);

	public int updateStatus(@Param("id") Integer id, @Param("status") String status);
}