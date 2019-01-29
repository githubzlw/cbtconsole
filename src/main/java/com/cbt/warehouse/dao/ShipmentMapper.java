package com.cbt.warehouse.dao;

import com.cbt.warehouse.pojo.Shipment;
import com.cbt.warehouse.pojo.ShipmentExample;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ShipmentMapper {
    int countByExample(ShipmentExample example);

    int deleteByExample(ShipmentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Shipment record);

    int insertSelective(Shipment record);

    List<Shipment> selectByExample(ShipmentExample example);

    Shipment selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Shipment record, @Param("example") ShipmentExample example);

    int updateByExample(@Param("record") Shipment record, @Param("example") ShipmentExample example);

    int updateByPrimaryKeySelective(Shipment record);

    int updateByPrimaryKey(Shipment record);
    
    
    /******add******/
    List<Shipment> selectShipmentByids(@Param("list") List<Integer> list);

    HashMap<?, ?> selectCountShipment(@Param("company") String company, @Param("senttimeBegin") String senttimeBegin, @Param("senttimeEnd") String senttimeEnd, @Param("choiseType") String choiseType, @Param("exchageRate") double exchageRate);

    String getNoTolPrice(@Param("company") String company, @Param("senttimeBegin") String senttimeBegin, @Param("senttimeEnd") String senttimeEnd, @Param("choiseType") String choiseType);


    List<Shipment> selectShipment(@Param("company") String company, @Param("senttimeBegin") String senttimeBegin, @Param("senttimeEnd") String senttimeEnd, @Param("choiseType") String choiseType, @Param("page") Integer page, @Param("pageSize") Integer pageSize, @Param("exchageRate") double exchageRate);

	/**
	 * 运单&运费列表查询全部运单信息
	 * @param map
	 * @return
	 */
    List<Shipment> queryAllShipmentInfo(Map<String, String> map);
	/**
	 * 运单&运费列表查询全部运单信息
	 * @param map
	 * @return
	 */
	HashMap<String,Double> queryAllShipmentInfoCount(Map<String, String> map);

	int getNoMatchAmount(Map<String, String> map);

	/**
	 * 运单&运费列表查询当前系统不存在的运单信息
	 * @param map
	 * @return
	 */
	List<Shipment> queryNoMatchShipmentInfo(Map<String, String> map);

	/**
	 * 运单&运费列表查询当前系统不存在的运单信息
	 * @param map
	 * @return
	 */
	HashMap<String,Double> queryNoMatchShipmentInfoCount(Map<String, String> map);

	/**
	 * 运单&运费列表查询运费超过5%的运单的运单信息
	 * @param map
	 * @return
	 */
	List<Shipment> queryBeyondFiveShipmentInfo(Map<String, String> map);

	/**
	 * 运单&运费列表查询运费超过5%的运单的运单信息
	 * @param map
	 * @return
	 */
	HashMap<String,Double> queryBeyondFiveShipmentInfoCount(Map<String, String> map);
	/**
	 * 运单&运费列表查询运费超过5%/重量超过20%的运单的运单信息
	 * @param map
	 * @return
	 */
	List<Shipment> queryBeyondWeightFiveShipmentInfo(Map<String, String> map);

	/**
	 * 运单&运费列表查询运费超过5%/重量超过20%的运单的运单信息
	 * @param map
	 * @return
	 */
	HashMap<String,Double> queryBeyondWeightFiveShipmentInfoCount(Map<String, String> map);

    int getCompensationAmounts(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("comps") String comps);
    //校验list中的运单信息是否存在于本系统
    List<Shipment> selectUnExistShipMent(@Param("idStr") String idStr);


    int deleteShipment(@Param("idList") List<Integer> idList);
    /**
     * 判断该物流商在该月的赔偿款是否已经录入
     * @param datas
     * @param comps
     * @return
     */
    int getLogisticsCompensation(@Param("datas") String datas, @Param("comps") String comps);
    /**
     * 录入某个月的物流赔偿款
     * @param amounts
     * @param datas
     * @return
     */
    int insertSources(@Param("amounts") String amounts, @Param("datas") String datas, @Param("comps") String comps);

    /**
     * 更新某个月的物流赔偿款
     * @param amounts
     * @param datas
     * @return
     */
    int upSources(@Param("amounts") String amounts, @Param("datas") String datas, @Param("comps") String comps);
	/**
	 * 人工判断运输公司给的运费可以支付
	 * whj 2017-08-31
	 */
	int updateShipMentFlag(@Param("idList") List<Integer> idList, @Param("flag_remark") String flag_remark, @Param("totalprice") String totalprice);

    List<Map<String, Object>> shipmentTimeCount(@Param("paytimestart") String paytimestart, @Param("paytimeend") String paytimeend, @Param("days") int days);
    /**
     * 统计采购出库时间
     * @Title PurchaseStatisticsInquiry
     * @Description TODO
     * @param map
     * @return
     * @return List<Map<String,Object>>
     */
    List<Map<String, Object>> PurchaseStatisticsInquiry(Map<Object, Object> map);

	int countShipmentTime(@Param("paytimestart") String paytimestart, @Param("paytimeend") String paytimeend, @Param("days") int days);

	List<Shipment> showCalculFreight(String time);

	/***
	 * 获取最近4个月订单中商品数量
	 * @Title getDetailsCount
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int getDetailsCount();
	/**
	 * 获取最近4个月订单平均采购时间
	 * @Title getOrderBuyTime
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int getOrderBuyTime();
	/**
	 * 获取最近4个月商品平均采购时间
	 * @Title getGoodsbuytime
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int getGoodsbuytime();
	/**
	 * 获取最近4个月订单平均出货时间
	 * @Title getChTime
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int getChTime();
	/**
	 * 获取最近4个月订单数量
	 * @Title getOrderCount
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int getOrderCount();

	int check(@Param("start") String start, @Param("end") String end);

	//查看该运单信息是否已经冻结(已支付过运费款)
	Shipment selectPayedShipMent(String orderno);

	//将不符合条件的运单信息插入校验失败的表
	int insertUnExistOrPayedSelective(Shipment shipment);
	
}