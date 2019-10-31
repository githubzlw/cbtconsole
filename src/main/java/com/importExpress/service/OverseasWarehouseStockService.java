package com.importExpress.service;

import java.util.List;
import java.util.Map;

import com.importExpress.pojo.OverseasWarehouseStock;
import com.importExpress.pojo.OverseasWarehouseStockLog;
import com.importExpress.pojo.OverseasWarehouseStockParamter;
import com.importExpress.pojo.OverseasWarehouseStockWrap;

public interface OverseasWarehouseStockService {

    /**订单完结或者取消减少订单占用库存，可用库存增加
     * @param stock
     * @return
     */
    int reduceOrderStock(OverseasWarehouseStock stock,String orderno,int odid,String remark);
    
    /**获取订单占用日志
     * @param orderno
     * @return
     */
    List<OverseasWarehouseStockLog> getLogByOrderno(String orderno);

   /* *//**海外仓库存变更记录日志
     * @param log
     * @return
     *//*
    int addLog(OverseasWarehouseStockLog log);*/

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
    
    /**同步库存
     * @param stock
     * @return
     */
    int syncStock(OverseasWarehouseStock stock);
    /**同步时间
     * @return
     */
    int addSyncStockTime(int syncCount);
    /**MQ更新海外仓订单添加运单号
	 * @param map
	 * @return
	 */
	int addOverseasWarehouseStockOrder(Map<String,Object> map);
	
	/**MQ更新海外仓订单出货
	 * @param map
	 * @return
	 */
	int shipoutOverseasWarehouseStockOrder(Map<String,Object> map);
}
