package com.importExpress.utli;

import com.cbt.util.SysParamUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author luohao
 * @date 2018/6/25
 */
public class SendMQ {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SendMQ.class);

    /** 直接执行的sql 及 下架*/
    private final static String QUEUE_NAME = "updateTbl";

    /** 优惠卷json数据*/
    private final static String COUPON_NAME = "coupon"; //发送优惠卷到线上 （mq 连接27更新线上，连接98更新153）
//    private final static String COUPON_NAME = "coupon2"; //发送优惠卷信息到镜像服 mq 连接27
    private final static String COUPON_NAME_KIDS = "coupon_kids"; //发送优惠卷到kids线上 （mq 连接27更新线上，连接98更新153）

    /**  优惠卷json数据*/
    private final static String RECOMMEND_NAME = "recommend";

    private final static String QUEUE_REDIS_NAME = "redis";
    private final static String QUEUE_REDIS_NAME_KIDS = "redis_kids";
    private static long totalConnect = 0;
    private static long totalDisConnect = 0;

    private final static HashMap<String,String> config = new HashMap(10);

    private Connection connection;
    private Channel channel;

    static{
        System.err.println("host:" + SysParamUtil.getParam("rabbitmq.host"));
        System.err.println("port:" + SysParamUtil.getParam("rabbitmq.port"));
        System.err.println("username:" + SysParamUtil.getParam("rabbitmq.username"));
        System.err.println("password:" + SysParamUtil.getParam("rabbitmq.password"));
        config.put("host",SysParamUtil.getParam("rabbitmq.host"));
        config.put("port",SysParamUtil.getParam("rabbitmq.port"));
        config.put("username",SysParamUtil.getParam("rabbitmq.username"));
        config.put("password",SysParamUtil.getParam("rabbitmq.password"));
    }

    public SendMQ() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.get("host"));
        factory.setPort(Integer.parseInt(config.get("port")));
        factory.setUsername(config.get("username"));
        factory.setPassword(config.get("password"));
        connection= factory.newConnection();
        channel = connection.createChannel();
        ++totalConnect;

        logger.info("取得MQ 返回总数/获取总数：" + totalDisConnect + "/" + totalConnect);
    }

    public void closeConn() {
        if(channel!=null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        if(connection!=null){
            try {
                connection.close();
                --totalDisConnect;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过消息队列更新线上数据  更新线上商品上下线状态信息及低库存标志字段更新的消息队列
     * @param model 其中type为1 2值的更新低库存标志，4值的更新上下线状态信息
     * @throws Exception
     */
    public void sendMsg(UpdateTblModel model) throws Exception {
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        JSONObject jsonObject = JSONObject.fromObject(model);
        channel.basicPublish("", QUEUE_NAME, null, jsonObject.toString().getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + jsonObject.toString() + "'");
    }

    /**
     * 通过消息队列更新线上数据 直接执行对应sql
     * @param model 其中type为2 直接执行对应sql
     * @throws Exception
     */
    public void sendMsg(RunSqlModel model) throws Exception {
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        JSONObject jsonObject = JSONObject.fromObject(model);
        channel.basicPublish("", QUEUE_NAME, null, jsonObject.toString().getBytes("UTF-8"));
        System.err.println(" [x] Sent '" + jsonObject.toString() + "'");
    }
    
    /**
     * 通过消息 发送折扣卷json数据到线上
     * @throws Exception
     */
    public void sendCouponMsg(String couponJson, int website) throws Exception {
        if(website == 2){
            channel.queueDeclare(COUPON_NAME_KIDS, false, false, false, null);
            channel.basicPublish("", COUPON_NAME_KIDS, null, couponJson.getBytes("UTF-8"));
        } else if (website == 1){
            channel.queueDeclare(COUPON_NAME, false, false, false, null);
            channel.basicPublish("", COUPON_NAME, null, couponJson.getBytes("UTF-8"));
        } else if (website == 0) {
            sendCouponMsg(couponJson, 1);
            sendCouponMsg(couponJson, 2);
        }
    	System.err.println("Site=" + website + " [x] Sent '" + couponJson + "'");
    }

    /**
     * 通过消息将新购物车推荐商品数据传到线上
     * @param recommendJson
     * @throws Exception
     */
    public void sendRecommend(String recommendJson) throws Exception {
        channel.exchangeDeclare(RECOMMEND_NAME, BuiltinExchangeType.FANOUT);
        channel.basicPublish(RECOMMEND_NAME,"",null,recommendJson.getBytes("UTF-8"));
//        channel.queueDeclare(RECOMMEND_NAME, false, false, false, null);
//        channel.basicPublish("", RECOMMEND_NAME, null, recommendJson.getBytes("UTF-8"));
        System.err.println(" [x] Sent '" + recommendJson + "'");
    }

    public static String repCha(String str){
    	if (StringUtils.isBlank(str)) {
			return "";
		}
    	return str.replaceAll("\'", "\\\\\'").replaceAll("\"", "\\\\\"");
    }

    /**
     * redis客户数据清空
     * @param model  type=2是，输入userid为数组，如：[11,222,333]
     * @throws Exception
     */
    public void sendMsg(RedisModel model, int website) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(model);
        sendMessageStr( jsonObject.toString(), website);
    }

    /**
     * 更新redis数据
     * @param model type=4
     * @throws Exception
     */
    public void sendMsg(JSONObject model, int website) throws Exception {
        sendMessageStr( model.toString(), website);
    }

    private void sendMessageStr(String json, int website) throws Exception{
        if (website > 0) {
            channel.queueDeclare(QUEUE_REDIS_NAME_KIDS, false, false, false, null);
            channel.basicPublish("", QUEUE_REDIS_NAME_KIDS, null, json.getBytes("UTF-8"));
        } else {
            channel.queueDeclare(QUEUE_REDIS_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_REDIS_NAME, null, json.getBytes("UTF-8"));
        }
        System.err.println(" [x] Sent '" + json + "'");
    }


    public static void main(String[] argv) throws Exception {
        SendMQ sendMQ = new SendMQ();
        // 直接执行sql示例
        String sql = "INSERT INTO shipping_package (shipmentno,orderid,remarks,createtime) VALUES('0001','O4051803889279563','O4051803889279563',now())" +
                "on duplicate key update shipmentno ='0001',createtime =now(),remarks ='O4051803889279563'";

        sendMQ.sendMsg(new RunSqlModel(sql));
        // sendMQ.sendMsg(new RunSqlModel(sql1));
        //执行sql并保存记录，对应可以注入
//    	SendMQServiceImpl sendMQ = new SendMQServiceImpl();
//    	sendMQ.runSqlOnline("543232153010", sql);


        //redis示例
//        sendMQ.sendMsg(new RedisModel(new String[]{"15937"}), 1);


        sendMQ.closeConn();
    	
    }
    
}