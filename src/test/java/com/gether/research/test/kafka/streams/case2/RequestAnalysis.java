package com.gether.research.test.kafka.streams.case2;

import com.gether.research.test.kafka.streams.base.GenericSerializer;
import com.gether.research.test.kafka.streams.base.SerdesFactory;
import com.gether.research.test.kafka.streams.case2.bean.RequestInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by myp on 2017/8/23.
 */
public class RequestAnalysis {

    public static final String servername = "";

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-request-analysis");
        props.put("bootstrap.servers", "localhost:9092");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put("value.serializer", GenericSerializer.class.getName());
        props.put("value.serializer.type", RequestInfo.class.getName());
        props.put("auto.offset.reset", "earliest");

        KStreamBuilder kStreamBuilder = new KStreamBuilder();
        KStream<String, RequestInfo> streams = kStreamBuilder.stream(Serdes.String(), SerdesFactory.serdFrom(RequestInfo.class), "requests");

        KTable<String, Long> ktable =
                streams.filter((key, requestInfo) -> StringUtils.equalsIgnoreCase(servername, requestInfo.getServer()))
                        .map((String key, RequestInfo requestInfo) -> KeyValue.<String, RequestInfo>pair(requestInfo.getPath(), requestInfo))
                        .groupByKey(Serdes.String(), SerdesFactory.serdFrom(RequestInfo.class))
                        .count();

        ktable.toStream()
                .map((key, value) -> new KeyValue<>(key, key + ":" + String.valueOf(value)))
                .to("path-count");
        KafkaStreams kafkaStreams = new KafkaStreams(kStreamBuilder, props);
        kafkaStreams.cleanUp();
        kafkaStreams.start();

        System.in.read();
        kafkaStreams.close();
        kafkaStreams.cleanUp();
    }

    @Test
    public void testConsumeResult() {
        Properties props = new Properties();
        props.put("group.id", "consumer");
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("max.poll.interval.ms", "300000");
        props.put("max.poll.records", "500");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(props);
        kafkaConsumer.subscribe(Arrays.asList("path-count"));
        while (true) {
            ConsumerRecords<String, String> result = kafkaConsumer.poll(1000);
            result.forEach(res -> {
                System.err.println(res.key() + " value:" + res.value());
            });
        }
    }
}