package com.cbt.warehouse.service;

import com.cbt.warehouse.dao.ProblemFeedBackDao;
import com.cbt.warehouse.pojo.ProblemFeedBackBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取注册页面、购物车页面、支付页面反馈弹框数据;
 * @author tb
 * @time 2017.3.9
 */
@Service
public class ProblemFeedBackServiceImpl implements ProblemFeedBackService {

	@Autowired
	private ProblemFeedBackDao problemFeedBackDao;
	
	@Override
	public List<ProblemFeedBackBean> queryForList(int type, String beginDate,
			String endDate,int adminId, int pageNo,String is_report) {
		return problemFeedBackDao.queryForList(type, beginDate, endDate,adminId, pageNo,is_report);
	}

	@Override
	public Long queryCount(int type, String beginDate, String endDate,int adminId,String is_report) {
		return problemFeedBackDao.queryCount(type, beginDate, endDate,adminId,is_report);
	}

	@Override
	public List<ProblemFeedBackBean> queryForAllList() {
		return problemFeedBackDao.queryForAllList();
	}
	
	@Override
	public List<ProblemFeedBackBean> getReportProblem(String report_id) {
		
		return problemFeedBackDao.getReportProblem(report_id);
	}

}
