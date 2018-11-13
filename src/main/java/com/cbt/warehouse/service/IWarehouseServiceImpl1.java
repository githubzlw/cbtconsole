package com.cbt.warehouse.service;

import com.cbt.warehouse.dao.IWarehouseDao1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IWarehouseServiceImpl1 implements IWarehouseService1 {

	@Autowired
	private IWarehouseDao1 iwarehouseDao ;
	
	@Override
	public void insertSp(String shipmentno) {
		// TODO Auto-generated method stub
		iwarehouseDao.insertSp(shipmentno);
	}

}
