package com.cbt.dao.impl;

import com.cbt.dao.GoodsCarConfigDao;
import com.cbt.jdbc.DBHelper;
import com.importExpress.pojo.GoodsCarconfigWithBLOBs;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class GoodsCarConfigDaoImpl implements GoodsCarConfigDao {


    @Override
    public boolean updateGoodsCarConfig(GoodsCarconfigWithBLOBs record) {
        String sql = "update goods_carconfig set buyForMeCarConfig= ?,updateTime = now(),needCheck=0 where userid = ?";
//        Connection conn = DBHelper.getInstance().getConnection2(); // 27
//        PreparedStatement stmt = null;
        int rs = 0;
        try {
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, record.getBuyformecarconfig());
//            stmt.setInt(2, record.getUserid());
//            rs = stmt.executeUpdate();

            List<String> lstValues = new ArrayList<String>();
            lstValues.add(record.getBuyformecarconfig());
            lstValues.add(String.valueOf(record.getUserid()));

            String runSql = DBHelper.covertToSQL(sql,lstValues);
            rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("userId:" + record.getUserid() + ",update error:" + e.getMessage());
        } finally {
//            DBHelper.getInstance().closePreparedStatement(stmt);
//            DBHelper.getInstance().closeConnection(conn);
        }
        return rs > 0;
    }
}
