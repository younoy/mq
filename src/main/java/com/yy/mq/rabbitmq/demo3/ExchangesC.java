package com.yy.mq.rabbitmq.demo3;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 上午9:19 on 18-3-15.
 * @Modified By:
 */
public class ExchangesC {

    private final static String QUEUE_NAME = "test_queue_fanout_1";

//    private final static String EXCHANGE_NAME = "test_exchange_direct";
    private final static String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        //建立连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置rabbitmq地址
        connectionFactory.setHost("localhost");
        //建立连接
        Connection connection = connectionFactory.newConnection();
        //创建channel
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列到交换机
//        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "love");
//        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "sakura");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("C [x] Received '" + message + "'");
            }
        };
        // 监听队列，手动返回完成
        channel.basicConsume(QUEUE_NAME, false, consumer);

    }
}
