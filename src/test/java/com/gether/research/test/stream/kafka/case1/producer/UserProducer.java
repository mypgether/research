package com.gether.research.test.stream.kafka.case1.producer;

import com.alibaba.fastjson.JSON;
import com.gether.research.test.stream.kafka.base.GenericDeserializer;
import com.gether.research.test.stream.kafka.base.GenericSerializer;
import com.gether.research.test.stream.kafka.base.HashPartitioner;
import com.gether.research.test.stream.kafka.case1.bean.Item;
import com.gether.research.test.stream.kafka.case1.bean.User;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class UserProducer {
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 3);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", GenericSerializer.class.getName());
		props.put("value.serializer.type", User.class.getName());
		props.put("partitioner.class", HashPartitioner.class.getName());
		Producer<String, User> producer = new KafkaProducer<String, User>(props);
		List<User> users = readUser();
		users.forEach((User user) -> producer.send(new ProducerRecord<String, User>("users", user.getName(), user)));
		producer.close();
	}

	public static List<User> readUser() throws IOException {
		List<String> lines = FileUtils.readLines(new File("/Users/myp/Git/github/research/src/test/java/com/gether/research/test/kafka/streams/case1/producer/users.csv"));
		List<User> users = lines.stream()
				.filter(StringUtils::isNoneBlank)
				.map((String line) -> line.split("\\s*,\\s*"))
				.filter((String[] values) -> values.length == 4)
				.map((String[] values) -> new User(values[0], values[1], values[2], Integer.parseInt(values[3])))
				.collect(Collectors.toList());
		return users;
	}

	@Test
	public void consumSource() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "consumer.3.4");
		props.put("enable.auto.commit", "false");
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", GenericDeserializer.class.getName());
		props.put("value.deserializer.type", User.class.getName());
		props.put("max.poll.interval.ms", "300000");
		props.put("max.poll.records", "500");
		props.put("auto.offset.reset", "earliest");
		KafkaConsumer<String, Item> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("users"));
		AtomicLong atomicLong = new AtomicLong();
		while (true) {
			ConsumerRecords<String, Item> records = consumer.poll(100);
			records.forEach(record -> {
				System.err.printf("topic: %s , partition: %d , offset = %d, key = %s, value = %s%n", record.topic(), record.partition(), record.offset(), record.key(), JSON.toJSONString(record.value()));
				if (atomicLong.get() % 10 == 0) {
					consumer.commitSync();
				}
			});
		}
	}
}