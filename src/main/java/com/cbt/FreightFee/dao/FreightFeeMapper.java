package com.cbt.FreightFee.dao;

import com.cbt.warehouse.pojo.TransitPricecost;
import com.cbt.warehouse.pojo.ZoneBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FreightFeeMapper {

	ZoneBean getZone(@Param("countId") String countId);

	List<TransitPricecost> selectTransitInfo(@Param("countId") String countId, @Param("shippingmethod") String shippingmethod);

	List<Map<String, Object>> getShippingInfo();
	List<Map<String, Object>> getShippingCostInfo();
	int updatePackCost(@Param("id") String id, @Param("cost") double cost, @Param("company") String company);
	/**
	 * 更新预估运费
	 * @param freightFee
	 * @param expressno
	 */
	public void updateFreightByexpressno(@Param("freightFee") double freightFee, @Param("expressno") String expressno, @Param("id") String id,
                                         @Param("subShippingmethod") String subShippingmethod, @Param("shippingmethod") String shippingmethod);

	/**
	 * 获取订单汇率
	 * @param sp_id
	 * @return
	 */
	public String getOrderRate(@Param("sp_id") String sp_id);

}
