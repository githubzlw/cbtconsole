package com.importExpress.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.jdbc.DBHelper;
import com.google.common.collect.Maps;
import com.importExpress.mapper.OverseasWarehouseStockMapper;
import com.importExpress.pojo.OverseasWarehouseStock;
import com.importExpress.pojo.OverseasWarehouseStockLog;
import com.importExpress.pojo.OverseasWarehouseStockParamter;
import com.importExpress.pojo.OverseasWarehouseStockWrap;
import com.importExpress.service.OverseasWarehouseStockService;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
@Service
public class OverseasWarehouseStockServiceImpl implements OverseasWarehouseStockService {
    @Autowired
    private OverseasWarehouseStockMapper stockMapper;

    @Override
    public int reduceOrderStock(OverseasWarehouseStock stock,String orderno,int odid,String remark) {
    	/*int reduceOrderStock = stockMapper.reduceOrderStock(stock);
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
    	*/
    	String runSql = new StringBuilder("update overseas_warehouse_stock set order_stock=order_stock-")
    			.append(stock.getOrderStock()).append(",available_stock=available_stock+")
    			.append(stock.getOrderStock()).append(" where code=").append(stock.getCode()).toString();
    	//mq操作更新线上数据表
    	int reduceOrderStock = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
    	if(reduceOrderStock > 0) {
    		runSql = new StringBuilder("insert into overseas_warehouse_stock_log (ows_id, change_stock,od_id,orderno,change_type,remark,code,create_time)" )
    				.append("values ((select id from overseas_warehouse_stock where code=\"" )
    				.append(stock.getCode()).append("\" limit 1),")
    				.append(stock.getOrderStock()).append(", ")
    				.append(odid).append(",\"")
    				.append(orderno).append("\", 1,\"")
    				.append(remark).append("\",\"")
    				.append(stock.getCode()).append("\",now())")
    				.toString();
    		SendMQ.sendMsg(new RunSqlModel(runSql));
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
		/*int updateStock = stockMapper.updateStock(stock);
		if(updateStock == 0) {
			updateStock = stockMapper.addStock(stock);
		}*/
		String runSql = new StringBuilder("update overseas_warehouse_stock set ow_stock=")
				.append(stock.getOwStock())
				.append(",available_stock=")
				.append(stock.getOwStock())
				.append("-order_stock,update_time=now() where code=")
				.append(stock.getCode())
				.toString();
		int updateStock = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
		if(updateStock == 0) {
			runSql = new StringBuilder("insert into overseas_warehouse_stock(ow_stock,remark,available_stock,goods_pid,")
					.append("goods_name,code,sku,skuid,specid,create_time,update_time,order_stock)values(")
					.append(stock.getOwStock()).append(",\"")
					.append(stock.getRemark()).append("\",")
					.append(stock.getOwStock()).append(",\"")
					.append(stock.getGoodsPid()).append("\",\"")
					.append(stock.getGoodsName()).append("\",\"")
					.append(stock.getCode()).append("\",\"")
					.append(stock.getSku()).append("\",\"")
					.append(stock.getSkuid()).append("\",\"")
					.append(stock.getSpecid()).append("\",now(),now(),)")
					.toString();
			updateStock = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
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
