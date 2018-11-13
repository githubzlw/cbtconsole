package com.cbt.report.service;

import com.cbt.bean.OrderProductSource;
import com.cbt.bean.buyGoods;
import com.cbt.pojo.*;
import com.cbt.report.dao.ReportDetailMapper;
import com.cbt.report.vo.OrderProductVo;
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
public class ReportDetailServiceImpl implements ReportDetailService{

	@Autowired
	private ReportDetailMapper reportDetailDao;

	/**
	 * @param ReportDetailExample
	 * 按条件删除报表详细信息
	 */
	@Override
	public int deleteByExample(ReportDetailExample example) {
		return reportDetailDao.deleteByExample(example);
	}

	/**
	 * @param ReportDetail
	 * 插入报表详细
	 */
	@Override
	public int insertSelective(ReportDetail record) {
		return reportDetailDao.insertSelective(record);
	}

	/**
	 * @param ReportDetailExample
	 * 查询报表详细
	 */
	@Override
	public List<ReportDetail> selectByExample(ReportDetailExample example) {
		return reportDetailDao.selectByExample(example);
	}

	/**
	 * @param statisticalReportVo
	 * 从订单信息，商品信息表，分类信息表 中查询要添加的报表基本信息
	 */
	@Override
	public List<ReportDetail> selectByTime(
			StatisticalReportVo statisticalReportVo) {
		return reportDetailDao.selectByTime(statisticalReportVo);
	}

	/**
	 * 查询报表基本信息
	 * @param GeneralReport
	 * @return List<ReportDetail>
	 */
	@Override
	public List<ReportDetail> selectByGeneral(GeneralReport generalReport, String orderName, int startRow) {
		//int month = Integer.parseInt(generalReport.getReportMonth());
		return reportDetailDao.selectByGeneral(generalReport,orderName, startRow);
	}
	public int selectByGeneralCount(GeneralReport generalReport){
		return reportDetailDao.selectByGeneralCount(generalReport);
	}

	@Override
	public List<OrderProductSource> selectByTimeAndCategroy(
			StatisticalReportVo statisticalReportVo) {
		return reportDetailDao.selectByTimeAndCategroy(statisticalReportVo);
	}
    //查询商品购买频率报表
	@Override
	public List<buyGoods> selectBuyGoods(Integer page,String start,String end){
		return reportDetailDao.selectBuyGoods((page-1)*20,20,start,end);
	}

	@Override
	public List<StatisticalReportPojo> getOrderReportList(Map<String, String> map) {
		return reportDetailDao.getOrderReportList(map);
	}
	@Override
	public List<StatisticalReportPojo> getAllOrderReportList(Map<String, String> map) {
		return reportDetailDao.getAllOrderReportList(map);
	}
	@Override
	public List<StatisticalReportPojo> getOrderReportListCount(Map<String, String> map) {
		return reportDetailDao.getOrderReportListCount(map);
	}
	@Override
	public List<buyGoods> selectBuyGoods1(Integer page,String start,String end){
		return reportDetailDao.selectBuyGoods1((page-1)*20,20,start,end);
	}
	@Override
	public List<buyGoods> selectBuyGoods2(Integer page,String start,String end){
		return reportDetailDao.selectBuyGoods2((page-1)*20,20,start,end);
	}
	@Override
	public List<buyGoods> selectBuyGoods4(String start,String end){
		return reportDetailDao.selectBuyGoods4(start,end);
	}@Override
	public List<buyGoods> selectBuyGoods5(String start,String end){
		return reportDetailDao.selectBuyGoods5(start,end);
	}@Override
	public List<buyGoods> selectBuyGoods3(String start,String end){
		return reportDetailDao.selectBuyGoods3(start,end);
	}
	public int selectBuyGoodsCount(String start,String end){
		return reportDetailDao.selectBuyGoodsCount(start,end);
	}
	/**
	 * 查询每个订单报表信息
	 * @param statisticalReportVo
	 */
	@Override
	public List<ReportDetail> selectOrderByTime(
			StatisticalReportVo statisticalReportVo) {
		return reportDetailDao.selectOrderByTime(statisticalReportVo);
	}

    /**
     * 按照订单号查询商品信息
     * @param StatisticalReportVo
     * @return List<OrderProductSource>
     */
	@Override
	public List<OrderProductSource> selectByTimeAndOrder(
			StatisticalReportVo statisticalReportVo) {
		return reportDetailDao.selectByTimeAndOrder(statisticalReportVo);
	}

