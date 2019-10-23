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
    public final static String QUEUE_NAME = "updateTbl";

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

    public final static HashMap<String,String> config = new HashMap(10);
    /**
     * 客户授权MQ
     */
    private final static String EXCHANGE_USER_AUTH_NAME = "usersauth";

    static{
        log.info("host:" + SysParamUtil.getParam("rabbitmq.host"));
        log.info("port:" + SysParamUtil.getParam("rabbitmq.port"));
        config.put("host",SysParamUtil.getParam("rabbitmq.host"));
        config.put("port",SysParamUtil.getParam("rabbitmq.port"));
        config.put("username",SysParamUtil.getParam("rabbitmq.username"));
        config.put("password",SysParamUtil.getParam("rabbitmq.password"));
    }

    /**
     * @deprecated
     *
     */
    public SendMQ()  {
    }

    /**
     * @deprecated
     */
    public void closeConn() {

    }

    /**
     * 通过消息队列更新线上数据  更新线上商品上下线状态信息及低库存标志字段更新的消息队列
     * @param model 其中type为1 2值的更新低库存标志，4值的更新上下线状态信息
     * @throws Exception
     */
    public static void sendMsg(UpdateTblModel model) {

        sendMsg(JSONObject.fromObject(model).toString());
    }

    /**
     * 通过消息队列更新线上数据 直接执行对应sql
     * @param model 其中type为2 直接执行对应sql
     * @throws Exception
     */
    public static void sendMsg(RunSqlModel model)  {
        sendMsg(JSONObject.fromObject(model).toString());
    }

    public static void sendMsg(String data) {

        try (RPCClient rpcClient = new RPCClient()) {
            log.info(" [x] Sent [{}]" , data);
            rpcClient.callNoReturn(data);
            log.info(" [x] Sent [{}] is OK" , data);
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("MQ sendMsg",e);
        }

    }
    
    /**
     * 通过消息 发送折扣卷json数据到线上
     * @throws Exception
     */
    public void sendCouponMsg(String couponJson, int website) throws Exception {
        Channel channel=getChannel();
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

        closeChannel(channel);
    }

    private Channel getChannel(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.get("host"));
        factory.setPort(Integer.parseInt(config.get("port")));
        factory.setUsername(config.get("username"));
        factory.setPassword(config.get("password"));
        try {
            Channel channel = factory.newConnection().createChannel();
            return channel;
        } catch (Exception e) {
            log.error("getChannel",e);
            return null;
        }
    }

    public void closeChannel(Channel channel) {
        if(channel!=null) {
            try {
                channel.close();
            } catch (IOException e) {
                log.error("closeChannel",e);
            } catch (TimeoutException e) {
                log.error("closeChannel",e);
            }
        }
        if(channel.getConnection()!=null){
            try {
                channel.getConnection().close();
            } catch (IOException e) {
                log.error("closeChannel",e);
            }
        }
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

    /**
     * 通过消息队列更新线上数据 直接执行对应sql集合 批量带事务
     * @param model 需要执行的sql对象集合 (type=3)
     *      {"type":"3","sqls":["insert into test values(1);","insert into test values(2);"]}
     * @throws Exception
     */
    public void sendMsg(RunBatchSqlModel model) throws Exception {
        Channel channel=getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        JSONObject jsonObject = JSONObject.fromObject(model);
        System.out.println(jsonObject.toString().getBytes("UTF-8"));

        channel.basicPublish("", QUEUE_NAME, null, jsonObject.toString().getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + jsonObject.toString() + "'");
        closeChannel(channel);
    }

    /**
     * 授权的，仅限import
     * @param json
     * @throws Exception
     */
    private void sendAuthorizationFlagStr(String json) throws Exception{
        Channel channel=getChannel();
        channel.exchangeDeclare(EXCHANGE_USER_AUTH_NAME,"fanout",true);
        channel.basicPublish(EXCHANGE_USER_AUTH_NAME, "", null, json.getBytes("UTF-8"));

        // channel.queueDeclare(QUEUE_USER_AUTH_NAME, false, false, false, null);
        // channel.basicPublish("", QUEUE_USER_AUTH_NAME, null, json.getBytes("UTF-8"));
        System.err.println(" [x] Sent '" + json + "'");
        closeChannel(channel);
    }


    public static void sendMqSql(RunBatchSqlModel model) {
        SendMQ sendMQ = null;
        try {
            sendMQ = new SendMQ();
            sendMQ.sendMsg(model);
        } catch (Exception e){
            throw new RuntimeException("sendMQ exception!");
        } finally {
            if (null != sendMQ){
                try {
                    sendMQ.closeConn();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 授权使用MQ
     * @param userId
     * @param flag
     */
    public static void sendAuthorizationFlagMqSql(int userId, int flag) {
        SendMQ sendMQ = null;
        try {
            JSONObject json = new JSONObject();
            json.put("userid",String.valueOf(userId));
            // 1是添加，2是删除
            json.put("type",flag > 0 ? "1" : "2");
            sendMQ = new SendMQ();
            sendMQ.sendAuthorizationFlagStr(json.toString());
        } catch (Exception e){
            e.printStackTrace();
            // throw new RuntimeException("sendMQ exception!");
        } finally {
            if (null != sendMQ){
                try {
                    sendMQ.closeConn();
                } catch (Exception e) {
                }
            }
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
    }

    private void sendMessageStr(String json, int website) throws Exception{
        Channel channel=getChannel();
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
        closeChannel(channel);
    }

    /**
     * RPC方式调用MQ（有返回值）
     * 不需要关闭mq连接（mq执行完成后自动释放资源）
     * @param model
     * @return
     */
    public static String sendMsgByRPC(RunSqlModel model)  {
        try (RPCClient rpcClient = new RPCClient()) {

            JSONObject jsonObject = JSONObject.fromObject(model);
            log.info(" [x] Sent [{}]" , jsonObject);
            String response = rpcClient.call(jsonObject.toString());
            log.info(" [x] response [{}]" , response );
            return response;
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("sendMsgByRPC",e);
            return  null;
        }
    }

}