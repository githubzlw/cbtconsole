package com.cbt.fee.dao;

import com.cbt.bean.DeliveryFee;

import java.util.List;

/**
 * @author Administrator
 * 国际运费接口
 */
public interface IDeliveryFee {
	//获取运费信息
	public List<DeliveryFee> getAllDeliveryFee();
}
