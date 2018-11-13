package com.cbt.report.dao;

import com.cbt.pojo.PurchaseReport;
import com.cbt.pojo.ReportUnpackPurchase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurchaseReportMapper {
	List<PurchaseReport> selectPurchaseByDate(@Param("startdate") String startdate, @Param("enddate") String enddate);

	List<ReportUnpackPurchase> selectUnpacking(@Param("startdate") String startdate, @Param("enddate") String enddate);

	List<ReportUnpackPurchase> selectPurchasePackage(@Param("startdate") String startdate,
                                                     @Param("enddate") String enddate);

	List<ReportUnpackPurchase> selectOutCount(@Param("startdate") String startdate, @Param("enddate") String enddate);

	/**
	 * 调用存储过程noAvailableRateStatistics获取无货率
	 * 
	 * @param beginTime
	 *            开始时间,格式:yyyy-MM-dd HH:mm:ss
	 * @param endTime
	 *            结束时间,格式:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	float noAvailableRateStatistics(@Param("beginTime") String beginTime, @Param("endTime") String endTime);
}
