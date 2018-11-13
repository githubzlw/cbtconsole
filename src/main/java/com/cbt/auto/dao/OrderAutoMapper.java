package com.cbt.auto.dao;

import com.cbt.auto.ctrl.OrderAutoBean;
import com.cbt.auto.ctrl.PureAutoPlanBean;
import com.cbt.bean.AdmDsitribution;
import com.cbt.bean.OrderAutoDetail;
import com.cbt.pojo.GoodsDistribution;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderAutoMapper {
  
	public List getALLOrderDetailCount(@Param("order") String order);

	public List getGoodsDistributionCount(@Param("goodsId") String goodsId);

	public int insertDG(GoodsDistribution gd);

	public int insertPap(PureAutoPlanBean pap);

	public List<AdmDsitribution> getALLAdmDsitribution();

	public List getAllAdmUser();

	public List<OrderAutoDetail> getOrderDetail(@Param("goodid") String goodid, @Param("order") String order);
	
	public int getMaxId();
	
	public List<OrderAutoBean> getAllOrderAuto(int maxId);
	
	public List getAllocatedOrder();
}
