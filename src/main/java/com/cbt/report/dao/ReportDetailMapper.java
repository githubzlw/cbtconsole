package com.cbt.report.dao;

import com.cbt.bean.OrderProductSource;
import com.cbt.bean.buyGoods;
import com.cbt.pojo.*;
import com.cbt.report.vo.OrderProductVo;
import com.cbt.report.vo.StatisticalReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReportDetailMapper {
    int countByExample(ReportDetailExample example);

    int deleteByExample(ReportDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ReportDetail record);

    int insertSelective(ReportDetail record);

    List<ReportDetail> selectByExample(ReportDetailExample example);

    ReportDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ReportDetail record, @Param("example") ReportDetailExample example);

    int updateByExample(@Param("record") ReportDetail record, @Param("example") ReportDetailExample example);

    int updateByPrimaryKeySelective(ReportDetail record);

    int updateByPrimaryKey(ReportDetail record);
    
    List<ReportDetail> selectByTime(StatisticalReportVo statisticalReportVo);
    
    List<OrderProductVo> selectOrderProductByTime(StatisticalReportVo statisticalReportVo);
    
    List<ReportDetail> selectOrderByTime(StatisticalReportVo statisticalReportVo);
    
  List<ReportDetail> selectByGeneral(@Param("generalReport") GeneralReport generalReport, @Param("orderName") String orderName, @Param("startRow") int startRow);
  // List<ReportDetail> selectByGeneral(@Param("beginDate") String beginDate,@Param("endDate") String endDate,@Param("orderName") String orderName);
  int selectByGeneralCount(@Param("generalReport") GeneralReport generalReport);

    List<OrderProductSource> selectByTimeAndCategroy(StatisticalReportVo statisticalReportVo);

   List<buyGoods> selectBuyGoods(@Param("page") Integer page, @Param("epage") Integer epage, @Param("start") String start, @Param("end") String end);
   List<buyGoods> selectBuyGoods1(@Param("page") Integer page, @Param("epage") Integer epage, @Param("start") String start, @Param("end") String end);
   List<buyGoods> selectBuyGoods2(@Param("page") Integer page, @Param("epage") Integer epage, @Param("start") String start, @Param("end") String end);
   List<buyGoods> selectBuyGoods3(@Param("start") String start, @Param("end") String end);
   List<buyGoods> selectBuyGoods4(@Param("start") String start, @Param("end") String end);
   List<buyGoods> selectBuyGoods5(@Param("start") String start, @Param("end") String end);
   int selectBuyGoodsCount(@Param("start") String start, @Param("end") String end);
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
    List<OrderProductSource> selectByTimeAndOrder(StatisticalReportVo statisticalReportVo);

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

	List<ReportDetail> selectOrderDetailByTime(@Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

	List<ReportDetail> selectReportDetailByTime(@Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

    List<ReportDetailOrder> selectOrderReportDetailss(@Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

    //----订单报表
    int insertReportDetailOrder(List<ReportDetailOrder> list);

    List<ReportDetailOrder> selectReportOrder(@Param("generalReport") GeneralReport generalReport, @Param("orderName") String orderName, @Param("startRow") int startRow);
    List<ReportDetailOrder> selectReportOrderTotal(@Param("generalReport") GeneralReport generalReport); //订单报表合计
    int selectReportOrderCount(@Param("generalReport") GeneralReport generalReport);

    int deleteReportDetailOrder(ReportDetailExample example);

    //销售报表
    List<SalesReport> selectSalesReport(@Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo, @Param("orderName") String orderName);
    int insertSalesReport(List<SalesReport> list);
    int selectSalesReportCount(@Param("startTime") String startTime, @Param("endTime") String endTime);
    List<SalesReport> selectSalesReportByPage(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("orderName") String orderName, @Param("page") int page);
    int deleteSalesReportByDate(@Param("startTime") String startTime, @Param("endTime") String endTime);

    int insertSalesReportInfo(ReportSalesInfo ReportSalesInfo);
    int deleteSalesReportInfoByMonth(@Param("years") int years, @Param("months") int months);
    ReportSalesInfo selectSaleReportInfo(@Param("years") int years, @Param("months") int months);
   }