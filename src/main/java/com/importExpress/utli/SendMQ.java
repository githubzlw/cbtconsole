package com.importExpress.utli;

import com.cbt.mq.RPCClient;
import com.cbt.util.SysParamUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SendMQ {

    /** 直接执行的sql 及 下架*/
    private final static String QUEUE_NAME = "updateTbl";

    /** 直接执行的sql带返回值*/
    public final static String QUEUE_NAME_RPC = "updateTbl_rpc";



    /** 优惠卷json数据*/
    private final static String COUPON_NAME = "coupon"; //发送优惠卷到线上 （mq 连接27更新线上，连接98更新153）
//    private final static String COUPON_NAME = "coupon2"; //发送优惠卷信息到镜像服 mq 连接27
    private final static String COUPON_NAME_KIDS = "coupon_kids"; //发送优惠卷到kids线上 （mq 连接27更新线上，连接98更新153）
    private final static String COUPON_NAME_PETS = "coupon_pets"; //发送优惠卷到pet线上 （mq 连接27更新线上，连接98更新153）

    /**  优惠卷json数据*/
    private final static String RECOMMEND_NAME = "recommend";

    private final static String QUEUE_REDIS_NAME = "redis";
    private final static String QUEUE_REDIS_NAME_KIDS = "redis_kids";
    private final static String QUEUE_REDIS_NAME_PETS = "redis_pets";
    private static long totalConnect = 0;
    private static long totalDisConnect = 0;

    public final static HashMap<String,String> config = new HashMap(10);

    private Connection connection;
    private Channel channel;

    static{
        log.info("host:" + SysParamUtil.getParam("rabbitmq.host"));
        log.info("port:" + SysParamUtil.getParam("rabbitmq.port"));
        config.put("host",SysParamUtil.getParam("rabbitmq.host"));
        config.put("port",SysParamUtil.getParam("rabbitmq.port"));
        config.put("username",SysParamUtil.getParam("rabbitmq.username"));
        config.put("password",SysParamUtil.getParam("rabbitmq.password"));
    }

    public SendMQ()  {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.get("host"));
        factory.setPort(Integer.parseInt(config.get("port")));
        factory.setUsername(config.get("username"));
        factory.setPassword(config.get("password"));
        try {
            connection= factory.newConnection();
            channel = connection.createChannel();
            ++totalConnect;
        } catch (Exception e) {
           log.error("SendMQ",e);
        }

        log.info("取得MQ 返回总数/获取总数：" + totalDisConnect + "/" + totalConnect);
    }

    public void closeConn() {
        if(channel!=null) {
            try {
                channel.close();
            } catch (IOException e) {
                log.error("closeConn",e);
            } catch (TimeoutException e) {
                log.error("closeConn",e);
            }
        }
        if(connection!=null){
            try {
                connection.close();
                --totalDisConnect;
            } catch (IOException e) {
                log.error("closeConn",e);
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
        log.info(" [x] Sent '" + jsonObject.toString() + "'");
    }

    /**
     * 通过消息队列更新线上数据 直接执行对应sql
     * @param model 其中type为2 直接执行对应sql
     * @throws Exception
     */
    public void sendMsg(RunSqlModel model)  {
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            JSONObject jsonObject = JSONObject.fromObject(model);
            channel.basicPublish("", QUEUE_NAME, null, jsonObject.toString().getBytes("UTF-8"));
            log.info(" [x] Sent '" + jsonObject.toString() + "'");
        } catch (IOException e) {
            log.error("sendMsg",e);
        }
    }
    
    /**
     * 通过消息 发送折扣卷json数据到线上
     * @throws Exception
     */
    public void sendCouponMsg(String couponJson, int website) throws Exception {
        if (website == 3) {
            channel.queueDeclare(COUPON_NAME_PETS, false, false, false, null);
            channel.basicPublish("", COUPON_NAME_PETS, null, couponJson.getBytes("UTF-8"));
        } else if(website == 2){
            channel.queueDeclare(COUPON_NAME_KIDS, false, false, false, null);
            channel.basicPublish("", COUPON_NAME_KIDS, null, couponJson.getBytes("UTF-8"));
        } else if (website == 1){
            channel.queueDeclare(COUPON_NAME, false, false, false, null);
            channel.basicPublish("", COUPON_NAME, null, couponJson.getBytes("UTF-8"));
        } else if (website == 0) {
            sendCouponMsg(couponJson, 1);
            sendCouponMsg(couponJson, 2);
            sendCouponMsg(couponJson, 3);
        }
        log.info("Site=" + website + " [x] Sent '" + couponJson + "'");
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
        log.info(" [x] Sent '" + recommendJson + "'");
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
        if (website == 0) {
            channel.queueDeclare(QUEUE_REDIS_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_REDIS_NAME, null, json.getBytes("UTF-8"));
        }else if (website == 1) {
            channel.queueDeclare(QUEUE_REDIS_NAME_KIDS, false, false, false, null);
            channel.basicPublish("", QUEUE_REDIS_NAME_KIDS, null, json.getBytes("UTF-8"));
        }else if (website == 2) {
            channel.queueDeclare(QUEUE_REDIS_NAME_PETS, false, false, false, null);
            channel.basicPublish("", QUEUE_REDIS_NAME_PETS, null, json.getBytes("UTF-8"));
        }
        log.info(" [x] Sent '" + json + "'");
    }

    /**
     * RPC方式调用MQ（有返回值）
     * 不需要关闭mq连接（mq执行完成后自动释放资源）
     * @param model
     * @return
     */
    public static String sendMsgByRPC(RunSqlModel model)  {
        try (RPCClient fibonacciRpc = new RPCClient()) {

            JSONObject jsonObject = JSONObject.fromObject(model);
            log.info(" [x] Sent [{}]" , jsonObject);
            String response = fibonacciRpc.call(jsonObject.toString());
            log.info(" [x] response [{}]" , response );
            return response;
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("sendMsgByRPC",e);
            return  null;
        }
    }

    public static void main(String[] argv) throws Exception {
        SendMQ sendMQ = new SendMQ();
        /*// 直接执行sql示例
        String sql = "INSERT INTO shipping_package (shipmentno,orderid,remarks,createtime) VALUES('0001','O4051803889279563','O4051803889279563',now())" +
                "on duplicate key update shipmentno ='0001',createtime =now(),remarks ='O4051803889279563'";

        sendMQ.sendMsg(new RunSqlModel(sql));*/
        // sendMQ.sendMsg(new RunSqlModel(sql1));
        //执行sql并保存记录，对应可以注入
//    	SendMQServiceImpl sendMQ = new SendMQServiceImpl();
//    	sendMQ.runSqlOnline("543232153010", sql);


        //redis示例
//        sendMQ.sendMsg(new RedisModel(new String[]{"15937"}), 1);


        String[] userIds = {"13895"};
        RedisModel redisModel = new RedisModel(userIds);
        redisModel.setType("3");
        sendMQ.sendMsg(redisModel, 1);
        sendMQ.closeConn();
    	
    }
    
}