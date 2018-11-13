package com.cbt.FreightFee.service;

import java.util.List;
import java.util.Map;

public interface FreightFeeSerive {

	/**
	 * 运费接口
	 * @param weights_  重量
	 * @param volume_   泡重     例如:10*10*10/5000
	 * @param countId   国家id
	 * @param shippingmethod   运输方式
	 * @param subShippingmethod   子运输方式(一般原飞航,JCEX 没有,针对 CNE 例如：邮政-全球速递)
	 * @return
	 */
	Map getFreightFee(double weights_, double volume_, String countId,
                      String shippingmethod, String subShippingmethod, String volume);

	List<Map<String, Object>> getShippingInfo();
	List<Map<String, Object>> getShippingCostInfo();
	int updatePackCost(String id, double cost, String company);
	void updateFreightByexpressno(double freightFee, String expressno, String id, String subShippingmethod, String shippingmethod);

	/**
	 * 获取出运订单的实时汇率
	 * @param sp_id
	 * @return
	 */
	double getOrderRate(String sp_id);

}
