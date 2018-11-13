package com.importExpress.service.impl;

import com.cbt.warehouse.util.StringUtil;
import com.importExpress.mapper.KeywordCatidRecordMapper;
import com.importExpress.pojo.KeywordCatidRecordBean;
import com.importExpress.service.KeywordCatidRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeywordCatidRecordServiceImpl implements KeywordCatidRecordService {
	
	@Autowired
	private KeywordCatidRecordMapper keywordCatidRecordMapper;

	@Override
	public Map<String,Object> getList(String keyword,
			Integer currentPage, Integer pageSize) {
		
		if(StringUtil.isBlank(keyword)) {
			keyword = null;
		}
		
		Integer offset = (currentPage-1)*pageSize;
		
		List<KeywordCatidRecordBean> list = keywordCatidRecordMapper.getList(keyword, offset, pageSize);
		Integer count = keywordCatidRecordMapper.getCount(keyword);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	@Override
	public int addKeyword(KeywordCatidRecordBean bean) {
		// TODO Auto-generated method stub
		return keywordCatidRecordMapper.addKeyword(bean);
	}

	@Override
	public KeywordCatidRecordBean getDetail(Integer id) {
		// TODO Auto-generated method stub
		return keywordCatidRecordMapper.getDetail(id);
	}

	@Override
	public int updateKeyword(KeywordCatidRecordBean bean) {
		// TODO Auto-generated method stub
		return keywordCatidRecordMapper.updateKeyword(bean);
	}

	@Override
	public int delKeyword(Integer id) {
		// TODO Auto-generated method stub
		return keywordCatidRecordMapper.delKeyword(id);
	}

	@Override
	public int updateSyn(Integer id, Integer issyn) {
		// TODO Auto-generated method stub
		return keywordCatidRecordMapper.updateSyn(id, issyn);
	}

	@Override
	public List<Map<String,Object>> getKeyword1s() {
		// TODO Auto-generated method stub
		return keywordCatidRecordMapper.getKeyword1s();
	}

	@Override
	public int getCountByKeyword(String keyword) {
		// TODO Auto-generated method stub
		return keywordCatidRecordMapper.getCountByKeyword(keyword);
	}

	@Override
	public int getCountByKeyword1(String keyword, Integer id) {
		// TODO Auto-generated method stub
		return keywordCatidRecordMapper.getCountByKeyword1(keyword, id);
	}

	@Override
	public List<Map<String,Object>> getCategorys() {
		// TODO Auto-generated method stub
		return keywordCatidRecordMapper.getCategorys();
	}

}
