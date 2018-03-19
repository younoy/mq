package com.yy.mq.rabbitmq.demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 上午11:15 on 18-3-14.
 * @Modified By:
 */
public class Producer01 {

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

        String message = "";
        long start  = System.currentTimeMillis();
        for(int i=0;i<600000;i++) {
            message = ""+i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("P [x] Send '" + message + "'");
        }
        long end  = System.currentTimeMillis();
        System.out.println("rabbitmq 一共花费 "+(end - start)/1000 + "s");

        channel.close();
        connection.close();
    }
}
