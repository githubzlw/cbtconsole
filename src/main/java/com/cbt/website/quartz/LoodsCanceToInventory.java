package com.cbt.website.quartz;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cbt.jdbc.DBHelper;
import com.cbt.warehouse.service.InventoryService;
import com.cbt.warehouse.service.InventoryServiceImpl;
import com.cbt.website.dao.IOrderSplitDao;
import com.cbt.website.dao.OrderSplitDaoImpl;


public class LoodsCanceToInventory implements Job {
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		 IOrderSplitDao splitDao = new OrderSplitDaoImpl();
		 InventoryService inventoryService = new InventoryServiceImpl();
	     Connection conn = DBHelper.getInstance().getConnection();
		 PreparedStatement stmt = null;
		 ResultSet rs=null;
		 String sql="";
		 List<String> list = new ArrayList<String>();
		try{
			StringBuilder sb=new StringBuilder();
			sql="select od.id as odid,sd.id from split_details sd inner join order_details od on sd.new_orderid=od.goodsid and sd.goodsid=od.orderid where sd.flag=0";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				String od_id=rs.getString("odid");
				list.add(od_id);
//				splitDao.addInventory(od_id,"拆单取消库存");
				sb.append(rs.getInt("id")).append(",");
			}
			if(list.size() > 0) {
				String[] odidLst = list.toArray(new String[list.size()]);  
				inventoryService.cancelToInventory(odidLst, 0, "");
			}
			if(sb.toString().length()>0){
				sql="update split_details set flag=1 where id in ("+sb.toString().substring(0,sb.toString().length()-1)+")";
				stmt=conn.prepareStatement(sql);
				stmt.executeUpdate();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
	}
}
