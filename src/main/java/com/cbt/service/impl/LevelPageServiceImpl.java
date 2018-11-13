package com.cbt.service.impl;

import com.cbt.bean.LevelPageBean;
import com.cbt.dao.LevelPageDao;
import com.cbt.dao.impl.LevelPageDaoImpl;
import com.cbt.service.LevelPageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelPageServiceImpl implements LevelPageService {

	private LevelPageDao LevelPagedao = new LevelPageDaoImpl();
	
	@Override
	public List<LevelPageBean> getList(int page) {

		return LevelPagedao.getList(page);
	}

	@Override
	public int update(LevelPageBean bean) {

		return LevelPagedao.update(bean);
	}

	@Override
	public int insert(LevelPageBean bean) {

		return LevelPagedao.insert(bean);
	}

	@Override
	public int delete(int id) {

		return 0;
	}

}
