package com.gether.research.test.kafka.streams.case1.producer;

import com.alibaba.fastjson.JSON;
import com.gether.research.test.kafka.streams.base.GenericDeserializer;
import com.gether.research.test.kafka.streams.base.GenericSerializer;
import com.gether.research.test.kafka.streams.base.HashPartitioner;
import com.gether.research.test.kafka.streams.case1.bean.Item;
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

public class ItemProducer {

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
		props.put("value.serializer.type", Item.class.getName());
		props.put("partitioner.class", HashPartitioner.class.getName());
		Producer<String, Item> producer = new KafkaProducer<String, Item>(props);
		List<Item> items = readItem();
		items.forEach((Item item) -> producer.send(new ProducerRecord<String, Item>("items", item.getItemName(), item)));
		producer.close();
	}

	public static List<Item> readItem() throws IOException {
		List<String> lines = FileUtils.readLines(new File("/Users/myp/Git/github/research/src/test/java/com/gether/research/test/kafka/streams/case1/producer/items.csv"));
		List<Item> items = lines.stream()
				.filter(StringUtils::isNoneBlank)
				.map((String line) -> line.split("\\s*,\\s*"))
				.filter((String[] values) -> values.length == 4)
				.map((String[] values) -> new Item(values[0], values[1], values[2], Double.parseDouble(values[3])))
				.collect(Collectors.toList());
		return items;
	}

	@Test
	public void consumSource() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "consumer.2");
		props.put("enable.auto.commit", "false");
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", GenericDeserializer.class.getName());
		props.put("value.deserializer.type", Item.class.getName());
		props.put("max.poll.interval.ms", "300000");
		props.put("max.poll.records", "500");
		props.put("auto.offset.reset", "earliest");
		KafkaConsumer<String, Item> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("items"));
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