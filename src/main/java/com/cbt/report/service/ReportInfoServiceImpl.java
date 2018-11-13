package com.cbt.report.service;

import com.cbt.pojo.GeneralReport;
import com.cbt.pojo.ReportInfo;
import com.cbt.pojo.ReportInfoExample;
import com.cbt.pojo.StatisticalReportPojo;
import com.cbt.report.dao.ReportInfoMapper;
import com.cbt.report.vo.StatisticalReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author zlg
 *
 */
@Service
public class ReportInfoServiceImpl implements ReportInfoService{

	@Autowired
	private ReportInfoMapper reportInfoDao;
	
	/**
	 * @param List<ReportInfo>
	 * 查询生成报表的类容
	 */
	@Override
	public List<ReportInfo> selectByTime(
			StatisticalReportVo statisticalReportVo) {
		List<ReportInfo> reportInfoList = reportInfoDao.selectByTime(statisticalReportVo);
		return reportInfoList;
	}

	/**
	 * @param ReportInfo
	 * 插入报表基本汇总信息
	 */
	@Override
	public int insertSelective(ReportInfo record) {
		return reportInfoDao.insertSelective(record);
	}

	/**
	 * 查询汇总报表信息
	 * @param GeneralReport
	 * @return List<ReportInfo>
	 */
	@Override
	public List<ReportInfo> selectByGeneral(GeneralReport generalReport) {
		return reportInfoDao.selectByGeneral(generalReport);
	}

	/**
	 * 删除报表总信息
	 * @param ReportInfoExample
	 * @return int 
	 */
	@Override
	public int deleteByExample(ReportInfoExample example) {
		// TODO Auto-generated method stub
		return reportInfoDao.deleteByExample(example);
	}

	/**
	 * 按订单查询报表汇总信息
	 * @param StatisticalReportVo
	 * @return List<ReportInfo>
	 */
	@Override
	public List<ReportInfo> selectOrderByTime(
			StatisticalReportVo statisticalReportVo) {
		return reportInfoDao.selectOrderByTime(statisticalReportVo);
	}

	@Override
	public List<StatisticalReportPojo> getCategoryReport(Map<String, String> map) {
		List<StatisticalReportPojo> list=reportInfoDao.getCategoryReport(map);
		return list;
	}
	@Override
	public List<StatisticalReportPojo> getAllCategoryReport(Map<String, String> map) {
		List<StatisticalReportPojo> list=reportInfoDao.getAllCategoryReport(map);
		return list;
	}
	@Override
	public List<StatisticalReportPojo> getCategoryReportCount(Map<String, String> map) {
		List<StatisticalReportPojo> list=reportInfoDao.getCategoryReportCount(map);
		return list;
	}
}