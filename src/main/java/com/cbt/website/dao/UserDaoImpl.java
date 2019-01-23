package com.cbt.website.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.cbt.jdbc.DBHelper;
import com.cbt.onlinesql.ctr.SaveSyncTable;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.Utility;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ApplicationSummary;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.GradeDiscount;
import com.cbt.website.userAuth.bean.Admuser;
import com.stripe.model.Event;
import com.stripe.net.ApiResource;

import net.sf.json.JSONObject;

public class UserDaoImpl implements UserDao {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public String getUserEmailByUserName(String username, int userId) {
        String sql = "select email from user where ";
        if (userId != 0) {
            sql += " id=?";
        } else {
            sql += " name=?";
        }
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String email = "";
        try {
            stmt = conn.prepareStatement(sql);
            if (userId != 0) {
                stmt.setInt(1, userId);
            } else {
                stmt.setString(1, username);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                email = rs.getString("email");
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
        return email;
    }

    @Override
    public String getUserIdByEmail(String email) {

        String sql = "select id from user where email=?";
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String id = "";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getString("id");
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
        return id;
    }

    @Override
    public List<Object[]> getMessage(String date, int userid, String username, String email, int page, int pagesize) {
        // TODO Auto-generated method stub
        // and a.create_time=str_to_date('2015-04-21','%Y-%m-%d')
        String sql = "select user_id,user_name,b.email,count(a.id) as count,create_time from guestbook  a LEFT JOIN `user` b  on a.user_id=b.id   where  1=1 ";

        if (!date.equals("") && date != null) {
            sql += "and a.create_time=str_to_date(?,'%Y-%m-%d')";
        }
        sql += " group by user_id,user_name,b.email ORDER BY create_time desc limit ?,?";
        Connection conn = DBHelper.getInstance().getConnection();
        List<Object[]> oblist = new ArrayList<Object[]>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int i = 1;
        try {
            stmt = conn.prepareStatement(sql);

            if (!date.equals("") && date != null) {
                stmt.setString(i, date);
                i++;
            }
            System.out.println(i);
            stmt.setInt(i, (page - 1) * pagesize);
            stmt.setInt(i + 1, pagesize);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] ob = new Object[5];
                ob[0] = rs.getInt("user_id");
                ob[1] = rs.getString("user_name");
                ob[2] = rs.getString("email");
                ob[3] = rs.getString("count");
                ob[4] = rs.getDate("create_time");
                oblist.add(ob);
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
        return oblist;
    }

    @Override
    public List<Object[]> getBatchApplication(String date, int userid, String username, String email, int page,
            int pagesize) {
        // TODO Auto-generated method stub
        // a.createtime>str_to_date('2015-09-01 00:00:00','%Y-%m-%d %H:%i:%s')
        String sql = "select a.userid,a.username,a.email,count(a.id) as count,a.createtime  from goodsdata g,preferential_application  a LEFT JOIN `user` b  on a.userid=b.id   where  1=1 and a.goodsid=g.id group by a.userid,a.username,a.email ORDER BY createtime desc";
        Connection conn = DBHelper.getInstance().getConnection2();
        List<Object[]> oblist = new ArrayList<Object[]>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] ob = new Object[5];
                ob[0] = rs.getInt("userid");
                ob[1] = rs.getString("username");
                ob[2] = rs.getString("email");
                ob[3] = rs.getString("count");
                ob[4] = rs.getString("createtime");
                oblist.add(ob);
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
        return oblist;
    }

    @Override
    public List<Object[]> getPostageDiscount(String date, int userid, String username, String email, int page,
            int pagesize) {
        // TODO Auto-generated method stub
        String sql = "select pd.userid,pd.name,pd.email,count(pd.id) as count,pd.createtime from postage_discounts pd  left join `user` b on pd.userid=b.id where state!=2 GROUP BY pd.userid,pd.name,pd.email order by pd.createtime desc";
        Connection conn = DBHelper.getInstance().getConnection();
        List<Object[]> oblist = new ArrayList<Object[]>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] ob = new Object[5];
                ob[0] = rs.getInt("userid");
                ob[1] = rs.getString("name");
                ob[2] = rs.getString("email");
                ob[3] = rs.getString("count");
                ob[4] = rs.getString("createtime");
                oblist.add(ob);
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
        return oblist;
    }

