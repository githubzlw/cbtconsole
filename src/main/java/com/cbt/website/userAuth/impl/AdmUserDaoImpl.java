package com.cbt.website.userAuth.impl;

import com.cbt.jdbc.DBHelper;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.bean.Admuser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdmUserDaoImpl implements AdmUserDao {

    @Override
    public Admuser getAdmUser(String name, String pass) throws Exception {
        StringBuffer sql = new StringBuffer("select * from admuser where 1=1 ");
        if (name != null && !"".equals(name)) {
            sql.append("and admName = '" + name + "' ");
        }
        if (pass != null && !"".equals(pass)) {
            sql.append("and password = md5('" + pass + "')");
        }
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Admuser admuser = null;
        try {
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                System.err.println(sql + " has next");
                admuser = new Admuser();
                admuser.setId(rs.getInt("id"));
                admuser.setAdmName(rs.getString("admName"));
                admuser.setEmail(rs.getString("email"));
                admuser.setPassword(rs.getString("password"));
                admuser.setTitle(rs.getString("title"));
                admuser.setRoletype(rs.getString("roleType"));
                admuser.setStatus(rs.getString("status"));
                admuser.setEmialpass(rs.getString("emailpass"));
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
        return admuser;
    }

    @Override
    public Admuser getAdmUserMd5(String name, String pass) throws Exception {
        String sql = "select * from admuser where 1=1 and admName = ? and password = ? ";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Admuser admuser = null;
        try {

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, pass);
            rs = stmt.executeQuery();
            if (rs.next()) {
                System.err.println(sql + " has next");
                admuser = new Admuser();
                admuser.setId(rs.getInt("id"));
                admuser.setAdmName(rs.getString("admName"));
                admuser.setEmail(rs.getString("email"));
                admuser.setPassword(rs.getString("password"));
                admuser.setTitle(rs.getString("title"));
                admuser.setRoletype(rs.getString("roleType"));
                admuser.setStatus(rs.getString("status"));
                admuser.setEmialpass(rs.getString("emailpass"));
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
        return admuser;
    }

    @Override
    public Admuser getAdmUserById(int id) throws Exception {
        String sql = "select * from admuser where id= " + id;
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Admuser admuser = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                admuser = new Admuser();
                admuser.setId(rs.getInt("id"));
                admuser.setAdmName(rs.getString("admName"));
                admuser.setEmail(rs.getString("email"));
                admuser.setPassword(rs.getString("password"));
                admuser.setTitle(rs.getString("title"));
                admuser.setRoletype(rs.getString("roleType"));
                admuser.setStatus(rs.getString("status"));
                admuser.setEmialpass(rs.getString("emailpass"));
            }
        } catch (Exception e) {
            throw new Exception(e);
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
        return admuser;
    }

    @Override
    public List<String> getAllAdmuser(String admName) throws Exception {
        StringBuffer sql = new StringBuffer("select distinct admName from admuser where 1=1 and status =1");
        if (admName != null && !"".equals(admName)) {
            sql.append(" and admName != '" + admName + "'");
        }

        sql.append(" order by status desc,admName");
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> userList = new ArrayList<String>();
        try {
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                userList.add(rs.getString("admName"));
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
        return userList;
    }

    @Override
    public Admuser getAdmUserFromUser(int userid) throws Exception {
        String sql = "select * from admuser where id in (select adminid from admin_r_user where userid = ?)";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Admuser admuser = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                admuser = new Admuser();
                admuser.setId(rs.getInt("id"));
                admuser.setAdmName(rs.getString("admName"));
                admuser.setEmail(rs.getString("email"));
                admuser.setPassword(rs.getString("password"));
                admuser.setTitle(rs.getString("title"));
                admuser.setRoletype(rs.getString("roleType"));
                admuser.setStatus(rs.getString("status"));
                admuser.setEmialpass(rs.getString("emailpass"));
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
        return admuser;
    }

    @Override
    public List<Admuser> queryForList() throws Exception {

        String sql = "select * from admuser where 1=1";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Admuser> admusers = new ArrayList<Admuser>();
        try {
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                Admuser admuser = new Admuser();
                admuser.setId(rs.getInt("id"));
                admuser.setAdmName(rs.getString("admName"));
                admuser.setEmail(rs.getString("email"));
                admuser.setPassword(rs.getString("password"));
                admuser.setTitle(rs.getString("title"));
                admuser.setRoletype(rs.getString("roleType"));
                admuser.setStatus(rs.getString("status"));
                admuser.setEmialpass(rs.getString("emailpass"));
                admusers.add(admuser);
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
        return admusers;
    }

    @Override
    public List<Admuser> queryByRoleType(int roleType) throws Exception {

        String sql = "select * from admuser where 1=1 and roleType = " + roleType;
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Admuser> admusers = new ArrayList<Admuser>();
        try {
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                Admuser admuser = new Admuser();
                admuser.setId(rs.getInt("id"));
                admuser.setAdmName(rs.getString("admName"));
                admuser.setEmail(rs.getString("email"));
                admuser.setPassword(rs.getString("password"));
                admuser.setTitle(rs.getString("title"));
                admuser.setRoletype(rs.getString("roleType"));
                admuser.setStatus(rs.getString("status"));
                admuser.setEmialpass(rs.getString("emailpass"));
                admusers.add(admuser);
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
        return admusers;
    }

    /**
     * ??????orderid,goodsid??????????????????
     *
     * @param orderid
     * @param odid
     * @return ????????????ID
     * @author ????????? 2017-07-27
     */
    @Override
    public int queryByBuyerOrderNo(String orderid, String odid) {
        int admuserid = 0;
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT admuserid FROM goods_distribution WHERE orderid=? AND odid=?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderid);
            stmt.setString(2, odid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                admuserid = rs.getInt("admuserid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
        }
        return admuserid > 0 ? admuserid : 1;
    }

    @Override
    public List<Admuser> queryByOrderNo(String orderNo) throws Exception {

        List<Admuser> admusers = new ArrayList<Admuser>();
        String sql = "select admuser.* from admuser,admin_r_user, orderinfo "
                + "where admuser.id = admin_r_user.adminid and admin_r_user.userid = orderinfo.user_id "
                + "and orderinfo.order_no = ?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Admuser admuser = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                admuser = new Admuser();
                admuser.setId(rs.getInt("id"));
                admuser.setAdmName(rs.getString("admName"));
                admuser.setEmail(rs.getString("email"));
                admuser.setPassword(rs.getString("password"));
                admuser.setTitle(rs.getString("title"));
                admuser.setRoletype(rs.getString("roleType"));
                admuser.setStatus(rs.getString("status"));
                admuser.setEmialpass(rs.getString("emailpass"));
                admusers.add(admuser);
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
        return admusers;

    }

    @Override
    public Admuser querySalesByOrderNoAndGoodid(String orderNo, int goodid)
            throws Exception {
        Admuser admuser = new Admuser();
        String sql = "SELECT a.* FROM admuser a INNER JOIN admin_r_user ar ON a.id=ar.adminid INNER JOIN orderinfo o ON ar.userid=o.user_id WHERE o.order_no=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                admuser = new Admuser();
                admuser.setId(rs.getInt("id"));
                admuser.setAdmName(rs.getString("admName"));
                admuser.setEmail(rs.getString("email"));
                admuser.setPassword(rs.getString("password"));
                admuser.setTitle(rs.getString("title"));
                admuser.setRoletype(rs.getString("roleType"));
                admuser.setStatus(rs.getString("status"));
                admuser.setEmialpass(rs.getString("emailpass"));
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
        return admuser;
    }

    @Override
    public Admuser queryByOrderNoAndGoodid(String orderNo, int goodid) throws Exception {

        Admuser admuser = new Admuser();
        String sql = "select admuser.* from admuser,goods_distribution "
                + "where admuser.id = goods_distribution.admuserid and goods_distribution.orderid = ? "
                + "and goods_distribution.goodsid = ?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderNo);
            stmt.setInt(2, goodid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                admuser = new Admuser();
                admuser.setId(rs.getInt("id"));
                admuser.setAdmName(rs.getString("admName"));
                admuser.setEmail(rs.getString("email"));
                admuser.setPassword(rs.getString("password"));
                admuser.setTitle(rs.getString("title"));
                admuser.setRoletype(rs.getString("roleType"));
                admuser.setStatus(rs.getString("status"));
                admuser.setEmialpass(rs.getString("emailpass"));
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
        return admuser;
    }

	@Override
	public Admuser queryForListByName(String name) {
		 	String sql = "select * from admuser where status=1 and admName=? limit 1";
	        Connection conn = DBHelper.getInstance().getConnection();
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        Admuser admuser = null;
	        try {
	            stmt = conn.prepareStatement(sql.toString());
	            stmt.setString(1, name);
	            rs = stmt.executeQuery();
	            if (rs.next()) {
	            	admuser = new Admuser();
	                admuser.setId(rs.getInt("id"));
	                admuser.setAdmName(rs.getString("admName"));
	                admuser.setEmail(rs.getString("email"));
	                admuser.setPassword(rs.getString("password"));
	                admuser.setTitle(rs.getString("title"));
	                admuser.setRoletype(rs.getString("roleType"));
	                admuser.setStatus(rs.getString("status"));
	                admuser.setEmialpass(rs.getString("emailpass"));
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
	        return admuser;
	}

	public List<Map<String, String>> getAllAnthn() {
		String sql = "select a.admName,a.authId,b.authName,b.url,b.reMark,c.id as admid, " + 
				"c.roleType,c.buyAuto,c.automatic  " + 
				"from  tbl_userauth_info a  " + 
				"left join tbl_auth_info b on a.authId=b.authId " + 
				"left join  admuser c on a.admName=c.admName " + 
				"where b.del=0 and c.`status`=1";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> map = null;
        try {
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
            	map = new HashMap<String, String>();
            	map.put("url", rs.getString("url"));
            	map.put("role", rs.getString("roleType"));
            	map.put("url", rs.getString("url"));
            	map.put("role", rs.getString("roleType"));
            	result.add(map);
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
        return result;
	}
	@Override
	public List<Map<String, String>> getAllAnth() {
//		String sql = "select a.admName,a.authId,b.authName,b.url,b.reMark,c.id as admid, " + 
//				"c.roleType,c.buyAuto,c.automatic  " + 
//				"from  tbl_userauth_info a  " + 
//				"left join tbl_auth_info b on a.authId=b.authId " + 
//				"left join  admuser c on a.admName=c.admName " + 
//				"where b.del=0 and c.`status`=1";
		String sql = "select authId,url from tbl_auth_info";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> map = null;
        try {
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
            	map = new HashMap<String, String>();
            	map.put("url", rs.getString("url"));
            	map.put("authId", rs.getString("authId"));
            	result.add(map);
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
        return result;
	}

}
