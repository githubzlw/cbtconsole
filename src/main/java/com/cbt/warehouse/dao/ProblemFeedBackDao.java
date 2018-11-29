package com.cbt.warehouse.dao;

import com.cbt.warehouse.pojo.ProblemFeedBackBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProblemFeedBackDao {

	public List<ProblemFeedBackBean> queryForList(@Param("type") int type, @Param("beginDate") String beginDate,
                                                  @Param("endDate") String endDate, @Param("adminId") int adminId, @Param("pageNo") int pageNo, @Param("is_report") String is_report);

	public Long queryCount(@Param("type") int type, @Param("beginDate") String beginDate,
                           @Param("endDate") String endDate, @Param("adminId") int adminId, @Param("is_report") String is_report);

	public List<ProblemFeedBackBean> queryForAllList();
	
	/**
	 * 获取客户反馈信息内容
	 * @Title getReportProblem 
	 * @Description TODO
	 * @param report_id
	 * @return
	 * @return List<ProblemFeedBackBean>
	 */
	public List<ProblemFeedBackBean> getReportProblem(@Param("report_id") String report_id);

    List<Map<String,String>> queryWarningNum(@Param("adminid") int adminid);
}
