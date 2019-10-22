package com.cbt.winit.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cbt.winit.api.component.QueryInventory;
import com.cbt.winit.api.component.QueryWarehouse;
import com.cbt.winit.api.model.WarehouseWrap;
import com.cbt.winit.api.service.WinitService;
@Service
public class WinitServiceImpl implements WinitService {
	private QueryInventory queryInventory = new QueryInventory();
	private QueryWarehouse queryWarehouse = new QueryWarehouse();

	@Override
	public List<WarehouseWrap> queryWarehouse() {
		queryWarehouse.toDo();
		return queryWarehouse.getWarehouses();
	}

	@Override
	public int queryInventory(WarehouseWrap warehouse) {
		queryInventory.setWarehouse(warehouse);
		
		int loopSize = 1;
		for(int i=0;i<loopSize;i++) {
			queryInventory.setPageNum(i+1);
			queryInventory.toDo();
			loopSize = queryInventory.getPageTotal();
		}
		return 0;
	}


}
