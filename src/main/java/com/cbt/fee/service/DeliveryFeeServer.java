package com.cbt.fee.service;

import com.cbt.bean.DeliveryFee;
import com.cbt.fee.dao.DeliveryFeeDao;
import com.cbt.fee.dao.IDeliveryFee;

import java.util.List;

public class DeliveryFeeServer implements IDeliveryFeeServer{
	
	IDeliveryFee dao = new DeliveryFeeDao();
	@Override
	public List<DeliveryFee> getAllDeliveryFee() {
		// TODO Auto-generated method stub
		return dao.getAllDeliveryFee();
	}
	
}
