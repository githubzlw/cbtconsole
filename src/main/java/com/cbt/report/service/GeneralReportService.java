package com.cbt.report.service;

import com.cbt.bean.ExpbuyGoods;
import com.cbt.pojo.*;
import com.cbt.report.vo.StatisticalReportVo;
import com.cbt.warehouse.pojo.JcexPrintInfo;
import com.cbt.warehouse.pojo.Shipments;
import com.cbt.warehouse.pojo.ShippingPackage;
import com.cbt.website.bean.InventoryData;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface GeneralReportService {

    int deleteByExample(GeneralReportExample example);

    int insertSelective(GeneralReport record);

    List<GeneralReport> selectByExample(GeneralReportExample example);

    public HSSFWorkbook export(StatisticalReportVo statisticalReportVo, String type);

    public HSSFWorkbook export1(List<ExpbuyGoods> list);

    public HSSFWorkbook exportSalesReport(List<SalesReport> list, ReportSalesInfo info, String sheet);

    GeneralReport selectBySelective(GeneralReport record);

    /**
     * 月拥挤利润统计
     * @param list
     * @return
     */
    public HSSFWorkbook exportUserProfitByMonth(List<OrderSalesAmountPojo> list);
    /**
     * 生成订单分类报表 王宏杰2018-06-21
     * @param map
     * @return
     */
    int insertOrderReport(Map<String, String> map);
    int selectExistExport(GeneralReport generalReport);

    int deleteReportDetailOrder(GeneralReport generalReport);

    public HSSFWorkbook exportTaoBaoOrder(List<TaoBaoOrderInfo> list, double pagePrice);

    public HSSFWorkbook exportGoodsInventory(List<InventoryData> list);

    /**
     * 库存盘点记录导出
     * @param list
     * @return
     */
    public HSSFWorkbook exportUpdateInventoryExcel(List<Inventory> list);
    /**
     * 库存删除记录导出
     * @param list
     * @return
     */
    public HSSFWorkbook exportDeleteInventoryExcel(List<Inventory> list);
    /**
     * 库存损耗记录导出
     * @param list
     * @return
     */
    public HSSFWorkbook exportLossInventoryExcel(List<LossInventoryPojo> list);
    /**
     * 分类销售数据导出
     * @param listCount
     * @return
     */
    public HSSFWorkbook exportCategoryList(List<StatisticalReportPojo> listCount);
    /**
     * 月采购对账报表结果导出excel
     * @param list
     * @return
     */
    public HSSFWorkbook exportBuyReconciliation(List<BuyReconciliationPojo> list);
    /**
     * 采购订单报表导出
     * @param list
     * @return
     */
    public HSSFWorkbook exportBuyOrderDetails(List<TaoBaoOrderInfo> list, String type, BuyReconciliationPojo buyReconciliation);
    /**
     * 当月产生库存明细报表导出
     */
    public HSSFWorkbook exportInventoryAmount(List<InventoryDetailsPojo> list);
    /**
     * 当月产生库存明细报表
     * @param list
     * @return
     */
    public HSSFWorkbook exportSaleInventory(List<InventoryDetailsPojo> list);
    /**
     * 线下采购付款申请明细报表
     * @param list
     * @return
     */
    public HSSFWorkbook exportOfflinePayment(List<OfflinePaymentApplicationPojo> list);
    /**
     * 预估订单运费明细报表导出
     * @param list
     * @return
     */
    public HSSFWorkbook exportForecastAmount(List<ShippingPackage> list, List<Shipments> list2, String type);
    /**
     *佳成面单信息导出
     * @param list
     * @return
     */
    public HSSFWorkbook exportJcexPrintInfo(List<JcexPrintInfo> list);

}