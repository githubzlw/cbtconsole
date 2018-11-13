package com.importExpress.service.impl;

import com.importExpress.mapper.CustomizedMapper;
import com.importExpress.pojo.CustomizedBean;
import com.importExpress.service.CustomizedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CustomizedServiceImpl implements CustomizedService {

	@Autowired
	private CustomizedMapper customizedMapper;
	
	@Transactional
	@Override
	public void add(CustomizedBean customizedBean, Map<String, Object> busiessMap) {

		
	}

	@Override
	public void update(CustomizedBean customizedBean) {

		customizedMapper.update(customizedBean);
	}

	@Override
	public void delete(int id) {

		customizedMapper.delete(id);
	}

	@Transactional
	@Override
	public void updateStatus(int id, String status) {
			
		customizedMapper.updateStatus(id,status);
	}

	@Override
	public CustomizedBean get(Integer id) {
		
		return customizedMapper.get(id);
	}

	@Override
	public List<CustomizedBean> getList(Integer id) {

		return customizedMapper.getList(id);
	}

	@Override
	public Map<String, Object> getMap(Integer id) {

		return customizedMapper.getMap(id);
	}

	@Override
	public List<Map<String, Object>> getMapList(Integer id) {

		return customizedMapper.getMapList(id);
	}

}
