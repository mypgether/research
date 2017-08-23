package com.gether.research.test.kafka.streams.case1.producer;

import com.gether.research.test.kafka.streams.base.GenericSerializer;
import com.gether.research.test.kafka.streams.base.HashPartitioner;
import com.gether.research.test.kafka.streams.case1.bean.Order;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class OrderProducer {

	private static DateTimeFormatter dataTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
		props.put("value.serializer.type", Order.class.getName());
		props.put("partitioner.class", HashPartitioner.class.getName());
		Producer<String, Order> producer = new KafkaProducer<String, Order>(props);
		List<Order> orders = readOrder();
		orders.forEach((Order order) -> producer.send(new ProducerRecord<String, Order>("orders", order.getUserName(), order)));
		producer.close();
	}

	public static List<Order> readOrder() throws IOException {
		List<String> lines = FileUtils.readLines(new File("/Users/myp/Git/github/research/src/test/java/com/gether/research/test/kafka/streams/case1/producer/orders.csv"));
		List<Order> orders = lines.stream()
				.filter(StringUtils::isNoneBlank)
				.map((String line) -> line.split("\\s*,\\s*"))
				.filter((String[] values) -> values.length == 4)
				.map((String[] values) -> new Order(values[0], values[1], LocalDateTime.parse(values[2], dataTimeFormatter).toEpochSecond(ZoneOffset.UTC) * 1000, Integer.parseInt(values[3])))
				.collect(Collectors.toList());
		return orders;
	}
}