	/**
	 * 查询时间段商品明细
	 * @param StatisticalReportVo
	 * @return List<OrderProductVo>
	 */
	@Override
	public List<OrderProductVo> selectOrderProductByTime(
			StatisticalReportVo statisticalReportVo) {
		return reportDetailDao.selectOrderProductByTime(statisticalReportVo);
	}

	/**
	 * 批量插入报表明细
	 * @param List<ReportDetail>
	 * @return int
	 */
	@Override
	public int insertReportDetailList(List<ReportDetail> record) {
		return reportDetailDao.insertReportDetailList(record);
	}

	@Override
	public int updateTabletmp() {
		return reportDetailDao.updateTabletmp();
	}

	@Override
	public int updateTabletmp2() {
		return reportDetailDao.updateTabletmp2();
	}

	@Override
	public int createTabletmp(StatisticalReportVo statisticalReportVo) {
		return reportDetailDao.createTabletmp(statisticalReportVo);
	}

	@Override
	public int createTabletmp2() {
		return reportDetailDao.createTabletmp2();
	}

	@Override
	public int updateTabletmpcate() {
		return reportDetailDao.updateTabletmpcate();
	}

	@Override
	public int updateTabletmpcate2() {
		return reportDetailDao.updateTabletmpcate2();
	}

	@Override
	public int createTabletmpcate(StatisticalReportVo statisticalReportVo) {
		return reportDetailDao.createTabletmpcate(statisticalReportVo);
	}

	@Override
	public int createTabletmpcate2() {
		return reportDetailDao.createTabletmpcate2();
	}

	/**
	 * 批量插入订单报表数据
	 */
	@Override
	public int insertReportDetailListO(List<ReportDetail> record) {
		return reportDetailDao.insertReportDetailListO(record);
	}

	@Override
	public List<ReportDetail> selectOrderDetailByTime(String timeFrom,
                                                      String timeTo) {
	
		return reportDetailDao.selectOrderDetailByTime(timeFrom,timeTo);
	}

	@Override
	public List<ReportDetail> selectReportDetailByTime(String timeFrom,
                                                       String timeTo) {
		return reportDetailDao.selectReportDetailByTime(timeFrom,timeTo);
	}
	
	public List<ReportDetailOrder> selectOrderReportDetailss(String timeFrom, String timeTo){
		return reportDetailDao.selectOrderReportDetailss(timeFrom, timeTo);
	}
	
	/**
	 * 插入订单数据 
	 */
	public int insertReportDetailOrder(List<ReportDetailOrder> list){
		return reportDetailDao.insertReportDetailOrder(list);
	}
	public List<ReportDetailOrder> selectReportOrder(GeneralReport generalReport, String orderName, int startRow){
		return reportDetailDao.selectReportOrder(generalReport, orderName, startRow);
	}
	public int selectReportOrderCount( GeneralReport generalReport){
		return reportDetailDao.selectReportOrderCount(generalReport);
	}
	public int deleteReportDetailOrder(ReportDetailExample ex){
		return reportDetailDao.deleteReportDetailOrder(ex);
	}
	public List<ReportDetailOrder> selectReportOrderTotal(GeneralReport generalReport){
		return reportDetailDao.selectReportOrderTotal(generalReport);
	}
	
	// 销售统计报表
    public List<SalesReport> selectSalesReport(String startTime, String endTime, String orderName){
    	return reportDetailDao.selectSalesReport(startTime, endTime, orderName);
    }
    
    public int insertSalesReport(List<SalesReport> list){
    	return reportDetailDao.insertSalesReport(list);
    }
    
    public int selectSalesReportCount(String starttime,String endtime){
    	return reportDetailDao.selectSalesReportCount(starttime, endtime);
    }
    
    public List<SalesReport> selectSalesReportByPage(String starttime, String endtime, String orderName, int page){
    	return reportDetailDao.selectSalesReportByPage(starttime, endtime, orderName, page);
    }
    
    public int deleteSalesReportByDate(String startTime, String endTime){
    	return reportDetailDao.deleteSalesReportByDate( startTime, endTime);
    }
    
    public int insertSalesReportInfo(ReportSalesInfo reportsalesinfo){
    	return reportDetailDao.insertSalesReportInfo(reportsalesinfo);
    }
    public int deleteSalesReportInfoByMonth(int years, int months){
    	return reportDetailDao.deleteSalesReportInfoByMonth(years, months);
    }
    
    public ReportSalesInfo selectSaleReportInfo(int years, int months){
    	return reportDetailDao.selectSaleReportInfo(years, months);
    }
    
}