package com.cbt.warehouse.service;

import com.cbt.bean.OrderBuyBean;
import com.cbt.common.BaseService;

import java.util.List;

public interface IOrderBuyService extends BaseService<OrderBuyBean> {
	public List<OrderBuyBean> getOrderBuyById(Integer id);
}
 