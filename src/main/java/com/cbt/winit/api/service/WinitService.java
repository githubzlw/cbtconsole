package com.cbt.winit.api.service;

import java.util.List;

import com.cbt.winit.api.model.WarehouseWrap;

public interface WinitService {
	
	/**
	 * 获取仓库列表
	 * @return
	 */
	List<WarehouseWrap> queryWarehouse();
	
	/**获取仓库库存
	 * @return
	 */
	int queryInventory(WarehouseWrap warehouse);

}
