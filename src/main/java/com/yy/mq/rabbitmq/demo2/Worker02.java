package com.yy.mq.rabbitmq.demo2;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 上午11:15 on 18-3-14.
 * @Modified By:
 */
public class Worker02 {

//    private final static String QUEUE_NAME = "task";
    private final static String QUEUE_NAME = "test_queue_fanout_1";

    private final static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        //建立连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置rabbitmq地址
        connectionFactory.setHost("localhost");
        //建立连接
        Connection connection = connectionFactory.newConnection();
        //创建channel
        Channel channel = connection.createChannel();

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "delete");
        //声明队列
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        System.out.println("C [*] Waiting for messages. To exit press CTRL+C");
        channel.basicQos(1);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Worker02 [x] Received '" + message + "'");
                try {
                    doWork(message);
                } finally {
                    System.out.println("Worker1 [x] Done");
                    // 消息处理完成确认
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }

    public static void doWork(String s){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
