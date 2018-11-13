package com.cbt.customer.dao;

import com.cbt.bean.*;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.StrUtils;
import com.cbt.warehouse.util.StringUtil;
import com.importExpress.controller.TabSeachPageController;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ShopUrlDaoImpl implements IShopUrlDao {
    private static final Log LOG = LogFactory.getLog(ShopUrlDaoImpl.class);

    @SuppressWarnings("resource")
    @Override
    public ShopUrl findById(int id) {
        ShopUrl su = null;
        String sql = "select * from shop_url_bak where id=?";
        String sql1 = "select quality_avg,service_avg,level from supplier_scoring  where shop_id=?";
        String sql2 = "select shop_url from shop_type_url where parent_id=?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        List<ShopTypeUrl> stuList = new ArrayList<ShopTypeUrl>();
        conn = DBHelper.getInstance().getConnection5();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                su = new ShopUrl();
                su.setId(rs.getInt("id"));
                su.setShopId(rs.getString("shop_id"));
                su.setShopName(rs.getString("shop_name"));
                su.setShopUrl(rs.getString("shop_url"));
                su.setAdmUser(rs.getString("admuser"));
                su.setCreateTime(rs.getDate("createtime"));
                su.setUpdatetime(rs.getDate("updatetime"));
                su.setRemark(rs.getInt("remark"));
                su.setFlag(rs.getInt("flag"));
                su.setSystemEvaluation(rs.getInt("system_evaluation"));
                su.setProFlag(rs.getInt("pro_flag"));
                su.setDownloadNum(rs.getInt("download_num"));
                su.setIsValid(rs.getInt("is_valid"));
                su.setUrlType(rs.getInt("url_type"));
                su.setSalesVolume(rs.getInt("sales_volume_threshold"));
                su.setInputShopName(rs.getString("input_shop_name"));
                su.setInputShopDescription(rs.getString("input_shop_description"));
                su.setInputShopEnName(rs.getString("shop_enname"));
                su.setInputShopBrand(rs.getString("shop_brand"));
                su.setOnlineStatus(rs.getInt("online_status"));

                stmt = conn.prepareStatement(sql1);
                stmt.setString(1, su.getShopId());
                rs1 = stmt.executeQuery();
                while (rs1.next()) {
                    su.setQualityAvg(rs1.getString("quality_avg") + "");
                    su.setServiceAvg(rs1.getString("service_avg"));
                    su.setLevel(rs1.getString("level"));

                }

                stmt = conn.prepareStatement(sql2);
                stmt.setInt(1, id);
                rs2 = stmt.executeQuery();

                while (rs2.next()) {
                    ShopTypeUrl stu = new ShopTypeUrl();
                    stu.setShopUrl(rs2.getString(1));
                    stuList.add(stu);

                }
                su.setStuList(stuList);
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
        return su;
    }

    @Override
    public List<ShopUrl> findAll(String shopId, String shopUserName, String date, int start, int end, String timeFrom,
                                 String timeTo, int isOn, int state, int isAuto, int readyDel,int shopTypeFlag,
                                 int authorizedFlag,int authorizedFileFlag,String shopids) {
        List<ShopUrl> suList = new ArrayList<ShopUrl>();
        ShopUrl su = null;
        String sql = "select a.*,(select count(b.id) from cross_border.custom_benchmark_ready_newest b where b.shop_id =a.shop_id and b.valid=1) as on_line_num, au.file_name au_file_name, au.file_url au_file_url, au.admuser au_admuser, au.start_time au_start_time, au.end_time au_end_time, au.remark au_remark, au.valid au_valid from shop_url_bak a ";
        sql += "LEFT JOIN shop_url_authorized_info au ON a.shop_id = au.shop_id ";
        sql += "where 1=1 ";
        if (shopId != null && !"".equals(shopId)) {
            sql += " and a.shop_id='" + shopId + "'";
        }
        if(StringUtil.isNotBlank(shopids)){
            sql += " and a.shop_id in ("+shopids+")";
        }
        if (shopUserName != null && !"".equals(shopUserName)) {
            sql += " and a.admuser='" + shopUserName + "'";
        }

        if (timeFrom != null && !"".equals(timeFrom) && timeTo != null && !"".equals(timeTo)) {
            sql += " and a.updatetime >='" + timeFrom + "' and a.updatetime<='" + timeTo + "'";
        }
        if (date != null && !"".equals(date)) {
            sql += " and a.updatetime = '" + date + "'";
        }

        if (isOn > -1) {
            sql += " and a.is_valid = " + isOn;
        }
        if (state > -1) {
            sql += " and a.online_status = " + state;
        }
        if (isAuto > -1) {
            sql += " and a.is_auto = " + isAuto;
        }
        if (readyDel > -1) {
            sql += " and a.error_flag = " + readyDel;
        }
        if(shopTypeFlag > -1){
            sql += " and a.shop_type = " + shopTypeFlag;
        }
        if(authorizedFlag > -1){
            sql += " and a.authorized_flag = " + authorizedFlag;
        }
        if(authorizedFileFlag > 0){
        	switch (authorizedFileFlag) {
			case 1: //已授权但无授权文件
				sql += " and a.shop_id in(SELECT a.shop_id FROM shop_url_bak a LEFT JOIN shop_url_authorized_info b ON a.shop_id = b.shop_id WHERE a.authorized_flag = 1 AND (b.file_url is null OR b.file_url = ''))";
				break;
			case 2: //授权文件到期
				sql += " and a.shop_id in(SELECT a.shop_id FROM shop_url_bak a LEFT JOIN shop_url_authorized_info b ON a.shop_id = b.shop_id WHERE a.authorized_flag = 1 AND (b.end_time IS NOT NULL AND b.end_time < NOW()))";
				break;
			case 3: //1+2
				sql += " and a.shop_id in(SELECT a.shop_id FROM shop_url_bak a LEFT JOIN shop_url_authorized_info b ON a.shop_id = b.shop_id WHERE a.authorized_flag = 1 AND (b.file_url is null OR b.file_url = '' OR (b.end_time IS NOT NULL AND b.end_time < NOW())))";
				break;
			default:
				break;
			}
        }  

        sql += " order by a.createtime desc limit " + start + ", " + end + "";
        System.out.println(sql);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        conn = DBHelper.getInstance().getConnection5();
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                su = new ShopUrl();
                su.setId(rs.getInt("id"));
                su.setShopId(rs.getString("shop_id"));
                su.setShopName(rs.getString("shop_name"));
                su.setShopUrl(rs.getString("shop_url"));
                su.setAdmUser(rs.getString("admuser"));
                su.setCreateTime(rs.getDate("createtime"));
                su.setUpdatetime(rs.getDate("updatetime"));
                su.setRemark(rs.getInt("remark"));
                su.setFlag(rs.getInt("flag"));
                su.setSystemEvaluation(rs.getInt("system_evaluation"));
                su.setProFlag(rs.getInt("pro_flag"));
                su.setDownloadNum(rs.getInt("download_num"));
                su.setIsValid(rs.getInt("is_valid"));
                su.setOnlineStatus(rs.getInt("online_status"));
                su.setInputShopName(rs.getString("input_shop_name"));
                su.setInputShopDescription(rs.getString("input_shop_description"));
                su.setInputShopEnName(rs.getString("shop_enname"));
                su.setInputShopBrand(rs.getString("shop_brand"));
                su.getAuthorizedInfo().setFileName(rs.getString("au_file_name"));
                if (StringUtils.isNotBlank(rs.getString("au_file_url"))) {
                	su.getAuthorizedInfo().setFileUrl(TabSeachPageController.IMAGEHOSTURL + rs.getString("au_file_url"));
				}
                su.getAuthorizedInfo().setAdmuser(rs.getString("au_admuser"));
                su.getAuthorizedInfo().setStartTime(rs.getTimestamp("au_start_time"));
                su.getAuthorizedInfo().setEndTime(rs.getTimestamp("au_end_time"));
                su.getAuthorizedInfo().setRemark(rs.getString("au_remark"));
                su.getAuthorizedInfo().setValid(rs.getInt("au_valid"));

                if (rs.getInt("is_valid") == 0) {
                    su.setIsValidView("是");
                } else {
                    su.setIsValidView("否");
                }

                su.setIsAuto(rs.getInt("is_auto"));

                int onlineStatus = rs.getInt("online_status");
                if (onlineStatus == 0) {
                    su.setOnlineStatusView("待办");
                } else if (onlineStatus == 1) {
                    su.setOnlineStatusView("详细信息已下载");
                } else if (onlineStatus == 2) {
                    su.setOnlineStatusView("<a href=\"/cbtconsole/ShopUrlC/jumpGoodsReady.do?shopId=" + su.getShopId()
                            + "\" target=\"_blank\">详情图片已下载(点击进入数据准备/清洗)</a>");
                } else if (onlineStatus == 3) {
                    su.setOnlineStatusView("图片已上传美服");
                } else if (onlineStatus == 4) {
                    su.setOnlineStatusView("数据上线成功");
                } else if (onlineStatus == 5) {
                    su.setOnlineStatusView("数据上传中");
                } else if (onlineStatus == 6) {
                    su.setOnlineStatusView("<a href=\"/cbtconsole/ShopUrlC/jumpGoodsReady.do?shopId=" + su.getShopId()
                            + "\" target=\"_blank\">数据上传失败(请重新上传)</a>");
                } else if (onlineStatus == 7) {
                    su.setOnlineStatusView("<a href=\"/cbtconsole/ShopUrlC/jumpGoodsReady.do?shopId=" + su.getShopId()
                            + "\" target=\"_blank\">数据准备完成，待上线</a>");
                }

                su.setSalesVolume(rs.getInt("sales_volume_threshold"));
                su.setIsAuto(rs.getInt("is_auto"));

                String stateInfo = "";
                if (onlineStatus == 0) {
                    su.setStateInfo("<button class=\"but_color\" onclick=\"edit(" + su.getId() + "," + su.getIsAuto() + ")\">编辑</button>"
                            + "<button class=\"del_color\" onclick=\"delreply(" + su.getId()
                            + ")\">删除</button>");
                } else {
                    stateInfo = "<button class=\"but_color\" onclick=\"edit(" + su.getId() + "," + su.getIsAuto() + ")\">编辑</button>";
                    int errorFlag = rs.getInt("error_flag");
                    if (errorFlag < 1) {
                        stateInfo += "<button class=\"remark_color\" onclick=\"readyDelete('" + su.getShopId()
                                + "')\">备注问题</button>";
                    }else{
                        //1MOQ太高  2水印太多  3不适合运输的产品 4其他
                        if(errorFlag == 1){
                            su.setInputShopDescription("MOQ太高");
                        }else if(errorFlag == 2){
                            su.setInputShopDescription("水印太多");
                        }else if(errorFlag == 3){
                            su.setInputShopDescription("不适合运输");
                        }else if(errorFlag == 4){
                            su.setInputShopDescription("其他问题");
                        }
                    }

                    su.setStateInfo(stateInfo);
                }

                int shopType = rs.getInt("shop_type");
                if(shopType == 0){
                    stateInfo += "<button class=\"but_color\" onclick=\"setShopType('" + su.getShopId()
                            + "',1)\">精品店铺</button>";
                    stateInfo += "<button class=\"remark_color\" onclick=\"setShopType('" + su.getShopId()
                            + "',2)\">侵权店铺</button>";
                }else if(shopType == 1){
                    stateInfo += "<button class=\"remark_color\" onclick=\"setShopType('" + su.getShopId()
                            + "',2)\">侵权店铺</button>";
                    su.setInputShopDescription("精品店铺");
                }else if(shopType == 2){
                    stateInfo += "<button class=\"but_color\" onclick=\"setShopType('" + su.getShopId()
                            + "',1)\">精品店铺</button>";
                    su.setInputShopDescription("侵权店铺");
                }
                int queryAuthorizedFlag = rs.getInt("authorized_flag");
                /*if(queryAuthorizedFlag < 1){
                    stateInfo += "<button class=\"but_color\" onclick=\"setAuthorizedFlag('" + su.getShopId()
                            + "')\">授权店铺</button>";
                }*/
                su.setAuthorizedFlag(queryAuthorizedFlag);
                su.setStateInfo(stateInfo);

                // + "<button style=\"margin-left: 10px;\" onclick=\"delreply("
                // + su.getId() + ")\">删除</button>");
                su.setOnLineNum("<a href=\"/cbtconsole/website/shop_goods_list.jsp?shop_id=" + su.getShopId()
                        + "\" target=\"_blank\">" + rs.getInt("on_line_num") + "(view)" + "</a>");
                suList.add(su);
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
        return suList;
    }

    @Override
    public int total(String shopId, String shopUserName, String date, String timeFrom, String timeTo, int isOn,
                     int state, int isAuto, int readyDel,int shopType,int authorizedFlag,int authorizedFileFlag,String shopids) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;
        String sql = "select count(id) from shop_url_bak where 1=1 ";
        if (shopId != null && !"".equals(shopId)) {
            sql += " and shop_id='" + shopId + "'";
        }
        if(StringUtil.isNotBlank(shopids)){
            sql += " and shop_id in ("+shopids+")";
        }
        if (shopUserName != null && !"".equals(shopUserName)) {
            sql += " and admuser='" + shopUserName + "'";
        }

        if (timeFrom != null && !"".equals(timeFrom) && timeTo != null && !"".equals(timeTo)) {
            sql += " and updatetime >='" + timeFrom + "' and updatetime<='" + timeTo + "'";
        }
        if (date != null && !"".equals(date)) {
            sql += " and updatetime = '" + date + "'";
        }
        if (isOn > -1) {
            sql += " and is_valid = " + isOn;
        }
        if (state > -1) {
            sql += " and online_status = " + state;
        }
        if (isAuto > -1) {
            sql += " and is_auto = " + isAuto;
        }
        if (readyDel > -1) {
            sql += " and error_flag = " + readyDel;
        }
        if(shopType > -1){
            sql += " and shop_type = " + shopType;
        }
        if(authorizedFlag > -1){
            sql += " and authorized_flag = " + authorizedFlag;
        }
        if(authorizedFileFlag > 0){
        	switch (authorizedFileFlag) {
			case 1: //已授权但无授权文件
				sql += " and shop_id in(SELECT a.shop_id FROM shop_url_bak a LEFT JOIN shop_url_authorized_info b ON a.shop_id = b.shop_id WHERE a.authorized_flag = 1 AND (b.file_url is null OR b.file_url = ''))";
				break;
			case 2: //授权文件到期
				sql += " and shop_id in(SELECT a.shop_id FROM shop_url_bak a LEFT JOIN shop_url_authorized_info b ON a.shop_id = b.shop_id WHERE a.authorized_flag = 1 AND (b.end_time IS NOT NULL AND b.end_time < NOW()))";
				break;
			case 3: //1+2
				sql += " and shop_id in(SELECT a.shop_id FROM shop_url_bak a LEFT JOIN shop_url_authorized_info b ON a.shop_id = b.shop_id WHERE a.authorized_flag = 1 AND (b.file_url is null OR b.file_url = '' OR (b.end_time IS NOT NULL AND b.end_time < NOW())))";
				break;
			default:
				break;
			}
        }        

        System.out.println(sql);
        conn = DBHelper.getInstance().getConnection5();
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
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
    public String getShopList(String admName, String days) {
        StringBuilder shopids=new StringBuilder();
        String shop="";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        int result = 0;
        try{
            conn=DBHelper.getInstance().getConnection5();
            StringBuilder sql=new StringBuilder();
            sql.append("select distinct shop_id from shop_url_bak where admuser='"+admName+"'");
            if(StringUtil.isNotBlank(days)){
                sql.append(" AND TO_DAYS(NOW())-TO_DAYS(updatetime)<=").append(days);
            }
            stmt=conn.prepareStatement(sql.toString());
            rs=stmt.executeQuery();
            while(rs.next()){
                shopids.append("'").append(rs.getString("shop_id")).append("',");
            }
            if(shopids.toString().length()>0){
                shop=shopids.toString().substring(0,shopids.toString().length()-1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closePreparedStatement(stmt);
        }
        return shop;
    }

    @SuppressWarnings("resource")
    @Override
    public int delById(int id) {
        String sql = "";
        String sql1 = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        int result = 0;
        sql = "delete from shop_url_bak where id=?";
        sql1 = "delete from shop_type_url where parent_id=" + id;
        conn = DBHelper.getInstance().getConnection5();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            result = stmt.executeUpdate();

            stmt = conn.prepareStatement(sql1);
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
        return result;
    }

    @SuppressWarnings("resource")
    @Override
    public int insertOrUpdate(ShopUrl su, String[] urls) {
        int result = 0;
        int id = 0;
        Connection conn = null, conn1 = null;
        PreparedStatement stmt = null, stmt1 = null;
        String sql = "";
        if (su.getId() != null) {
            sql = "update shop_url_bak set shop_id=?,shop_url=?,is_valid=?,sales_volume_threshold=?,download_num=?,admuser=?,admin_id=?,url_type=?,input_shop_name=?, flag =?,updatetime=?,input_shop_description=?,shop_enname=?,shop_brand=? where id=?";
        } else {
            sql = "insert into shop_url_bak(shop_id,shop_url,is_valid,sales_volume_threshold,download_num,admuser,admin_id,url_type,"
                    + "input_shop_name,createtime,updatetime,input_shop_description,shop_enname,shop_brand,is_trade) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }

        conn = DBHelper.getInstance().getConnection5();
        conn1 = DBHelper.getInstance().getConnection2();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String nowDate = df.format(date);

        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (su.getId() != null) {
                int i = 1;
                stmt.setString(i++, su.getShopId());
                stmt.setString(i++, su.getShopUrl());
                stmt.setInt(i++, su.getIsValid());
                stmt.setInt(i++, su.getSalesVolume());
                stmt.setInt(i++, su.getDownloadNum());
                stmt.setString(i++, su.getAdmUser());
                stmt.setInt(i++, su.getAdminId());
                stmt.setInt(i++, su.getUrlType());
                stmt.setString(i++, su.getInputShopName());
                stmt.setInt(i++, su.getFlag());
                stmt.setString(i++, nowDate);
                stmt.setString(i++, su.getInputShopDescription());
                stmt.setString(i++, su.getInputShopEnName());
                stmt.setString(i++, su.getInputShopBrand());
                stmt.setInt(i++, su.getId());
                id = su.getId();

            } else {
                int i = 1;
                stmt.setString(i++, su.getShopId());
                stmt.setString(i++, su.getShopUrl());
                stmt.setInt(i++, su.getIsValid());
                stmt.setInt(i++, su.getSalesVolume());
                stmt.setInt(i++, su.getDownloadNum());
                stmt.setString(i++, su.getAdmUser());
                stmt.setInt(i++, su.getAdminId());
                stmt.setInt(i++, su.getUrlType());
                stmt.setString(i++, su.getInputShopName());
                stmt.setString(i++, nowDate);
                stmt.setString(i++, nowDate);
                stmt.setString(i++, su.getInputShopDescription());
                stmt.setString(i++, su.getInputShopEnName());
                stmt.setString(i++, su.getInputShopBrand());
                stmt.setInt(i++, su.getIsTrade());
            }
            result = stmt.executeUpdate();

            if (su.getUrlType() == 1) {
                if (su.getId() == null) {
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                } else {
                    id = su.getId();
                }
            }
            String sql1 = "delete from shop_type_url where parent_id=" + id;
            stmt = conn.prepareStatement(sql1);
            stmt.executeUpdate();

            if (urls != null && urls.length > 0) {
                String sql2 = "insert into shop_type_url(parent_id,shop_url)value(?,?) ";
                stmt = conn.prepareStatement(sql2);

                for (String ss : urls) {
                    ss = ss.substring(0, ss.indexOf(".htm") + 4);
                    if (!ss.contains(su.getShopId())) {
                        continue;
                    }
                    stmt.setInt(1, id);
                    stmt.setString(2, ss);
                    stmt.addBatch();
                }
                stmt.executeBatch();

            }

            String sql3 = "replace into shop_description(shop_id,input_shop_description) values(?,?) ";
            if (!org.springframework.util.StringUtils.isEmpty(su.getInputShopDescription())) {
                stmt1 = conn1.prepareStatement(sql3);
                stmt1.setString(1, su.getShopId());
                stmt1.setString(2, su.getInputShopDescription());
                stmt1.executeUpdate();
            }

        } catch (Exception e) {
            // e.printStackTrace();
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
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public List<ShopGoods> findAllGoods(String shopId, int start, int end) {
        List<ShopGoods> sgList = new ArrayList<ShopGoods>();
        ShopGoods sg = null;
        String sql = "select name,pid,custom_main_image,remotpath  from custom_benchmark_ready_newest  where shop_id =? and valid=1 ";
        sql += " order by createtime desc limit " + start + ", " + end + "";
        System.out.println(sql);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        conn = DBHelper.getInstance().getConnection8();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                sg = new ShopGoods();
                sg.setName(rs.getString("name"));
                sg.setPid(rs.getString("pid"));
                sg.setImage(
                        "<img src=\"" + rs.getString("remotpath") + rs.getString("custom_main_image") + "\"></img>");
                sg.setUrlInfo("<div><a href=\"https://detail.1688.com/offer/" + rs.getString("pid")
                        + ".html\" target=\"_blank\">1688原链接</a></div>"
                        + "<div><a href=\"https://www.import-express.com/goodsinfo/baby-formal-dress-plenilune-dress-baby-formal-dress-princess-skirt-1"
                        + rs.getString("pid") + ".html\" target=\"_blank\">线上产品链接</a></div>"
                        + "<div><a href=\"/cbtconsole/editc/detalisEdit?pid=" + rs.getString("pid")
                        + "\" target=\"_blank\">线上产品编辑</a></div>");
                sgList.add(sg);
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
        return sgList;
    }

    @Override
    public int goodsTotal(String shopId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;

        String sql = "select count(id) from custom_benchmark_ready_newest  where shop_id =? and valid=1 ";
        System.out.println(sql);
        conn = DBHelper.getInstance().getConnection8();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
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
    public List<ShopInfoBean> queryInfoByShopId(String shopId, String catid) {
        List<ShopInfoBean> infos = new ArrayList<ShopInfoBean>();
        Connection conn28 = null;
        PreparedStatement stmt28 = null;
        ResultSet rs28 = null;
        Connection connOffer = null;
        PreparedStatement stmtOffer = null;
        ResultSet rsOffer = null;
        String sql28 = "select shop_id,category_id,weight_interval,jd_result as weight,first_interval_rate,"
                + "other_interval_rate,is_choose from shop_categroy_data where shop_id = ? and id > 100583 ";
        if (!(catid == null || "".equals(catid) || "0".equals(catid))) {
            sql28 += " and category_id = ?";
        }
        String sqlOffer = "select catid,count(goods_pid) as goods_num from shop_goods_offers where shop_id= ? GROUP BY catid";
        Map<String,Integer> offerMap = new HashMap<String,Integer>();
        try {

            connOffer = DBHelper.getInstance().getConnection6();
            stmtOffer = connOffer.prepareStatement(sqlOffer);
            stmtOffer.setString(1, shopId);
            rsOffer = stmtOffer.executeQuery();
            while(rsOffer.next()){
                offerMap.put(rsOffer.getString("catid"),rsOffer.getInt("goods_num"));
            }

            int count = 1;
            conn28 = DBHelper.getInstance().getConnection5();
            stmt28 = conn28.prepareStatement(sql28);
            stmt28.setString(count++, shopId);
            if (!(catid == null || "".equals(catid) || "0".equals(catid))) {
                stmt28.setString(count++, catid);
            }
            rs28 = stmt28.executeQuery();
            while (rs28.next()) {
                ShopInfoBean spInfo = new ShopInfoBean();
                spInfo.setCategoryId(rs28.getString("category_id"));
                spInfo.setFirstIntervalRate(rs28.getFloat("first_interval_rate"));
                //spInfo.setGoodsNum(rs.getInt("goods_num"));
                if(offerMap.containsKey(spInfo.getCategoryId())){
                    spInfo.setGoodsNum(offerMap.get(spInfo.getCategoryId()));
                }
                spInfo.setOtherIntervalRate(rs28.getFloat("other_interval_rate"));
                spInfo.setShopId(rs28.getString("shop_id"));
                spInfo.setWeightInterval(rs28.getString("weight_interval"));
                String weight = rs28.getString("weight");
                if (weight == null || "".equals(weight)) {
                    weight = "0";
                } else {
                    weight = StrUtils.matchStr(weight, "(\\d+(\\.\\d+){0,1})");
                    if (weight == null || "".equals(weight)) {
                        weight = "0";
                    }
                }
                spInfo.setWeightVal(Float.valueOf(weight));
                spInfo.setIsChoose(rs28.getInt("is_choose"));
                infos.add(spInfo);
            }
            offerMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closePreparedStatement(stmtOffer);
            DBHelper.getInstance().closeResultSet(rs28);
            DBHelper.getInstance().closeResultSet(rsOffer);
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closeConnection(connOffer);
        }
        return infos;
    }

    @Override
    public List<GoodsProfitReference> queryGoodsProfitReferences(String shopId) {
        List<GoodsProfitReference> profits = new ArrayList<GoodsProfitReference>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select pid,catid1,"
                + "case when REPLACE(func_get_split_string(func_get_split_string(func_get_split_string(wprice, '$', 2),',',1),'-',1),']','')='[' then price "
                + " when REPLACE(func_get_split_string(func_get_split_string(func_get_split_string(wprice, '$', 2),',',1),'-',1),']','')='' then price "
                + " else REPLACE(func_get_split_string(func_get_split_string(func_get_split_string(wprice, '$', 2),',',1),'-',1),']','') end as price,"
                + "final_weight,wholesale_price,wprice,range_price,ali_price,custom_main_image,enname,remotpath"
                + " from custom_benchmark_ready where shop_id = ? and valid =1 ";
        try {
            conn = DBHelper.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);

            rs = stmt.executeQuery();
            while (rs.next()) {
                GoodsProfitReference spInfo = new GoodsProfitReference();
                if (rs.getString("catid1") == null || "".equals(rs.getString("catid1"))) {
                    spInfo.setCategoryId("0");
                } else {
                    spInfo.setCategoryId(rs.getString("catid1"));
                }
                spInfo.setFinalWeight(rs.getString("final_weight"));
                spInfo.setGoodsName(rs.getString("enname"));
                String remotpath = rs.getString("remotpath");
                if (remotpath == null) {
                    remotpath = "";
                }
                spInfo.setImgUrl(remotpath + rs.getString("custom_main_image"));
                spInfo.setPid(rs.getString("pid"));
                spInfo.setPrice(rs.getString("price"));
                spInfo.setRangePrice(rs.getString("range_price"));
                spInfo.setWholesalePrice(rs.getString("wholesale_price"));
                spInfo.setWprice(rs.getString("wprice"));
                spInfo.setAliPrice(rs.getString("ali_price"));

                profits.add(spInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryGoodsProfitReferences error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return profits;
    }

    @Override
    public boolean saveCategoryInfos(List<ShopInfoBean> shopInfos) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String updateSql = "update shop_categroy_data set jd_result = ?,first_interval_rate=?,"
                + "other_interval_rate=?,admin_id=?,is_choose=? where shop_id=? and category_id = ?";
        try {
            conn = DBHelper.getInstance().getConnection5();
            stmt = conn.prepareStatement(updateSql);
            for (ShopInfoBean spInfo : shopInfos) {
                stmt.setString(1, String.valueOf(spInfo.getWeightVal()));
                stmt.setFloat(2, spInfo.getFirstIntervalRate());
                stmt.setFloat(3, spInfo.getOtherIntervalRate());
                stmt.setInt(4, spInfo.getAdminId());
                stmt.setInt(5, spInfo.getIsChoose());
                stmt.setString(6, spInfo.getShopId());
                stmt.setString(7, spInfo.getCategoryId());
                stmt.addBatch();
            }
            int[] rs = stmt.executeBatch();
            return rs.length == shopInfos.size();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("saveCategoryInfos error: " + e.getMessage());
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
        return false;
    }

    @Override
    public boolean batchUpdateGoodsInfo(List<ShopInfoBean> shopInfos) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String updateSql = "update shop_categroy_data set weight = ?,first_interval_rate=?,"
                + "other_interval_rate=?,admin_id=? where shop_id=? and category_id = ?";
        try {
            conn = DBHelper.getInstance().getConnection5();
            stmt = conn.prepareStatement(updateSql);
            for (ShopInfoBean spInfo : shopInfos) {
                stmt.setString(1, String.valueOf(spInfo.getWeightVal()));
                stmt.setFloat(2, spInfo.getFirstIntervalRate());
                stmt.setFloat(3, spInfo.getOtherIntervalRate());
                stmt.setInt(4, spInfo.getAdminId());
                stmt.setString(5, spInfo.getShopId());
                stmt.setString(6, spInfo.getCategoryId());
                stmt.addBatch();
            }
            int[] rs = stmt.executeBatch();
            return rs.length == shopInfos.size();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("batchUpdateGoodsInfo error: " + e.getMessage());
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
        return false;
    }

    @Override
    public List<ShopGoodsInfo> query1688GoodsByShopId(String shopId) {
        List<ShopGoodsInfo> goodsList = new ArrayList<ShopGoodsInfo>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select goods_pid,catid,weight from shop_goods_offers where shop_id = ?";
        try {
            conn = DBHelper.getInstance().getConnection6();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ShopGoodsInfo spGoods = new ShopGoodsInfo();
                spGoods.setPid(rs.getString("goods_pid"));
                spGoods.setCategoryId(rs.getString("catid"));
                spGoods.setWeight(rs.getString("weight"));
                goodsList.add(spGoods);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("query1688GoodsByShopId error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return goodsList;
    }

    @Override
    public List<ShopGoodsInfo> queryDealGoodsByShopId(String shopId) {
        List<ShopGoodsInfo> goodsList = new ArrayList<ShopGoodsInfo>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select shop_id,pid,custom_main_image,remotpath,enname,price,range_price,wprice,sync_flag,"
                + "eninfo,sync_remark,valid from shop_goods_ready where shop_id = ? and p_d_flag < 2";
        try {
            conn = DBHelper.getInstance().getConnection7();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ShopGoodsInfo spGoods = new ShopGoodsInfo();
                spGoods.setShopId(rs.getString("shop_id"));
                spGoods.setPid(rs.getString("pid"));
                spGoods.setRemotePath(rs.getString("remotpath"));
                spGoods.setImgUrl(rs.getString("custom_main_image"));
                spGoods.setEnName(rs.getString("enname"));
                spGoods.setPrice(rs.getString("price"));
                spGoods.setRangePrice(rs.getString("range_price"));
                spGoods.setWprice(rs.getString("wprice"));
                spGoods.setValid(rs.getInt("valid"));
                spGoods.setSyncFlag(rs.getInt("sync_flag"));
                spGoods.setSyncRemark(rs.getString("sync_remark"));
                spGoods.setEnInfo(rs.getString("eninfo"));
                goodsList.add(spGoods);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryDealGoodsByShopId error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return goodsList;
    }

    @Override
    public boolean insertShopInfos(List<ShopInfoBean> infos) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String insertSql = "insert into shop_categroy_data(shop_id,category_id,goods_num,weight_interval,jd_result) "
                + "values(?,?,?,?,?)";
        int rs = 0;
        try {
            conn = DBHelper.getInstance().getConnection5();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(insertSql);
            for (ShopInfoBean spInfo : infos) {
                stmt.setString(1, spInfo.getShopId());
                stmt.setString(2, spInfo.getCategoryId());
                stmt.setInt(3, spInfo.getGoodsNum());
                stmt.setString(4, spInfo.getWeightInterval());
                stmt.setString(5, "0");
                stmt.addBatch();
            }
            rs = stmt.executeBatch().length;
            if (rs == infos.size()) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.err.println("insertShopInfos error: " + e.getMessage());
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
        return rs == infos.size();
    }

    @Override
    public boolean updateShopInfos(List<ShopInfoBean> infos) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String insertSql = "update shop_categroy_data set weight_interval = ? where shop_id = ? and category_id = ? ";
        int rs = 0;
        try {
            conn = DBHelper.getInstance().getConnection5();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(insertSql);
            for (ShopInfoBean spInfo : infos) {
                stmt.setString(1, spInfo.getWeightInterval());
                stmt.setString(2, spInfo.getShopId());
                stmt.setString(3, spInfo.getCategoryId());
                stmt.addBatch();
            }
            rs = stmt.executeBatch().length;
            if (rs == infos.size()) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.err.println("updateShopInfos error: " + e.getMessage());
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
        return rs > 0 && rs == infos.size();
    }

    @Override
    public boolean insertShopCatidAvgWeight(List<ShopInfoBean> infos) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DBHelper.getInstance().getConnection5();
            stmt = conn.createStatement();
            for (ShopInfoBean spIf : infos) {
                String insertSql = "insert into shop_catid_avg_weight(shop_id,catid,avg_weight,keyword,admin_id) "
                        + "select '" + spIf.getShopId()
                        + "' as shop_id,'' as catid,avg_weight,keyword, 0 as admin_id from shop_catid_avg_weight  "
                        + "where  catid = '" + spIf.getCategoryId() + "' and shop_id !='" + spIf.getShopId() + "'";
                stmt.addBatch(insertSql);
            }
            stmt.executeBatch();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("insertShopCatidAvgWeight error: " + e.getMessage());
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
        return false;
    }

    @Override
    public boolean deleteShopReadyGoods(String shopId, String pids) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String upSql = "update shop_goods_ready set p_d_flag = 2 where shop_id = ? and pid = ? ";
        int rs = 0;
        int count = 0;
        try {
            conn = DBHelper.getInstance().getConnection7();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(upSql);
            String[] pidList = pids.split(",");
            for (String pid : pidList) {
                if (pid == null || "".equals(pid)) {
                    continue;
                }
                count++;
                stmt.setString(1, shopId);
                stmt.setString(2, pid);
                stmt.addBatch();
            }
            rs = stmt.executeBatch().length;
            if (rs == count && rs > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",pids:" + pids + ",deleteShopReadyGoods error: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
        return rs == count && rs > 0;
    }

    @Override
    public boolean deleteShopOfferGoods(String shopId, String pids) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String upSql = "delete from shop_goods_offers where shop_id = ? and goods_pid = ? ";
        int rs = 0;
        int count = 0;
        try {
            conn = DBHelper.getInstance().getConnection6();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(upSql);
            String[] pidList = pids.split(",");
            for (String pid : pidList) {
                if (pid == null || "".equals(pid)) {
                    continue;
                }
                count++;
                stmt.setString(1, shopId);
                stmt.setString(2, pid);
                stmt.addBatch();
            }
            rs = stmt.executeBatch().length;
            if (rs == count && rs > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",pids:" + pids + ",deleteShopOfferGoods error: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
        return rs == count && rs > 0;
    }

    @Override
    public List<ShopGoodsEnInfo> queryDealGoodsWithInfoByShopId(String shopId) {
        List<ShopGoodsEnInfo> goodsList = new ArrayList<ShopGoodsEnInfo>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select a.*,b.local_path from (select shop_id,pid,custom_main_image,remotpath,enname,price,range_price,wprice,eninfo "
                + "from useful_data.shop_goods_ready where shop_id = ? and p_d_flag < 2) a,"
                + "(select goods_pid,loc_mainpath as local_path from shop_goods_offers where shop_id=? ) b where a.pid = b.goods_pid";
        try {
            conn = DBHelper.getInstance().getConnection6();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            stmt.setString(2, shopId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ShopGoodsEnInfo spGoods = new ShopGoodsEnInfo();
                spGoods.setShopId(rs.getString("shop_id"));
                spGoods.setPid(rs.getString("pid"));
                spGoods.setRemotePath(rs.getString("remotpath"));
                spGoods.setImgUrl(rs.getString("custom_main_image"));
                spGoods.setEnName(rs.getString("enname"));
                spGoods.setPrice(rs.getString("price"));
                spGoods.setRangePrice(rs.getString("range_price"));
                spGoods.setWprice(rs.getString("wprice"));
                spGoods.setEnInfo(rs.getString("eninfo"));
                spGoods.setLocalPath(rs.getString("local_path"));
                goodsList.add(spGoods);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryDealGoodsByShopId error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return goodsList;
    }

    @Override
    public boolean updateGoodsEninfo(List<ShopGoodsEnInfo> goodsList) {
        boolean is = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        String insertSql = "update shop_goods_ready set eninfo = ? where pid=? and shop_id = ?";
        try {
            conn = DBHelper.getInstance().getConnection7();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(insertSql);
            for (ShopGoodsInfo gd : goodsList) {
                stmt.setString(1, gd.getEnInfo());
                stmt.setString(2, gd.getPid());
                stmt.setString(3, gd.getShopId());
                stmt.addBatch();
            }
            int[] rs = stmt.executeBatch();
            if (rs.length == goodsList.size()) {
                conn.commit();
                is = true;
            } else {
                conn.rollback();
                is = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("updateGoodsEninfo error: " + e.getMessage());
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
        return is;
    }

    @Override
    public CustomGoodsPublish queryGoodsInfo(String shopId, String pid) {

        String sql = "select pid,enname,endetail,eninfo,feeprice,fprice,"
                + "keyword,custom_main_image as main_img,price,final_weight,wprice,entype,sku,"
                + "goodsstate,bm_flag,valid,isBenchmark,source_pro_flag,source_used_flag,"
                + "ocr_match_flag,priority_flag,img,range_price,revise_weight,localpath,remotpath,"
                + "is_sold_flag,is_add_car_flag,ali_pid,shop_id,valid,weight_flag from shop_goods_ready where shop_id=? and pid=?";
        Connection conn = DBHelper.getInstance().getConnection7();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        CustomGoodsPublish bean = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            stmt.setString(2, pid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                bean = new CustomGoodsPublish();
                bean.setValid(rs.getInt("valid"));
                bean.setEndetail(rs.getString("endetail"));
                bean.setEninfo(rs.getString("eninfo"));
                bean.setFeeprice(rs.getString("feeprice"));
                bean.setFprice(rs.getString("fprice"));
                bean.setKeyword(rs.getString("keyword"));
                bean.setLocalpath(rs.getString("localpath"));
                bean.setEnname(rs.getString("enname"));
                bean.setRangePrice(rs.getString("range_price"));
                bean.setReviseWeight(rs.getString("revise_weight"));
                bean.setFinalWeight(rs.getString("final_weight"));
                bean.setPid(rs.getString("pid"));
                bean.setPrice(rs.getString("price"));
                bean.setImg(rs.getString("img"));
                bean.setShowMainImage(rs.getString("main_img"));
                bean.setSku(rs.getString("sku"));
                bean.setEntype(rs.getString("entype"));
                bean.setRemotpath(rs.getString("remotpath"));

                bean.setUrl("https://detail.1688.com/offer/" + rs.getString("pid") + ".html");
                String wprice = rs.getString("wprice");
                if (wprice == null || "".equals(wprice)) {
                    bean.setWprice("");
                } else {
                    // bean.setWprice(wprice.substring(1, wprice.length() -
                    // 1).replace("$", "@"));
                    bean.setWprice(wprice.replace("[", "").replace("]", "").replace("$", "@"));
                }
                String aliPid = rs.getString("ali_pid");
                if (aliPid == null || "".equals(aliPid)) {
                    aliPid = "0";
                }
                bean.setAliGoodsPid(aliPid);
                bean.setAliGoodsUrl("https://www.aliexpress.com/item/aligoods/" + aliPid + ".html");

                bean.setBmFlag(rs.getInt("bm_flag"));
                bean.setSourceProFlag(rs.getInt("source_pro_flag"));
                bean.setSoldFlag(rs.getInt("is_sold_flag"));

                bean.setPriorityFlag(rs.getInt("priority_flag"));
                bean.setAddCarFlag(rs.getInt("is_add_car_flag"));
                bean.setSourceUsedFlag(rs.getInt("source_used_flag"));
                bean.setOcrMatchFlag(rs.getInt("ocr_match_flag"));
                bean.setShopId(rs.getString("shop_id"));
                bean.setWeightFlag(rs.getInt("weight_flag"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryGoodsInfo error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return bean;
    }

    @Override
    public boolean saveEditGoods(CustomGoodsPublish cgp, String shopId, int adminId) {

        Connection conn = DBHelper.getInstance().getConnection7();
        String upSql = "update shop_goods_ready set enname = ?,"
                + "img = ?,endetail = ?,eninfo=?,goodsstate=?,keyword=?,";
        if (!(cgp.getReviseWeight() == null || "".equals(cgp.getReviseWeight()) || "0".equals(cgp.getReviseWeight()))) {
            upSql += "feeprice=?,revise_weight=?,final_weight=?,";
        }
        if (cgp.getRangePrice() == null || "".equals(cgp.getRangePrice())) {
            upSql += "price=?,wprice=?,";
        } else {
            upSql += "range_price=?,sku=?,";
        }
        upSql += "admin_id=?,updatetime=sysdate(),bm_flag=1 where pid = ? and shop_id = ?";
        PreparedStatement stmt = null;
        int rs = 0;
        int i = 1;
        try {
            stmt = conn.prepareStatement(upSql);
            stmt.setString(i++, cgp.getEnname());
            stmt.setString(i++, cgp.getImg());
            stmt.setString(i++, cgp.getEndetail());
            stmt.setString(i++, cgp.getEninfo());
            stmt.setInt(i++, 5);
            stmt.setString(i++, cgp.getKeyword());
            if (!(cgp.getReviseWeight() == null || "".equals(cgp.getReviseWeight())
                    || "0".equals(cgp.getReviseWeight()))) {
                stmt.setString(i++, cgp.getFeeprice());
                stmt.setString(i++, cgp.getReviseWeight());
                stmt.setString(i++, cgp.getReviseWeight());
            }
            if (cgp.getRangePrice() == null || "".equals(cgp.getRangePrice())) {
                stmt.setString(i++, cgp.getPrice());
                stmt.setString(i++, cgp.getWprice());
            } else {
                stmt.setString(i++, cgp.getRangePrice());
                stmt.setString(i++, cgp.getSku());
            }
            stmt.setInt(i++, adminId);
            stmt.setString(i++, cgp.getPid());
            stmt.setString(i++, shopId);
            rs = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",pid:" + cgp.getPid() + ",saveEditGoods error :" + e.getMessage());
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
        return rs > 0;
    }

    @Override
    public List<CustomOnlineGoodsBean> queryReadyDealGoods(String shopId) {

        List<CustomOnlineGoodsBean> goodList = new ArrayList<CustomOnlineGoodsBean>();
        Connection conn28 = DBHelper.getInstance().getConnection7();
        String querySql = "select id,pid,custom_main_image,entype,img,eninfo,img_down_flag,localpath"
                + " from shop_goods_ready where shop_id = ? and p_d_flag < 2 and sync_flag = 0 and valid=1";
        PreparedStatement stmt = null;
        ResultSet rss = null;
        try {
            stmt = conn28.prepareStatement(querySql);
            stmt.setString(1, shopId);
            rss = stmt.executeQuery();
            while (rss.next()) {
                CustomOnlineGoodsBean good = new CustomOnlineGoodsBean();
                good.setId(rss.getInt("id"));
                good.setPid(rss.getString("pid"));
                good.setCustomMainImage(rss.getString("custom_main_image"));
                good.setEntype(rss.getString("entype"));
                good.setImg(rss.getString("img"));
                good.setEninfo(rss.getString("eninfo"));
                good.setImgDownFlag(rss.getInt("img_down_flag"));
                good.setLocalPath(rss.getString("localpath"));
                goodList.add(good);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",queryReadyDealGoods error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn28);
        }

        return goodList;
    }

    @Override
    public CustomOnlineGoodsBean queryGoodsByShopIdAndPid(String shopId, String pid) {
        Connection conn28 = DBHelper.getInstance().getConnection7();
        String querySql = "select * from shop_goods_ready where shop_id = ? and pid = ?";
        PreparedStatement stmt = null;
        ResultSet rss = null;
        CustomOnlineGoodsBean goods = new CustomOnlineGoodsBean();
        try {
            stmt = conn28.prepareStatement(querySql);
            stmt.setString(1, shopId);
            stmt.setString(2, pid);
            rss = stmt.executeQuery();
            if (rss.next()) {

                goods.setAliFreight(rss.getString("ali_freight"));
                goods.setAliImg(rss.getString("ali_img"));
                goods.setAliMorder(rss.getString("ali_morder"));
                goods.setAliName(rss.getString("ali_name"));
                goods.setAliPid(rss.getString("ali_pid"));
                goods.setAliPrice(rss.getString("ali_price"));
                goods.setAliSellunit(rss.getString("ali_sellunit"));
                goods.setAliSold(rss.getInt("ali_sold"));
                goods.setAliUnit(rss.getString("ali_unit"));
                goods.setAliWeight(rss.getString("ali_weight"));
                goods.setBmFlag(rss.getInt("bm_flag"));
                goods.setCatid(rss.getString("catid"));
                goods.setCatid1(rss.getString("catid1"));
                goods.setCatidb(rss.getString("catidb"));
                goods.setCatidParenta(rss.getString("catidparenta"));
                goods.setCatidParentb(rss.getString("catidparentb"));
                goods.setCatpath(rss.getString("catpath"));
                goods.setCreatetime(rss.getString("createtime"));
                goods.setCurTime(rss.getString("cur_time"));
                goods.setCustomMainImage(rss.getString("custom_main_image"));
                goods.setEndetail(rss.getString("endetail"));
                goods.setEninfo(rss.getString("eninfo"));
                goods.setEnname(rss.getString("enname"));
                goods.setEntype(rss.getString("entype"));
                goods.setFeeprice(rss.getString("feeprice"));
                goods.setFinalName(rss.getString("finalName"));
                goods.setFinalWeight(rss.getString("final_weight"));
                // goods.setFlag(rss.getInt("flag"));
                goods.setFprice(rss.getString("fprice"));
                goods.setFpriceStr(rss.getString("fprice_str"));
                goods.setGoodsState(rss.getInt("goodsstate"));
                goods.setId(rss.getInt("id"));
                goods.setImg(rss.getString("img"));
                goods.setImgCheck(rss.getInt("img_check"));
                goods.setInfoReviseFlag(rss.getInt("infoReviseFlag"));
                goods.setIsAddCarFlag(rss.getInt("is_add_car_flag"));
                goods.setIsBenchmark(rss.getInt("isBenchmark"));
                goods.setIsNewCloud(rss.getInt("isNewCloud"));
                goods.setIsShowDetImgFlag(rss.getInt("is_show_det_img_flag"));
                goods.setIsShowDetTableFlag(rss.getInt("is_show_det_table_flag"));
                goods.setIsSoldFlag(rss.getInt("is_sold_flag"));
                goods.setKeyword(rss.getString("keyword"));
                goods.setLocalPath(rss.getString("localpath"));
                goods.setMorder(rss.getInt("morder"));
                goods.setName(rss.getString("name"));
                goods.setOcrMatchFlag(rss.getInt("ocr_match_flag"));
                goods.setOriginalCatid(rss.getString("originalcatid"));
                goods.setOriginalCatpath(rss.getString("originalcatpath"));
                goods.setPid(rss.getString("pid"));
                goods.setPrice(rss.getDouble("price"));
                goods.setPriceReviseFlag(rss.getInt("priceReviseFlag"));
                goods.setPriorityFlag(rss.getInt("priority_flag"));
                goods.setPvids(rss.getString("pvids"));
                goods.setRangePrice(rss.getString("range_price"));
                goods.setRemotPath(rss.getString("remotpath"));
                goods.setReviseWeight(rss.getString("revise_weight"));
                goods.setSellUnit(rss.getString("sellunit"));
                goods.setShopId(rss.getString("shop_id"));
                goods.setSku(rss.getString("sku"));
                goods.setSold(rss.getInt("sold"));
                goods.setSourceProFlag(rss.getInt("source_pro_flag"));
                goods.setSourceUsedFlag(rss.getInt("source_used_flag"));
                goods.setValid(rss.getInt("valid"));
                goods.setWeight(rss.getString("weight"));
                goods.setWholesalePrice(rss.getString("wholesale_price"));
                goods.setWprice(rss.getString("wprice"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err
                    .println("shopId:" + shopId + ",pid:" + pid + ",queryGoodsByShopIdAndPid error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn28);
        }
        return goods;
    }

    @SuppressWarnings("resource")
    @Override
    public boolean syncSingleGoodsToOnline(CustomOnlineGoodsBean goods, String shopId) {

        int rs = 0;
        Connection connOnline = DBHelper.getInstance().getConnection2();
        Connection conn27 = DBHelper.getInstance().getConnection();
        Connection conn28 = DBHelper.getInstance().getConnection7();
        String querySql = "select count(1) from custom_benchmark_ready where pid = ?";
        String insertSql = "(ali_freight,ali_img,ali_morder,ali_name,ali_pid,"
                + "ali_price,ali_sellunit,ali_sold,ali_unit,ali_weight,bm_flag,catid,catid1,catidb,catidparenta,"
                + "catidparentb,catpath,custom_main_image,endetail,eninfo,"
                + "enname,entype,feeprice,finalName,final_weight,fprice,fprice_str,img,img_check,infoReviseFlag,"
                + "is_add_car_flag,isBenchmark,isNewCloud,is_show_det_img_flag,is_show_det_table_flag,"
                + "is_sold_flag,keyword,localpath,morder,name,"
                + "ocr_match_flag,originalcatid,originalcatpath,pid,price,"
                + "priceReviseFlag,priority_flag,pvids,range_price,remotpath,"
                + "revise_weight,sellunit,shop_id,sku,sold,"
                + "source_pro_flag,source_used_flag,valid,weight,wholesale_price,wprice) "
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String insertSql27 = "insert into custom_benchmark_ready(ali_freight,ali_img,ali_morder,ali_pid,ali_price,"
                + "ali_sellunit,ali_sold,ali_unit,ali_weight,bm_flag,"
                + "catid,catid1,catidb,catidparenta,catidparentb,catpath,custom_main_image,endetail,eninfo,enname,"
                + "entype,feeprice,final_weight,fprice,img,img_check,"
                + "is_add_car_flag,isBenchmark,isNewCloud,is_sold_flag,keyword,"
                + "localpath,morder,ocr_match_flag,originalcatid,originalcatpath,"
                + "pid,price,priority_flag,range_price,remotpath,revise_weight,shop_id,sku,sold,source_pro_flag,"
                + "source_used_flag,valid,weight,wprice) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String updateSql = "update shop_goods_ready set sync_flag =?,sync_remark=? where pid = ? and shop_id = ?";
        PreparedStatement stmtOnline = null;
        PreparedStatement stmt27 = null;
        PreparedStatement stmt28Up = null;
        PreparedStatement stmt28 = null;
        ResultSet rset = null;
        try {

            stmtOnline = connOnline.prepareStatement(querySql);
            stmtOnline.setString(1, goods.getPid());
            rset = stmtOnline.executeQuery();
            int count = 0;
            if (rset.next()) {
                count = rset.getInt(1);
            }
            // 线上存在直接更新同步
            if (count > 0) {
                stmt28Up = conn28.prepareStatement(updateSql);
                stmt28Up.setInt(1, 2);
                stmt28Up.setString(2, "当前 pid已经存在！");
                stmt28Up.setString(3, goods.getPid());
                stmt28Up.setString(4, shopId);
                stmt28Up.executeUpdate();
                System.err.println("当前 pid:" + goods.getPid() + " 已经存在！！");
                // LOG.error("当前 pid:" + goods.getPid() + " 已经存在！！");
                rs = 3;
            } else {
                // 同步Online
                connOnline.setAutoCommit(false);
                stmtOnline.clearParameters();
                stmtOnline = connOnline.prepareStatement("insert into custom_benchmark_ready" + insertSql);
                setSingleParam(stmtOnline, goods, true);
                count = stmtOnline.executeUpdate();
                if (count > 0) {

                    // 更新同步
                    conn28.setAutoCommit(false);
                    stmt28Up = conn28.prepareStatement(updateSql);
                    stmt28Up.setInt(1, 1);
                    stmt28Up.setString(2, "success");
                    stmt28Up.setString(3, goods.getPid());
                    stmt28Up.setString(4, shopId);
                    int upCount = stmt28Up.executeUpdate();
                    if (upCount > 0) {
                        // 同步Online成功
                        connOnline.commit();
                        rs++;
                        conn28.commit();
                        // 同步27
                        conn27.setAutoCommit(false);
                        count = 0;
                        stmt27 = conn27.prepareStatement(insertSql27);
                        setSingleParam(stmt27, goods, false);
                        count = stmt27.executeUpdate();
                        if (count > 0) {
                            // 同步27成功
                            conn27.commit();
                            rs++;
                            // 同步28

                            count = 0;
                            stmt28 = conn28.prepareStatement("insert into custom_benchmark_ready_newest" + insertSql);
                            setSingleParam(stmt28, goods, true);
                            count = stmt28.executeUpdate();
                            if (count > 0) {
                                // 同步28成功
                                conn28.commit();
                                rs++;
                            } else {
                                // 同步28失败
                                conn28.rollback();
                                System.err.println("publishGoods 28 failure");
                                LOG.error("publishGoods 28 failure");
                            }
                        } else {
                            // 同步27失败
                            conn27.rollback();
                            System.err.println("publishGoods 27 failure");
                            LOG.error("publishGoods 27 error");
                        }
                    } else {
                        connOnline.rollback();
                        conn28.rollback();
                        System.err.println("update sync_flag failure");
                        LOG.error("update sync_flag failure");
                    }
                } else {
                    // 同步Online失败
                    connOnline.rollback();
                    System.err.println("publishGoods Online failure");
                    LOG.error("publishGoods Online failure");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + goods.getPid() + " publishGoods error :" + e.getMessage());
            LOG.error("pid:" + goods.getPid() + " publishGoods error :" + e.getMessage());
        } finally {
            if (stmtOnline != null) {
                try {
                    stmtOnline.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt27 != null) {
                try {
                    stmt27.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt28 != null) {
                try {
                    stmt28.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(connOnline);
            DBHelper.getInstance().closeConnection(conn27);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return rs == 3;
    }

    private void setSingleParam(PreparedStatement stmt, CustomOnlineGoodsBean goods, boolean no27) {

        try {
            int i = 1;
            stmt.setString(i++, goods.getAliFreight() == null ? "" : goods.getAliFreight());
            stmt.setString(i++, goods.getAliImg() == null ? "" : goods.getAliImg());
            stmt.setString(i++, goods.getAliMorder() == null ? "0" : goods.getAliMorder());
            if (no27) {
                stmt.setString(i++, goods.getAliName() == null ? "" : goods.getAliName());
            }
            stmt.setString(i++, goods.getAliPid() == null ? "" : goods.getAliPid());
            stmt.setString(i++, goods.getAliPrice() == null ? "" : goods.getAliPrice());

            stmt.setString(i++, goods.getAliSellunit() == null ? "" : goods.getAliSellunit());
            stmt.setInt(i++, goods.getAliSold());
            stmt.setString(i++, goods.getAliUnit() == null ? "" : goods.getAliUnit());
            stmt.setString(i++, goods.getAliWeight() == null ? "" : goods.getAliWeight());
            stmt.setInt(i++, goods.getBmFlag());

            stmt.setString(i++, goods.getCatid() == null ? "" : goods.getCatid());
            stmt.setString(i++, goods.getCatid1() == null ? "" : goods.getCatid1());
            stmt.setString(i++, goods.getCatidb() == null ? "" : goods.getCatidb());
            stmt.setString(i++, goods.getCatidParenta() == null ? "" : goods.getCatidParenta());
            stmt.setString(i++, goods.getCatidParentb() == null ? "" : goods.getCatidParentb());

            stmt.setString(i++, goods.getCatpath() == null ? "" : goods.getCatpath());
            stmt.setString(i++, goods.getCustomMainImage() == null ? "" : goods.getCustomMainImage());
            stmt.setString(i++, goods.getEndetail() == null ? "" : goods.getEndetail());
            stmt.setString(i++, goods.getEninfo() == null ? "" : goods.getEninfo());

            stmt.setString(i++, goods.getEnname() == null ? "" : goods.getEnname());

            stmt.setString(i++, goods.getEntype() == null ? "" : goods.getEntype());
            stmt.setString(i++, goods.getFeeprice() == null ? "" : goods.getFeeprice());
            if (no27) {
                stmt.setString(i++, goods.getFinalName() == null ? "" : goods.getFinalName());
            }
            stmt.setString(i++, goods.getFinalWeight() == null ? "" : goods.getFinalWeight());
            stmt.setString(i++, goods.getFprice() == null ? "" : goods.getFprice());
            if (no27) {
                stmt.setString(i++, goods.getFpriceStr() == null ? "" : goods.getFpriceStr());
            }
            stmt.setString(i++, goods.getImg() == null ? "" : goods.getImg());
            stmt.setInt(i++, goods.getImgCheck());
            if (no27) {
                stmt.setInt(i++, goods.getInfoReviseFlag());
            }
            stmt.setInt(i++, goods.getIsAddCarFlag());
            stmt.setInt(i++, goods.getIsBenchmark());
            stmt.setInt(i++, goods.getIsNewCloud());
            if (no27) {
                stmt.setInt(i++, goods.getIsShowDetImgFlag());
                stmt.setInt(i++, goods.getIsShowDetTableFlag());
            }
            stmt.setInt(i++, goods.getIsSoldFlag());

            stmt.setString(i++, goods.getKeyword() == null ? "" : goods.getKeyword());
            stmt.setString(i++, goods.getLocalPath() == null ? "" : goods.getLocalPath());
            stmt.setInt(i++, goods.getMorder());
            if (no27) {
                stmt.setString(i++, goods.getName() == null ? "" : goods.getName());
            }

            stmt.setInt(i++, goods.getOcrMatchFlag());
            stmt.setString(i++, goods.getOriginalCatid() == null ? "" : goods.getOriginalCatid());

            stmt.setString(i++, goods.getOriginalCatpath() == null ? "" : goods.getOriginalCatpath());
            stmt.setString(i++, goods.getPid() == null ? "" : goods.getPid());
            stmt.setDouble(i++, goods.getPrice());
            if (no27) {
                stmt.setInt(i++, goods.getPriceReviseFlag());
            }
            stmt.setInt(i++, goods.getPriorityFlag());
            if (no27) {
                stmt.setString(i++, goods.getPvids() == null ? "" : goods.getPvids());
            }
            stmt.setString(i++, goods.getRangePrice() == null ? "" : goods.getRangePrice());
            stmt.setString(i++, goods.getRemotPath() == null ? "" : goods.getRemotPath());
            stmt.setString(i++, goods.getReviseWeight() == null ? "" : goods.getReviseWeight());
            if (no27) {
                stmt.setString(i++, goods.getSellUnit() == null ? "" : goods.getSellUnit());
            }
            stmt.setString(i++, goods.getShopId() == null ? "" : goods.getShopId());
            stmt.setString(i++, goods.getSku() == null ? "" : goods.getSku());
            stmt.setInt(i++, goods.getSold());
            stmt.setInt(i++, goods.getSourceProFlag());
            stmt.setInt(i++, goods.getSourceUsedFlag());
            stmt.setInt(i++, goods.getValid());
            stmt.setString(i++, goods.getWeight() == null ? "" : goods.getWeight());
            if (no27) {
                stmt.setString(i++, goods.getWholesalePrice() == null ? "" : goods.getWholesalePrice());
            }

            stmt.setString(i++, goods.getWprice() == null ? "" : goods.getWprice());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("setSingleParam error :" + e.getMessage());
            LOG.error("setSingleParam error :" + e.getMessage());
        }
    }

    @Override
    public boolean updateGoodsImg(CustomOnlineGoodsBean goods) {

        Connection conn28 = DBHelper.getInstance().getConnection7();
        String updateSql = "update shop_goods_ready set img_down_flag = ?,custom_main_image = ?,"
                + "entype = ?,img = ?,eninfo = ?,remotpath = ? where id = ? and pid = ?";
        PreparedStatement stmt = null;
        int rs = 0;
        try {
            stmt = conn28.prepareStatement(updateSql);
            stmt.setInt(1, 1);
            stmt.setString(2, goods.getCustomMainImage());
            stmt.setString(3, goods.getEntype());
            stmt.setString(4, goods.getImg());

            stmt.setString(5, goods.getEninfo());
            stmt.setString(6, goods.getRemotPath());
            stmt.setInt(7, goods.getId());
            stmt.setString(8, goods.getPid());

            rs = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(
                    "id:" + goods.getId() + ",pid:" + goods.getPid() + " updateGoodsImg error :" + e.getMessage());
            LOG.error("id:" + goods.getId() + ",pid:" + goods.getPid() + " updateGoodsImg error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn28);
        }

        return rs > 0;
    }

    @Override
    public boolean updateGoodsImgError(CustomOnlineGoodsBean goods) {

        Connection conn28 = DBHelper.getInstance().getConnection7();
        String updateSql = "update shop_goods_ready set img_down_flag = ? where id = ? and pid = ?";
        PreparedStatement stmt = null;
        int rs = 0;
        try {
            stmt = conn28.prepareStatement(updateSql);
            stmt.setInt(1, 2);
            stmt.setInt(2, goods.getId());
            stmt.setString(3, goods.getPid());

            rs = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(
                    "id:" + goods.getId() + ",pid:" + goods.getPid() + " updateGoodsImgError error :" + e.getMessage());
            LOG.error(
                    "id:" + goods.getId() + ",pid:" + goods.getPid() + " updateGoodsImgError error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn28);
        }

        return rs > 0;
    }

    @Override
    public Map<String, String> queryShopDealState(String shopId) {

        Connection conn28 = DBHelper.getInstance().getConnection5();
        String querySql = "select shop_id,shop_state,online_state from shop_clear_state where shop_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, String> result = new HashMap<String, String>();
        try {
            stmt = conn28.prepareStatement(querySql);
            stmt.setString(1, shopId);

            rs = stmt.executeQuery();
            if (rs.next()) {
                result.put("shopId", rs.getString("shop_id"));
                result.put("shopState", String.valueOf(rs.getInt("shop_state")));
                result.put("onlineState", String.valueOf(rs.getInt("online_state")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",queryShopDealState error :" + e.getMessage());
            LOG.error("shopId:" + shopId + ",queryShopDealState error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn28);
        }

        return result;
    }

    @Override
    public boolean updateShopState(String shopId, int state) {

        // state状态 0待处理 1正在发布 2发布成功3发布失败4待发布

        // shopState状态 0:代办；1：详情信息已下载；2：详情图片已下载；3：图片已上传美服；4：数据上线完成;
        // 5:数据上传中;6:数据上传失败;7:数据准备完成，待上线
        Connection conn28 = DBHelper.getInstance().getConnection5();
        String updateSql = "update shop_clear_state set online_state = " + state + " where shop_id = '" + shopId + "'";
        int shopState = 0;
        if (state == 1) {
            shopState = 5;
        } else if (state == 2) {
            shopState = 4;
        } else if (state == 3) {
            shopState = 6;
        } else if (state == 4) {
            shopState = 7;
        } else if (state == 0) {
            shopState = 2;
        }
        String updateShop = "update shop_url_bak set online_status = " + shopState + " where shop_id = '" + shopId
                + "'";
        Statement stmt = null;
        int rs = 0;
        try {
            stmt = conn28.createStatement();
            stmt.addBatch(updateSql);
            stmt.addBatch(updateShop);

            rs = stmt.executeBatch().length;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",updateShopState error :" + e.getMessage());
            LOG.error("shopId:" + shopId + ",updateShopState error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn28);
        }

        return rs == 2;
    }

    @Override
    public List<Category1688Bean> queryAll1688Category() {
        List<Category1688Bean> categoryList = new ArrayList<Category1688Bean>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select id,category_id,lv,name,childids,path,parent_id from 1688_cate";
        try {
            conn = DBHelper.getInstance().getConnection8();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Category1688Bean category = new Category1688Bean();
                category.setId(rs.getInt("id"));
                category.setCategoryId(rs.getString("category_id"));
                category.setLv(rs.getInt("lv"));
                category.setCategoryName(rs.getString("name"));
                category.setChildIds(rs.getString("childids"));
                category.setPath(rs.getString("path"));
                category.setParentId(rs.getString("parent_id"));
                categoryList.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryAll1688Category error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return categoryList;
    }

    @Override
    public String queryGoodsLocalPath(String shopId, String goodsPid) {
        String localPath = "";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select loc_mainpath from shop_goods_offers where shop_id = ? and goods_pid = ?";
        try {
            conn = DBHelper.getInstance().getConnection6();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            stmt.setString(2, goodsPid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                localPath = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryGoodsLocalPath error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        if (localPath == null) {
            localPath = "";
        }
        return localPath;
    }

    @Override
    public Map<String, Integer> queryDealState(String shopId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select shop_state,online_state from shop_clear_state where shop_id = ?";
        Map<String, Integer> rsMap = new HashMap<String, Integer>();
        try {
            conn = DBHelper.getInstance().getConnection5();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                rsMap.put("shop_state", rs.getInt("shop_state"));
                rsMap.put("online_state", rs.getInt("online_state"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryDealState error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return rsMap;
    }

    @Override
    public Map<String, Integer> queryShopGoodsSync(String shopId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select (select count(1)  from shop_goods_ready where shop_id=?) as total_num,"
                + "(select count(1)  from shop_goods_ready where shop_id=? and sync_flag = 1) as sync_num from dual";
        Map<String, Integer> rsMap = new HashMap<String, Integer>();
        try {
            conn = DBHelper.getInstance().getConnection7();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            stmt.setString(2, shopId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                rsMap.put("total_num", rs.getInt("total_num"));
                rsMap.put("sync_num", rs.getInt("sync_num"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryShopGoodsSync error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return rsMap;
    }

    @Override
    public List<ShopCatidWeight> queryShopCatidWeightListByShopId(String shopId) {
        List<ShopCatidWeight> list = new ArrayList<ShopCatidWeight>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select id,shop_id,catid,avg_weight,keyword from shop_catid_avg_weight where shop_id = ? ";
        try {
            conn = DBHelper.getInstance().getConnection5();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);

            rs = stmt.executeQuery();
            while (rs.next()) {
                ShopCatidWeight ctwt = new ShopCatidWeight();
                ctwt.setId(rs.getInt("id"));
                ctwt.setAvgWeight(rs.getDouble("avg_weight"));
                ctwt.setCatid(rs.getString("catid"));
                ctwt.setKeyword(rs.getString("keyword"));
                ctwt.setShopId(shopId);
                list.add(ctwt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryShopCatidWeightListByShopId error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public boolean batchInsertCatidWeight(List<ShopCatidWeight> list) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rs = 0;
        String sql = "insert into shop_catid_avg_weight(shop_id,catid,avg_weight,keyword,admin_id) values(?,?,?,?,?) ";
        try {
            conn = DBHelper.getInstance().getConnection6();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            for (ShopCatidWeight ctwt : list) {
                stmt.setString(1, ctwt.getShopId());
                stmt.setString(2, ctwt.getCatid());
                stmt.setDouble(3, ctwt.getAvgWeight());
                stmt.setString(4, ctwt.getKeyword());
                stmt.setInt(5, ctwt.getAdminId());
                stmt.addBatch();
            }
            rs = stmt.executeBatch().length;
            if (rs == list.size()) {
                conn.commit();
            } else {
                conn.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("batchInsertCatidWeight error: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
        return rs > 0 && rs == list.size();
    }

    @Override
    public boolean batchUpdateCatidWeight(List<ShopCatidWeight> list) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rs = 0;
        String sql = "update shop_catid_avg_weight set avg_weight = ?,keyword = ?,admin_id = ? where id = ?";
        try {
            conn = DBHelper.getInstance().getConnection6();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            for (ShopCatidWeight ctwt : list) {
                stmt.setDouble(1, ctwt.getAvgWeight());
                stmt.setString(2, ctwt.getKeyword());
                stmt.setInt(3, ctwt.getAdminId());
                stmt.setInt(4, ctwt.getId());
                stmt.addBatch();
            }
            rs = stmt.executeBatch().length;
            if (rs == list.size()) {
                conn.commit();
            } else {
                conn.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("batchInsertCatidWeight error: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
        return rs > 0 && rs == list.size();
    }

    @Override
    public float calculateAvgWeightByCatid(String shopId, String catid) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        float weight = 0;

        String sql = "select AVG(a.avg_weight) from (select shop_id,catid,avg_weight from shop_catid_avg_weight "
                + "where catid =  '" + catid + "' and shop_id != '" + shopId + "' "
                + "union all select shop_id,category_id,jd_result from shop_categroy_data where id > 100583 "
                + "and  category_id =  '" + catid + "' and shop_id != '" + shopId + "' ) a";
        try {
            conn = DBHelper.getInstance().getConnection5();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                weight = rs.getFloat(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("calculateAvgWeightByCatid error: " + e.getMessage());
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
        return weight;
    }

    @Override
    public int batchUpdateCatidPath(String shopId) {
        Connection remoteConn = null;
        Statement stmt = null;
        int rs = 0;

        String sql = "update custom_benchmark_ready a,1688_category b set  a.path_catid = b.path  "
                + "where a.catid1 = b.category_id and shop_id = '" + shopId + "' and ifnull(a.path_catid,'') = '' ";
        try {
            remoteConn = DBHelper.getInstance().getConnection2();
            stmt = remoteConn.createStatement();
            rs = stmt.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("batchUpdateCatidPath error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(remoteConn);
        }
        return rs;
    }

    @Override
    public List<String> queryErrorClearShopList() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> shopIds = new ArrayList<String>();
        String sql = "select shop_id from shop_clear_state  where shop_state =1";
        try {
            conn = DBHelper.getInstance().getConnection5();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                shopIds.add(rs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryErrorClearShopList error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return shopIds;
    }

    @Override
    public List<GoodsOfferBean> queryOriginalGoodsInfo(String shopId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<GoodsOfferBean> goodsInfos = new ArrayList<GoodsOfferBean>();
        String sql = "select catid,goods_pid,goods_name,pic,price,shop_id,weight,weight_flag,"
                + "weight_deal,set_weight,detail from shop_goods_offers  where shop_id = ? ";
        try {
            conn = DBHelper.getInstance().getConnection6();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                GoodsOfferBean gd = new GoodsOfferBean();
                gd.setCatid(rs.getString("catid"));
                gd.setGoodsName(rs.getString("goods_name"));
                gd.setImgUrl(rs.getString("pic"));
                gd.setPid(rs.getString("goods_pid"));
                gd.setPriceStr(rs.getString("price"));
                gd.setShopId(rs.getString("shop_id"));
                gd.setWeightStr(rs.getString("weight"));
                gd.setWeightFlag(rs.getInt("weight_flag"));
                gd.setWeightDeal(rs.getInt("weight_deal"));
                gd.setSetWeight(rs.getDouble("set_weight"));
                gd.setDetail(rs.getString("detail"));
                goodsInfos.add(gd);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryOriginalGoodsInfo error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return goodsInfos;
    }

    @Override
    public boolean batchUpdateErrorWeightGoods(List<GoodsOfferBean> goodsErrInfos) {
        Connection remoteConn = null;
        PreparedStatement stmt = null;
        int rs = 0;

        String sql = "update shop_goods_offers set set_weight = ?,weight_flag = ?,weight_deal = ? "
                + "where goods_pid = ? and shop_id = ?";
        try {
            remoteConn = DBHelper.getInstance().getConnection6();
            remoteConn.setAutoCommit(false);
            stmt = remoteConn.prepareStatement(sql);
            for (GoodsOfferBean gdOf : goodsErrInfos) {
                stmt.setDouble(1, gdOf.getSetWeight());
                stmt.setInt(2, gdOf.getWeightFlag());
                stmt.setInt(3, gdOf.getWeightDeal());
                stmt.setString(4, gdOf.getPid());
                stmt.setString(5, gdOf.getShopId());
                stmt.addBatch();
            }
            rs = stmt.executeBatch().length;
            if (rs > 0 && rs == goodsErrInfos.size()) {
                remoteConn.commit();
            } else {
                remoteConn.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("batchUpdateCatidPath error: " + e.getMessage());
            try {
                remoteConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(remoteConn);
        }
        return rs > 0 && rs == goodsErrInfos.size();
    }

    @Override
    public boolean deleteOriginalGoodsOffer(String shopId, String pids) {
        Connection remoteConn = null;
        PreparedStatement stmt = null;
        int rs = 0;

        String[] pidLs = pids.split(",");
        String sql = "delete from  shop_goods_offers where goods_pid = ? and shop_id = ?";
        try {
            remoteConn = DBHelper.getInstance().getConnection6();
            remoteConn.setAutoCommit(false);
            stmt = remoteConn.prepareStatement(sql);
            for (String pid : pidLs) {
                stmt.setString(1, pid);
                stmt.setString(2, shopId);
                stmt.addBatch();
            }
            rs = stmt.executeBatch().length;
            if (rs > 0 && rs == pidLs.length) {
                remoteConn.commit();
            } else {
                remoteConn.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("batchUpdateCatidPath error: " + e.getMessage());
            try {
                remoteConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(remoteConn);
        }
        return rs > 0 && rs == pidLs.length;
    }

    @Override
    public boolean insertShopGoodsDeleteImgs(List<ShopGoodsInfo> shopGoodsInfos, int adminId) {
        Connection localConn = null;
        PreparedStatement stmt = null;
        int rs = 0;
        String sql = "insert into shop_goods_img_delete(pid,shop_id,local_path,remote_path,img,admin_id) values(?,?,?,?,?,?)";
        try {
            localConn = DBHelper.getInstance().getConnection5();
            localConn.setAutoCommit(false);
            stmt = localConn.prepareStatement(sql);
            for (ShopGoodsInfo gd : shopGoodsInfos) {
                stmt.setString(1, gd.getPid());
                stmt.setString(2, gd.getShopId());
                stmt.setString(3, gd.getLocalPath());
                stmt.setString(4, gd.getRemotePath());
                stmt.setString(5, gd.getImgUrl());
                stmt.setInt(6, adminId);
                stmt.addBatch();
            }
            rs = stmt.executeBatch().length;
            if (rs > 0 && rs == shopGoodsInfos.size()) {
                localConn.commit();
            } else {
                localConn.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("insertShopGoodsDeleteImgs error: " + e.getMessage());
            try {
                localConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(localConn);
        }
        return rs > 0 && rs == shopGoodsInfos.size();
    }

    @Override
    public boolean updateWeightFlag(String shopId) {
        Connection localConn = null;
        PreparedStatement stmt = null;
        int rs = 0;
        String sql = "update shop_goods_offers set weight_flag = 0,weight_deal = 0 where shop_id = ?";
        try {
            localConn = DBHelper.getInstance().getConnection6();
            stmt = localConn.prepareStatement(sql);
            stmt.setString(1, shopId);
            rs = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("updateWeightFlag error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(localConn);
        }
        return rs > 0;
    }

    @Override
    public List<CustomGoodsPublish> queryOnlineGoodsByShopId(String shopId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CustomGoodsPublish> goodsInfos = new ArrayList<CustomGoodsPublish>();
        String sql = "select pid,valid,is_edited,goodsstate from custom_benchmark_ready where shop_id= ? ";
        try {
            conn = DBHelper.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                CustomGoodsPublish gd = new CustomGoodsPublish();
                gd.setPid(rs.getString("pid"));
                gd.setValid(rs.getInt("valid"));
                gd.setIsEdited(rs.getInt("is_edited"));
                gd.setGoodsState(rs.getInt("goodsstate"));
                goodsInfos.add(gd);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryOnlineGoodsByShopId error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return goodsInfos;
    }

    @Override
    public boolean saveReadyDeleteShop(String shopId, int type, String remark) {
        Connection localConn = null;
        PreparedStatement stmt = null;
        int rs = 0;
        String sql = "update shop_url_bak set error_flag = ?,error_remark = ? where shop_id = ?";
        try {
            localConn = DBHelper.getInstance().getConnection5();
            stmt = localConn.prepareStatement(sql);
            stmt.setInt(1, type);
            stmt.setString(2, remark);
            stmt.setString(3, shopId);
            rs = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("saveReadyDeleteShop error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(localConn);
        }
        return rs > 0;
    }

    @Override
    public boolean deleteCatidGoods(String shopId, String catids) {
        Connection conn28 = DBHelper.getInstance().getConnection5();
        Connection offerConn = DBHelper.getInstance().getConnection6();
        Connection readyConn = DBHelper.getInstance().getConnection7();
        PreparedStatement stmt28 = null;
        PreparedStatement offerStmt = null;
        PreparedStatement readyStmt = null;
        int rs = 0;
        String shopCategroySql = "delete from shop_categroy_data where shop_id = ? and category_id = ?";
        String offerSql = "delete from shop_goods_offers where shop_id = ? and catid = ?";
        String readySql = "delete from shop_goods_ready where shop_id = ? and catid1 = ?";
        String[] catidList = catids.split(",");

        try {
            conn28.setAutoCommit(false);
            offerConn.setAutoCommit(false);
            stmt28 = conn28.prepareStatement(shopCategroySql);
            offerStmt = offerConn.prepareStatement(offerSql);
            readyStmt = readyConn.prepareStatement(readySql);
            for (String catid : catidList) {
                if (StringUtils.isNotBlank(catid)) {
                    stmt28.setString(1, shopId);
                    stmt28.setString(2, catid);
                    stmt28.addBatch();
                    offerStmt.setString(1, shopId);
                    offerStmt.setString(2, catid);
                    offerStmt.addBatch();
                    readyStmt.setString(1, shopId);
                    readyStmt.setString(2, catid);
                    readyStmt.addBatch();
                }
            }

            rs = stmt28.executeBatch().length;
            if (rs > 0) {
                rs = 0;
                rs = offerStmt.executeBatch().length;
                if (rs > 0) {
                    conn28.commit();
                    offerConn.commit();
                    readyStmt.executeBatch();
                } else {
                    conn28.rollback();
                    offerConn.rollback();
                }
            } else {
                conn28.rollback();
            }
        } catch (Exception e) {
            try {
                conn28.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                offerConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",deleteCatidGoods error: " + e.getMessage());
        } finally {
            if (stmt28 != null) {
                try {
                    stmt28.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (offerStmt != null) {
                try {
                    offerStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (readyStmt != null) {
                try {
                    readyStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closeConnection(offerConn);
            DBHelper.getInstance().closeConnection(readyConn);
        }
        return rs > 0;
    }

    @Override
    public boolean changShopToManually(String shopId) {
        Connection conn28 = DBHelper.getInstance().getConnection5();
        Connection conn31 = DBHelper.getInstance().getConnection6();
        Statement stmt28 = null;
        Statement stmt31 = null;
        int rs = 0;
        int count31 = 0;
        String shopClearSql = "update shop_clear_state set shop_state = 0 where shop_id = '" + shopId + "'";
        String catidSql = "delete from shop_catid_avg_weight where shop_id = '" + shopId + "'";
        String upSql = "update shop_url_bak set is_auto =0,online_status=0 where shop_id = '" + shopId + "'";

        String offersSql = "delete from shop_goods_offers where shop_id = '" + shopId + "'";
        String readySql = "delete from useful_data.shop_goods_ready where shop_id = '" + shopId + "'";


        try {
            conn28.setAutoCommit(false);
            stmt28 = conn28.createStatement();
            stmt31 = conn31.createStatement();

            stmt28.addBatch(shopClearSql);
            stmt28.addBatch(catidSql);
            stmt28.addBatch(upSql);

            stmt31.addBatch(offersSql);
            stmt31.addBatch(readySql);


            rs = stmt28.executeBatch().length;
            if (rs > 0) {
                conn28.commit();
                count31 = stmt31.executeBatch().length;
                if (count31 == 0) {
                    stmt31.executeBatch();
                }
            } else {
                conn28.rollback();
            }
        } catch (Exception e) {
            try {
                conn28.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",changShopToManually error: " + e.getMessage());
        } finally {
            if (stmt28 != null) {
                try {
                    stmt28.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt31 != null) {
                try {
                    stmt31.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return rs > 0;
    }

    @Override
    public boolean checkIsBlackShopByShopId(String shopId) {
        Connection conn = DBHelper.getInstance().getConnection5();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select count(0) from supplier_scoring  where shop_id = ? and level ='黑名单' ";
        int count = 0;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",checkIsBlackShopByShopId error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(conn);
        }
        return count > 0;
    }

    @Override
    public boolean setShopType(String shopId, int type) {
        Connection localConn = null;
        PreparedStatement stmt = null;
        int rs = 0;
        String sql = "update shop_url_bak set shop_type = ? where shop_id = ?";
        try {
            localConn = DBHelper.getInstance().getConnection5();
            stmt = localConn.prepareStatement(sql);
            stmt.setInt(1, type);
            stmt.setString(2, shopId);
            rs = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",type" + type + ",setShopType error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(localConn);
        }
        return rs > 0;
    }

    @Override
    public boolean setAuthorizedFlag(String shopId, Integer authorizedFlag) {

        Connection localConn = null;
        PreparedStatement stmt = null;
        int rs = 0;
        String sql = "update shop_url_bak set authorized_flag = ? where shop_id = ?";
        try {
            localConn = DBHelper.getInstance().getConnection5();
            stmt = localConn.prepareStatement(sql);
            stmt.setInt(1, authorizedFlag);
            stmt.setString(2, shopId);
            rs = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",setAuthorizedFlag error: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(localConn);
        }
        return rs > 0;
    }

    @Override
    public List<NeedOffShelfBean> queryNeedOffShelfByParam(NeedOffShelfBean offShelf) {
        List<NeedOffShelfBean> list = new ArrayList<NeedOffShelfBean>();
        //,cbr.is_edited
        String sql = "select ns.id,ns.pid,ns.operateCount,ns.update_flag,ns.update_time," +
                " concat(cbr.remotpath,cbr.custom_main_image) as img_url,cbr.unsellableReason as reason," +
                "cbr.catid1,ct18.name as catid_name,cbr.valid as isOffShelf,cbr.is_edited  " +
                " from needoffshelf ns,cross_border.custom_benchmark_ready_newest cbr " +
                " left join 1688_category ct18 on cbr.catid1 = ct18.category_id" +
                " where 1=1 and ns.pid = cbr.pid ";

        if(StringUtils.isNotBlank(offShelf.getPid())){
            sql += " and ns.pid = ?";
        }
        if(offShelf.getIsOffShelf() > -1){
            sql += " and cbr.valid = ?";
        }
        if(offShelf.getUpdateFlag() > -1){
            sql += " and ns.update_flag = ?";
        }
        if(offShelf.getReason() > 0){
            sql += " and cbr.unsellableReason = ?";
        }
        if(StringUtils.isNotBlank(offShelf.getBeginTime())){
            sql += " and date(ns.update_time) >= ?";
        }
        if(StringUtils.isNotBlank(offShelf.getEndTime())){
            sql += " and date(ns.update_time) <= ?";
        }
        if(StringUtils.isNotBlank(offShelf.getCatid())){
            sql += " and cbr.catid1 = ?";
        }
        if(offShelf.getNeverOffFlag() > 0){
            sql += " and cbr.is_edited = ?";
        }
        sql += " order by ns.update_time desc ";
        if(offShelf.getLimitNum() > 0){
            sql += " limit ?,?";
        }
        Connection conn28 = DBHelper.getInstance().getConnection5();
        PreparedStatement stmt28 = null;
        ResultSet rs28 = null;
        try {
            stmt28 = conn28.prepareStatement(sql);
            int count = 1;
            if(StringUtils.isNotBlank(offShelf.getPid())){
                stmt28.setString(count++,offShelf.getPid());
            }
            if(offShelf.getIsOffShelf() > -1){
                stmt28.setInt(count++,offShelf.getIsOffShelf());
            }
            if(offShelf.getUpdateFlag() > -1){
                stmt28.setInt(count++,offShelf.getUpdateFlag());
            }
            if(offShelf.getReason() > 0){
                stmt28.setInt(count++,offShelf.getReason());
            }
            if(StringUtils.isNotBlank(offShelf.getBeginTime())){
                stmt28.setString(count++,offShelf.getBeginTime());
            }
            if(StringUtils.isNotBlank(offShelf.getEndTime())){
                stmt28.setString(count++,offShelf.getEndTime());
            }
            if(StringUtils.isNotBlank(offShelf.getCatid())){
                stmt28.setString(count++,offShelf.getCatid());
            }
            if(offShelf.getNeverOffFlag() > 0){
                stmt28.setString(count++,"3");
            }
            if(offShelf.getLimitNum() > 0){
                stmt28.setInt(count++,offShelf.getStartNum());
                stmt28.setInt(count++,offShelf.getLimitNum());
            }
            rs28 = stmt28.executeQuery();
            while (rs28.next()) {
                NeedOffShelfBean offShelfBean = new NeedOffShelfBean();
                offShelfBean.setId(rs28.getInt("id"));
                offShelfBean.setIsOffShelf(rs28.getInt("isOffShelf"));
                offShelfBean.setPid(rs28.getString("pid"));
                offShelfBean.setReason(rs28.getInt("reason"));
                offShelfBean.setUpdateFlag(rs28.getInt("update_flag"));
                offShelfBean.setUpdateTime(rs28.getString("update_time"));
                offShelfBean.setImgUrl(rs28.getString("img_url") == null ? "" : rs28.getString("img_url"));
                offShelfBean.setCatid(rs28.getString("catid1"));
                offShelfBean.setCatidName(rs28.getString("catid_name"));
                if("3".equals(rs28.getString("is_edited"))){
                    offShelfBean.setNeverOffFlag(1);
                }
                list.add(offShelfBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeResultSet(rs28);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return list;
    }

    @Override
    public int queryNeedOffShelfByParamCount(NeedOffShelfBean offShelf) {

        String sql = "select count(0) from needoffshelf ns,cross_border.custom_benchmark_ready_newest cbr " +
                " where 1=1 and ns.pid = cbr.pid ";

        if(StringUtils.isNotBlank(offShelf.getPid())){
            sql += " and ns.pid = ?";
        }
        if(offShelf.getIsOffShelf() > -1){
            sql += " and cbr.valid = ?";
        }
        if(offShelf.getUpdateFlag() > -1){
            sql += " and ns.update_flag = ?";
        }
        if(offShelf.getReason() > 0){
            sql += " and cbr.unsellableReason = ?";
        }
        if(StringUtils.isNotBlank(offShelf.getBeginTime())){
            sql += " and date(ns.update_time) >= ?";
        }
        if(StringUtils.isNotBlank(offShelf.getEndTime())){
            sql += " and date(ns.update_time) <= ?";
        }
        if(StringUtils.isNotBlank(offShelf.getCatid())){
            sql += " and cbr.catid1 = ?";
        }
        if(offShelf.getNeverOffFlag() > 0){
            sql += " and cbr.is_edited = ?";
        }
        Connection conn28 = DBHelper.getInstance().getConnection5();
        PreparedStatement stmt28 = null;
        ResultSet rs28 = null;
        int totalCount = 0;
        try {
            stmt28 = conn28.prepareStatement(sql);
            int count = 1;
            if(StringUtils.isNotBlank(offShelf.getPid())){
                stmt28.setString(count++,offShelf.getPid());
            }
            if(offShelf.getIsOffShelf() > -1){
                stmt28.setInt(count++,offShelf.getIsOffShelf());
            }
            if(offShelf.getUpdateFlag() > -1){
                stmt28.setInt(count++,offShelf.getUpdateFlag());
            }
            if(offShelf.getReason() > 0){
                stmt28.setInt(count++,offShelf.getReason());
            }
            if(StringUtils.isNotBlank(offShelf.getBeginTime())){
                stmt28.setString(count++,offShelf.getBeginTime());
            }
            if(StringUtils.isNotBlank(offShelf.getEndTime())){
                stmt28.setString(count++,offShelf.getEndTime());
            }
            if(StringUtils.isNotBlank(offShelf.getCatid())){
                stmt28.setString(count++,offShelf.getCatid());
            }
            if(offShelf.getNeverOffFlag() > 0){
                stmt28.setString(count++,"3");
            }
            rs28 = stmt28.executeQuery();
            if (rs28.next()) {
                totalCount = rs28.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeResultSet(rs28);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return totalCount;
    }

    @Override
    public Map<String, Integer> queryCompetitiveFlag(List<String> pids) {
        Map<String, Integer> rsMap = new HashMap<>(pids.size());
        Connection conn27 = DBHelper.getInstance().getConnection();
        StringBuffer querySql = new StringBuffer();
        String beginSql = "select pid,chineseflag from goods_list_search where chineseflag = 15 and pid in(";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn27.createStatement();
            for (String pid : pids) {
                querySql.append(",'" + pid + "'");
            }
            if(querySql.toString().length() > 5){
                rs = stmt.executeQuery(beginSql + querySql.toString().substring(1) + ")");
                while (rs.next()) {
                    rsMap.put(rs.getString("pid"), rs.getInt("chineseflag"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryCompetitiveFlag error :" + e.getMessage());
            LOG.error("queryCompetitiveFlag error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeStatement(stmt);
            DBHelper.getInstance().closeConnection(conn27);
        }
        return rsMap;
    }

}
