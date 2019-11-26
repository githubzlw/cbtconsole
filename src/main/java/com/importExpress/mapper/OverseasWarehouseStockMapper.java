package com.importExpress.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.importExpress.pojo.OverseasWarehouseStock;
import com.importExpress.pojo.OverseasWarehouseStockLog;
import com.importExpress.pojo.OverseasWarehouseStockParamter;
import com.importExpress.pojo.OverseasWarehouseStockWrap;

public interface OverseasWarehouseStockMapper {

    /**获取订单占用日志
     * @param orderno
     * @return
     */
    List<OverseasWarehouseStockLog> getLogByOrderno(String orderno);
    /**库存列表
     * @param param
     * @return
     */
    List<OverseasWarehouseStock> getStockList(OverseasWarehouseStockParamter param);
    /**库存列表数量
     * @param param
     * @return
     */
    int getStockListCount(OverseasWarehouseStockParamter param);
    
    /**最近一次同步时间
     * @return
     */
    String getLastSyncStock();
    
    /**同步时间
     * @return
     */
    int addSyncStockTime(int syncCount);
    /**库存日志列表
     * @param param
     * @return
     */
    List<OverseasWarehouseStockWrap> getStockLogList(OverseasWarehouseStockParamter param);
    /**库存日志列表数量
     * @param param
     * @return
     */
    int getStockLogListCount(OverseasWarehouseStockParamter param);
    
    /**订单明细
     * @param orderno
     * @return
     */
    List<Map<String,Object>> getOrderDetails(@Param("orderno")String orderno,@Param("odid")int odid);


}