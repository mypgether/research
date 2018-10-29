package com.gether.research.test.stream.kafka.case4;

import com.alibaba.fastjson.JSON;
import com.gether.research.test.stream.kafka.case4.bean.ItemInfo;
import com.gether.research.test.stream.kafka.case4.bean.TopCategory;
import com.gether.research.test.stream.kafka.base.SerdesFactory;
import com.gether.research.test.stream.kafka.case1.OrderTimestampExtractor;
import com.gether.research.test.stream.kafka.case1.bean.Item;
import com.gether.research.test.stream.kafka.case1.bean.Order;
import com.gether.research.test.stream.kafka.case1.bean.User;
import com.gether.research.test.stream.kafka.case3.bean.OrderUser;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
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
import org.apache.kafka.streams.kstream.Reducer;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * 场景： 每5秒输出过去1小时18岁到35岁用户所购买的商品中，每种品类销售额排名前十的订单汇总信息。
 使用数据内的时间(Event Time)作为timestamp
 每5秒输出一次
 每次计算到输出为止过去1小时的数据
 支持订单详情和用户详情的更新和增加
 输出字段包含时间窗口（起始时间，结束时间），品类（category），商品名（item_name），销量（quantity），单价（price），总销售额，该商品在该品类内的销售额排名
 * Created by myp on 2017/8/23.
 */
public class TopNWindowAnalysis {

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-user-purchase-analysis-window-1");
        props.put("bootstrap.servers", "localhost:9092");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put("auto.offset.reset", "earliest");
        props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG, OrderTimestampExtractor.class);

        KStreamBuilder kStreamBuilder = new KStreamBuilder();

        KStream<String, Order> orderKStreamam = kStreamBuilder.stream(Serdes.String(), SerdesFactory.serdFrom(Order.class), "orders-windows");

        KTable<String, User> userKTable = kStreamBuilder.table(Serdes.String(), SerdesFactory.serdFrom(User.class), "users");
        KTable<String, Item> itemKTable = kStreamBuilder.table(Serdes.String(), SerdesFactory.serdFrom(Item.class), "items");


        KTable<Windowed<String>, TopCategory> windowResult = orderKStreamam
                .leftJoin(userKTable, (order, user) -> {
                    OrderUser orderItem = new OrderUser();
                    try {
                        BeanUtils.copyProperties(orderItem, order);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    if (user != null) {
                        orderItem.setUserAddress(user.getAddress());
                        orderItem.setGender(user.getGender());
                        orderItem.setAge(user.getAge());
                        return orderItem;
                    }
                    return null;
                }, Serdes.String(), SerdesFactory.serdFrom(Order.class))
                .filter((key, value) -> value != null)
                .filter((key, value) -> value.getAge() >= 18 && value.getAge() <= 35)
                .map((key, value) -> new KeyValue<String, OrderUser>(value.getItemName(), value))
                // 需要join就一定要保证同样的key在一个partition当中
                .through(Serdes.String(), SerdesFactory.serdFrom(OrderUser.class), (s, orderUser, numPartitions) -> (orderUser.getItemName().hashCode() & 0x7FFFFFFF) % numPartitions, "orderuser-repartition-by-item")
                .leftJoin(itemKTable, (orderUser, item) -> {
                    ItemInfo topItem = new ItemInfo();
                    topItem.setItemPrice(item.getPrice());
                    topItem.setItemName(item.getItemName());
                    topItem.setCategory(item.getType());
                    topItem.setQuantity(orderUser.getQuantity());
                    topItem.setTotalPrice(item.getPrice() * orderUser.getQuantity());
                    return topItem;
                }, Serdes.String(), SerdesFactory.serdFrom(OrderUser.class))

                .map((String itemName, ItemInfo itemInfo) -> {
                    System.err.println(JSON.toJSONString(itemInfo));
                    List<ItemInfo> lists = new ArrayList<ItemInfo>();
                    lists.add(itemInfo);
                    TopCategory topCategory = new TopCategory();
                    topCategory.setCategory(itemInfo.getCategory());
                    topCategory.setItemInfoList(lists);
                    return new KeyValue<String, TopCategory>(itemInfo.getCategory(), topCategory);
                })
                .groupByKey(Serdes.String(), SerdesFactory.serdFrom(TopCategory.class))
                .reduce((Reducer<TopCategory>) (v1, v2) -> {
                            TopCategory topItem = new TopCategory();
                            topItem.setCategory(v1.getCategory());
                            List<ItemInfo> itemInfoList = Lists.newArrayList();
                            itemInfoList.addAll(v1.getItemInfoList());
                            itemInfoList.addAll(v2.getItemInfoList());
                            topItem.setItemInfoList(itemInfoList);
                            return topItem;
                        }
                        , TimeWindows.of(1000 * 60 * 5).advanceBy(1000 * 60)
                        , "gender-amount-state-store");

        windowResult.toStream().map((Windowed<String> window, TopCategory topCategory) -> {
            return new KeyValue<String, String>(window.key(), topCategory.printTop10(window.window().start(), window.window().end()));
        }).to(Serdes.String(), Serdes.String(), "gender-amount-window-result");

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
        kafkaConsumer.subscribe(Arrays.asList("gender-amount-window-result"));
        while (true) {
            ConsumerRecords<String, String> result = kafkaConsumer.poll(1000);
            result.forEach(res -> {
                System.err.println("key:" + JSON.toJSONString(res.key()) + " value:" + JSON.toJSONString(res.value()));
            });
        }
    }
}