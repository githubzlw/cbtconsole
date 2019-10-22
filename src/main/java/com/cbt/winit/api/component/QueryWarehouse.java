package com.cbt.winit.api.component;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.cbt.winit.api.model.RequestMsg;
import com.cbt.winit.api.model.WarehouseWrap;
import com.google.common.collect.Lists;

import net.sf.json.JSONObject;
/**
 * @author Administrator
 *
 */
public class QueryWarehouse extends QueryBase{
	
	private List<WarehouseWrap> warehouses;
	
	
	public List<WarehouseWrap> getWarehouses() {
		return warehouses;
	}

	@Test
	public void toDo() {
		doAction();
	}

	@Override
	protected void setdBusinessData(RequestMsg requestMsg) {
		requestMsg.setData("");
		
	}

	@Override
	protected void setRequestAction(RequestMsg requestMsg) {
		requestMsg.setAction("queryWarehouse");
		
	}

	@Override
	protected void parseRequestResult(String result) {
		List<WarehouseWrap> lstWarehouse = Lists.newArrayList();
		System.out.println(result);
		JSONObject resultObject = JSONObject.fromObject(result);
		JSONArray lstData = (JSONArray)resultObject.get("data");
		for(int i=0,size=lstData.size();i<size;i++) {
			JSONObject lstObject = (JSONObject)lstData.get(i);
			String warehouseAddress = lstObject.getString("warehouseAddress");
			String warehouseID = lstObject.getString("warehouseID");
			String warehouseName = lstObject.getString("warehouseName");
			String warehouseCode = lstObject.getString("warehouseCode");
			WarehouseWrap wrap = WarehouseWrap.builder().address(warehouseAddress)
														.code(warehouseCode)
														.id(warehouseID)
														.name(warehouseName).build();
			lstWarehouse.add(wrap);
		}
		this.warehouses = lstWarehouse;
	}

}
