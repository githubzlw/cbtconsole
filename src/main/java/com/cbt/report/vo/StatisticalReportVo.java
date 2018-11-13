package com.cbt.report.vo;

import com.cbt.pojo.*;

import java.util.Date;
import java.util.List;


public class StatisticalReportVo {
	
	private List<ReportInfo> reportInfoList;
	
	private List<ReportDetail> reportDetailList;
	
	private List<ReportDetailOrder> reportDetailOrder;
	
	private List<SalesReport> saleReport;
	private ReportSalesInfo reportSalesInfo;
	private Object obj;
	
	private List<PurchaseReport> purchaseReport;
	
	private List<ReportUnpackPurchase> unpack;
	private List<ReportUnpackPurchase> purchase;
	
	private Date timeFrom;
	private Date timeTo;
	
	private String year;
	
	private String month;
	
	private String week;
	
	private String day;
	
	private String category;
	
	private Double currency;
	
	//拆单/采购包裹总数
	private int  unpackNum;   // 拆包总计
	private  int purchaseNum ; // 采购包裹总计
	
	
	public ReportSalesInfo getReportSalesInfo() {
		return reportSalesInfo;
	}
	public void setReportSalesInfo(ReportSalesInfo reportSalesInfo) {
		this.reportSalesInfo = reportSalesInfo;
	}
	public List<ReportUnpackPurchase> getUnpack() {
		return unpack;
	}
	public void setUnpack(List<ReportUnpackPurchase> unpack) {
		this.unpack = unpack;
	}
	public List<ReportUnpackPurchase> getPurchase() {
		return purchase;
	}
	public void setPurchase(List<ReportUnpackPurchase> purchase) {
		this.purchase = purchase;
	}
	public List<PurchaseReport> getPurchaseReport() {
		return purchaseReport;
	}
	public void setPurchaseReport(List<PurchaseReport> purchaseReport) {
		this.purchaseReport = purchaseReport;
	}
	public List<SalesReport> getSaleReport() {
		return saleReport;
	}
	public void setSaleReport(List<SalesReport> saleReport) {
		this.saleReport = saleReport;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public List<ReportDetailOrder> getReportDetailOrder() {
		return reportDetailOrder;
	}
	public void setReportDetailOrder(List<ReportDetailOrder> reportDetailOrder) {
		this.reportDetailOrder = reportDetailOrder;
	}
	
	public Date getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(Date timeFrom) {
		this.timeFrom = timeFrom;
	}
	public Date getTimeTo() {
		return timeTo;
	}
	public void setTimeTo(Date timeTo) {
		this.timeTo = timeTo;
	}
	public List<ReportInfo> getReportInfoList() {
		return reportInfoList;
	}
	public void setReportInfoList(List<ReportInfo> reportInfoList) {
		this.reportInfoList = reportInfoList;
	}
	public List<ReportDetail> getReportDetailList() {
		return reportDetailList;
	}
	public void setReportDetailList(List<ReportDetail> reportDetailList) {
		this.reportDetailList = reportDetailList;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Double getCurrency() {
		return currency;
	}
	public void setCurrency(Double currency) {
		this.currency = currency;
	}
	public int getUnpackNum() {
		return unpackNum;
	}
	public void setUnpackNum(int unpackNum) {
		this.unpackNum = unpackNum;
	}
	public int getPurchaseNum() {
		return purchaseNum;
	}
	public void setPurchaseNum(int purchaseNum) {
		this.purchaseNum = purchaseNum;
	}
	
}