    @Override
    public List<Object[]> getBusinessInquiries(String date, int userid, String username, String email, int page,
            int pagesize) {
        // TODO Auto-generated method stub
        String sql = "select a.userid,b.`name`,a.email,count(a.id) as count,a.createtime from busiess a left join `user` b  on a.userid=b.id group by a.userid,a.email order by a.createtime desc;";
        Connection conn = DBHelper.getInstance().getConnection();
        List<Object[]> oblist = new ArrayList<Object[]>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] ob = new Object[5];
                ob[0] = rs.getInt("userid");
                ob[1] = rs.getString("name");
                ob[2] = rs.getString("email");
                ob[3] = rs.getString("count");
                ob[4] = rs.getString("createtime");
                oblist.add(ob);
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
        return oblist;
    }
    
    @Override
    public List<GradeDiscount> getDiscount() {
        String sql="select gid,gname from grade_discount where valid=1";
        Connection conn = DBHelper.getInstance().getConnection();
        List<GradeDiscount> list = new ArrayList<GradeDiscount>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                GradeDiscount info = new GradeDiscount();
                info.setGid(rs.getInt("gid"));
                info.setGname(rs.getString("gname"));
                list.add(info);
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
    public List<ConfirmUserInfo> getAll() {
        String sql = "SELECT id,admName,roleType FROM admuser  WHERE STATUS =1 AND roleType IN (0,3,4)";
        Connection conn = DBHelper.getInstance().getConnection();
        List<ConfirmUserInfo> list = new ArrayList<ConfirmUserInfo>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ConfirmUserInfo info = new ConfirmUserInfo();
                info.setId(rs.getInt("id"));
                info.setConfirmusername(rs.getString("admName"));
                info.setRole(rs.getInt("roleType"));
                list.add(info);
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
    public List<ConfirmUserInfo> getAllByRoleType(int roleType) {
        // TODO Auto-generated method stub
        String sql = "SELECT id,admName,roleType,status FROM admuser  WHERE roleType=? order by status desc,admName";
        Connection conn = DBHelper.getInstance().getConnection();
        List<ConfirmUserInfo> list = new ArrayList<ConfirmUserInfo>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, roleType);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (roleType == 0) {
                    if (rs.getInt("status") == 1) {
                        ConfirmUserInfo info = new ConfirmUserInfo();
                        info.setId(rs.getInt("id"));
                        info.setConfirmusername(rs.getString("admName"));
                        info.setRole(rs.getInt("roleType"));
                        list.add(info);
                    }
                } else {
                    ConfirmUserInfo info = new ConfirmUserInfo();
                    info.setId(rs.getInt("id"));
                    info.setConfirmusername(rs.getString("admName"));
                    info.setRole(rs.getInt("roleType"));
                    list.add(info);
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
    public List<ConfirmUserInfo> getAllByOperations() {
        // TODO Auto-generated method stub
        String sql = "SELECT id,admName,roleType FROM admuser  WHERE roleType=3"
                + " and status = 1 order by roleType desc,admName";
        Connection conn = DBHelper.getInstance().getConnection();
        List<ConfirmUserInfo> list = new ArrayList<ConfirmUserInfo>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ConfirmUserInfo info = new ConfirmUserInfo();
                info.setId(rs.getInt("id"));
                info.setConfirmusername(rs.getString("admName"));
                info.setRole(rs.getInt("roleType"));
                list.add(info);
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
    public List<ConfirmUserInfo> getAllUserHasOffUser() {
        // TODO Auto-generated method stub
        String sql = "SELECT id,admName,roleType FROM admuser  WHERE roleType in(0,2,3,4,5) and status = 1 order by roleType desc,admName";
        Connection conn = DBHelper.getInstance().getConnection();
        List<ConfirmUserInfo> list = new ArrayList<ConfirmUserInfo>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ConfirmUserInfo info = new ConfirmUserInfo();
                info.setId(rs.getInt("id"));
                info.setConfirmusername(rs.getString("admName"));
                info.setRole(rs.getInt("roleType"));
                list.add(info);
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
    public List<ApplicationSummary> getApplication(int userid, String username, String email, String previousDate,
            String nextDate, int page, int pagesize, int confirmuserid) {
        // TODO Auto-generated method stub
        // a.createtime>str_to_date('2015-04-21','%Y-%m-%d')
        String sql = "select  sql_calc_found_rows distinct app.*,adm.adminid,(select count(*) from orderinfo where user_id=app.userid) as cnt from applicationsummary app LEFT JOIN admin_r_user adm ";
        sql += " on   (app.userid=adm.userid or adm.useremail=app.useremail) where 1=1";
        if (Utility.getStringIsNull(previousDate) && !Utility.getStringIsNull(nextDate)) {
            sql += " and app.AppDate=str_to_date('" + previousDate + "','%Y-%m-%d')";
        }
        if (userid != 0) {
            sql += " and app.userid =" + userid;
        }
        if (Utility.getStringIsNull(username)) {
            sql += " and app.username='" + username + "'";
        }
        if (Utility.getStringIsNull(email)) {
            sql += " and app.useremail='" + email + "'";
        }
        if (Utility.getStringIsNull(previousDate) && Utility.getStringIsNull(nextDate)) {
            sql += " and app.AppDate>=str_to_date('" + previousDate + "','%Y-%m-%d') and app.AppDate<=str_to_date('"
                    + nextDate + "','%Y-%m-%d')";
        }
        if (confirmuserid != 0) {
            sql += " and adm.adminid=" + confirmuserid;
        }
        sql += " limit ?,?";
        System.out.println(sql);
        Connection conn = DBHelper.getInstance().getConnection();
        List<ApplicationSummary> list = new ArrayList<ApplicationSummary>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        PreparedStatement stmt2 = null;
        int total = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement("select found_rows();");
            stmt.setInt(1, (page - 1) * pagesize);
            stmt.setInt(2, pagesize);
            rs = stmt.executeQuery();
            rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                total = rs2.getInt("found_rows()");
            }
            while (rs.next()) {
                ApplicationSummary temp = new ApplicationSummary();
                temp.setTotal(total);
                temp.setId(rs.getInt("id"));
                temp.setUserid(rs.getInt("userid"));
                if (rs.getString("username") != null && !rs.getString("username").equals("")) {
                    temp.setUsername(rs.getString("username"));
                } else {
                    temp.setUsername("");
                }
                if (rs.getString("useremail") != null && !rs.getString("useremail").equals("")) {
                    temp.setUseremail(rs.getString("useremail"));
                } else {
                    temp.setUseremail("");
                }
                temp.setLeave_message(rs.getInt("leave_message"));
                temp.setBatch_application(rs.getInt("batch_application"));
                temp.setPostage_discount(rs.getInt("postage_discount"));
                temp.setBusiness_inquiries(rs.getInt("business_inquiries"));
                temp.setAdminId(rs.getInt("adm.adminid"));
                temp.setAppDate(rs.getString("AppDate"));
                temp.setCount(rs.getInt("cnt"));
                if (rs.getString("confirmUser") != null && !rs.getString("confirmUser").equals("")) {
                    temp.setConfirmUser(rs.getString("confirmUser"));
                } else {
                    temp.setConfirmUser("无");
                }
                list.add(temp);
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
    public Map<String,String> getIpnaddress(String orderid) {
        String sql = "select ipninfo from ipn_info  where orderNo=?";
        Connection conn = DBHelper.getInstance().getConnection();
        StringBuilder ipnaddress = new StringBuilder();
        Map<String,String> map=new HashMap<String,String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String info =rs.getString("ipninfo");
                if(info.startsWith("<com.stripe.model.Event")){
                    //stripe format
                    Event event = ApiResource.GSON.fromJson(info.substring(info.indexOf("{")), Event.class);
                    String value = event.toJson();
                    map.put("stripe", value);
                    //stripeJson.data.object.source.country
                    String string = JSONObject.fromObject(value).getJSONObject("data").getJSONObject("object").getJSONObject("source").getString("country");
                    logger.debug("from stripe json: country={}",string);
                    map.put("ipnaddress", string);
                }else{
                    //paypal format
                    if(StringUtil.isNotBlank(info) && info.startsWith("{")){
                        info=info.replace("{","").replace("}","");
                        String [] infos=info.split(",");
                        for(String s:infos){
                            String [] data=s.split("=");
                            if(data.length>=2){
                                map.put(data[0].replace(" ",""),data[1]);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getIpnaddress",e);
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
        return map;
    }

    @Override
    public int updateConfirmuser(String confirminfo, int id) {
        // TODO Auto-generated method stub
        String sql = "update applicationsummary set confirmUser=? where id=?";
        Connection conn = DBHelper.getInstance().getConnection();
        int res = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, confirminfo);
            stmt.setInt(2, id);
            res = stmt.executeUpdate();
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
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cbt.website.dao.UserDao#updateAdminuser(int, int) 销售变更
     */
    public int updateAdminuser(int userid, int adminid, String users, String email, String userName, String admName) {
        String Isql = "insert into admin_r_user(userid,username,useremail,adminid,createdate,admName) select  "
                + (userid == 0 ? "ifnull((select id from user where email=?),0)" : "?") + ",?,?,?,now(),? "
                + "FROM DUAL WHERE NOT EXISTS (SELECT * FROM admin_r_user WHERE userid="+(userid == 0 ? "ifnull((select id from user where email='"+email+"'),0)" : userid)+" and useremail='"+email+"' and adminid="+adminid+")";
        Connection conn = DBHelper.getInstance().getConnection();
        // 分配销售 同时更改线上
        Connection conn2 = DBHelper.getInstance().getConnection2();
        int res = 0;
        int res2 = 0;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;

        try {
            String Dsql = "";
            String oneSql = "";
            if (userid != 0) {
                Dsql = "delete from admin_r_user where userid=?";
                stmt1 = conn.prepareStatement(Dsql);
                stmt1.setInt(1, userid);
                stmt1.execute();
                // 分配销售,同时更改线上SQL保存
                // 判断是否开启线下同步线上配置
                if (GetConfigureInfo.openSync()) {
                    oneSql = "delete from admin_r_user where userid=" + userid;
                    SaveSyncTable.InsertOnlineDataInfo(userid, "", "分配销售", "admin_r_user", oneSql);
                } else{
                    pst1 = conn2.prepareStatement(Dsql);
                    pst1.setInt(1, userid);
                    pst1.execute();
                }
            } else if (email != null && !email.equals("")) {
                Dsql = "delete from admin_r_user where useremail=?";
                stmt1 = conn.prepareStatement(Dsql);
                stmt1.setString(1, email);
                stmt1.execute();

                // 分配销售,同时更改线上SQL保存
                // 判断是否开启线下同步线上配置
                if (GetConfigureInfo.openSync()) {
                    oneSql = "delete from admin_r_user where useremail='" + email + "'";
                    SaveSyncTable.InsertOnlineDataInfo(userid, "", "分配销售", "admin_r_user", oneSql);
                } else{
                    pst1 = conn2.prepareStatement(Dsql);
                    pst1.setString(1, email);
                    pst1.execute();
                }
            }
            // stmt = conn.prepareStatement(Dsql);
            // stmt.setString(1, email);
            // stmt.setInt(2, userid);
            // stmt.execute();
            stmt = conn.prepareStatement(Isql);
            pst = conn2.prepareStatement(Isql);

            String secondSql = "";

            if (userid == 0) {
                stmt.setString(1, email);
                pst.setString(1, email);
                secondSql = "insert into admin_r_user(userid,username,useremail,adminid,createdate,admName) "
                        + "select ifnull((select id from user where email='" + email + "'),0),'" + userName + "','"
                        + email + "'," + adminid + ",now()" + ",'" + admName + "' "
                        + "FROM DUAL WHERE NOT EXISTS (SELECT * FROM admin_r_user WHERE userid=ifnull((select id from user where email='" + email + "'),0) and useremail='"+email+"' and adminid="+adminid+")";
            } else {
                stmt.setInt(1, userid);
                pst.setInt(1, userid);
                secondSql = "insert into admin_r_user(userid,username,useremail,adminid,createdate,admName) "
                        + "select " + userid + ",'" + userName + "','" + email + "'," + adminid + ",now()" + ",'"
                        + admName + "' "
                        + "FROM DUAL WHERE NOT EXISTS (SELECT * FROM admin_r_user WHERE userid="+userid+" and useremail='"+email+"' and adminid="+adminid+")";
            }

            stmt.setString(2, userName);
            stmt.setString(3, email);
            stmt.setInt(4, adminid);
            stmt.setString(5, admName);
            stmt.executeUpdate();
            res = 1;

            pst.setString(2, userName);
            pst.setString(3, email);
            pst.setInt(4, adminid);
            pst.setString(5, admName);
            
            // 判断是否开启线下同步线上配置
            if (GetConfigureInfo.openSync()) {
                SaveSyncTable.InsertOnlineDataInfo(userid, "", "分配销售", "admin_r_user", secondSql);
            } else{
                res2 = pst.executeUpdate();
            }

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
            if (stmt1 != null) {
                try {
                    stmt1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst1 != null) {
                try {
                    pst1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeConnection(conn2);
        }

        return res;
    }

    // 记录销售变更日志
    private static void recordAdminuser(int userid, int adminid, String users, String email, String userName,
            String admName) {
        String sql = "insert into admin_r_user_log(id,userid,username,useremail,adminid,createdate,updatedate,updateadmin,admName) (select admin_r_user.id,userid,username,useremail,adminid,createdate,now(),?,admName from admin_r_user where  (useremail!='' and  useremail=?)  or ( userid=?  and userid<>0))";
        Connection conn = DBHelper.getInstance().getConnection();
        Connection conn2 = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt = null;
        PreparedStatement ps = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, users);
            stmt.setString(2, email);
            stmt.setInt(3, userid);
            stmt.executeUpdate();

            ps = conn2.prepareStatement(sql);
            ps.setString(1, users);
            ps.setString(2, email);
            ps.setInt(3, userid);
            ps.executeUpdate();
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
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeConnection(conn2);
        }
    }

    @Override
    public int updateUserAvailable(int userid, float available, String remark,String remarkId, String modifyuser, int usersign,
            float order_ac, int type) {
        String sql0 = "select available_m from user where id=?";
        String sql = "insert into recharge_record(userid,price,type,remark,remark_id,datatime,adminuser,usesign,balanceAfter)"
                + " values(?,?,?,?,?,now(),?,?,?)";
        String sql1 = "update user set available_m=available_m+?,applicable_credit=applicable_credit+? where id=?";
        Connection conn = DBHelper.getInstance().getConnection2();
        int res = 0, res1 = 0;
        PreparedStatement stmt0 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        ResultSet rs = null;
        double available_m = 0;
        try {
            stmt0 = conn.prepareStatement(sql0);
            stmt0.setInt(1, userid);
            rs = stmt0.executeQuery();
            if (rs.next()) {
                available_m = rs.getDouble("available_m");
            }
            available_m = available_m + available + order_ac;
            
            // 判断是否开启线下同步线上配置
            if (GetConfigureInfo.openSync()) {
                
                String syncSql = "";
                if (usersign == 0) {        
                    syncSql = "insert into recharge_record(userid,price,type,remark,remark_id,datatime,adminuser,usesign,balanceAfter) values("
                            + userid + "," + available + "," + type + ",'add:" + remark + "','" + remarkId + "',now(),'"
                            + modifyuser + "'," + usersign + "," + available_m + ")";
                } else {
                    syncSql = "insert into recharge_record(userid,price,type,remark,remark_id,datatime,adminuser,usesign,balanceAfter) values("
                                    + userid + "," + available + "," + type + ",'deduction:" + remark + "','" + remarkId + "',now(),'"
                                    + modifyuser + "'," + usersign + "," + available_m + ")";
                }
                SaveSyncTable.InsertOnlineDataInfo(userid, "", "修改余额", "recharge_record",syncSql);
                //res =1;
                
                String syncSqlTwo = "update user set available_m=available_m+"+available+",applicable_credit="
                        + "applicable_credit+"+order_ac+" where id="+userid;                
                SaveSyncTable.InsertOnlineDataInfo(userid, "", "修改余额", "user",syncSqlTwo);
                res1 =1;
                
                
            } else{
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userid);
                stmt.setFloat(2, available);
                stmt.setInt(3, type);
                if (usersign == 0) {
                    stmt.setString(4, "add:" + remark);
                } else {
                    stmt.setString(4, "deduction:" + remark);
                }
                stmt.setString(5, remarkId);
                stmt.setString(6, modifyuser);
                stmt.setInt(7, usersign);
                stmt.setDouble(8, available_m);
                res = stmt.executeUpdate();
                
                stmt1 = conn.prepareStatement(sql1);
                stmt1.setFloat(1, available);
                stmt1.setFloat(2, order_ac);
                stmt1.setInt(3, userid);
                res1 = stmt1.executeUpdate();
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
            if (stmt0 != null) {
                try {
                    stmt0.close();
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
            DBHelper.getInstance().closeConnection(conn);
        }
        return res + res1;
    }

    /*
     * @Override public int updateUserAvailable(int userid,float available,
     * String remark, String modifyuser,int usersign) { String sql="",sql1="";
     * // TODO Auto-generated method stub if(usersign==0){ sql=
     * "insert into recharge_record(userid,price,type,remark,datatime,adminuser,usesign) values(?,?,?,?,now(),?,0)"
     * ; sql1="update user set available_m=available_m+? where id=?"; }else{
     * sql=
     * "insert into recharge_record(userid,price,type,remark,datatime,adminuser,usesign) values(?,?,?,?,now(),?,1)"
     * ; sql1="update user set available_m=available_m-? where id=?"; }
     * 
     * Connection conn = DBHelper.getInstance().getConnection(); int res=0,res1=0;
     * PreparedStatement stmt = null; PreparedStatement stmt1 = null; ResultSet
     * rs=null; try { stmt = conn.prepareStatement(sql); stmt.setInt(1, userid);
     * stmt.setFloat(2, available); stmt.setInt(3, 5); if(usersign==0){
     * stmt.setString(4, "add:"+remark); }else{ stmt.setString(4,
     * "deduction:"+remark); }
     * 
     * stmt.setString(5, modifyuser); res=stmt.executeUpdate(); stmt1 =
     * conn.prepareStatement(sql1); stmt1.setFloat(1, available);
     * stmt1.setInt(2,userid); res1=stmt1.executeUpdate(); } catch (Exception e)
     * { e.printStackTrace(); } finally { if (rs != null) { try { rs.close(); }
     * catch (SQLException e) { e.printStackTrace(); } } if (stmt != null) { try
     * { stmt.close(); } catch (SQLException e) { e.printStackTrace(); } }
     * DBHelper.getInstance().closeConnection(conn); } return res+res1; }
     */

    @Override
    public boolean isPassword(int modifyuserid, String password) {
        // TODO Auto-generated method stub
        String sql = "select * from admuser where id=? and  password = md5('" + password + "')"  ;
        Connection conn = DBHelper.getInstance().getConnection();
        int count = 0;
        ResultSet res = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, modifyuserid);
            res = stmt.executeQuery();
            while (res.next()) {
                count++;
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
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getAvailableMoneyRemark(int userid) {
        // TODO Auto-generated method stub
        String sql = "select * from recharge_record where type=5 and userid=?;";
        Connection conn = DBHelper.getInstance().getConnection2();
        String str = "";
        ResultSet res = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userid);
            res = stmt.executeQuery();
            while (res.next()) {
                str += res.getString("remark") + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                try {
                    res.close();
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
        return str;
    }

    @Override
    public String[] getAdminUser(int adminId, String email, int userId) {
        String sql = "select email,emailpass from admuser where ";
        if (userId != 0) {
            sql += " id=(select adminid from admin_r_user where userid=?)";
        } else if (Utility.getStringIsNull(email)) {
            sql += " name=?";
        } else {
            sql += " id=?";
        }
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String[] emailinfo = null;
        try {
            stmt = conn.prepareStatement(sql);
            if (userId != 0) {
                stmt.setInt(1, userId);
            } else if (Utility.getStringIsNull(email)) {
                stmt.setString(1, email);
            } else {
                stmt.setInt(1, adminId);
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                emailinfo = new String[] { rs.getString("email"), rs.getString("emailpass") };
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
    public List<ConfirmUserInfo> getCurSub(Admuser user) {
        StringBuffer sql = new StringBuffer("select id,admName,roleType from admuser  where status= 1 ");
        if (Integer.parseInt(user.getRoletype()) == 0) {
            sql.append("and roleType in (0,1) ");
        } else {
            sql.append("and id = " + user.getId());
        }
        Connection conn = DBHelper.getInstance().getConnection();
        List<ConfirmUserInfo> list = new ArrayList<ConfirmUserInfo>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                ConfirmUserInfo info = new ConfirmUserInfo();
                info.setId(rs.getInt("id"));
                info.setConfirmusername(rs.getString("admName"));
                info.setRole(rs.getInt("roleType"));
                list.add(info);
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
    public String getCustomerNameByUid(int uid) {
        // TODO Auto-generated method stub
        String sql = "select name from user where id=?;";
        Connection conn = DBHelper.getInstance().getConnection();
        String str = "";
        ResultSet res = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, uid);
            res = stmt.executeQuery();
            while (res.next()) {
                str += res.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                try {
                    res.close();
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
        return str;
    }
}
