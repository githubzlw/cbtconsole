package com.importExpress.service;

import com.importExpress.pojo.CustomizedBean;

import java.util.List;
import java.util.Map;

public interface CustomizedService{
	
	public void add(CustomizedBean customizedBean, Map<String, Object> busiessMap) throws Exception;
	
	public void update(CustomizedBean customizedBean);
	
	public void delete(int id);
	
	public void updateStatus(int id, String status);
	
	public CustomizedBean get(Integer id);
	
	public List<CustomizedBean> getList(Integer id);

	public Map<String, Object> getMap(Integer id);

	public List<Map<String, Object>> getMapList(Integer id);
}
