package com.gether.research.kafka;

import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by myp on 2017/8/16.
 */
@Configuration
public class KafkaConfig {

    //生产者配置文件，具体配置可参考ProducerConfig类源码，或者参考官网介绍
    public static final Map<String, Object> config = new HashMap<String, Object>();

    public static final Properties props = new Properties();

    static {
        //kafka服务器地址
        config.put("bootstrap.servers", "localhost:9092");
        //kafka消息序列化类 即将传入对象序列化为字节数组
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //kafka消息key序列化类 若传入key的值，则根据该key的值进行hash散列计算出在哪个partition上
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("batch.size", 1024 * 1024 * 5);
        //往kafka服务器提交消息间隔时间，0则立即提交不等待
        config.put("linger.ms", 0);

        //消费者配置文件
        props.put("bootstrap.servers", "localhost:9092");
        props.put("zookeeper.connect", "localhost:2181");
        props.put("group.id", "research-consumer-1");
        props.put("enable.auto.commit", "true");
        //自动提交消费情况间隔时间
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }
    //
    //@Bean
    //public KafkaProducer getKafkaProducer() {
    //    KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);
    //    return producer;
    //}
    //
    //@Bean(name = "kafkaConsumer")
    //public KafkaConsumer getKafkaConsumer() {
    //    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
    //    return consumer;
    //}
    //
    //@Bean(name = "kafkaConsumer2")
    //public KafkaConsumer getKafkaConsumer2() {
    //    Properties props = new Properties();
    //    //消费者配置文件
    //    props.put("bootstrap.servers", "localhost:9092");
    //    props.put("zookeeper.connect", "localhost:2181");
    //    props.put("group.id", "research-consumer-1");
    //    props.put("enable.auto.commit", "true");
    //    //自动提交消费情况间隔时间
    //    props.put("auto.commit.interval.ms", "1000");
    //    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    //    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    //    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
    //    return consumer;
    //}
}