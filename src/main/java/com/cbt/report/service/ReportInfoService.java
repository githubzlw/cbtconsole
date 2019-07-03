package com.cbt.report.service;

import com.cbt.pojo.GeneralReport;
import com.cbt.pojo.ReportInfo;
import com.cbt.pojo.ReportInfoExample;
import com.cbt.pojo.StatisticalReportPojo;
import com.cbt.report.vo.StatisticalReportVo;
import com.importExpress.pojo.AliBillingDetails;
import com.importExpress.pojo.AliPayInfo;

import java.util.List;
import java.util.Map;

public interface ReportInfoService {
	
	List<ReportInfo> selectByTime(StatisticalReportVo statisticalReportVo);
	
	List<ReportInfo> selectOrderByTime(StatisticalReportVo statisticalReportVo);
	
	int insertSelective(ReportInfo record);
	
	List<ReportInfo> selectByGeneral(GeneralReport generalReport);
	
	int deleteByExample(ReportInfoExample example);

	/**
	 * 查询分类销售统计报表
	 * @param map
	 * @return
	 */
	List<StatisticalReportPojo> getCategoryReport(Map<String, String> map);
	/**
	 * 查询总分类销售统计报表
	 * @param map
	 * @return
	 */
	List<StatisticalReportPojo> getAllCategoryReport(Map<String, String> map);
	/**
	 * 查询分类销售总数
	 * @param map
	 * @return
	 */
	List<StatisticalReportPojo> getCategoryReportCount(Map<String, String> map);

	/**
	 * 插入支付宝账单信息
	 * @param infoList
	 * @return
	 */
	int insertAliPayInfo(List<AliPayInfo> infoList);

	int insertAliPayInfoSingle(AliPayInfo aliPayInfo);

	/**
	 * 插入支付宝对账信息
	 * @param detailsList
	 * @return
	 */
	int insertAliBillingDetails(List<AliBillingDetails> detailsList);
    
}