package com.cbt.processes.dao;

import com.cbt.bean.UserBean;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Utility;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserDao implements IUserDao {

	@Override
	public int regUser(UserBean user) {
		String sql = "insert user(name,pass,email,activationCode,createtime,picture,activationState,activationTime,currency,applicable_credit,businessName) values(?,?,?,?,now(),?,?,now(),?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int googds_id = 0;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getPass());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, user.getActivationCode());
			stmt.setString(5, user.getPicture());
			stmt.setInt(6, user.getActivationState());
			stmt.setString(7, user.getCurrency());
			stmt.setDouble(8, user.getApplicable_credit());
			stmt.setString(9, user.getBusinessName());
			int result = stmt.executeUpdate();
			 if (result == 1) {
					rs = stmt.getGeneratedKeys();
					if (rs.next()) {
						googds_id = rs.getInt(1);
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
	 
		return googds_id;
	}

	public int re() {
		String sql = "insert test(time) values(?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int googds_id = 0;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			Date utilDate=new Date();
			 java.sql.Timestamp time=new java.sql.Timestamp(utilDate.getTime());
			stmt.setTimestamp(1, time);
			int result = stmt.executeUpdate();
			 if (result == 1) {
					rs = stmt.getGeneratedKeys();
					if (rs.next()) {
						googds_id = rs.getInt(1);
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
	 
		return googds_id;
	}
	@Override
	public int upUser(UserBean user) {
		String sql = "update user set name=? where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
		 
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
		return res;
	}

	@Override
	public int getUserName(String name) {
		String sql = "select id from user where name = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt("id");
			}
		}catch (Exception e) {
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
		return res;
	}
	
	@Override
	public int getNameEmail(String name, String email) {
		String sql = "select id from user where name = ? or email = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, email);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt("id");
			}
		}catch (Exception e) {
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
		return res;
	}
	
	@Override
	public UserBean getUserEmail(String email) {
		String sql = "select id,name,activationState,activationCode,activationTime,activationPassCode,activationPassTime from user where email = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		UserBean user = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			rs = stmt.executeQuery();
			if(rs.next()){
				user = new UserBean();
				user.setEmail(email);
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setActivationCode(rs.getString("activationCode"));
				user.setActivationState(rs.getInt("activationState"));
				java.sql.Timestamp date = rs.getTimestamp("activationTime");
				user.setActivationTime(date);
				user.setActivationPassCode(rs.getString("activationPassCode"));
				user.setActivationPassTime(rs.getTimestamp("activationPassTime"));
			}
		}catch (Exception e) {
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
		return user;
	}
	@Override
	public UserBean getUserEmailId(int userid) {
		String sql = "select id,email,name,activationState,activationCode,activationTime,activationPassCode,activationPassTime from user where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		UserBean user = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			if(rs.next()){
				user = new UserBean();
				user.setEmail(rs.getString("email"));
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setActivationCode(rs.getString("activationCode"));
				user.setActivationState(rs.getInt("activationState"));
				java.sql.Timestamp date = rs.getTimestamp("activationTime");
				user.setActivationTime(date);
				user.setActivationPassCode(rs.getString("activationPassCode"));
				user.setActivationPassTime(rs.getTimestamp("activationPassTime"));
			}
		}catch (Exception e) {
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
		return user;
	}

	@Override
	public boolean upUserState(String email) {
		String sql = "update user set activationState=1 where email = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
		return res > 0 ? true : false;
	}

	@Override
	public UserBean getUser(String name, String pass) {
		String sql = "select id,name,activationState,activationCode,createtime,email,currency from user where (name = ? or email=?) and pass = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		UserBean user = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, name);
			stmt.setString(3, pass);
			rs = stmt.executeQuery();
			if(rs.next()){
				user = new UserBean();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setActivationCode(rs.getString("activationCode"));
				user.setActivationState(rs.getInt("activationState"));
				java.sql.Timestamp date = rs.getTimestamp("createtime");
				user.setCreatetime(date);
				user.setCurrency(rs.getString("currency"));
			}
		}catch (Exception e) {
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
		return user;
	}

	
	@Override
	public boolean facebookbound(int userid, String facebookid) {
		String sql = "insert facebookbound(userid,facebook_id,datetime) values(?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int result = 0;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			stmt.setString(2, facebookid);
			 result = stmt.executeUpdate();
			 
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
	 
		return result > 0 ? true : false;
	}

	@Override
	public UserBean getFacebookUser(String facebookid) {
		String sql = "select user.id,name,activationState,activationCode,user.createtime,currency from facebookbound,user where facebookbound.userid=user.id and facebook_id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		UserBean user = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, facebookid);
			rs = stmt.executeQuery();
			if(rs.next()){
				user = new UserBean();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setActivationCode(rs.getString("activationCode"));
				user.setActivationState(rs.getInt("activationState"));
				java.sql.Timestamp date = rs.getTimestamp("createtime");
				user.setCurrency(rs.getString("currency"));
				user.setCreatetime(date);
			}
		}catch (Exception e) {
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
		return user;
	}

	@Override
	public boolean getNameEmial(String name, String email) {
		String sql = "select id from user where name = ? and email = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, email);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt("id");
			}
		}catch (Exception e) {
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
		return res>0?true:false;
	}

	@Override
	public boolean upUserActivationCode(String email, String activationCode ,int state) {
		String sql = "update user set ";
		if(state == 1){
			sql += " activationTime=now(),activationCode=? where email = ?";
		}else{
			sql += " activationPassTime=now(),  activationPassCode=? where email = ?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, activationCode);
			stmt.setString(2, email);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
		return res > 0 ? true : false;
	}

	@Override
	public int upPassword(String password, String email) {
		String sql = "update user set pass=? where email = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, password);
			stmt.setString(2, email);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
		return res;
	}

	@Override
	public int upPasswordName(String password, String name) {
		String sql = "update user set pass=? where name = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, password);
			stmt.setString(2, name);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
		return res;
	}
	
	@Override
	public String getUserEmailName(String email) {
		String sql = "select name from user where email = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String name = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			rs = stmt.executeQuery();
			if(rs.next()){
				name = rs.getString("name");
			}
		}catch (Exception e) {
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
		return name;
	}
 
	@Override
	public int upUserPrice(int userId, double price) {
		
		String sql = "update user set available_m=available_m+(?)  where id =?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(2, userId);
			stmt.setDouble(1, price);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
		return res;
	}

	@Override
	public double[] getUserPrice(int userId) {
		String sql = "select available_m,applicable_credit from user where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		double[] res = new double[2];
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if(rs.next()){
				res[0] = rs.getDouble("available_m");
				res[1] = rs.getDouble("applicable_credit");
			}
		}catch (Exception e) {
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
		return res;
	}

	@Override
	public double getUserApplicableCredit(int userId) {
		String sql = "select applicable_credit from user where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getDouble("applicable_credit");
			}
		}catch (Exception e) {
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
		return res;
	}

	@Override
	public int upUserApplicableCredit(int userId, double acprice) {
		String sql = "update user set applicable_credit=? where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, acprice);
			stmt.setInt(2, userId);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
		return res;
	}

	@Override
	public String getUsernameByid(int userId) {
		// TODO Auto-generated method stub
		String sql = "select * from user where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String res = "";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getString("name");
			}
		}catch (Exception e) {
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
		return res;
	} 
 
	/*@Override
	public String getUserCurrency(int userId) {
		// TODO Auto-generated method stub
		String sql = "select currency from user where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String res = "";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getString("currency");
			}
		}catch (Exception e) {
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
		return res;
	}*/

	@Override
	public int upUserPrice(int userId, double price, double acprice) {
		String sql = "update user set available_m=available_m+(?),applicable_credit=applicable_credit+(?)  where id =?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(3, userId);
			stmt.setDouble(1, price);
			stmt.setDouble(2, acprice);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
		return res;
	}

	@Override
	public String[] getBalance_currency(int userId) {
		String sql = "select available_m,currency from user where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String[] res = new String[2];
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if(rs.next()){
				res[0] = rs.getString("available_m");
				res[1] = rs.getString("currency");
			}
		}catch (Exception e) {
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
		return res;
	} 
	
	@Override
	public UserBean getUserInfo(int userId) {
		String sql = "select s.admName,s.Email from admin_r_user r inner join admuser s on r.adminid = s.id where r.userid = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		UserBean user = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if(rs.next()){
				user = new UserBean();
				user.setName(rs.getString("admName"));
				user.setEmail(rs.getString("Email"));
			}
		}catch (Exception e) {
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
		return user;
	}
	@Override
	public String[] getAdminUser(int adminId, String email, int userId) {
		String sql="select email,emailpass from admuser where ";
		if(userId != 0){
			sql += " id=(select adminid from admin_r_user where userid=?)";
		}else if(Utility.getStringIsNull(email)){
			sql += " ( admname=? or email=? )";
		}else{
			sql += " id=?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String[] emailinfo = null;
		try {
			stmt = conn.prepareStatement(sql);
			if(userId != 0){
				stmt.setInt(1, userId);
			}else if(Utility.getStringIsNull(email)){
				stmt.setString(1, email);
				stmt.setString(2, email);
			}else{
				stmt.setInt(1, adminId);
			}
			rs = stmt.executeQuery();
			if(rs.next()){
				emailinfo = new String[]{rs.getString("email"),rs.getString("emailpass")};
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
		return emailinfo;
	}

	@Override
	public UserBean getUserFromId(int userId) {
		String sql="select * from user where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		UserBean user = new UserBean();
		try {
			stmt = conn.prepareStatement(sql);
			if(userId != 0){
				stmt.setInt(1, userId);
			}
			rs = stmt.executeQuery();
			if(rs.next()){
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setCurrency(rs.getString("currency"));
				user.setBusinessName(rs.getString("businessName"));
				user.setAvailable_m(rs.getString("available_m"));
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
		return user;
	}
	@Override
	public UserBean getUserFromIdForCheck(int userId) {
		String sql="select * from user where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		UserBean user = new UserBean();
		try {
			stmt = conn.prepareStatement(sql);
			if(userId != 0){
				stmt.setInt(1, userId);
			}
			rs = stmt.executeQuery();
			if(rs.next()){
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setCurrency(rs.getString("currency"));
				user.setBusinessName(rs.getString("businessName"));
				user.setAvailable_m(rs.getString("available_m"));
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
		return user;
	}
}
