package com.cbt.warehouse.dao;

import com.cbt.bean.OrderBuyBean;
import com.cbt.common.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IOrderbuySSMDao extends BaseDao<OrderBuyBean> {
	public List<OrderBuyBean> getOrderBuyById(@Param("id") Integer id);
} 
