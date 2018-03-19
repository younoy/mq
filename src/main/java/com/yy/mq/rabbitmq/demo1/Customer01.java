package com.yy.mq.rabbitmq.demo1;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 上午11:15 on 18-3-14.
 * @Modified By:
 */
public class Customer01 {

    private final static String QUEUE_NAME = "hello4";

    public static void main(String[] args) throws IOException, TimeoutException {

        //建立连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置rabbitmq地址
        connectionFactory.setHost("localhost");
        //建立连接
        Connection connection = connectionFactory.newConnection();
        //创建channel
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        System.out.println("C [*] Waiting for messages. To exit press CTRL+C");
        long start  = System.currentTimeMillis();
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                if(message.equals("599999")){
                    long end  = System.currentTimeMillis();
                    System.out.println("activeMq消费 一共花费 "+(end - start)/1000 + "s");
                }
                System.out.println("C [x] Received '" + message + "'");
            }
        };

        channel.basicConsume(QUEUE_NAME, true, consumer);

    }
}
