package com.importExpress.utli;

import com.alibaba.fastjson.JSONObject;
import com.cbt.mq.RPCClient;
import com.cbt.util.SysParamUtil;
import com.importExpress.mail.MailTemplateBean;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author luohao
 * @date 2018/6/25
 */
@Slf4j
public class SendMQ {

    /**
     * 直接执行的sql 及 下架
     */
    public final static String QUEUE_NAME = "updateTbl";

    /**
     * 直接执行的sql带返回值
     */
    public final static String QUEUE_NAME_RPC = "updateTbl_rpc";
    public final static HashMap<String, String> config = new HashMap(10);
    /**
     * 优惠卷json数据
     */
    private final static String COUPON_NAME = "coupon"; //发送优惠卷到线上 （mq 连接27更新线上，连接98更新153）
    private final static String COUPON_NAME_KIDS = "coupon_kids"; //发送优惠卷到kids线上 （mq 连接27更新线上，连接98更新153）
    private final static String COUPON_NAME_PETS = "coupon_pets"; //发送优惠卷到pet线上 （mq 连接27更新线上，连接98更新153）
    /**
     * 优惠卷json数据
     */
    private final static String RECOMMEND_NAME = "recommend";
    private final static String QUEUE_REDIS_NAME = "redis";
    private final static String QUEUE_REDIS_NAME_KIDS = "redis_kids";
    private final static String QUEUE_REDIS_NAME_PETS = "redis_pets";
    /**
     * 客户授权MQ
     */
    private final static String EXCHANGE_USER_AUTH_NAME = "usersauth";

    /**
     * 退款RPC
     */
    public final static String REFUND_RPC = "refund_rpc";

    static {
        log.info("host:" + SysParamUtil.getParam("rabbitmq.host"));
        log.info("port:" + SysParamUtil.getParam("rabbitmq.port"));
        config.put("host", SysParamUtil.getParam("rabbitmq.host"));
        config.put("port", SysParamUtil.getParam("rabbitmq.port"));
        config.put("username", SysParamUtil.getParam("rabbitmq.username"));
        config.put("password", SysParamUtil.getParam("rabbitmq.password"));
    }

    private SendMQ() {
    }

    /**
     * 通过消息队列更新线上数据  更新线上商品上下线状态信息及低库存标志字段更新的消息队列
     *
     * @param model 其中type为1 2值的更新低库存标志，4值的更新上下线状态信息
     * @throws Exception
     */
    public static void sendMsg(UpdateTblModel model) {

        sendMsg(JSONObject.toJSONString(model));
    }

    public static void sendMsg(String data) {

        try (RPCClient rpcClient = new RPCClient()) {
            System.err.println(data);
            log.info(" [x] Sent [{}]", data);
            rpcClient.callNoReturn(data);
            log.info(" [x] Sent [{}] is OK", data);
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("MQ sendMsg", e);
        }

    }

    /**
     * 通过消息队列更新线上数据 直接执行对应sql
     *
     * @param model 其中type为2 直接执行对应sql
     * @throws Exception
     */
    public static void sendMsg(RunSqlModel model) {
        sendMsg(JSONObject.toJSONString(model));
    }

