package com.cbt.website.service;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GoodsPriceHistoryserviceImpl implements GoodsPriceHistoryservice {

	@Override
	public List<Object[]> seehistoryPrice(String url) throws Exception {
		String sql = "select * from goods_price_historys where goods_url = ?;";
		List<Object[]> list = new ArrayList<Object[]>();
		if (!url.isEmpty() && !"".equals(url)) {
			Connection conn = DBHelper.getInstance().getConnection2();
			PreparedStatement stmt = null;
			ResultSet res = null;
			try {
				stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, url);
				res = stmt.executeQuery();
				while (res.next()) {
					Object[] obj1=new Object[2];
					Object[] obj2=new Object[2];
					Object[] obj3=new Object[2];
					Object[] obj4=new Object[2];
					Object[] obj5=new Object[2];
					Object[] obj6=new Object[2];
					Object[] obj7=new Object[2];
					Object[] obj8=new Object[2];
					obj1[0] = res.getString("new_price");
					obj1[1] = res.getString("new_time");
					obj2[0] = res.getString("nlt_price");
					obj2[1] = res.getString("nlt_time");
					obj3[0] = res.getString("nol_price");
					obj3[1] = res.getString("nol_time");
					obj4[0] = res.getString("nte_price");
					obj4[1] = res.getString("nte_time");
					obj5[0] = res.getString("oct_price");
					obj5[1] = res.getString("oct_time");
					obj6[0] = res.getString("ods_price");
					obj6[1] = res.getString("ods_time");
					obj7[0] = res.getString("oel_price");
					obj7[1] = res.getString("oel_time");
					obj8[0] = res.getString("old_price");
					obj8[1] = res.getDate("old_time");
//					goodsPriceHistory.setGoodsUrl(res.getString("goods_url"));
					
					if (!res.getString("new_price").isEmpty()) {
					list.add(obj1);
					}if (!res.getString("nlt_price").isEmpty()) {
					list.add(obj2);
					}if (!res.getString("nol_price").isEmpty()) {
					list.add(obj3);
					}if (!res.getString("nte_price").isEmpty()) {
					list.add(obj4);
					}if (!res.getString("oct_price").isEmpty()) {
					list.add(obj5);
					}if (!res.getString("ods_price").isEmpty()) {
					list.add(obj6);
					}if (!res.getString("oel_price").isEmpty()) {
					list.add(obj7);
					}if (!res.getString("old_price").isEmpty()) {
					list.add(obj8);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				stmt.close();
				DBHelper.getInstance().closeConnection(conn);
			}
		}
		return list;
	}

}
