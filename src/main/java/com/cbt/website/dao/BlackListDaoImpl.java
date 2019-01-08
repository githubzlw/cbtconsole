package com.cbt.website.dao;

import com.cbt.bean.BlackList;
import com.cbt.bean.SpiderBean;
import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlackListDaoImpl implements BlackListDao {
	
	@Override
	public int[] addBlackList(BlackList blackList, Set<String> ipSet) {
		String sql = "insert into tblacklist(email,createtime,operatorid,userip,username) values (?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res[] = {};
		try {
			stmt = conn.prepareStatement(sql);
			if(ipSet != null && ipSet.size()>0){
				for(String ip : ipSet){
					stmt.setString(1, blackList.getBlackVlue());
					stmt.setString(2, blackList.getCreatetime());
					stmt.setString(3, blackList.getOperatorid());
					stmt.setString(4, ip);
					stmt.setString(5, blackList.getUsername());
					stmt.addBatch();
				}
				res = stmt.executeBatch();
			}						
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
	public boolean getBlackListCount(BlackList blackList) {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("select count(*) as count from tblacklist where 1=1 ");
		String email=blackList.getBlackVlue();
		String createtime=blackList.getCreatetime();
		String operatorid=blackList.getOperatorid();
//		String userip=blackList.getUserip();
		String username=blackList.getUsername();
		if(email !=null && !"".equals(email)){
			sql.append(" and email='"+email+"'");
		}
		
		if(createtime !=null && !"".equals(createtime)){
			sql.append(" and createtime='"+createtime+"'");
		}
		
		if(operatorid !=null && !"".equals(email)){
			sql.append(" and operatorid='"+operatorid+"'");
		}
		
//		if(userip !=null && !"".equals(userip)){
//			sql.append(" and userip='"+userip+"'");
//		}
		
		if(username !=null && !"".equals(username)){
			sql.append(" and username='"+username+"'");
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		boolean flag=true;
		try {
			stmt = conn.prepareStatement(sql.toString());	
			rs = stmt.executeQuery();
			if(rs.next()){
				String count=rs.getString("count");
				if(Integer.parseInt(count)>0){
					flag=false;
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
		return flag;
	}
	
	@Override
	public int getBlackListPageCount(BlackList blackList) {
		// TODO Auto-generated method stub
		Integer id=blackList.getId();
		String email=blackList.getBlackVlue();
		String createtime=blackList.getCreatetime();
		String operatorid=blackList.getOperatorid();
//		String userip=blackList.getUserip();
		String username=blackList.getUsername();
		StringBuffer sql=new StringBuffer();
		sql.append("select count(*) as count from tblacklist where 1=1 ");
		if(id != null){
			sql.append(" and id="+id);
		}
		
		if(email !=null && !"".equals(email)){
			sql.append(" and email='"+email+"'");
		}
		
		if(createtime !=null && !"".equals(createtime)){
			sql.append(" and createtime='"+createtime+"'");
		}
		
		if(operatorid !=null && !"".equals(email)){
			sql.append(" and operatorid='"+operatorid+"'");
		}
		
//		if(userip !=null && !"".equals(userip)){
//			sql.append(" and userip='"+userip+"'");
//		}
		
		if(username !=null && !"".equals(username)){
			sql.append(" and username='"+username+"'");
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int total=0;
		try {
			stmt = conn.prepareStatement(sql.toString());	
			rs = stmt.executeQuery();
			if(rs.next()){
				total=rs.getInt("count");
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
		return total;
	}
	
	@Override
	public List<BlackList> getBlackListPage(BlackList blackList, int pagenum, int pagesize) {
		// TODO Auto-generated method stub
		Integer id=blackList.getId();
		String email=blackList.getBlackVlue();
		String createtime=blackList.getCreatetime();
		String operatorid=blackList.getOperatorid();
//		String userip=blackList.getUserip();
		String username=blackList.getUsername();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from tblacklist where 1=1 ");
		if(id != null){
			sql.append(" and id="+id);
		}
		
		if(email !=null && !"".equals(email)){
			sql.append(" and email='"+email+"'");
		}
		
		if(createtime !=null && !"".equals(createtime)){
			sql.append(" and createtime='"+createtime+"'");
		}
		
		if(operatorid !=null && !"".equals(email)){
			sql.append(" and operatorid='"+operatorid+"'");
		}
		
//		if(userip !=null && !"".equals(userip)){
//			sql.append(" and userip='"+userip+"'");
//		}
//
		if(username !=null && !"".equals(username)){
			sql.append(" and username='"+username+"'");
		}
		sql.append("limit "+((pagenum-1)*pagesize)+","+pagesize );	
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<BlackList> list=new ArrayList<BlackList>();
		try {
			stmt = conn.prepareStatement(sql.toString());	
			rs = stmt.executeQuery();
			while(rs.next()){
				blackList=new BlackList();
				blackList.setId(rs.getInt("id"));
//				blackList.setEmail(rs.getString("email"));
				blackList.setCreatetime(rs.getString("createtime"));
				blackList.setOperatorid(rs.getString("operatorid"));
//				blackList.setUserip(rs.getString("userip"));
				blackList.setUsername(rs.getString("username"));
				list.add(blackList);
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
	public int delBlackList(String ids) {
		// TODO Auto-generated method stub
		String sql = "delete from tblacklist where id in ("+ids+") ";
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
	public BlackList getBlackList(BlackList blackList) {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("select * from tblacklist where 1=1 ");
		Integer id=blackList.getId();
//		String email=blackList.getEmail();
		String createtime=blackList.getCreatetime();
		String operatorid=blackList.getOperatorid();
//		String userip=blackList.getUserip();
		String username=blackList.getUsername();
		if(id != null){
			sql.append(" and id="+id);
		}
//
//		if(email !=null && !"".equals(email)){
//			sql.append(" and email='"+email+"'");
//		}
		
		if(createtime !=null && !"".equals(createtime)){
			sql.append(" and createtime='"+createtime+"'");
		}
		
//		if(operatorid !=null && !"".equals(email)){
//			sql.append(" and operatorid='"+operatorid+"'");
//		}
//
//		if(userip !=null && !"".equals(userip)){
//			sql.append(" and userip='"+userip+"'");
//		}
		
		if(username !=null && !"".equals(username)){
			sql.append(" and username='"+username+"'");
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {
			stmt = conn.prepareStatement(sql.toString());	
			rs = stmt.executeQuery();
			while(rs.next()){
				blackList.setId(rs.getInt("id"));
//				blackList.setEmail(rs.getString("email"));
				blackList.setCreatetime(rs.getString("createtime"));
				blackList.setOperatorid(rs.getString("operatorid"));
//				blackList.setUserip(rs.getString("userip"));
				blackList.setUsername(rs.getString("username"));
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
		return blackList;
	}
	
	@Override
	public int modifyBlackList(BlackList blackList) {
		String sql="update tblacklist set email =? , userip=? where id =?"; 
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, blackList.getEmail());
//			stmt.setString(2, blackList.getUserip());
			stmt.setInt(3, blackList.getId());
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
	public List<SpiderBean> getBlackgoods() {
		// TODO Auto-generated method stub
		String sql="select * from goodsdata where valid=0"; 
		List<SpiderBean> templist=new ArrayList<SpiderBean>();
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet res = null;
		try {
			stmt = conn.prepareStatement(sql);
			res = stmt.executeQuery();
			while(res.next()){
				SpiderBean temp=new SpiderBean();
				temp.setName(res.getString("name"));
				temp.setGoodsdata_id(res.getInt("id"));
				temp.setUrl(res.getString("url"));
				templist.add(temp);
			}
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
		return templist;
	}

	@Override
	public void addBlackgoods(String url) {
		// TODO Auto-generated method stub
		String sql = "update goodsdata set valid=0 where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			stmt.executeUpdate();
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
	}

}
