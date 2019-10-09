package com.cbt.warehouse.dao;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.GroupBuyGoodsBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.ShopGoodsInfo;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.BigDecimalUtil;
import com.cbt.warehouse.pojo.GroupBuyManageBean;
import com.cbt.warehouse.pojo.UserCouponBean;
import com.google.common.collect.Lists;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupBuyDaoImpl implements GroupBuyDao {


    @Override
    public List<GroupBuyManageBean> getGroupBuyInfos(int id, String beginTime, String endTime,int type, int startNum, int offset) {
        List<GroupBuyManageBean> list = new ArrayList<GroupBuyManageBean>();

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select c.*,d.enname,d.custom_main_image,d.remotpath,d.shop_id from " +
                " (select a.id,a.final_price_need_num,a.final_price,a.init_virtual_num,a.effective_day," +
                " a.active_begin_time,a.active_end_time,a.active_desc,a.is_on,a.admin_id,a.create_time,a.end_flag,a.type,b.pid  " +
                " from group_buy_manage a,group_buy_goods b where a.id = b.gb_id and b.is_main = 1) c " +
                " left join custom_benchmark_ready d on c.pid = d.pid where 1=1 ";
        if (StringUtils.isNotBlank(beginTime)) {
            sql += " and c.active_begin_time >= '" + beginTime + "'";
        }
        if (StringUtils.isNotBlank(endTime)) {
            sql += " and c.active_end_time <= '" + endTime + "'";
        }
        if (id > 0) {
            sql += " and c.id = " + id;
        }
        if(type > -1){
            sql += " and c.type = " + type;
        }
        sql += " order by c.create_time desc,c.active_begin_time desc ";
        if (offset > 0) {
            sql += " limit " + startNum + ", " + offset + "";
        }
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                GroupBuyManageBean info = new GroupBuyManageBean();
                info.setActiveBeginTime(rs.getString("active_begin_time"));
                info.setActiveDesc(rs.getString("active_desc"));
                info.setActiveEndTime(rs.getString("active_end_time"));
                info.setAdminId(rs.getInt("admin_id"));
                info.setEffectiveDay(rs.getInt("effective_day"));
                info.setFinalPrice(rs.getDouble("final_price"));
                info.setFinalPriceNeedNum(rs.getInt("final_price_need_num"));
                info.setId(rs.getInt("id"));
                info.setInitVirtualNum(rs.getInt("init_virtual_num"));
                info.setIsOn(rs.getInt("is_on"));
                info.setPid(rs.getString("pid"));
                info.setShopId(rs.getString("shop_id"));
                info.setImgUrl(rs.getString("remotpath") + rs.getString("custom_main_image"));
                info.setGoodsName(rs.getString("enname"));
                info.setCreateTime(rs.getString("create_time"));
                info.setType(rs.getInt("type"));
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
    public GroupBuyManageBean queryInfoById(int id) {

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select a.id,b.pid,a.final_price_need_num,a.final_price,a.init_virtual_num,a.effective_day,"
                + "a.active_begin_time,a.active_end_time,a.active_desc,a.is_on,a.admin_id,a.create_time,a.type," +
                "c.enname,c.custom_main_image,c.remotpath,c.shop_id from group_buy_manage a,group_buy_goods b " +
                " left join custom_benchmark_ready c on b.pid = c.pid "
                + " where 1=1 and a.id = b.gb_id and b.is_main =1 and a.id =" + id;

        GroupBuyManageBean info = new GroupBuyManageBean();
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {

                info.setActiveBeginTime(rs.getString("active_begin_time"));
                info.setActiveDesc(rs.getString("active_desc"));
                info.setActiveEndTime(rs.getString("active_end_time"));
                info.setAdminId(rs.getInt("admin_id"));
                info.setEffectiveDay(rs.getInt("effective_day"));
                info.setFinalPrice(rs.getDouble("final_price"));
                info.setFinalPriceNeedNum(rs.getInt("final_price_need_num"));
                info.setId(rs.getInt("id"));
                info.setInitVirtualNum(rs.getInt("init_virtual_num"));
                info.setIsOn(rs.getInt("is_on"));
                info.setPid(rs.getString("pid"));
                info.setShopId(rs.getString("shop_id"));
                info.setImgUrl(rs.getString("remotpath") + rs.getString("custom_main_image"));
                info.setGoodsName(rs.getString("enname"));
                info.setCreateTime(rs.getString("create_time"));
                info.setType(rs.getInt("type"));
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
        return info;
    }

    @Override
    public int getGroupBuyInfosCount(int id, String beginTime, String endTime,int type) {

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select count(0) from group_buy_manage where 1=1 ";
        if (StringUtils.isNotBlank(beginTime)) {
            sql += " and active_begin_time >= '" + beginTime + "'";
        }
        if (StringUtils.isNotBlank(endTime)) {
            sql += " and active_begin_time <= '" + endTime + "'";
        }
        if (id > 0) {
            sql += " and id = " + id;
        }
        if(type > -1){
            sql += " and type = " + type;
        }
        int count = 0;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
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
        return count;
    }

    @Override
    public int insertGroupBuyInfos(GroupBuyManageBean gbmInfo) {
        throw new RuntimeException("already cancel method called");
//        Connection remoteConn = DBHelper.getInstance().getConnection2();
//        PreparedStatement remoteStmt = null;
//        String sqlManage = "insert into group_buy_manage(final_price_need_num,final_price,init_virtual_num,effective_day,"
//                + "active_begin_time,active_end_time,active_desc,is_on,admin_id,type)"
//                + " values(?,?,?,?,?,?,?,?,?,?)";
//        ResultSet rsSet = null;
//        int mainId = 0;
//        try {
//            int remoteCount = 1;
//            remoteStmt = remoteConn.prepareStatement(sqlManage, Statement.RETURN_GENERATED_KEYS);
//            remoteStmt.setInt(remoteCount++, gbmInfo.getFinalPriceNeedNum());
//            remoteStmt.setDouble(remoteCount++, gbmInfo.getFinalPrice());
//            remoteStmt.setInt(remoteCount++, gbmInfo.getInitVirtualNum());
//            remoteStmt.setInt(remoteCount++, gbmInfo.getEffectiveDay());
//            remoteStmt.setString(remoteCount++, gbmInfo.getActiveBeginTime());
//            remoteStmt.setString(remoteCount++, gbmInfo.getActiveEndTime());
//            remoteStmt.setString(remoteCount++, gbmInfo.getActiveDesc());
//            remoteStmt.setInt(remoteCount++, gbmInfo.getIsOn());
//            remoteStmt.setInt(remoteCount++, gbmInfo.getAdminId());
//            remoteStmt.setInt(remoteCount++, gbmInfo.getType());
//
//            remoteStmt.executeUpdate();
//            rsSet = remoteStmt.getGeneratedKeys();
//            if (rsSet.next()) {
//                mainId = rsSet.getInt(1);
//            } else {
//                System.err.println("gbmInfo:" + gbmInfo.toString() + ",插入失败");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (remoteStmt != null) {
//                try {
//                    remoteStmt.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (rsSet != null) {
//                try {
//                    rsSet.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            DBHelper.getInstance().closeConnection(remoteConn);
//        }
//        return mainId;
    }

    @Override
    public boolean updateGroupBuyInfos(GroupBuyManageBean gbmInfo) {

        PreparedStatement remoteRtmt = null;
        Connection localConn = DBHelper.getInstance().getConnection();
        PreparedStatement localRtmt = null;
        String sql = "update group_buy_manage set final_price_need_num = ?,final_price = ?,init_virtual_num = ?,"
                + "effective_day = ?,active_begin_time = ?,active_end_time = ?,active_desc = ?,is_on = ?,type = ?"
                + " where id = ?";

        int isUpdate = 0;
        try {
//            reomteConn.setAutoCommit(false);
//            localConn.setAutoCommit(false);
            int remoteCount = 1;
            /*remoteRtmt = reomteConn.prepareStatement(sql);
            remoteRtmt.setInt(remoteCount++, gbmInfo.getFinalPriceNeedNum());
            remoteRtmt.setDouble(remoteCount++, gbmInfo.getFinalPrice());
            remoteRtmt.setInt(remoteCount++, gbmInfo.getInitVirtualNum());
            remoteRtmt.setInt(remoteCount++, gbmInfo.getEffectiveDay());
            remoteRtmt.setString(remoteCount++, gbmInfo.getActiveBeginTime());
            remoteRtmt.setString(remoteCount++, gbmInfo.getActiveEndTime());
            remoteRtmt.setString(remoteCount++, gbmInfo.getActiveDesc());
            remoteRtmt.setInt(remoteCount++, gbmInfo.getIsOn());
            remoteRtmt.setInt(remoteCount++, gbmInfo.getType());
            remoteRtmt.setInt(remoteCount++, gbmInfo.getId());*/
            List<String> lstValues = Lists.newArrayList();
            lstValues.add(String.valueOf(gbmInfo.getFinalPriceNeedNum()));
            lstValues.add(String.valueOf(gbmInfo.getFinalPrice()));
            lstValues.add(String.valueOf(gbmInfo.getInitVirtualNum()));
            lstValues.add(String.valueOf( gbmInfo.getEffectiveDay()));
            lstValues.add(gbmInfo.getActiveBeginTime());
            lstValues.add(gbmInfo.getActiveEndTime());
            lstValues.add(gbmInfo.getActiveDesc());
            lstValues.add(String.valueOf(gbmInfo.getIsOn()));
            lstValues.add(String.valueOf(gbmInfo.getType()));
            lstValues.add(String.valueOf(gbmInfo.getId()));


            int localCount = 1;
            localRtmt = localConn.prepareStatement(sql);
            localRtmt.setInt(localCount++, gbmInfo.getFinalPriceNeedNum());
            localRtmt.setDouble(localCount++, gbmInfo.getFinalPrice());
            localRtmt.setInt(localCount++, gbmInfo.getInitVirtualNum());
            localRtmt.setInt(localCount++, gbmInfo.getEffectiveDay());
            localRtmt.setString(localCount++, gbmInfo.getActiveBeginTime());
            localRtmt.setString(localCount++, gbmInfo.getActiveEndTime());
            localRtmt.setString(localCount++, gbmInfo.getActiveDesc());
            localRtmt.setInt(localCount++, gbmInfo.getIsOn());
            localRtmt.setInt(localCount++, gbmInfo.getType());
            localRtmt.setInt(localCount++, gbmInfo.getId());

            String runSql = DBHelper.covertToSQL(sql, lstValues);
            
            isUpdate = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));//remoteRtmt.executeUpdate();
            if (isUpdate > 0) {
                isUpdate = 0;
                isUpdate = localRtmt.executeUpdate();
                /*if (isUpdate > 0) {
                    reomteConn.commit();
                    localConn.commit();
                } else {
                    reomteConn.rollback();
                    localConn.rollback();
                }*/
            } else {
//                reomteConn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            /*try {
                reomteConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }*/
            /*try {
                localConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }*/
        } finally {
            if (remoteRtmt != null) {
                try {
                    remoteRtmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (localRtmt != null) {
                try {
                    localRtmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
//            DBHelper.getInstance().closeConnection(reomteConn);
            DBHelper.getInstance().closeConnection(localConn);
        }
        return isUpdate > 0;
    }

    @Override
    public boolean deleteGroupBuyInfos(int id) {

        PreparedStatement remoteRtmt = null;
        Connection localConn = DBHelper.getInstance().getConnection();
        PreparedStatement localRtmt = null;
        String sql = "delete from group_buy_manage where id = ?";

        int isDelete = 0;
        try {
//            reomteConn.setAutoCommit(false);
//            localConn.setAutoCommit(false);
//            remoteRtmt = reomteConn.prepareStatement(sql);
//            remoteRtmt.setInt(1, id);
        	List<String> lstValues = Lists.newArrayList();
        	lstValues.add(String.valueOf(id));
        	
            localRtmt = localConn.prepareStatement(sql);
            localRtmt.setInt(1, id);
            String runSql = DBHelper.covertToSQL(sql, lstValues);
            isDelete = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));//remoteRtmt.executeUpdate();
            if (isDelete > 0) {
                isDelete = 0;
                isDelete = localRtmt.executeUpdate();
                /*if (isDelete > 0) {
                    reomteConn.commit();
                    localConn.commit();
                } else {
                    reomteConn.rollback();
                    localConn.rollback();
                }*/
            } else {
//                reomteConn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
           /* try {
                reomteConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                localConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }*/
        } finally {
            if (remoteRtmt != null) {
                try {
                    remoteRtmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (localRtmt != null) {
                try {
                    localRtmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
//            DBHelper.getInstance().closeConnection(reomteConn);
            DBHelper.getInstance().closeConnection(localConn);
        }
        return isDelete > 0;
    }

    @Override
    public GroupBuyManageBean queryLatestDateInfoByPid(String pid) {
        {

            Connection conn = DBHelper.getInstance().getConnection();
            PreparedStatement stmt = null;
            ResultSet rs = null;
            String sql = "select a.id,a.final_price_need_num,a.final_price,a.init_virtual_num,a.effective_day,"
                    + "a.active_begin_time,a.active_end_time,a.active_desc,a.is_on,a.admin_id,a.create_time " +
                    "from group_buy_manage a where a.id in (select gb_id from group_buy_goods where pid = ? group by gb_id)" +
                    " order by a.active_begin_time desc limit 1";

            GroupBuyManageBean info = new GroupBuyManageBean();
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, pid);
                rs = stmt.executeQuery();
                if (rs.next()) {

                    info.setId(rs.getInt("id"));
                    info.setActiveBeginTime(rs.getString("active_begin_time"));
                    info.setActiveDesc(rs.getString("active_desc"));
                    info.setActiveEndTime(rs.getString("active_end_time"));
                    info.setAdminId(rs.getInt("admin_id"));
                    info.setEffectiveDay(rs.getInt("effective_day"));
                    info.setFinalPrice(rs.getDouble("final_price"));
                    info.setFinalPriceNeedNum(rs.getInt("final_price_need_num"));
                    info.setInitVirtualNum(rs.getInt("init_virtual_num"));
                    info.setIsOn(rs.getInt("is_on"));
                    info.setCreateTime(rs.getString("create_time"));
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
            return info;
        }
    }

    @Override
    public List<ShopGoodsInfo> queryShopGoodsFromGroupBuy(int gbId) {
        List<ShopGoodsInfo> goodsList = new ArrayList<ShopGoodsInfo>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select a.shop_id,a.pid,a.custom_main_image,a.remotpath,a.enname,a.price,a.range_price,a.wprice,a.eninfo " +
                " from custom_benchmark_ready a,group_buy_goods b where a.pid = b.pid and b.gb_id =? ";
        try {
            conn = DBHelper.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gbId);
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
                spGoods.setEnInfo(rs.getString("eninfo"));
                goodsList.add(spGoods);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("gbId:" + gbId + ",queryShopGoodsFromGroupBuy error: " + e.getMessage());
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
    public boolean insertGroupBuyShopGoods(List<GroupBuyGoodsBean> infos) {
        Connection localConn = null;
        PreparedStatement localStmt = null;
        Connection remoteConn = null;
        PreparedStatement remoteStmt = null;
        String insertSql = "insert into group_buy_goods(gb_id,pid,shop_id,is_main,admin_id,is_on) values(?,?,?,?,?,?)";
        int count = 0;
        try {
            localConn = DBHelper.getInstance().getConnection();
//            remoteConn = DBHelper.getInstance().getConnection2();
//            localConn.setAutoCommit(false);
//            remoteConn.setAutoCommit(false);

//            remoteStmt = remoteConn.prepareStatement(insertSql);
            
            localStmt = localConn.prepareStatement(insertSql);
            for (GroupBuyGoodsBean gbGoods : infos) {
            	List<String> lstValues = Lists.newArrayList();
            	lstValues.add(String.valueOf(gbGoods.getGbId()));
            	lstValues.add(gbGoods.getPid());
            	lstValues.add(gbGoods.getShopId());
            	lstValues.add(String.valueOf(gbGoods.getIsMain()));
            	lstValues.add(String.valueOf(gbGoods.getAdminId()));
            	lstValues.add(String.valueOf(gbGoods.getIsOn()));
            	String runSql = DBHelper.covertToSQL(insertSql, lstValues);
				//                remoteStmt.addBatch();
            	count = count +Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql )));
            	
                localStmt.setInt(1, gbGoods.getGbId());
                localStmt.setString(2, gbGoods.getPid());
                localStmt.setString(3, gbGoods.getShopId());
                localStmt.setInt(4, gbGoods.getIsMain());
                localStmt.setInt(5, gbGoods.getAdminId());
                localStmt.setInt(6, gbGoods.getIsOn());
                localStmt.addBatch();

            }
//            count = remoteStmt.executeBatch().length;
            if (count > 0) {
                count = 0;
                count = localStmt.executeBatch().length;
                /*if (count > 0) {
                    localConn.commit();
                    remoteConn.commit();
                } else {
                    localConn.rollback();
                    remoteConn.rollback();
                }*/

            } else {
//                remoteConn.rollback();
            }
        } catch (Exception e) {
           /* try {
                remoteConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }*/
            e.printStackTrace();
            System.err.println("insertGroupBuyShopGoods error: " + e.getMessage());
        } finally {
            if (localStmt != null) {
                try {
                    localStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (remoteStmt != null) {
                try {
                    remoteStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(localConn);
            DBHelper.getInstance().closeConnection(remoteConn);
        }
        return count > 0;
    }

    @Override
    public boolean deleteGroupBuyShopGoods(List<GroupBuyGoodsBean> infos) {
        Connection localConn = null;
        PreparedStatement localStmt = null;
        Connection remoteConn = null;
        PreparedStatement remoteStmt = null;
        String deleteSql = "delete from group_buy_goods where pid = ? and is_main = 0 ";
        int count = 0;
        try {
            localConn = DBHelper.getInstance().getConnection();
//            remoteConn = DBHelper.getInstance().getConnection2();
//            localConn.setAutoCommit(false);
//            remoteConn.setAutoCommit(false);

//            remoteStmt = remoteConn.prepareStatement(deleteSql);
            localStmt = localConn.prepareStatement(deleteSql);
            for (GroupBuyGoodsBean gbGoods : infos) {
//                remoteStmt.setString(1, gbGoods.getPid());
//                remoteStmt.addBatch();

            	List<String> lstValues = Lists.newArrayList();
            	lstValues.add(gbGoods.getPid());
            	String runSql = DBHelper.covertToSQL(deleteSql, lstValues);
            	count +=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
            	
                localStmt.setString(1, gbGoods.getPid());
                localStmt.addBatch();
            }
//            count = remoteStmt.executeBatch().length;
            if (count > 0) {
                count = 0;
                count = localStmt.executeBatch().length;
                /*if (count > 0) {
                    remoteConn.commit();
                    localConn.commit();
                } else {
                    remoteConn.rollback();
                    localConn.rollback();
                }*/
            } else {
//                remoteConn.rollback();
            }
        } catch (Exception e) {
          /*  try {
                remoteConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                localConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }*/
            e.printStackTrace();
            System.err.println("deleteGroupBuyShopGoods error: " + e.getMessage());
        } finally {
            if (remoteStmt != null) {
                try {
                    remoteStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (localStmt != null) {
                try {
                    localStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(localConn);
            DBHelper.getInstance().closeConnection(remoteConn);
        }
        return count > 0;
    }

    @Override
    public List<ShopGoodsInfo> queryShopGoodsByShopIdAndPid(String shopId, String pid) {
        List<ShopGoodsInfo> goodsList = new ArrayList<ShopGoodsInfo>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select shop_id,pid,custom_main_image,remotpath,enname,price,range_price,wprice,eninfo" +
                " from custom_benchmark_ready where shop_id = ? and pid != ? and valid =1";
        try {
            conn = DBHelper.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, shopId);
            stmt.setString(2, pid);
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
                spGoods.setEnInfo(rs.getString("eninfo"));
                goodsList.add(spGoods);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",pid:" + pid + ",queryShopGoodsByShopIdAndPid error: " + e.getMessage());
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
    public CustomGoodsPublish queryFor1688Goods(String pid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select cbr.*,ifnull(c8g.name,'') as name from " +
                "( select id,catid,catid1,pid,shop_id,custom_main_image as img, localpath,remotpath,enname,price,wprice,isNewCloud " +
                "from custom_benchmark_ready where pid =? and valid =1 UNION " +
                "select id,catid,catid1,pid,'' as shop_id,custom_main_image as img, localpath,remotpath,enname,price,wprice,isNewCloud " +
                "from custom_benchmark_ready_cloud where pid =? and valid =1 ) cbr left join " +
                "(select pid,name from custom_1688_goods where pid =?) c8g on cbr.pid = c8g.pid";
        CustomGoodsPublish spGoods = new CustomGoodsPublish();
        try {
            conn = DBHelper.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pid);
            stmt.setString(2, pid);
            stmt.setString(3, pid);
            rs = stmt.executeQuery();

            if (rs.next()) {

                spGoods.setId(rs.getInt("id"));
                spGoods.setShopId(rs.getString("shop_id"));

                spGoods.setCatid(rs.getString("catid"));
                spGoods.setCatid1(rs.getString("catid1"));
                spGoods.setPid(rs.getString("pid"));

                spGoods.setImg(rs.getString("img"));
                spGoods.setLocalpath(rs.getString("localpath"));
                spGoods.setRemotpath(rs.getString("remotpath"));
                spGoods.setEnname(rs.getString("enname"));

                spGoods.setPrice(rs.getString("price"));

                spGoods.setWprice(rs.getString("wprice"));
                spGoods.setIsNewCloud(rs.getInt("isNewCloud"));

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + pid + ",queryFor1688Goods error: " + e.getMessage());
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
        return spGoods;
    }

    @Override
    public List<OrderDetailsBean> queryOrderDetailsBuyGroupBuyIds(String groupBuyIds) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select id,userid,goodsid,orderid,yourorder,goodsprice,goods_pid,group_buy_id from order_details where group_buy_id in(";
        sql += groupBuyIds + ")";
        List<OrderDetailsBean> list = new ArrayList<OrderDetailsBean>();
        try {
            conn = DBHelper.getInstance().getConnection2();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {

                OrderDetailsBean gd = new OrderDetailsBean();
                gd.setId(rs.getInt("id"));
                gd.setUserid(rs.getInt("userid"));
                gd.setGoodsid(rs.getInt("goodsid"));
                gd.setOrderid(rs.getString("orderid"));

                gd.setYourorder(rs.getInt("yourorder"));
                gd.setGoodsprice(rs.getString("goodsprice"));
                gd.setGoods_pid(rs.getString("goods_pid"));
                gd.setGroupBuyId(rs.getInt("group_buy_id"));
                list.add(gd);

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("groupBuyIds:" + groupBuyIds + ",queryOrderDetailsBuyGroupBuyIds error: " + e.getMessage());
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
    public boolean insertAndUpdateUserCoupon(List<UserCouponBean> couponList, Map<Integer, Double> userCpMap, String gbIds) {
        Connection localConn = null;
        Statement localStmt = null;
//        Connection remoteConn = null;
        PreparedStatement remoteIsStmt = null;
        Statement remoteUpStmt = null;

        //插入coupon记录
        String insertSql = "insert into user_coupon_details(user_id,order_no,coupon_value,type,goods_pid,source_flag,goodsid)" +
                " values(?,?,?,?,?,?,?)";
        //更新用户表coupon记录
        String updateUser = "update user set coupon_value = coupon_value + ? where id = ?";
        //更新活动结束
        String uudateGroupBuy = "update group_buy_manage set end_flag =1,end_time = now() where id = ?";
        int count = 0;
        try {
//            remoteConn = DBHelper.getInstance().getConnection2();
//            remoteConn.setAutoCommit(false);
            localConn = DBHelper.getInstance().getConnection();
//            localConn.setAutoCommit(false);

//            remoteIsStmt = remoteConn.prepareStatement(insertSql);
            for (UserCouponBean ucb : couponList) {
            	List<String> lstValues = Lists.newArrayList();
            	lstValues.add(String.valueOf(ucb.getUserId()));
            	lstValues.add(ucb.getOrderNo());
            	lstValues.add(String.valueOf(ucb.getCouponValue()));
            	lstValues.add(String.valueOf(ucb.getType()));
            	lstValues.add(ucb.getGoodsPid());
            	lstValues.add(String.valueOf(ucb.getSourceFlag()));
            	lstValues.add(String.valueOf(ucb.getGoodsId()));
                /*remoteIsStmt.setInt(1, ucb.getUserId());
                remoteIsStmt.setString(2, ucb.getOrderNo());
                remoteIsStmt.setDouble(3, ucb.getCouponValue());
                remoteIsStmt.setInt(4, ucb.getType());
                remoteIsStmt.setString(5, ucb.getGoodsPid());
                remoteIsStmt.setInt(6, ucb.getSourceFlag());
                remoteIsStmt.setInt(7, ucb.getGoodsId());
                remoteIsStmt.addBatch();*/
            	String runSql = DBHelper.covertToSQL(insertSql, lstValues);
            	count = count + Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
            }

//            remoteUpStmt = remoteConn.createStatement();
            localStmt = localConn.createStatement();
            List<String> sqlList = new ArrayList<String>();
            int count1 = 0;
            for (int userId : userCpMap.keySet()) {
                String tempStr = "update user set coupon_value = coupon_value + "
                        + BigDecimalUtil.truncateDouble(userCpMap.get(userId), 2) + " where id = " + userId;
//                remoteUpStmt.addBatch(tempStr);
                count1 = count1 + Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(tempStr)));
                sqlList.add(tempStr);
            }
            String[] gbIdList = gbIds.split(",");
            for (String gbId : gbIdList) {
                if(StringUtils.isNotBlank(gbId)){
                    String tempSql = "update group_buy_manage set end_flag =1,end_time = now() where id = " + gbId;
                    sqlList.add(tempSql);
//                    remoteUpStmt.addBatch(tempSql);
                    count1 = count1 + Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(tempSql)));
                    localStmt.addBatch(tempSql);
                }

            }
            gbIdList = null;

            sqlList.clear();

//            count = remoteIsStmt.executeBatch().length;
           if (count > 0) {
                count = 0;
                count = count1;//remoteUpStmt.executeBatch().length;
                if (count > 0) {
                    count = 0;
                    count = localStmt.executeBatch().length;
                    /*if (count > 0) {
                        remoteConn.commit();
                        localConn.commit();
                    } else {
                        remoteConn.rollback();
                        localConn.rollback();
                    }*/
                } else {
//                    remoteConn.rollback();
                }

            } else {
//                remoteConn.rollback();
            }
        } catch (Exception e) {
           /* try {
                remoteConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                localConn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }*/
            e.printStackTrace();
            System.err.println("insertAndUpdateUserCoupon error: " + e.getMessage());
        } finally {
            if (remoteIsStmt != null) {
                try {
                    remoteIsStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (remoteUpStmt != null) {
                try {
                    remoteUpStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (localStmt != null) {
                try {
                    localStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
//            DBHelper.getInstance().closeConnection(remoteConn);
            DBHelper.getInstance().closeConnection(localConn);
        }
        return count > 0;
    }

    @Override
    public List<GroupBuyManageBean> getIsEndGroupBuyInfos(String beginTime, String endTime) {
        List<GroupBuyManageBean> list = new ArrayList<GroupBuyManageBean>();

        Connection conn = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select * from group_buy_manage " +
                "where 1=1 and is_on = 1 and end_flag = 0 and active_end_time >= ? and active_end_time <= ?";
        if (StringUtils.isBlank(beginTime) || beginTime.length() < 5) {
            return list;
        }
        if (StringUtils.isBlank(endTime) || endTime.length() < 5) {
            return list;
        }
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, beginTime);
            stmt.setString(2, endTime);
            rs = stmt.executeQuery();
            while (rs.next()) {
                GroupBuyManageBean info = new GroupBuyManageBean();
                info.setActiveBeginTime(rs.getString("active_begin_time"));
                info.setActiveDesc(rs.getString("active_desc"));
                info.setActiveEndTime(rs.getString("active_end_time"));
                info.setAdminId(rs.getInt("admin_id"));
                info.setEffectiveDay(rs.getInt("effective_day"));
                info.setFinalPrice(rs.getDouble("final_price"));
                info.setFinalPriceNeedNum(rs.getInt("final_price_need_num"));
                info.setId(rs.getInt("id"));
                info.setInitVirtualNum(rs.getInt("init_virtual_num"));
                info.setIsOn(rs.getInt("is_on"));
                info.setCreateTime(rs.getString("create_time"));
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

}
