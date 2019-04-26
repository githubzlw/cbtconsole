package com.importExpress.test;

import com.cbt.jdbc.DBHelper;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DealIpTest {


    public static void main(String[] args) throws IOException {

        File file = new File("E:/IP.txt");

        if (file.isFile() && file.exists()) {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "gbk");
            BufferedReader br = new BufferedReader(isr);
            String lineTxt = null;
            int num = 0;
            List<Map<String, String>> list = new ArrayList<>();
            while ((lineTxt = br.readLine()) != null) {
                System.out.println(lineTxt);
                String ip = lineTxt.substring(0, 16);
                String segment = lineTxt.substring(16, 32);
                String ot = lineTxt.substring(32);
                String[] lstt = ot.split(" ");
                String country = "";
                String website = "";
                if (lstt.length >= 2) {
                    country = lstt[0];
                    website = lstt[1];
                } else {
                    website = ot;
                }
                num++;
                Map<String, String> map = new HashMap<>();
                map.put("ip", ip.trim());
                map.put("segment", segment.trim());
                map.put("country", country.trim());
                map.put("website", website.trim());
                list.add(map);
                if (num % 500 == 0) {
                    insetInto(list);
                    list.clear();
                    list = new ArrayList<>();
                }
            }
            br.close();
        } else {
            System.out.println("文件不存在!");
        }
    }

    private static void insetInto(List<Map<String, String>> list) {
        Connection conn = DBHelper.getInstance().getConnection();

        String sql = "insert into ip_country(ip,segment,country,website)" +
                " values(?,?,?,?)";
        PreparedStatement stm = null;
        try {
            stm = conn.prepareStatement(sql);

            for (Map<String, String> map : list) {
                stm.setString(1, map.get("ip"));
                stm.setString(2, map.get("segment"));
                stm.setString(3, map.get("country"));
                stm.setString(4, map.get("website"));
                stm.addBatch();
            }
            stm.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.getInstance().closePreparedStatement(stm);
            DBHelper.getInstance().closeConnection(conn);
        }

    }
}
