package com.cbt.report.service;

import com.cbt.bean.OrderProductSource;
import com.cbt.bean.buyGoods;
import com.cbt.pojo.*;
import com.cbt.report.vo.OrderProductVo;
import com.cbt.report.vo.StatisticalReportVo;

import java.util.List;
import java.util.Map;

public interface ReportDetailService {
	
	int deleteByExample(ReportDetailExample example);
    
	int insertSelective(ReportDetail record);
	
	List<ReportDetail> selectByExample(ReportDetailExample example);
	
	List<ReportDetail> selectByTime(StatisticalReportVo statisticalReportVo);
	
	List<ReportDetail> selectOrderByTime(StatisticalReportVo statisticalReportVo);
	
	List<ReportDetail> selectByGeneral(GeneralReport generalReport, String orderName, int startRow);
	int selectByGeneralCount(GeneralReport generalReport);

	List<OrderProductSource> selectByTimeAndCategroy(StatisticalReportVo statisticalReportVo);

	/**
	 * 查询订单分类统计报表
	 * @param map
	 * @return
	 */
	List<StatisticalReportPojo> getOrderReportList(Map<String, String> map);
	/**
	 * 查询总订单统计报表
	 * @param map
	 * @return
	 */
	List<StatisticalReportPojo> getAllOrderReportList(Map<String, String> map);
	/**
	 * 查询订单分类总条数统计报表
	 * @param map
	 * @return
	 */
	List<StatisticalReportPojo> getOrderReportListCount(Map<String, String> map);
	List<buyGoods> selectBuyGoods(Integer page, String start, String end);
	List<buyGoods> selectBuyGoods1(Integer page, String start, String end);
	List<buyGoods> selectBuyGoods2(Integer page, String start, String end);
	List<buyGoods> selectBuyGoods3(String start, String end);
	List<buyGoods> selectBuyGoods4(String start, String end);
	List<buyGoods> selectBuyGoods5(String start, String end);
	int selectBuyGoodsCount(String start, String end);

	List<OrderProductSource> selectByTimeAndOrder(StatisticalReportVo statisticalReportVo);

	List<OrderProductVo> selectOrderProductByTime(StatisticalReportVo statisticalReportVo);

	int insertReportDetailList(List<ReportDetail> record);

	 int insertReportDetailListO(List<ReportDetail> record);

    int updateTabletmp();
    int updateTabletmp2();
    int createTabletmp(StatisticalReportVo statisticalReportVo);
    int createTabletmp2();

    int updateTabletmpcate();
    int updateTabletmpcate2();
    int createTabletmpcate(StatisticalReportVo statisticalReportVo);
    int createTabletmpcate2();

	List<ReportDetail> selectOrderDetailByTime(String timeFrom, String timeTo);

	List<ReportDetail> selectReportDetailByTime(String timeFrom, String timeTo);

	List<ReportDetailOrder> selectOrderReportDetailss(String timeFrom, String timeTo);

	int insertReportDetailOrder(List<ReportDetailOrder> list);
    List<ReportDetailOrder> selectReportOrder(GeneralReport generalReport, String orderName, int startRow);
    List<ReportDetailOrder> selectReportOrderTotal(GeneralReport generalReport);
    int selectReportOrderCount(GeneralReport generalReport);
    int deleteReportDetailOrder(ReportDetailExample ex);

    //销售统计报表
    List<SalesReport> selectSalesReport(String startTime, String endTime, String orderName);
    int insertSalesReport(List<SalesReport> list);
    int selectSalesReportCount(String starttime, String endtime);
    public List<SalesReport> selectSalesReportByPage(String starttime, String endtime, String orderName, int page);
    int deleteSalesReportByDate(String startTime, String endTime);

    int insertSalesReportInfo(ReportSalesInfo reportsalesinfo);
    int deleteSalesReportInfoByMonth(int years, int months);
    ReportSalesInfo selectSaleReportInfo(int years, int months);

}