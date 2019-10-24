package com.importExpress.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.importExpress.mapper.OverseasWarehouseStockMapper;
import com.importExpress.pojo.OverseasWarehouseStock;
import com.importExpress.pojo.OverseasWarehouseStockLog;
import com.importExpress.pojo.OverseasWarehouseStockParamter;
import com.importExpress.pojo.OverseasWarehouseStockWrap;
import com.importExpress.service.OverseasWarehouseStockService;
@Service
public class OverseasWarehouseStockServiceImpl implements OverseasWarehouseStockService {
    @Autowired
    private OverseasWarehouseStockMapper stockMapper;

    @Override
    public int reduceOrderStock(OverseasWarehouseStock stock,String orderno,int odid,String remark) {
    	int reduceOrderStock = stockMapper.reduceOrderStock(stock);
    	if(reduceOrderStock > 0) {
    		OverseasWarehouseStockLog log = OverseasWarehouseStockLog.builder()
    				.changeStock(stock.getOrderStock())
    				.changeType(1)
    				.code(stock.getCode())
    				.odid(odid)
    				.orderno(orderno)
    				.remark(remark)
    				.build();
    		stockMapper.addLog(log);
    	}
    	
        return reduceOrderStock;
    }
	@Override
	public List<OverseasWarehouseStock> getStockList(OverseasWarehouseStockParamter param) {
		
		return stockMapper.getStockList(param);
	}

	@Override
	public int getStockListCount(OverseasWarehouseStockParamter param) {
		
		return stockMapper.getStockListCount(param);
	}

	@Override
	public String getLastSyncStock() {
		
		return stockMapper.getLastSyncStock();
	}
	@Override
	public List<OverseasWarehouseStockWrap> getStockLogList(OverseasWarehouseStockParamter param) {
		
		return stockMapper.getStockLogList(param);
	}
	
	@Override
	public int getStockLogListCount(OverseasWarehouseStockParamter param) {
		
		return stockMapper.getStockLogListCount(param);
	}
	@Override
	public int syncStock(OverseasWarehouseStock stock) {
		int updateStock = stockMapper.updateStock(stock);
		if(updateStock == 0) {
			updateStock = stockMapper.addStock(stock);
		}
		return updateStock;
	}
	@Override
	public int addSyncStockTime(int syncCount) {
		return stockMapper.addSyncStockTime(syncCount);
	}
	@Override
	public List<OverseasWarehouseStockLog> getLogByOrderno(String orderno) {
		Map<String,OverseasWarehouseStockLog> zMap = Maps.newHashMap();
		List<OverseasWarehouseStockLog> logs = stockMapper.getLogByOrderno(orderno);
		logs.forEach(l->{
			String key = l.getOrderno()+"_"+l.getOdid();
			OverseasWarehouseStockLog zl = zMap.get(key);
			
			if(zl != null) {
				if(zl.getChangeType() == l.getChangeType()) {
					l.setChangeStock(l.getChangeStock()+zl.getChangeStock());
				}else {
					l.setChangeStock(Math.abs(l.getChangeStock()-zl.getChangeStock()));
				}
			}
			zMap.put(key, l);
		});
		logs.clear();
		zMap.entrySet().iterator().forEachRemaining(z ->{
			logs.add(z.getValue());
		});
		return logs;
	}
	
}
