package com.yy.mq.rabbitmq.demo2;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 上午11:15 on 18-3-14.
 * @Modified By:
 */
public class Task {

    private final static String QUEUE_NAME = "task";

    public static void main(String[] args) throws IOException, TimeoutException {

        //建立连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置rabbitmq地址
        connectionFactory.setHost("localhost");
        //建立连接
        Connection connection = connectionFactory.newConnection();
        //创建channel
        Channel channel = connection.createChannel();
        //声明exchanges
        channel.exchangeDeclare("logs", "fanout");
        //声明队列
        boolean durable = true;
//        channel.queueDeclare(QUEUE_NAME,durable,false,false,null);
        try {
//            channel.txSelect();
//            channel.confirmSelect();

            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                String message = "xxy " + i;
                channel.basicPublish("logs", "delete", MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes("UTF-8"));
                System.out.println("P [x] Send '" + message + "'");
            }
//            channel.txCommit();
//            channel.waitForConfirmsOrDie();
            long time = System.currentTimeMillis() - start;
            System.out.println("一共花费"+ (float)time/1000 + "s");
        }catch (Exception e) {
            e.printStackTrace();
            channel.txRollback();
        }
        channel.close();
        connection.close();
    }
}
