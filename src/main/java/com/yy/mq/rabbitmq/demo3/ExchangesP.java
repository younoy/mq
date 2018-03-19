package com.yy.mq.rabbitmq.demo3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 上午9:19 on 18-3-15.
 * @Modified By:
 */
public class ExchangesP {

    private final static String EXCHANGE_NAME = "test_exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        //建立连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置rabbitmq地址
        connectionFactory.setHost("localhost");
        //建立连接
        Connection connection = connectionFactory.newConnection();
        //创建channel
        Channel channel = connection.createChannel();

        // 声明exchange
//        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel.exchangeDeclare("logs", "fanout");

        //声明队列
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "SAKURA 樱花";

        channel.basicPublish("logs", "", null, message.getBytes("UTF-8"));

        System.out.println("P [x] Send '" + message + "'");
        channel.close();
        connection.close();
    }
}
