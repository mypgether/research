package com.gether.research.test.stream.kafka;

import com.gether.research.kafka.KafkaConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by myp on 2017/8/18.
 */
public class WordCountProducer {


    @Test
    public void produceWord() {
        AtomicInteger count = new AtomicInteger(0);
        while (true) {
            KafkaProducer producer = new KafkaProducer(KafkaConfig.config);
            //for (int i = 0; i <= 10; i++) {
            int cnt = count.incrementAndGet();
            producer.send(new ProducerRecord<String, String>("streams-file-2", "this is " + cnt));
            //}
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}