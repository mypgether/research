package com.gether.research.test.stream.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * Created by myp on 2017/8/18.
 */
public class DSLTest {

    public static final String TOPIC_RESULT = "streams-wordcount-output";

    @Test
    public void testWordCount() {
        KStreamBuilder builder = new KStreamBuilder();

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application-dsl999");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        KStream<String, String> kstream = builder.stream("streams-file-2");

        KTable<String, Long> counts = kstream
                .flatMapValues(new ValueMapper<String, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(String s) {
                        return Arrays.asList(s.split(" "));
                    }
                })
                .groupBy(new KeyValueMapper<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) {
                        return s2;
                    }
                })
                .count("Counts");


        counts.to(Serdes.String(), Serdes.Long(), TOPIC_RESULT);

        final KafkaStreams streaming = new KafkaStreams(builder, config);
        final CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread("close hook") {
            @Override
            public void run() {
                streaming.close();
                latch.countDown();
            }
        });
        try {
            streaming.start();
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    @Test
    public void getCountResult() {
        Properties props = new Properties();
        //消费者配置文件
        props.put("bootstrap.servers", "localhost:9092");
        props.put("zookeeper.connect", "localhost:2181");
        props.put("group.id", "research-consumer-hehe1");
        props.put("enable.auto.commit", "true");
        //自动提交消费情况间隔时间
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        KafkaConsumer kafkaConsumer = new KafkaConsumer(props);

        kafkaConsumer.subscribe(Arrays.asList(TOPIC_RESULT));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                //System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                System.err.println("consumer1 " + record.toString());
            }
        }
    }
}