    /**
     * 通过消息 发送折扣卷json数据到线上
     *
     * @throws Exception
     */
    public static void sendCouponMsg(String couponJson, int website) throws Exception {
        Channel channel = getChannel();
        if (website == 3) {
            channel.queueDeclare(COUPON_NAME_PETS, false, false, false, null);
            channel.basicPublish("", COUPON_NAME_PETS, null, couponJson.getBytes("UTF-8"));
        } else if (website == 2) {
            channel.queueDeclare(COUPON_NAME_KIDS, false, false, false, null);
            channel.basicPublish("", COUPON_NAME_KIDS, null, couponJson.getBytes("UTF-8"));
        } else if (website == 1) {
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

    public static String repCha(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str.replaceAll("\'", "\\\\\'").replaceAll("\"", "\\\\\"");
    }

    public static void sendMqSql(RunBatchSqlModel model) {

        try {

            SendMQ.sendMsg(model);
        } catch (Exception e) {
            throw new RuntimeException("sendMQ exception!");
        }
    }

    /**
     * 通过消息队列更新线上数据 直接执行对应sql集合 批量带事务
     *
     * @param model 需要执行的sql对象集合 (type=3)
     *              {"type":"3","sqls":["insert into test values(1);","insert into test values(2);"]}
     * @throws Exception
     */
    public static void sendMsg(RunBatchSqlModel model) throws Exception {
        Channel channel = getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String jsonStr = JSONObject.toJSONString(model);
        System.out.println(jsonStr.getBytes("UTF-8"));

        channel.basicPublish("", QUEUE_NAME, null, jsonStr.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + jsonStr + "'");
        closeChannel(channel);
    }

    /**
     * 授权使用MQ
     *
     * @param userId
     * @param flag
     */
    public static void sendAuthorizationFlagMqSql(Channel channel,int userId, int flag) {
        try {
            JSONObject json = new JSONObject();
            json.put("userid", String.valueOf(userId));
            // 1是添加，2是删除
            json.put("type", flag > 0 ? "1" : "2");
            SendMQ.sendAuthorizationFlagStr(channel,json.toString());
        } catch (Exception e) {
            log.error("sendMsgByRPC", e);
        }
    }

    /**
     * 授权的，仅限import
     *
     * @param json
     * @throws Exception
     */
    private static void sendAuthorizationFlagStr(Channel channel,String json) throws Exception {
        channel.exchangeDeclare(EXCHANGE_USER_AUTH_NAME, "fanout", true);
        channel.basicPublish(EXCHANGE_USER_AUTH_NAME, "", null, json.getBytes("UTF-8"));
        System.err.println(EXCHANGE_USER_AUTH_NAME + ":" + json);
    }


    /**
     * RPC方式调用MQ（有返回值）
     * 不需要关闭mq连接（mq执行完成后自动释放资源）
     *
     * @param model
     * @return
     */
    public static String sendMsgByRPC(RunSqlModel model) {
        try (RPCClient rpcClient = new RPCClient()) {

            log.info(" [x] Sent [{}]", model);
            System.err.println("[x] Sent" + model);
            String response = rpcClient.call(JSONObject.toJSONString(model));
            log.info(" [x] response [{}]", response);
            return response;
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("sendMsgByRPC", e);
            return null;
        }
    }

    /**
     * redis客户数据清空
     *
     * @param model type=2是，输入userid为数组，如：[11,222,333]
     * @throws Exception
     */
    public static void sendMsg(RedisModel model, int website) throws Exception {
        sendMessageStr(JSONObject.toJSONString(model), website);
    }

    public static void sendMessageStr(String json, int website) throws Exception {
        Channel channel = getChannel();
        if (website == 0) {
            channel.queueDeclare(QUEUE_REDIS_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_REDIS_NAME, null, json.getBytes("UTF-8"));
        } else if (website == 1) {
            channel.queueDeclare(QUEUE_REDIS_NAME_KIDS, false, false, false, null);
            channel.basicPublish("", QUEUE_REDIS_NAME_KIDS, null, json.getBytes("UTF-8"));
        } else if (website == 2) {
            channel.queueDeclare(QUEUE_REDIS_NAME_PETS, false, false, false, null);
            channel.basicPublish("", QUEUE_REDIS_NAME_PETS, null, json.getBytes("UTF-8"));
        }
        System.err.println(" [x] Sent '" + json + "'");
        log.info(" [x] Sent '" + json + "'");
        closeChannel(channel);
    }

    public static Channel getChannel() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.get("host"));
        factory.setPort(Integer.parseInt(config.get("port")));
        factory.setUsername(config.get("username"));
        factory.setPassword(config.get("password"));
        try {
            Channel channel = factory.newConnection().createChannel();
            return channel;
        } catch (Exception e) {
            log.error("getChannel", e);
            return null;
        }
    }

    public static void closeChannel(Channel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                log.error("closeChannel", e);
            } catch (TimeoutException e) {
                log.error("closeChannel", e);
            }
        }
        if (channel.getConnection() != null) {
            try {
                channel.getConnection().close();
            } catch (IOException e) {
                log.error("closeChannel", e);
            }
        }
    }

    /**
     * 更新redis数据
     *
     * @param model type=4
     * @throws Exception
     */
    public static void sendMsg(JSONObject model, int website) throws Exception {
        sendMessageStr(model.toString(), website);
    }


    /**
     * 通过RPC方式调用MQ，进行退款（有返回值）
     * 不需要关闭mq连接（mq执行完成后自动释放资源）
     *
     * @param jsonParam
     * @return
     */
    public static String sendRefundByRPC(com.alibaba.fastjson.JSONObject jsonParam) {
        try (RPCClient rpcClient = new RPCClient()) {

            log.info(" [x] Sent [{}]", jsonParam);
            System.err.println("[x] Sent" + jsonParam);
            String response = rpcClient.refundCall(jsonParam.toString());
            log.info(" [x] response [{}]", response);
            return response;
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("sendMsgByRPC", e);
            return null;
        }
    }

    public static void sendMail(MailTemplateBean mailTemplateBean) throws Exception {
        Channel channel = getChannel();
        channel.queueDeclare("mail", true, false, false, null);
        String jsonStr = JSONObject.toJSONString(mailTemplateBean);
        System.out.println(jsonStr.getBytes("UTF-8"));

        channel.basicPublish("", "mail", null, jsonStr.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + jsonStr + "'");
        closeChannel(channel);
    }

}