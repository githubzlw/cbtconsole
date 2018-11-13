package com.cbt.email.util;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * 事务管理器 
 * @author lzj 
 * @version 0.9Beta 
 */  
public class TransactionManager {  
    private Connection conn;  
      
    public TransactionManager(Connection conn) {  
        this.conn = conn;  
    }  
      
    /** 开启事务 */  
    public void beginTransaction() throws Exception{  
        try {  
            conn.setAutoCommit(false);  //把事务提交方式改为手工提交  
        } catch (SQLException e) {  
            throw new Exception("开户事务时出现异常",e);  
        }  
    }  
      
    /** 提交事务并关闭连接 */  
    public void commitAndClose() throws Exception{  
        try {  
            conn.commit(); //提交事务  
        } catch (SQLException e) {  
            throw new Exception("提交事务时出现异常",e);  
        }finally{  
        	DBHelper.getInstance().closeConnection(conn);
        }  
    }  
      
    /** 回滚并关闭连接 */  
    public void rollbackAndClose()throws Exception{  
        try {  
            conn.rollback();  
        } catch (SQLException e) {  
            throw new Exception("回滚事务时出现异常",e);  
        }finally{  
        	DBHelper.getInstance().closeConnection(conn);
        }  
    }  
}  