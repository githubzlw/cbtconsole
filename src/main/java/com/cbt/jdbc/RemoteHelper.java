
package com.cbt.jdbc;

import java.sql.*;


public class RemoteHelper {
    public static String driver="com.mysql.jdbc.Driver";
    /*public static String url="jdbc:sqlserver://192.168.1.2; DatabaseName=ShangHaiSourcing"; 
   public static String user="sa";
    public static String pwd="Safevault73_1";*/
    public static String url="jdbc:mysql://216.244.83.218:3306/cross_border_shop?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useCompression=true"; 
    public static String user="websql";
    public static String pwd="websqlImport";
    
   
     
    public static Connection getConnection(){
        try {
            Class.forName(driver);
            Connection con=DriverManager.getConnection(url, user, pwd);
            System.out.println("con success");
            return con;
        } catch (ClassNotFoundException e) {
           
            e.printStackTrace();
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        return null;
    }
     
    public static void close(Connection con,Statement stm,ResultSet rs){
        if(con!=null){
            try {
                con.close();
            } catch (SQLException e) {
               
                e.printStackTrace();
            }
        }
        if(stm!=null){
            try {
                stm.close();
            } catch (SQLException e) {
              
                e.printStackTrace();
            }
        }
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                
                e.printStackTrace();
            }
        }
    }
 

	public static void returnConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
     
    public static void main(String[] args) {
        RemoteHelper.getConnection();
    }
}