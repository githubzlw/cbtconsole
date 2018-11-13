package com.cbt.website.YFHtask;

import com.cbt.jdbc.DBHelper;
import net.sf.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;

public class YFHtask extends TimerTask {
	public void run() {
		this.getYFHOrder();
		System.out.println("正在执行拉取“原飞航中转编号”作业......");
	}
	
	//读取数据库原飞航单号
	public void getYFHOrder(){
		String sql = "select yfhorder from forwarder where isneed=1 and express_no='' ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String yfhorderno = rs.getString("yfhorder");
				this.getYFHTurnOrder(yfhorderno);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}
	
	//获取原飞航中转单号
	public String getYFHTurnOrder(String yfhOrder){
		String json = this.getOrderMsg("http://www.yfhex.com/ServicePlatform/track?num="+yfhOrder);
		JSONArray jsonArr = JSONArray.fromObject(json);
		String a[] = new String[jsonArr.size()];
		String b[] = new String[jsonArr.size()];
		for (int i = 0; i < jsonArr.size(); i++) {
			a[i] = jsonArr.getJSONObject(i).getString("SecondBillID");
			b[i] = jsonArr.getJSONObject(i).getString("URLAddress");
		}
		for (int i = 0; i < a.length; i++) {
			a[i] = jsonArr.getJSONObject(i).getString("SecondBillID");
		}
		String shipurl = "";
		for (int i = 0; i < b.length; i++) {
			if(b[i].length()!=0){
				shipurl = b[i];
			}
		}
		String yfhTurnOrder = "";
		if(a.length!=0){
			yfhTurnOrder = a[0];
		}
		String yfhshipname = "";
		if(shipurl.length()>7){
			System.out.println("hh"+shipurl);
			yfhshipname = shipurl.substring(4,7) + "en";
		}
		if(yfhTurnOrder.length()==0){
			
		} else {
			String sql = "update forwarder set express_no=?,logistics_name=? where yfhorder=?";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,yfhTurnOrder);
				stmt.setString(2,yfhshipname);
				stmt.setString(3,yfhOrder);
				stmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				DBHelper.getInstance().closeConnection(conn);
			}
		}
		return yfhTurnOrder;
	}
	
	public String getOrderMsg(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			URL reurl = new URL(url);
			URLConnection connection = reurl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in = null;
			}
		}
		return result;
	}
}