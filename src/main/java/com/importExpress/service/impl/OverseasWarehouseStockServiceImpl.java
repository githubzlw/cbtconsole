package com.importExpress.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.jdbc.DBHelper;
import com.cbt.util.StrUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
    public int reduceOrderStock(String orderno,int odid,String remark) {
    	int reduceOrderStock = 0;
    	//获取订单数据
    	List<Map<String,Object>> orderDetails = stockMapper.getOrderDetails(orderno,odid);
    	Set<String> shipedCoden = Sets.newHashSet();
    	for(Map<String,Object> m : orderDetails) {
    		int changeType = Integer.parseInt(StrUtils.object2NumStr(m.get("change_type")));
    		
    		String od_id = StrUtils.object2NumStr(m.get("odid"));
    		odid = Integer.parseInt(od_id);
    		if(changeType > 0) {
    			shipedCoden.add(od_id);
    			continue;
    		}
    		if(shipedCoden.contains(od_id)) {
    			continue;
    		}
    		String coden = StrUtils.object2Str(m.get("code_n"));
    		int orderStock = Integer.parseInt(StrUtils.object2NumStr(m.get("yourorder")));
    		orderno =  StrUtils.object2Str(m.get("orderid"));
    		
    		remark = remark.replace("orderno", orderno);
    		remark = remark.replace("odid", StrUtils.object2NumStr(m.get("odid")));
    		remark = remark.replace("orderStock", StrUtils.object2NumStr(m.get("yourorder")));
    		
    		String runSql = new StringBuilder("update overseas_warehouse_stock set order_stock=order_stock-")
    				.append(orderStock).append(",available_stock=available_stock+")
    				.append(orderStock).append(" where code_n='").append(coden).append("'").toString();
    		//mq操作更新线上数据表
    		reduceOrderStock += Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
    		if(reduceOrderStock > 0) {
    			runSql = new StringBuilder("insert into overseas_warehouse_stock_log (ows_id,code, change_stock,od_id,orderno,change_type,remark,code_n,create_time)" )
    					.append("values ((select id,code, from overseas_warehouse_stock where code_n='" )
    					.append(coden).append("' limit 1),")
    					.append(orderStock).append(", ")
    					.append(odid).append(",'")
    					.append(orderno).append("', 1,'")
    					.append(remark).append("','")
    					.append(coden).append("',now())")
    					.toString();
    			SendMQ.sendMsg(new RunSqlModel(runSql));
    		}
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
				.append("-order_stock,update_time=now() where code_n='")
				.append(stock.getCoden())
				.append("'")
				.toString();
		int updateStock = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
		if(updateStock == 0) {
			runSql = new StringBuilder("insert into overseas_warehouse_stock(ow_stock,remark,available_stock,goods_pid,")
					.append("goods_name,code,code_n,sku,skuid,specid,create_time,update_time,order_stock)values(")
					.append(stock.getOwStock()).append(",'")
					.append(stock.getRemark()).append("',")
					.append(stock.getOwStock()).append(",'")
					.append(stock.getGoodsPid()).append("','")
					.append(stock.getGoodsName()).append("','")
					.append(stock.getCode()).append("','")
					.append(stock.getCoden()).append("','")
					.append(stock.getSku()).append("','")
					.append(stock.getSkuid()).append("','")
					.append(stock.getSpecid()).append("',now(),now(),0)")
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
	@Override
	public int addOwsOrderShipno(Map<String, Object> map) {
		String sql = "update overseas_warehouse_ship_package set ship_no=? where orderid=?";
		List<String> lstValues = Lists.newArrayList();
		lstValues.add(String.valueOf(map.get("shipno")));
		lstValues.add(String.valueOf(map.get("orderno")));
		String runSql = DBHelper.covertToSQL(sql, lstValues);
		runSql = DBHelper.covertToSQL(sql, lstValues);
		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
		int result = StrUtils.isNum(sendMsgByRPC) ? Integer.parseInt(sendMsgByRPC) : 0;
		if(result == 0) {
			sql = "insert into overseas_warehouse_ship_package(ship_no,orderid,state,create_time) values(?,?,?,now())";
			lstValues.add("0");
			runSql = DBHelper.covertToSQL(sql, lstValues);
			sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
			result = StrUtils.isNum(sendMsgByRPC) ? Integer.parseInt(sendMsgByRPC) : 0;
		}
		return result;
	}

	@Override
	public int shipoutOwsOrder(Map<String, Object> map) {
		String sql = "update overseas_warehouse_ship_package set ship_out_time=now(),state=1 where orderid=?";
		List<String> lstValues = Lists.newArrayList();
		lstValues.add(String.valueOf(map.get("orderno")));
		String runSql = DBHelper.covertToSQL(sql, lstValues);
		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
		return StrUtils.isNum(sendMsgByRPC) ? Integer.parseInt(sendMsgByRPC) : 0;
	}
	@Override
	public int updateOrderState(String orderno) {
		String sql = "update orderinfo set state=3  where order_no=?";
		List<String> lstValues = Lists.newArrayList();
		lstValues.add(orderno);
		String runSql = DBHelper.covertToSQL(sql, lstValues);
		String sendMsgByRPC = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
		return StrUtils.isNum(sendMsgByRPC) ? Integer.parseInt(sendMsgByRPC) : 0;
	}
}
