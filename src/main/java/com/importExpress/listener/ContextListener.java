package com.importExpress.listener;

import com.cbt.bean.CategoryBean;
import com.cbt.service.MongoGoodsService;
import com.cbt.service.impl.MongoGoodsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.listener
 * @date:2019/11/27
 */
public class ContextListener implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger(ContextListener.class);
    public static List<CategoryBean> categoryBeanList = null;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {


        try {
            WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
            MongoGoodsService mongoGoodsService = springContext.getBean(MongoGoodsServiceImpl.class);
            categoryBeanList = mongoGoodsService.queryAllCategoryBean();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("contextInitialized error:", e);
        }
    }

    public static List<CategoryBean> getCopyList(){
        return new ArrayList<>(categoryBeanList);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
