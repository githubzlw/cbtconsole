package com.cbt.jdbc;

/**
 * 业务服务器使用
 * @author chenhaishen
 */

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;


public class DBHelper {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DBHelper.class);

//    private static DruidDataSource dataSource = null;
    private static ComboPooledDataSource dataSource = null;
    private static ComboPooledDataSource dataSource2 = null;
    private static ComboPooledDataSource dataSource3 = null;
    private static ComboPooledDataSource dataSource4 = null;
    /***
     *dataSource28alidata
     */
    private static ComboPooledDataSource dataSource5 = null;
    /***
     *DB31Source
     */
    private static ComboPooledDataSource dataSource6 = null;
    /**
     * url31userful
     */
    private static ComboPooledDataSource dataSource7 = null;
    /**
     * url28
     */
    private static ComboPooledDataSource dataSource8 = null;
    private static DBHelper instance;
    private static AtomicInteger totalConnect1  = new AtomicInteger();
    private static AtomicInteger totalConnect2  = new AtomicInteger();
    private static AtomicInteger totalConnect3  = new AtomicInteger();
    private static AtomicInteger totalConnect4 = new AtomicInteger();
    private static AtomicInteger totalConnect5 = new AtomicInteger();
    private static AtomicInteger totalConnect6 = new AtomicInteger();
    private static AtomicInteger totalConnect7 = new AtomicInteger();
    private static AtomicInteger totalConnect8 = new AtomicInteger();

    private static AtomicInteger totalDisConnectNum = new AtomicInteger();
    private static AtomicInteger totalConnectNum = new AtomicInteger();


    private DBHelper() {
        try {
            logger.info("初始化数据库...");

            synchronized (DBHelper.class) {

                InputStream ins = null;
                Properties p = null;
                try {
                    ins = DBHelper.class.getResourceAsStream("../../../jdbc.properties");
                    p = new Properties();
                    p.load(ins);
                } catch (IOException ioe) {
                    logger.error("DBHelper",ioe);
                }finally {
                    try{
                        ins.close();
                    }catch (IOException ioe){
                        logger.error("DBHelper",ioe);
                    }
                }
                initDataSource(p);

                initDataSource2(p);

                initDataSource3(p);

                initDataSource4(p);

                initDataSource5(p);

                initDataSource6(p);

                initDataSource7(p);

                initDataSource8(p);
            }
        } catch (Exception e) {
            logger.error("初始化数据库ERROR",e);
            throw new RuntimeException("初始化数据库ERROR");
        }
    }

