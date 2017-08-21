package com.gether.research.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by myp on 2017/8/16.
 */
@Component
public class KProducer {

    public static final String TOPIC_TEST = "topic_test1";

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void produce() {
        //kafkaTemplate.send(TOPIC_TEST, SendObj.builder().age(10).name("gether").remark(LocalDateTime.now().toString()).build().toString());
        //kafkaTemplate.send(TOPIC_TEST,"1", "heheheh ");
        kafkaProducer.send(new ProducerRecord<String, String>(TOPIC_TEST, "123123"));
    }
}