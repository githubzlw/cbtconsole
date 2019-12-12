package com.cbt.mq;

import com.importExpress.utli.SendMQ;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.importExpress.utli.SendMQ.*;

@Slf4j
public class RPCClient implements AutoCloseable {

    private Connection connection;
    private Channel channel;


    public RPCClient() throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(SendMQ.config.get("host"));
        factory.setPort(Integer.parseInt(SendMQ.config.get("port")));
        factory.setUsername(SendMQ.config.get("username"));
        factory.setPassword(SendMQ.config.get("password"));

        System.err.println("host:" + SendMQ.config.get("host"));
        System.err.println("port:" + SendMQ.config.get("port"));
        System.err.println("username:" + SendMQ.config.get("username"));
        System.err.println("password:" + SendMQ.config.get("password"));
        connection = factory.newConnection();
        channel = connection.createChannel();
    }


    public String call(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME_RPC, props, message.getBytes("UTF-8"));

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        //超过指定时间后强制结束
        String result = response.poll(60, TimeUnit.SECONDS);
        channel.basicCancel(ctag);
        return result;
    }


    public String refundCall(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", SendMQ.REFUND_RPC, props, message.getBytes("UTF-8"));

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        //超过指定时间后强制结束
        String result = response.poll(60, TimeUnit.SECONDS);
        channel.basicCancel(ctag);
        return result;
    }

    public void callNoReturn(String message) throws IOException, InterruptedException {

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }

    public static void main(String[] argv) {
        try (RPCClient fibonacciRpc = new RPCClient()) {
            for (int i = 0; i < 32; i++) {
                String i_str = Integer.toString(i);
                System.out.println(" [x] Requesting fib(" + i_str + ")");
                String response = fibonacciRpc.call(i_str);
                System.out.println(" [.] Got '" + response + "'");
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("main",e);
        }
    }
}

