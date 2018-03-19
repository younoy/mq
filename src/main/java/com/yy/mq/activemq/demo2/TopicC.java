package com.yy.mq.activemq.demo2;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 下午5:23 on 18-3-15.
 * @Modified By:
 */
public class TopicC {

    public static void main(String[] args) throws JMSException {

        //建立连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // MessageProducer：消息发送者
        MessageConsumer consumer;
        // TextMessage message;
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://192.168.87.8:61616");

            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值是一个服务器的queue
            Topic topic = session.createTopic("MessageTopic");
            // 得到消息生成者
            consumer = session.createConsumer(topic);

            consumer.setMessageListener(new MessageListener(){

                public void onMessage(Message message) {
                    TextMessage tm = (TextMessage)message;
                    try {
                        System.out.println(tm.getText());
                        tm.acknowledge();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
}
