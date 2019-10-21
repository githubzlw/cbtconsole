package com.winit.api.service;

import java.util.Map;
import java.util.TreeMap;

import com.winit.api.model.RequestMsg;

public class QueryInventory extends QueryBase{
	
	public void toDo() {
		doAction();
	}

	@Override
	protected void setdBusinessData(RequestMsg requestMsg) {
		Map<String, Object> data = new TreeMap<String, Object>();
		//产品类型ID
		data.put("categoryID", "");
		//DOI层级： 1：30以下 2：30-60 3：60-90 4：90以上
		data.put("DOITier", "");
		//库存类型：Country：国家，Warehouse：仓库
		data.put("inventoryType", "Warehouse");
		//商品是否有效,Y/N
		data.put("isActive", "Y");
		//页码
		data.put("pageNum", "1");
		//每页显示数量
		data.put("pageSize", "100");
		//产品SKU编码
		data.put("productCode", "");
		//产品名称
		data.put("name", "");
		data.put("specification", "");
		//	仓库ID（warehouseID、warehouseCode必填其中一个）
		data.put("warehouseId", "");
		//仓库Code（warehouseID、warehouseCode必填其中一个）
		data.put("warehouseCode", "");
		requestMsg.setData(data);
		
	}

	@Override
	protected void setRequestAction(RequestMsg requestMsg) {
		requestMsg.setAction("queryProductInventoryList4Page");
		
	}

	@Override
	protected void parseRequestResult(String result) {
		System.out.println(result);
		
	}

}
