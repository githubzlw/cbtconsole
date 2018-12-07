package com.cbt.warehouse.service;

import com.cbt.warehouse.pojo.ProblemFeedBackBean;

import java.util.List;
import java.util.Map;

public interface ProblemFeedBackService {

	public List<ProblemFeedBackBean> queryForList(int type, String beginDate, String endDate, int adminId, int pageNo, String is_report);

	public Long queryCount(int type, String beginDate, String endDate, int adminId, String is_report);
	
	public List<ProblemFeedBackBean> queryForAllList();
	/**
	 * 获取客户反馈信息内容
	 * @Title getReportProblem 
	 * @Description TODO
	 * @param report_id
	 * @return
	 * @return List<ProblemFeedBackBean>
	 */
	public List<ProblemFeedBackBean> getReportProblem(String report_id);

    Map<String,String> queryWarningNum(int admuserid);
}
