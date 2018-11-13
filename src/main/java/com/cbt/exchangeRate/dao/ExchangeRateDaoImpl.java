package com.cbt.exchangeRate.dao;

import com.cbt.bean.ExchangeRateDaily;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.BigDecimalUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeRateDaoImpl implements ExchangeRateDao {

    @Override
    public int updateRate(List<Map<String, String>> list) {
        // TODO Auto-generated method stub
//        "UPDATE 表名 SET 字段1= CASE WHEN 条件1 THEN 1 ELSE 0 END, 字段2= CASE WHEN 条件2 THEN 0 ELSE 1 END"
        String sql = "update  exchange_rate  set exchange_rate = case ";
        if (list != null) {
            for (Map<String, String> map : list) {
                sql += "  when country = '" + map.get("country") + "'  then  '" + map.get("rate") + "'";
            }
            sql += " end   where ";
            for (Map<String, String> map : list) {
                sql += "  country = '" + map.get("country") + "'  or";
            }
            sql = sql.substring(0, sql.lastIndexOf("or"));
        }
        System.out.println(sql);
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        int rs = 0;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeUpdate();
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
        return rs;
    }

    @Override
    public List<Map<String, Object>> getExchangeRate() {
        // TODO Auto-generated method stub
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        String sql = "select id,country,exchange_rate from exchange_rate ";
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", rs.getInt("id"));
                map.put("country", rs.getString("country"));
                map.put("exchange_rate", rs.getString("exchange_rate"));
                list.add(map);
            }

            return list;
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
        return null;
    }

    @Override
    public List<ExchangeRateDaily> queryExchangeRateByDate(int year, int month, int start, int rows) {
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        String sql = "select id,eur_rate,cad_rate,gbp_rate,aud_rate,rmb_rate,create_time,DATE_FORMAT(create_time,'%Y-%m') as get_time"
                + " from exchange_rate_daily"
                + " where YEAR(create_time) = ? ";
        if(month > 0){
            sql += " and MONTH(create_time) = ? ";
        }
        sql += " order by create_time";
        if (rows > 0) {
            sql += " limit ?,?";
        }
        ResultSet rs = null;
        List<ExchangeRateDaily> list = new ArrayList<ExchangeRateDaily>();
        try {
            stmt = conn.prepareStatement(sql);

            int count = 1;
            stmt.setInt(count++, year);
            if(month > 0){
                stmt.setInt(count++, month);
            }

            if (rows > 0) {
                stmt.setInt(count++, start);
                stmt.setInt(count++, rows);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                ExchangeRateDaily exchangeRateDaily = new ExchangeRateDaily();
                exchangeRateDaily.setAudRate(BigDecimalUtil.truncateDouble(rs.getDouble("aud_rate"),4));
                exchangeRateDaily.setCadRate(BigDecimalUtil.truncateDouble(rs.getDouble("cad_rate"),4));
                exchangeRateDaily.setEurRate(BigDecimalUtil.truncateDouble(rs.getDouble("eur_rate"),4));
                exchangeRateDaily.setGbpRate(BigDecimalUtil.truncateDouble(rs.getDouble("gbp_rate"),4));
                exchangeRateDaily.setGetTime(rs.getString("get_time"));
                exchangeRateDaily.setRmbRate(BigDecimalUtil.truncateDouble(rs.getDouble("rmb_rate"),4));
                exchangeRateDaily.setCreateTime(rs.getString("create_time"));
                list.add(exchangeRateDaily);
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
            DBHelper.getInstance().closeConnection(conn);
        }
        return list;
    }

    @Override
    public int queryExchangeRateByDateCount(int year, int month) {
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        String sql = "select count(0) from exchange_rate_daily where YEAR(create_time) = ?  and MONTH(create_time) = ? ";
        ResultSet rs;
        int count = 0;
        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, year);
            stmt.setInt(2, month);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
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
            DBHelper.getInstance().closeConnection(conn);
        }
        return count;
    }

}
