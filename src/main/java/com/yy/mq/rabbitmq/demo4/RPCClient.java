package com.yy.mq.rabbitmq.demo4;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 下午1:12 on 18-3-15.
 * @Modified By:
 */
public class RPCClient {

    private final static String QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        RPCClient fibonacciRpc = new RPCClient();
        System.out.println(" [x] Requesting fib(30)");
        String response = fibonacciRpc.call("30");
        System.out.println(" [.] Got '" + response + "'");
    }


    public  String call(String message) throws IOException, TimeoutException, InterruptedException {

        //建立连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置rabbitmq地址
        connectionFactory.setHost("localhost");
        //建立连接
        Connection connection = connectionFactory.newConnection();
        //创建channel
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String replyQueueName = channel.queueDeclare().getQueue();

        String corrId = UUID.randomUUID().toString();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, message.getBytes("UTF-8"));

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body, "UTF-8"));
                }
            }
        });

        return response.take();
    }
}