package com.cbt.website.dao2;

import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.LoggableStatement;
import com.cbt.util.SqlErrorUtil;
import com.cbt.website.bean.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GoodsDao implements IGoodsDao {

	@Override
	public List<Object[]> getGoodsPrice(int type) { 
		// TODO Auto-generated method stub
		String sql = "select sum(googs_price) sums,count(goods_car.id) counts,user.id,name from goods_car,user where user.id=goods_car.userid group by goods_car.userid";
		String sessionsql = "select sum(googs_price) sums,count(goods_car.id) counts,sessionid from goods_car where !isnull(sessionid) group by sessionid";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			if(type==1){
				stmt = conn.prepareStatement(sql);
			}else{
				stmt = conn.prepareStatement(sessionsql);
			} 
			rs =stmt.executeQuery(); 
			if(type==1){
				while(rs.next()){
				  float sums = rs.getFloat("sums");
				  Object[] objects = {(Math.round(sums*100))/100,rs.getInt("counts"),rs.getString("user.id"),rs.getString("name")};
				  list.add(objects);
				}
			}else{
				while(rs.next()){
				  float sums = rs.getFloat("sums");
				  Object[] objects = {(Math.round(sums*100))/100,rs.getInt("counts"),rs.getString("sessionid"),""};
				  list.add(objects);
				}
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
		return list;
	}

	@Override
	public int getGoodsNumber(int type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<UserInfo> getUserInfos(int userid, Date stateDate, Date endDate) {
		String sqluser = "select user.id,name,email,createtime,pass,address,address2,phone_number,statename,zip_code,  (select country  from zone where zone.id = address.country) country from user,address where user.id = address.userid ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<UserInfo> list = new ArrayList<UserInfo>();
		try {
				stmt = conn.prepareStatement(sqluser);
			
			rs =stmt.executeQuery(); 
			 
				while(rs.next()){
				  UserInfo userInfo = new UserInfo();
				  userInfo.setUserid(rs.getInt("user.id"));
				  userInfo.setUserName(rs.getString("name"));
				  userInfo.setEmail(rs.getString("email"));
				  userInfo.setIsfacebook(rs.getString("pass")==null?1:0);
				  userInfo.setAddress(rs.getString("address"));
				  userInfo.setAddress2(rs.getString("address2"));
				  userInfo.setZone(rs.getString("country"));
				  userInfo.setPhone(rs.getString("phone_number"));
				  userInfo.setStatename(rs.getString("statename"));
				  userInfo.setZip_code(rs.getString("zip_code"));
				  userInfo.setCreattime(rs.getString("createtime"));
				  list.add(userInfo);
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
		return list;
	}

	@Override
	public List<UserInfo> getUserInfoForPrice(String conutry,String admUserId,String vip,int userid, Date stateDate,
			Date endDate,Date date,String name,String email,String recipients,String recipientsaddress,String paymentusername,String paymentid,String paymentemail) {
		/*String sqluser = "select user.id,name,email,pass,user.createtime,user.currency,address,address2,phone_number,statename,zip_code,  (select country  from zone where zone.id = address.country) country,
		  (select sum(googs_price*googs_number)from goods_car where state=0 and goods_car.userid = user.id) goods,(select sum(product_cost) from orderinfo where orderinfo.user_id=user.id and state=0) ordersu,
		  (select sum(goodsprice)from order_details,orderinfo where orderinfo.order_no = order_details.orderid and orderinfo.state!=0 and orderinfo.state!=-1  and order_details.userid = user.id) orders,
		  (select sum(payment_amount)from payment where payment.userid = user.id and paystatus=1) pay,user.available_m,
		  (select admuser.admName from admuser,admin_r_user where admuser.id=admin_r_user.adminid and admin_r_user.userid=user.id) as adminname from user  
		  left   join address   on user.id = address.userid and address.id= address.defaultaddress LEFT JOIN payment on payment.userid=user.id and payment.paytype=0 where 1=1 "; */
		String sqluser = "select user_ex.otherphone,user.grade AS gid,user.id,NAME,email,pass,bind_facebook,bind_google,user.createtime,user.currency,address,address2,phone_number,statename,zip_code,  zone.country,admin_r_user.admName, " 
						+" (select grade from user_grade ug where user.grade=ug.id) grade, (select sum(googs_price*googs_number)from goods_car where state=0 and goods_car.userid = user.id) goods, "
						+" (select sum(payment_amount)from payment where payment.userid = user.id and paystatus=1) pay,user.available_m  "
						+" from user INNER JOIN  zone ON zone.id = user.countryId   "
						+"  left join admin_r_user on user.id=admin_r_user.userid "
						+" left join user_ex on user_ex.userid=user.id"
						+" left join address  on user.id = address.userid and address.id= address.defaultaddress  "
						+" LEFT JOIN payment on payment.userid=user.id "
						+" where 1=1";
		Connection conn = DBHelper.getInstance().getConnection2();
		Connection con = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet rs0 = null;
		List<UserInfo> list = new ArrayList<UserInfo>();
		try {
			if(userid!=0){
				sqluser += " and user.id = "+userid;
			}
		     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			if(date != null){
				 Calendar   calendar   =   new   GregorianCalendar(); 
			     calendar.setTime(date); 
			     calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
			     Date dates=calendar.getTime();   //这个时间就是日期往后推一天的结果 
				sqluser += " and user.createtime  between '"+sdf.format(date)+"' and '" + sdf.format(dates)+"'";;
			}else{
				if(stateDate != null && endDate !=null){
					 Calendar   calendar   =   new   GregorianCalendar(); 
				     calendar.setTime(endDate); 
				     calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
				     Date dates=calendar.getTime();   //这个时间就是日期往后推一天的结果 
					sqluser += " and user.createtime  between '"+sdf.format(stateDate)+"' and '" + sdf.format(dates)+"'";;
				}
				if(stateDate != null && endDate ==null){
					sqluser += " and user.createtime > '"+sdf.format(stateDate)+"'";
				}
				if(stateDate == null && endDate !=null){
					sqluser += " and user.createtime < '"+sdf.format(endDate)+"'";
				}
				if(name != null && !name.equals("")){
					sqluser += " and name ='"+name+"'";
				}
				if(!StringUtils.isStrNull(conutry)){
					sqluser += " and zone.country like ('%"+conutry+"%')";
				}
				if(!"0".equals(admUserId)){
					sqluser += " and admin_r_user.adminid ="+admUserId+"";
				}
				//else if(email !=null && !email.equals("")){
				//	sqluser += " and email ='"+email+"'";
				if(email !=null && !email.equals("")){
					email = email.toLowerCase();
				sqluser += " and email like ('%"+email+"%')";

				}
				if(recipientsaddress!=null&&!recipientsaddress.equals("")){
					sqluser += " and (address.address like ('%"+recipientsaddress+"%') ||address.address2 like ('%"+recipientsaddress+"%'))";
				}
				if(paymentusername!=null&&!paymentusername.equals("")){
					paymentusername = paymentusername.toLowerCase();
					sqluser += " and payment.username like ('%"+paymentusername+"%')";
				}
				if(paymentid!=null&&!paymentid.equals("")){
					sqluser += " and payment.paymentid = ('"+paymentid+"')";
				}
				//if(paymentemail!=null&&!paymentemail.equals("")){
				//	sqluser += " and payment.orderdesc = ('"+paymentemail+"')";
				//}
				
				if(paymentemail!=null&&!paymentemail.equals("")){
					paymentemail=paymentemail.toLowerCase();
					sqluser += " and payment.orderdesc like ('%"+paymentemail+"%')";
				}
				if(recipients!=null&&!recipients.equals("")){
					sqluser += " and address.recipients like ('%"+recipients+"%')";
				}
			}
			sqluser += " group by user.id order by id desc";
			stmt = conn.prepareStatement(sqluser);
			System.out.println(sqluser);
			rs =stmt.executeQuery(); 
			
			//8.25 admName在本地查
//			String sql_admName = "select admName from admin_r_user where userid=?";
//			ps = con.prepareStatement(sql_admName);
//			ps.setInt(1, userid);
//			rs0 = ps.executeQuery();
			
			while(rs.next()){
				  UserInfo userInfo = new UserInfo();
				  userInfo.setUserid(rs.getInt("user.id"));
				  userInfo.setUserName(rs.getString("name"));
				  userInfo.setEmail(rs.getString("email"));
				  String pass=rs.getString("pass");
				  String facebook=rs.getString("bind_facebook");
				  String google=rs.getString("bind_google");
				  if(!StringUtils.isStrNull(pass)){
					  userInfo.setLoginStyle("网站登录");
				  }else if(StringUtils.isStrNull(pass) && !StringUtils.isStrNull(google)){
					  userInfo.setLoginStyle("google登录");
				  }else{
					  userInfo.setLoginStyle("facebook登录");
				  }
				  userInfo.setAddress(rs.getString("address"));
				  userInfo.setAddress2(rs.getString("address2"));
				  userInfo.setZone(rs.getString("country"));
				  userInfo.setPhone(rs.getString("phone_number"));
				  userInfo.setStatename(rs.getString("statename"));
				  userInfo.setZip_code(rs.getString("zip_code"));
				  userInfo.setGoodsPrice(rs.getFloat("goods"));
				  userInfo.setCreattime(rs.getString("createtime"));
				  userInfo.setGid(rs.getInt("gid"));
				  userInfo.setGrade(rs.getString("grade"));
				  userInfo.setOtherphone(rs.getString("otherphone"));
				  //userInfo.setOrderPrice(rs.getFloat("orders"));
				  //userInfo.setOrderPriceu(rs.getFloat("ordersu"));
				  userInfo.setPayment(rs.getFloat("pay"));
				  userInfo.setAvailable(rs.getFloat("available_m"));
//				  userInfo.setAdminname(rs.getString("adminname"));
				  // adminName到本地查
//				  String sql_admName = "select admName from admin_r_user where userid="+rs.getInt("id");
//				  ps = con.prepareStatement(sql_admName);
//				  rs0 = ps.executeQuery();
				  if(!StringUtils.isStrNull(rs.getString("admName"))){
					  userInfo.setAdminname(rs.getString("admName"));
				  }else{
					  userInfo.setAdminname("");
				  }
				  userInfo.setCurrency(rs.getString("currency"));
				  list.add(userInfo);
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
			if (rs0 != null) {
				try {
					rs0.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(con);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	@Override
	public List<Object[]> getOrdersPay() {
			String sqluser = "select paymentid,payment.state pstate,payment.orderid,payment.payment_amount,orderinfo.state ostate,orderinfo.addressid,payment.userid from orderinfo,payment where orderinfo.order_no=payment.orderid";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			List<Object[]> list = new ArrayList<Object[]>();
			try {
					stmt = conn.prepareStatement(sqluser);
				
				rs =stmt.executeQuery(); 
				 
					while(rs.next()){
						Object[] obj = {rs.getString("paymentid"),rs.getString("userid"),rs.getString("orderid"),rs.getInt("pstate"),rs.getInt("ostate"),rs.getInt("payment_amount"),rs.getInt("addressid")};
					    list.add(obj);
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
			return list;
	}

	@Override
	public int upOrders(String orderno) {
		String sqlString = "update orderinfo set state=5 where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
		stmt=conn.prepareStatement(sqlString);
		stmt.setString(1, orderno);
		result=stmt.executeUpdate();
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
		return result;
	}
	
	@Override
	public int upPay(String orderno) {
		String sqlString = "update payment set state=5 where orderid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
		stmt=conn.prepareStatement(sqlString);
		stmt.setString(1, orderno);
		result=stmt.executeUpdate();
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
		return result;
	}

	@Override
	public int updateUserCategory(Integer userid,String email,String category,String signkey) {
		String sql = "update user set ";
		if (category != null && category != "") {
			sql += " user_category = '"+category+"' ";
		}
		if (signkey != null && signkey != "") {
			sql += " signkey = '"+signkey+"' ";
		}
		sql += " where 1=1 ";
		if (userid != null && userid != 0) {
			sql += " and id = "+userid ;
		}
		if (email != null && email != "") {
			sql += " and email = '"+email +"' ";
		}
		Connection con = DBHelper.getInstance().getConnection();
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null,stmt1 = null;
		int result = 0;
		int result1 = 0;
		try {
			stmt = con.prepareStatement(sql);
			stmt1 = conn.prepareStatement(sql);
//			stmt = new LoggableStatement(con,sql,0);
//			stmt1= new LoggableStatement(conn,sql,0);
//			 System.out.println("Executing SQL:　"+((LoggableStatement)stmt).getQueryString()); 
			result=stmt.executeUpdate();
			result1=stmt1.executeUpdate();
//			if (result == 0) {
//				result = SqlErrorUtil.executeSqlError(((LoggableStatement)stmt).getQueryString(), 1, 2);
//			}
//			if (result1 == 0) {
//				result1 = SqlErrorUtil.executeSqlError(((LoggableStatement)stmt1).getQueryString(), 2, 2);
//			}
		} catch (Exception e) {
			SqlErrorUtil.executeSqlError(((LoggableStatement)stmt).getQueryString(), 1, 3);
			SqlErrorUtil.executeSqlError(((LoggableStatement)stmt).getQueryString(), 2, 3);
//			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(con);
			DBHelper.getInstance().closeConnection(conn);
		}
		if (result != 0 && result1 != 0) {
			return result;
		}else {
			return 0;
		}
	}

	@Override
	public List<UserInfo> getUserInfo(Integer userid, String email) {
		String sqluser = "select * from user where 1=1 ";
		Connection conn = DBHelper.getInstance().getConnection2();
//		Connection con = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<UserInfo> list = new ArrayList<UserInfo>();
		try {
			if(userid != null && userid != 0){
				sqluser += " and id = "+userid;
			}
			if(email != null && email !=""){
				sqluser += " and email = '"+email+"' ";
			}
			stmt = conn.prepareStatement(sqluser);
			rs =stmt.executeQuery(); 
			while(rs.next()){
				  UserInfo userInfo = new UserInfo();
				  userInfo.setUserid(rs.getInt("id"));
				  userInfo.setUserName(rs.getString("name"));
				  userInfo.setEmail(rs.getString("email"));
				  userInfo.setCurrency(rs.getString("currency"));
				  list.add(userInfo);
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
		return list;
	}
}
