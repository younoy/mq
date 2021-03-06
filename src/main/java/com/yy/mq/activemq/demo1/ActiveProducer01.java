package com.yy.mq.activemq.demo1;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 下午5:01 on 18-3-15.
 * @Modified By:
 */
public class ActiveProducer01 {

    private static final int SEND_NUMBER = 100000;

    public static void main(String[] args) {

        //建立连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // MessageProducer：消息发送者
        MessageProducer producer;
        // TextMessage message;
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://192.168.87.8:61616");

        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue("ThirdQueue");
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            long start  = System.currentTimeMillis();
            // 构造消息，此处写死，项目就是参数，或者方法获取
            for (int i = 0; i < 600000; i++) {
                TextMessage message = session
                        .createTextMessage("" + i);
                System.out.println("发送消息：" + i);
                producer.send(message);
            }
            session.commit();
            long end  = System.currentTimeMillis();
            System.out.println("activeMq 一共花费 "+(end - start)/1000 + "s");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

}
