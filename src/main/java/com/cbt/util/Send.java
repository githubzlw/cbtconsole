package com.cbt.util;

import com.cbt.pojo.Inventory;
import com.cbt.warehouse.pojo.SampleOrderBean;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Send {

        private final static String QUEUE_NAME = "deliver_queue";

        public  Boolean deliverMqSend(List<SampleOrderBean> message) {
            // 获取到连接
            Connection connection = null;
            try {
                System.out.println(SysParamUtil.getParam("rabbitmq.host"));
//                JSONArray jsonArray = JSONArray.fromObject(message);
                connection = ConnectionUtil.getConnection();
                // 从连接中创建通道，使用通道才能完成消息相关的操作
                Channel channel = connection.createChannel();
                // 声明（创建）队列
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                // 向指定的队列中发送消息
                channel.basicPublish("", QUEUE_NAME, null, message.toString().getBytes());
                System.out.println(" [x] Sent '" + message + "'");
                //关闭通道和连接
                channel.close();
                connection.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

    }