//    private void initDataSource(Properties p) {
//        dataSource = new DruidDataSource();
//
//        dataSource.setDriverClassName(p.getProperty("driver"));
//        dataSource.setUrl(p.getProperty("url"));
//        dataSource.setUsername(p.getProperty("userName"));
//        dataSource.setPassword(p.getProperty("userPass"));
//        dataSource.setInitialSize(30);
//        dataSource.setMinIdle(30);
//        dataSource.setMaxActive(300);
//        dataSource.setMaxWait(3000);
//        dataSource.setTimeBetweenEvictionRunsMillis(60000);
//        dataSource.setMinEvictableIdleTimeMillis(60000);
//        dataSource.setValidationQuery("select 1");
//        dataSource.setTestWhileIdle(true);
//        dataSource.setTestOnBorrow(true);
//        dataSource.setTestOnReturn(true);
//        dataSource.setRemoveAbandoned(true);
//        dataSource.setRemoveAbandonedTimeout(1800);
//        dataSource.setLogAbandoned(true);
//        dataSource.setDefaultTransactionIsolation(2);
//
//        try {
//            dataSource.setFilters("stat,log4j");
//        } catch (SQLException e) {
//            logger.error("initDataSource",e);
//        }
//
//        logger.info("初始化数据库1完成");
//    }

    private void initDataSource(Properties p) throws PropertyVetoException {
        dataSource = new ComboPooledDataSource();
        dataSource.setUser(p.getProperty("userName"));
        dataSource.setPassword(p.getProperty("userPass"));
        dataSource.setJdbcUrl(p.getProperty("url"));
        dataSource.setDriverClass(p.getProperty("driver"));
        dataSource.setInitialPoolSize(100);
        dataSource.setMinPoolSize(100);
        dataSource.setMaxPoolSize(100);
        dataSource.setMaxStatements(50);
        dataSource.setMaxIdleTime(60);

        logger.info("初始化数据库1完成");
    }

    private void initDataSource2(Properties p) throws PropertyVetoException {
        dataSource2 = new ComboPooledDataSource();
        dataSource2.setUser(p.getProperty("userName127hop"));
        dataSource2.setPassword(p.getProperty("userPass127hop"));
        dataSource2.setJdbcUrl(p.getProperty("url127hop"));
        dataSource2.setDriverClass(p.getProperty("driver"));
        dataSource2.setInitialPoolSize(20);
        dataSource2.setMinPoolSize(10);
        dataSource2.setMaxPoolSize(100);
        dataSource2.setMaxStatements(50);
        dataSource2.setMaxIdleTime(60);

        logger.info("初始化数据库2完成");
    }

    private void initDataSource3(Properties p) throws PropertyVetoException {
        dataSource3 = new ComboPooledDataSource();
        dataSource3.setUser(p.getProperty("new_name"));
        dataSource3.setPassword(p.getProperty("new_pass"));
        dataSource3.setJdbcUrl(p.getProperty("new_url")
                + "?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useCompression=true&allowMultiQueries=true");
        dataSource3.setDriverClass(p.getProperty("driver"));
        dataSource3.setInitialPoolSize(20);
        dataSource3.setMinPoolSize(10);
        dataSource3.setMaxPoolSize(100);
        dataSource3.setMaxStatements(50);
        dataSource3.setMaxIdleTime(60);

        logger.info("初始化数据库3完成");
    }

    /**
     * connection_28_alidata
     * @param p
     * @throws PropertyVetoException
     */
    private void initDataSource4(Properties p) throws PropertyVetoException {
        dataSource4 = new ComboPooledDataSource();
        dataSource4.setUser(p.getProperty("jdbc.userName28hop"));
        dataSource4.setPassword(p.getProperty("jdbc.userPass28hop"));
        dataSource4.setJdbcUrl(p.getProperty("jdbc.url28hop")
                + "?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useCompression=true&allowMultiQueries=true");
        dataSource4.setDriverClass(p.getProperty("driver"));
        dataSource4.setInitialPoolSize(20);
        dataSource4.setMinPoolSize(10);
        dataSource4.setMaxPoolSize(100);
        dataSource4.setMaxStatements(50);
        dataSource4.setMaxIdleTime(60);

        logger.info("初始化数据库4完成");
    }

    private void initDataSource5(Properties p) throws PropertyVetoException {
        dataSource5 = new ComboPooledDataSource();
        dataSource5.setUser(p.getProperty("jdbc.userName28hop"));
        dataSource5.setPassword(p.getProperty("jdbc.userPass28hop"));
        dataSource5.setJdbcUrl(p.getProperty("jdbc.url28hop")
                + "?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useCompression=true&allowMultiQueries=true");
        dataSource5.setDriverClass(p.getProperty("jdbc.driver28hop"));
        dataSource5.setInitialPoolSize(20);
        dataSource5.setMinPoolSize(10);
        dataSource5.setMaxPoolSize(100);
        dataSource5.setMaxStatements(50);
        dataSource5.setMaxIdleTime(60);

        logger.info("初始化数据库5完成");
    }


    private void initDataSource6(Properties p) throws PropertyVetoException {
        dataSource6 = new ComboPooledDataSource();
        dataSource6.setUser(p.getProperty("name31source"));
        dataSource6.setPassword(p.getProperty("pass31source"));
        dataSource6.setJdbcUrl(p.getProperty("url31source")
                + "?useSSL=false&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true"
                + "&useCompression=true&rewriteBatchedStatements=true");
        dataSource6.setDriverClass(p.getProperty("driver"));
        dataSource6.setInitialPoolSize(20);
        dataSource6.setMinPoolSize(10);
        dataSource6.setMaxPoolSize(100);
        dataSource6.setMaxStatements(50);
        dataSource6.setMaxIdleTime(60);

        logger.info("初始化数据库6完成");
    }


    private void initDataSource7(Properties p) throws PropertyVetoException {
        dataSource7 = new ComboPooledDataSource();
        dataSource7.setUser(p.getProperty("name31userful"));
        dataSource7.setPassword(p.getProperty("pass31userful"));
        dataSource7.setJdbcUrl(p.getProperty("url31userful")
                + "?useSSL=false&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true"
                + "&useCompression=true&rewriteBatchedStatements=true");
        dataSource7.setDriverClass(p.getProperty("driver"));
        dataSource7.setInitialPoolSize(20);
        dataSource7.setMinPoolSize(10);
        dataSource7.setMaxPoolSize(100);
        dataSource7.setMaxStatements(50);
        dataSource7.setMaxIdleTime(60);

        logger.info("初始化数据库7完成");
    }


    private void initDataSource8(Properties p) throws PropertyVetoException {
        dataSource8 = new ComboPooledDataSource();
        dataSource8.setUser(p.getProperty("userName28"));
        dataSource8.setPassword(p.getProperty("userPass28"));
        dataSource8.setJdbcUrl(p.getProperty("url28")
                + "?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useCompression=true&allowMultiQueries=true");
        dataSource8.setDriverClass(p.getProperty("driver"));
        dataSource8.setInitialPoolSize(20);
        dataSource8.setMinPoolSize(10);
        dataSource8.setMaxPoolSize(100);
        dataSource8.setMaxStatements(50);
        dataSource8.setMaxIdleTime(60);

        logger.info("初始化数据库8完成");
    }

    /**
     * 销毁
     */
    public void destory() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public synchronized static final DBHelper getInstance() {
        if (instance == null) {
            try {
                instance = new DBHelper();
            } catch (Exception e) {
                logger.error("",e);
            }
        }
        return instance;
    }

    /**
     * connection 27_crossboder
     * @return
     */
    public synchronized final Connection getConnection() {
        Connection conn;
        try {
            conn = dataSource.getConnection();
            logger.info("取得数据库连接+1 (#1),连接数：" + totalConnect1.incrementAndGet() + ",返回总数/获取总数：" + totalDisConnectNum + "/" + totalConnectNum.incrementAndGet());
        } catch (SQLException e) {
            logger.error("getConnection",e);
            throw new RuntimeException("getConnection ERROR");
        }
        return conn;
    }

    /**
     * connection AWS
     * @return
     */
    public synchronized final Connection getConnection2() {
        Connection conn;
        try {
            conn = dataSource2.getConnection();
            logger.info("取得数据库连接+1 (#2),连接数：" + totalConnect2.incrementAndGet() + ",返回总数/获取总数：" + totalDisConnectNum + "/" + totalConnectNum.incrementAndGet());
        } catch (SQLException e) {
            logger.error("getConnection2",e);
            throw new RuntimeException("getConnection2 ERROR");
        }
        return conn;
    }

    /**
     * connection 27_new_product_cloud
     * @return
     */
    public synchronized final Connection getConnection3() {
        Connection conn;
        try {
            conn = dataSource3.getConnection();
            logger.info("取得数据库连接+1 (#3),连接数：" + totalConnect3.incrementAndGet() + ",返回总数/获取总数：" + totalDisConnectNum + "/" + totalConnectNum.incrementAndGet());
        } catch (SQLException e) {
            logger.error("getConnection3",e);
            throw new RuntimeException("getConnection3 ERROR");
        }
        return conn;
    }

    /**
     * connection 28_alidata
     * @return
     */
    public synchronized final Connection getConnection4() {
        Connection conn;
        try {
            conn = dataSource4.getConnection();
            logger.info("取得数据库连接+1 (#4),连接数：" + totalConnect4.incrementAndGet() + ",返回总数/获取总数：" + totalDisConnectNum + "/" + totalConnectNum.incrementAndGet());
        } catch (SQLException e) {
            logger.error("getConnection4",e);
            throw new RuntimeException("getConnection4 ERROR");
        }
        return conn;
    }

    /**
     * connection 28_alidata
     * @return
     */
    public synchronized final Connection getConnection5() {
        Connection conn;
        try {
            conn = dataSource5.getConnection();
            logger.info("取得数据库连接+1 (#5),连接数：" + totalConnect5.incrementAndGet() + ",返回总数/获取总数：" + totalDisConnectNum + "/" + totalConnectNum.incrementAndGet());
        } catch (SQLException e) {
            logger.error("getConnection5",e);
            throw new RuntimeException("getConnection5 ERROR");
        }
        return conn;
    }

    /**
     * connection 31_source_data
     * return
     */
    public synchronized final Connection getConnection6() {
        Connection conn;
        try {
            conn = dataSource6.getConnection();
            logger.info("取得数据库连接+1 (#6),连接数：" + totalConnect6.incrementAndGet() + ",返回总数/获取总数：" + totalDisConnectNum + "/" + totalConnectNum.incrementAndGet());
        } catch (SQLException e) {
            logger.error("getConnection6",e);
            throw new RuntimeException("getConnection6 ERROR");
        }
        return conn;
    }

    /**
     * connection 31_userful
     * return
     */
    public synchronized final Connection getConnection7() {
        Connection conn;
        try {
            conn = dataSource7.getConnection();
            logger.info("取得数据库连接+1 (#7),连接数：" + totalConnect7.incrementAndGet() + ",返回总数/获取总数：" + totalDisConnectNum + "/" + totalConnectNum.incrementAndGet());
        } catch (SQLException e) {
            logger.error("getConnection7",e);
            throw new RuntimeException("getConnection7 ERROR");
        }
        return conn;
    }

    /**
     * connection 28_cross_border
     * @return
     */
    public synchronized final Connection getConnection8() {
        Connection conn;
        try {
            conn = dataSource8.getConnection();
            logger.info("取得数据库连接+1 (#8),连接数：" + totalConnect8.incrementAndGet() + ",返回总数/获取总数：" + totalDisConnectNum + "/" + totalConnectNum.incrementAndGet());
        } catch (SQLException e) {
            logger.error("getConnection8",e);
            throw new RuntimeException("getConnection8 ERROR");
        }
        return conn;
    }


    public void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                logger.info("释放数据库连接-1,总返回数量：" + totalDisConnectNum.incrementAndGet());
            }
        } catch (SQLException e) {
            logger.error("getConnection",e);
        }
    }


    public void closePreparedStatement(PreparedStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            logger.error("closePreparedStatement",e);
        }
    }
    
    public void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("closeResultSet",e);
        }
    }

    public void rollbackConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            logger.error("rollbackConnection",e);
        }
    }

    public void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            logger.error("closeStatement",e);
        }
    }

}
