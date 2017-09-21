package com.gether.research.test.kafka.streams.case4.producer;

import com.gether.research.test.kafka.streams.base.GenericSerializer;
import com.gether.research.test.kafka.streams.base.HashPartitioner;
import com.gether.research.test.kafka.streams.case1.bean.Order;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by myp on 2017/8/31.
 */
public class OrderProducer {
    private static DateTimeFormatter dataTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //c创建订单文件
    @Test
    public void generateFile() throws IOException, ParseException {
        FileOutputStream outSTr = null;
        FileOutputStream out = null;
        BufferedOutputStream Buff = null;
        outSTr = new FileOutputStream(new File("/Users/myp/Git/github/research/src/test/java/com/gether/research/test/kafka/streams/case4/orders.csv"));
        Buff = new BufferedOutputStream(outSTr);
        String[] a1 = {"Jack", "Lily", "Mike", "Lucy", "LiLei", "HanMeimei"};
        Random rand = new Random();
        String[] a2 = {"iphone", "ipad", "iwatch", "ipod"};
        Calendar calendar = Calendar.getInstance();
        String str = "2017-09-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(str);
        calendar.setTime(date);
        System.out.println(calendar.getTime());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = "";
        int count = 0;
        while (!(count == 60 * 60 * 24)) {//86400
            System.out.println(count);
            count++;
            System.out.println(dateStr);
            int num = rand.nextInt(a1.length);
            // System.out.println(a1[num]);
            int num2 = rand.nextInt(a2.length);
            // System.out.println(a2[num2]);
            calendar.add(Calendar.SECOND, 1);
            sdf2.format(calendar.getTime());
            dateStr = sdf2.format(calendar.getTime());
            // System.out.println (dateStr);
            Buff.write((a1[num] + "," + a2[num2] + ", " + dateStr + "," + rand.nextInt(5) + "\r\n").getBytes());
            Buff.flush();
        }
    }


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
        orders.forEach((Order order) -> producer.send(new ProducerRecord<String, Order>("orders-windows", order.getUserName(), order)));
        producer.close();
    }

    public static List<Order> readOrder() throws IOException {
        List<String> lines = FileUtils.readLines(new File("/Users/myp/Git/github/research/src/test/java/com/gether/research/test/kafka/streams/case4/orders.csv"));
        List<Order> orders = lines.stream()
                .filter(StringUtils::isNoneBlank)
                .map((String line) -> line.split("\\s*,\\s*"))
                .filter((String[] values) -> values.length == 4)
                .map((String[] values) -> new Order(values[0], values[1], LocalDateTime.parse(values[2], dataTimeFormatter).toEpochSecond(ZoneOffset.UTC) * 1000, Integer.parseInt(values[3])))
                .collect(Collectors.toList());
        return orders;
    }
}