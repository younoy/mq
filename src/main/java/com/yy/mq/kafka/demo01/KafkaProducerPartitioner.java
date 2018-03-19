package com.yy.mq.kafka.demo01;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * @Author: upsmart
 * @Description:
 * @Date: Created by 下午4:50 on 18-3-19.
 * @Modified By:
 */
public class KafkaProducerPartitioner implements Partitioner {


    /**
     * 无参构造函数
     */
    public KafkaProducerPartitioner() {
        this(new VerifiableProperties());
    }

    /**
     * 构造函数，必须给定
     *
     * @param properties 上下文
     */
    public KafkaProducerPartitioner(VerifiableProperties properties) {
        // nothings
    }

    @Override
    public int partition(Object obj, int numPartitions) {
        int partition = 0;
        if (obj instanceof String) {
            String key=(String)obj;
            int offset = key.lastIndexOf('.');
            if (offset > 0) {
                partition = Integer.parseInt(key.substring(offset + 1)) % numPartitions;
            }
        }else{
            partition = obj.toString().length() % numPartitions;
        }

        return partition;
    }
}
