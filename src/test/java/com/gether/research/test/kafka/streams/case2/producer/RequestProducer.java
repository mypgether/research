package com.gether.research.test.kafka.streams.case2.producer;

import com.alibaba.fastjson.JSON;
import com.gether.research.test.kafka.streams.base.GenericDeserializer;
import com.gether.research.test.kafka.streams.base.GenericSerializer;
import com.gether.research.test.kafka.streams.base.HashPartitioner;
import com.gether.research.test.kafka.streams.case2.bean.RequestInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by myp on 2017/8/23.
 */
public class RequestProducer {

    public static final String TOPIC = "requests";

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", GenericSerializer.class.getName());
        props.put("value.serializer.type", RequestInfo.class.getName());
        props.put("partitioner.class", HashPartitioner.class.getName());


        //props.put("value.serializer.type", RequestInfo.class.getName());
        //props.put("partitioner.class", HashPartitioner.class.getName());
        Producer<String, RequestInfo> producer = new KafkaProducer<>(props);
        List<RequestInfo> items = read();
        System.out.println(items.size());
        items.forEach((RequestInfo item) -> producer.send(new ProducerRecord<>(TOPIC, item.getServer(), item)));
        producer.close();
    }

    public static List<RequestInfo> read() throws IOException {
        List<String> lines = FileUtils.readLines(new File("/Users/myp/Git/github/research/src/test/java/com/gether/research/test/kafka/streams/case2/producer/request.csv"));
        return lines.stream().filter(StringUtils::isNoneBlank)
                .map(s -> s.split("\t"))
                .filter(s -> s.length == 7)
                .map((s) -> {
                    RequestInfo requestInfo = new RequestInfo();
                    requestInfo.setServer(s[0]);
                    requestInfo.setIp(s[1]);
                    requestInfo.setPath(s[2]);
                    requestInfo.setReqParams(s[3]);
                    requestInfo.setRepResult(s[4]);
                    requestInfo.setTimes(Long.parseLong(s[5]));
                    requestInfo.setRemark(s[6]);
                    return requestInfo;
                })
                .collect(Collectors.toList());
    }

    @Test
    public void consume() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "request_consumer");
        properties.put("enable.auto.commit", "false");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", GenericDeserializer.class.getName());
        properties.put("value.deserializer.type", RequestInfo.class.getName());
        properties.put("max.poll.interval.ms", "300000");
        properties.put("max.poll.records", "100");
        properties.put("auto.offset.reset", "earliest");

        KafkaConsumer consumer = new KafkaConsumer(properties);
        consumer.subscribe(Arrays.asList(TOPIC));
        while (true) {
            ConsumerRecords<String, RequestInfo> results = consumer.poll(100);
            results.forEach(result -> {
                System.err.println(JSON.toJSONString(result.value()));
            });
        }
    }
}