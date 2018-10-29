package com.gether.research.test.stream.kafka;

import com.gether.research.kafka.streams.processor.WordCountProcessor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.StateStoreSupplier;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * Created by myp on 2017/8/18.
 */
public class ProcessorTopologyTest {


    @Test
    public void testWordCount() {
        StateStoreSupplier countStore = Stores.create("Counts")
                .withKeys(Serdes.String())
                .withValues(Serdes.Long())
                .persistent()
                .build();


        TopologyBuilder builder = new TopologyBuilder();
        builder.addSource("SOURCE", "streams-file")
                .addProcessor("PROCESS1", () -> new WordCountProcessor(), "SOURCE")
                .addStateStore(countStore, "PROCESS1")
                //.addProcessor("PROCESS2", () -> new WordCountProcessor(), "PROCESS1")
                //.addProcessor("PROCESS3", () -> new WordCountProcessor(), "PROCESS2")
                //.connectProcessorAndStateStores("PROCESS2", "COUNTS")
                //.connectProcessorAndStateStores("PROCESS3", "COUNTS")
                .addSink("SINK1", "sink-topic2", "PROCESS1");
        //.addSink("SINK2", "sink-topic2", "PROCESS2")
        //.addSink("SINK3", "sink-topic3", "PROCESS3");

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application-processor");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
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
        props.put("group.id", "research-consumer-21");
        props.put("enable.auto.commit", "true");
        //自动提交消费情况间隔时间
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        KafkaConsumer kafkaConsumer = new KafkaConsumer(props);

        kafkaConsumer.subscribe(Arrays.asList("sink-topic2"));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                //System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                System.out.println("consumer1 " + record.toString());
            }
        }
    }
}