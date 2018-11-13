package com.importExpress.controller;

import com.importExpress.mail.SendMailFactory;
import com.importExpress.utli.MongoDBHelp;
import com.rabbitmq.client.*;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;

//@Component("BeanDefineConfigue")
public class BeanDefineConfigue implements
        ApplicationListener<ContextRefreshedEvent> {

 //@Autowired
 private SendMailFactory sendMailFactory;

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BeanDefineConfigue.class);

    /**  Queue Name*/
    private final static String QNAME_MAIL = "mail";
    private final static String QNAME_WEBHOOK = "webhook_test";

    /**
     * 当一个ApplicationContext被初始化或刷新触发
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        ReceiveMailMQ();
        ReceiveWebHookMQ();
//        //test sample
//        Map<String, String> model =new HashMap<>();
//        model.put("name", "Import Express");
//        sendMailFactory.sendMail("luohao518@163.com", "", "222Amazon SES test (SMTP interface accessed using Java)", model,TemplateType.welcome);;
    }


    public void ReceiveMailMQ()  {

        Connection connection=null;
        Channel channel=null ;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri("amqp://mail:mail789@192.168.1.27:5672/mail");
            factory.setConnectionTimeout(10000);
            factory.setNetworkRecoveryInterval(10000);

            connection= factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QNAME_MAIL, false, false, false, null);
            logger.info(" [*] Waiting for mail MQ messages.");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    logger.info(" [x] Received '" + message + "'");
                }
            };
            channel.basicConsume(QNAME_MAIL, true, consumer);

        } catch (Exception e) {
            logger.error("ReceiveMailMQ" ,e);
        }
    }

    public void ReceiveWebHookMQ()  {

        Connection connection=null;
        Channel channel=null ;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri("amqp://webhook:webhook456@192.168.1.27:5672/webhook");
            connection= factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(QNAME_WEBHOOK, false, false, false, null);
            logger.info(" [*] Waiting for webhook MQ messages.");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    logger.info(" [x] Received '" + message + "'");
                    MongoDBHelp.INSTANCE.insert(QNAME_WEBHOOK,message);
                }
            };
            channel.basicConsume(QNAME_WEBHOOK, true, consumer);

        } catch (Exception e) {
            logger.error("ReceiveWebHookMQ" ,e);
        }
    }

}