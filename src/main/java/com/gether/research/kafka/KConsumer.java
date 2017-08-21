package com.gether.research.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by myp on 2017/8/16.
 */
@Component
public class KConsumer {

    @Resource(name = "kafkaConsumer")
    private KafkaConsumer kafkaConsumer;

    @Resource(name = "kafkaConsumer2")
    private KafkaConsumer kafkaConsumer2;

    public void consume() {
        kafkaConsumer.subscribe(Arrays.asList(KProducer.TOPIC_TEST), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                System.out.println("onPartitionsRevoked collection" + collection.toString());
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                System.out.println("onPartitionsAssigned collection" + collection.toString());
            }
        });

        kafkaConsumer2.subscribe(Arrays.asList(KProducer.TOPIC_TEST), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                System.out.println("onPartitionsRevoked collection" + collection.toString());
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                System.out.println("onPartitionsAssigned collection" + collection.toString());
            }
        });


        new Thread(() -> {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    //System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                    System.out.println("consumer1 " + record.toString());
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer2.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("consumer2 " + record.toString());
                }
            }
        }).start();
    }
}