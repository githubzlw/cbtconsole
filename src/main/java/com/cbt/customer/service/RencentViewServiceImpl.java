package com.cbt.customer.service;

import com.cbt.bean.RecentViewBean;
import com.cbt.customer.dao.IRencentViewDao;
import com.cbt.customer.dao.RencentViewDaoImpl;

import java.util.List;

public class RencentViewServiceImpl implements IRecentViewService{
	
	IRencentViewDao dao = new RencentViewDaoImpl();

	@Override
	public int addRecode(RecentViewBean rvb) {
		int addRecode = dao.addRecode(rvb);
		return addRecode;
	}

	@Override
	public RecentViewBean findByPid(String pid) {
		return dao.findByPid(pid);
	}

	@Override
	public List<RecentViewBean> findAll(int start, int page_size) {
		return dao.findAll(start, page_size);
	}

}
