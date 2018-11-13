package com.importExpress.utli;

import com.cbt.util.SysParamUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author luohao
 * @date 2018/10/10
 */
public class NewSendMQ {

	private static final Log logger = LogFactory.getLog(NewSendMQ.class);

	/**  优惠卷json数据*/
	private final static String RECOMMEND_NAME = "recommend";

	private final static HashMap<String,String> config = new HashMap();

	private Connection connection;
	private Channel channel;

	static{
		config.put("NEW_URL",SysParamUtil.getParam("rabbitmq.new_url"));
	}

	public NewSendMQ()  {
		ConnectionFactory factory = new ConnectionFactory();
		try {
			factory.setUri(config.get("NEW_URL"));
			connection= factory.newConnection();
			channel = connection.createChannel();
		} catch (Exception e) {
			logger.error("NewSendMQ",e);
		}
	}


	/**
	 * 通过消息将新购物车推荐商品数据传到线上
	 * @param recommendJson
	 * @throws Exception
	 */
	public void sendRecommend(String recommendJson) throws Exception {
		channel.exchangeDeclare(RECOMMEND_NAME, BuiltinExchangeType.DIRECT);
		channel.basicPublish(RECOMMEND_NAME,"",null,recommendJson.getBytes("UTF-8"));
		logger.info(" [x] Sent '" + recommendJson);
	}


	public void closeConn() {
		if(channel!=null) {
			try {
				channel.close();
			} catch (Exception e) {
				logger.error("closeConn",e);
			}
		}
		if(connection!=null){
			try {
				connection.close();
			} catch (IOException e) {
				logger.error("closeConn",e);
			}
		}
	}



}