package com.cbt.common.dynamics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据源切换选择器
 * 
 */
public class DataSourceSelector {

    private final static ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    private static final Log logger = LogFactory.getLog(DataSourceSelector.class);

    /**
     *    默认数据源
     */
    private final static String DEFAULT_SOURCE = "localDataSource";

    /**
     * 还原数据源
     * 
     */
    public static void restore() {
        contextHolder.set(DEFAULT_SOURCE);
        logger.info("$$$切换数据源:"+contextHolder.get());
    }

    /**
     * 设置数据源
     * 
     * @param source
     */
    public static void set(String source) {
        contextHolder.set(source);
        logger.info("$$$切换数据源:"+contextHolder.get());
    }

    /**
     * 获取数据源
     * 
     * @return String
     */
    public static String get() {
        String tmp = contextHolder.get();
        logger.info("$$$取出数据源:" + (tmp==null?DEFAULT_SOURCE:tmp));
        return tmp;
    }

    /**
     * 清空数据源
     */
    public static void clear() {
        logger.info("$$$清除数据源！");
        contextHolder.remove();
    }

}
