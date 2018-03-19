package com.yy.mq.kafka.demo01;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Date;
import java.util.Properties;


/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 下午5:04 on 18-3-19.
 * @Modified By:
 */
public class KafkaProducer{


    public static void main(String[] args) {

        int events=600000;
        // 设置配置属性
        Properties props = new Properties();
        props.put("metadata.broker.list","localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        // key.serializer.class默认为serializer.class
//            props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        // 可选配置，如果不配置，则使用默认的partitioner
//            props.put("partitioner.class", "com.yy.mq.kafka.demo01.KafkaProducerPartitioner");
        // 触发acknowledgement机制，否则是fire and forget，可能会引起数据丢失
        // 值为0,1,-1,可以参考
            props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);

        // 创建producer
        Producer<String, String> producer = new Producer<String, String>(config);
        // 产生并发送消息
        long start=System.currentTimeMillis();
            for (long i = 0; i < events; i++) {
            String msg = "" + i;
            System.out.println(msg);
            //如果topic不存在，则会自动创建，默认replication-factor为1，partitions为0
            KeyedMessage<String, String> data = new KeyedMessage<String, String>("kafka", null, msg);
            producer.send(data);
        }
        System.out.println("耗时:" + (System.currentTimeMillis() - start)/1000 + "s");
        // 关闭producer
        producer.close();

    }
}
