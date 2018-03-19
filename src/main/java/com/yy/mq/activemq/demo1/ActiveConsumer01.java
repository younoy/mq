package com.yy.mq.activemq.demo1;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 下午5:13 on 18-3-15.
 * @Modified By:
 */
public class ActiveConsumer01 {

    public static void main(String[] args) throws JMSException {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // 消费者，消息接收者
        MessageConsumer consumer;
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://192.168.87.8:61616");

        // 构造从工厂得到连接对象
        connection = connectionFactory.createConnection();
        // 启动
        connection.start();
        // 获取操作连接
        session = connection.createSession(Boolean.FALSE,
                Session.AUTO_ACKNOWLEDGE);
        // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
        destination = session.createQueue("ThirdQueue");
        consumer = session.createConsumer(destination);
        String reply;
        long start = System.currentTimeMillis();

        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                TextMessage tm = (TextMessage) message;
                try {
                    System.out.println(tm.getText());
                    if(tm.getText().equals("599999")){
                        long end = System.currentTimeMillis();
                        System.out.println("activeMq消费 一共花费 " + (end - start) / 1000 + "s");
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
