package com.gether.research.test.kafka.streams.case3;

import com.alibaba.fastjson.JSON;
import com.gether.research.test.kafka.streams.base.SerdesFactory;
import com.gether.research.test.kafka.streams.case1.bean.Item;
import com.gether.research.test.kafka.streams.case1.bean.Order;
import com.gether.research.test.kafka.streams.case1.bean.User;
import com.gether.research.test.kafka.streams.case3.bean.AddressGender;
import com.gether.research.test.kafka.streams.case3.bean.OrderItemUser;
import com.gether.research.test.kafka.streams.case3.bean.OrderUser;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
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
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Reducer;
import org.apache.kafka.streams.processor.StreamPartitioner;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Properties;

/**
 * 场景： 在上一个示例（算出用户与商品同地址的订单中，男女分别总共花了多少钱）的基础上，算出不同地区（用户地址），不同性别的订单数及商品总数和总金额。输出结果schema如下
 地区（用户地区，如SH），性别，订单总数，商品总数，总金额

 示例输出

 SH, male, 3, 4, 188888.88

 BJ, femail, 5, 8, 288888.88
 * Created by myp on 2017/8/23.
 */
public class UserPurchaseAnalysis {

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-user-purchase-analysis");
        props.put("bootstrap.servers", "localhost:9092");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put("auto.offset.reset", "earliest");

        KStreamBuilder kStreamBuilder = new KStreamBuilder();

        KStream<String, Order> orderKStreamam = kStreamBuilder.stream(Serdes.String(), SerdesFactory.serdFrom(Order.class), "orders");

        KTable<String, User> userKTable = kStreamBuilder.table(Serdes.String(), SerdesFactory.serdFrom(User.class), "users");
        KTable<String, Item> itemKTable = kStreamBuilder.table(Serdes.String(), SerdesFactory.serdFrom(Item.class), "items");

        KTable<AddressGender, OrderItemUser> purchaseResult = orderKStreamam
                .leftJoin(userKTable, (order, user) -> {
                    OrderUser orderItem = new OrderUser();
                    try {
                        BeanUtils.copyProperties(orderItem, order);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    if (null == user) {
                        return null;
                    }
                    orderItem.setUserAddress(user.getAddress());
                    orderItem.setGender(user.getGender());
                    return orderItem;
                }, Serdes.String(), SerdesFactory.serdFrom(Order.class))
                .filter((key,value)->null!=value && StringUtils.isNotBlank(value.getUserAddress()))
                .map((key, value) -> new KeyValue<String, OrderUser>(value.getItemName(), value))
                // 需要join就一定要保证同样的key在一个partition当中
                .through(Serdes.String(), SerdesFactory.serdFrom(OrderUser.class), new StreamPartitioner<String, OrderUser>() {
                    @Override
                    public Integer partition(String s, OrderUser orderUser, int numPartitions) {
                        return (orderUser.getItemName().hashCode() & 0x7FFFFFFF) % numPartitions;
                    }
                }, "orderuser-repartition-by-item-1")
                .leftJoin(itemKTable, (orderUser, item) -> {
                    OrderItemUser orderItemUser = new OrderItemUser();
                    orderItemUser.setItemAddress(item.getAddress());
                    orderItemUser.setUserAddress(orderUser.getUserAddress());
                    orderItemUser.setGender(orderUser.getGender());
                    orderItemUser.setItemPrice(item.getPrice());
                    orderItemUser.setQuantity(orderUser.getQuantity());
                    return orderItemUser;
                }, Serdes.String(), SerdesFactory.serdFrom(OrderUser.class))
                .filter((key, value)-> StringUtils.equalsIgnoreCase(value.getItemAddress(), value.getUserAddress()))
                .groupBy(new KeyValueMapper<String, OrderItemUser, AddressGender>() {
                    @Override
                    public AddressGender apply(String s, OrderItemUser orderItemUser) {
                        return new AddressGender(orderItemUser.getItemAddress(), orderItemUser.getGender());
                    }
                }, SerdesFactory.serdFrom(AddressGender.class), SerdesFactory.serdFrom(OrderItemUser.class))
                .reduce(new Reducer<OrderItemUser>() {
                    @Override
                    public OrderItemUser apply(OrderItemUser v1, OrderItemUser v2) {
                        OrderItemUser newOrderItemUser = new OrderItemUser();
                        double p1 = v1.getItemPrice() * v1.getQuantity();
                        double p2 = v2.getItemPrice() * v2.getQuantity();
                        newOrderItemUser.setTotalPrice(p1 + p2);
                        newOrderItemUser.setQuantity(v1.getQuantity() + v2.getQuantity());
                        newOrderItemUser.setGender(v1.getGender());
                        newOrderItemUser.setItemPrice(v1.getItemPrice());
                        newOrderItemUser.setItemAddress(v1.getItemAddress());
                        return newOrderItemUser;
                    }
                });

        purchaseResult.toStream()
                .map((key, value) -> new KeyValue<>(JSON.toJSONString(key), JSON.toJSONString(value)))
                .to("result-table-userpurchase");

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
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("key.deserializer", StringDeserializer.class.getName());
        //props.put("value.deserializer", GenericSerializer.class.getName());
        //props.put("value.deserializer.type", OrderItemUser.class.getName());
        //props.put("key.deserializer", GenericSerializer.class.getName());
        //props.put("key.deserializer.type", AddressGender.class.getName());
        props.put("max.poll.interval.ms", "300000");
        props.put("max.poll.records", "500");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(props);
        kafkaConsumer.subscribe(Arrays.asList("result-table-userpurchase"));
        while (true) {
            ConsumerRecords<String, String> result = kafkaConsumer.poll(1000);
            result.forEach(res -> {
                System.err.println("key:" + JSON.toJSONString(res.key()) + " value:" + JSON.toJSONString(res.value()));
            });
        }
    }
}