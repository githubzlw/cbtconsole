package com.cbt.report.service;

import com.cbt.pojo.PurchaseReport;
import com.cbt.pojo.ReportUnpackPurchase;
import com.cbt.report.dao.PurchaseReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("purchaseReportService")
public class PurchaseReportServiceImpl implements PurchaseReportService {

	@Autowired
	private PurchaseReportMapper purchase;
	
	@Override
	public List<PurchaseReport> selectPurchaseByDate(String startdate, String enddate) {
		return purchase.selectPurchaseByDate(startdate, enddate);
	}

	public List<ReportUnpackPurchase> selectUnpacking(String startDate, String endDate){
		return purchase.selectUnpacking(startDate, endDate);
	}
	
	public List<ReportUnpackPurchase> selectPurchasePackage(String startdate, String enddate){
		return purchase.selectPurchasePackage(startdate, enddate);
	}
	
	public List<ReportUnpackPurchase> selectOutCount(String startdate, String enddate ){
		return purchase.selectOutCount(startdate, enddate);
	}

	@Override
	public float noAvailableRateStatistics(String beginTime, String endTime) {
		return purchase.noAvailableRateStatistics(beginTime, endTime);
	}
	
}
