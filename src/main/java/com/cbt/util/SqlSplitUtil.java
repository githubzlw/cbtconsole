package com.cbt.util;

public class SqlSplitUtil {
	
	public static String coutSqlCombine(String sql){
		StringBuilder countSql= new StringBuilder("select count(*) ");
		
		countSql.append(sql.substring(sql.indexOf("from"), sql.indexOf("limit")));
		
		return countSql.toString();
	}
	
	public static void main(String[] args) {
		String sql="SELECT o.order_no,o.user_id,o.create_time,od.car_img from orderinfo o "
				+ " LEFT JOIN order_details od on o.order_no = od.orderid WHERE o.user_id=105 order by create_time DESC limit 0,10";
		System.out.println(coutSqlCombine(sql));
		
		
		
	}
	
}
