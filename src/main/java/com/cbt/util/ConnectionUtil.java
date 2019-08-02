package com.cbt.util;


import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {
        /**
         * 建立与RabbitMQ的连接
         * @return
         * @throws Exception
         */
        public static Connection getConnection() throws Exception {
            //定义连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //设置服务地址
            factory.setHost(SysParamUtil.getParam("rabbitmq.host"));
            //端口
            factory.setPort(Integer.parseInt(SysParamUtil.getParam("rabbitmq.port")));
            //设置账号信息，用户名、密码、vhost
//            factory.setVirtualHost("/"SysParamUtil.getParam("rabbitmq.username"));
            factory.setUsername(SysParamUtil.getParam("rabbitmq.username"));
            factory.setPassword(SysParamUtil.getParam("rabbitmq.password"));
            // 通过工程获取连接
            Connection connection = factory.newConnection();
            return connection;
        }
    }