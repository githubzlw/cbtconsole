package com.cbt.fee.service;

import com.cbt.bean.DeliveryFee;

import java.util.List;

public interface IDeliveryFeeServer {
	//获取运费信息
	public  List<DeliveryFee> getAllDeliveryFee();
}
