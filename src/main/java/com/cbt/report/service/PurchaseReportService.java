package com.cbt.report.service;

import com.cbt.pojo.PurchaseReport;
import com.cbt.pojo.ReportUnpackPurchase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurchaseReportService {

	public List<PurchaseReport> selectPurchaseByDate(String startdate, String enddate);
	
	public List<ReportUnpackPurchase> selectUnpacking(String startDate, String endDate);
	
	public List<ReportUnpackPurchase> selectPurchasePackage(String startdate, String enddate);
	
	public List<ReportUnpackPurchase> selectOutCount(String startdate, String enddate);
	
	/**
	 * 调用存储过程noAvailableRateStatistics获取无货率
	 * 
	 * @param beginTime
	 *            开始时间,格式:yyyy-MM-dd HH:mm:ss
	 * @param endTime
	 *            结束时间,格式:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public float noAvailableRateStatistics(@Param("beginTime") String beginTime, @Param("endTime") String endTime);
}
