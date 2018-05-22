//package com.gether.research.test.mongo;
//
//import com.alibaba.fastjson.JSON;
//import com.gether.research.mongo.ProfilePojo;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.mongodb.Block;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientOptions;
//import com.mongodb.ServerAddress;
//import com.mongodb.client.DistinctIterable;
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.UpdateOptions;
//import com.mongodb.client.result.DeleteResult;
//import org.bson.Document;
//import org.bson.codecs.configuration.CodecRegistry;
//import org.bson.codecs.pojo.PojoCodecProvider;
//import org.bson.types.ObjectId;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.function.Consumer;
//
//import static com.mongodb.client.Filters.eq;
//import static com.mongodb.client.Updates.combine;
//import static com.mongodb.client.Updates.set;
//import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
//import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
//
///**
// * Created by myp on 2017/9/5.
// */
//public class MongoDbBaseTest {
//
//    private MongoClient mongoClient;
//    private MongoDatabase database;
//
//    @Before
//    public void before() {
//        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//        //MongoCredential credential = MongoCredential.createCredential(user, database, password);
//        mongoClient = new MongoClient(new ServerAddress("localhost", 32769), MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
//        database = mongoClient.getDatabase("mydb");
//    }
//
//    @Test
//    public void mongoCreate() {
//        // tables
//        MongoCollection<Document> collection = database.getCollection("test");
//
//        System.out.println(collection.count());
//
//        List<Document> documentList = Lists.newArrayList();
//        for (int i = 10; i < 100; i++) {
//            Document document = new Document("name", "user").append("type", "database")
//                    .append("count", 1)
//                    .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
//                    .append("info", new Document("x", 203).append("y", 102)).append("_id", i);
//            documentList.add(document);
//        }
//        collection.insertMany(documentList);
//    }
//
//    @Test
//    public void delete() {
//        MongoCollection<Document> collection = database.getCollection("test");
//        DeleteResult res = collection.deleteOne(eq("_id", 1));
//        System.out.println(res.getDeletedCount());
//
//        res = collection.deleteMany(eq("type", "database"));
//        System.out.println(res.getDeletedCount());
//    }
//
//
//    @Test
//    public void insertObj() {
//        MongoCollection<ProfilePojo> collection = database.getCollection("profile", ProfilePojo.class);
//
//        String deviceId = "2123451";
//        ProfilePojo pojo = new ProfilePojo();
//        pojo.setId(ObjectId.get());
//        pojo.setDeviceId(deviceId);
//        pojo.setProductKey("productkey");
//        pojo.setd("d");
//        Map<String, Map<String, String>> map = Maps.newHashMap();
//        Map<String, String> objMap = Maps.newHashMap();
//        objMap.put("support", "1");
//        objMap.put("supportKey", "2*1~100");
//        objMap.put("value", "这是名称😄");
//        map.put("title", objMap);
//
//        objMap = Maps.newHashMap();
//        objMap.put("support", "1");
//        objMap.put("value", "On");
//        map.put("status", objMap);
//        pojo.setMap(map);
//        collection.insertOne(pojo);
//
//        //collection.count()
//
//        FindIterable<ProfilePojo> res = collection.find(eq("productKey", "productkey"));
//        res.forEach((Consumer<? super ProfilePojo>) record -> {
//            System.out.println(JSON.toJSONString(record));
//
//            System.out.println("title:" + Optional.ofNullable(record.getMap()).map(a -> a.get("title")).map(a -> a.get("supportKey")).orElse(""));
//        });
//
//        collection.deleteOne(eq("deviceId", deviceId));
//    }
//
//    @Test
//    public void updateObj() {
//        MongoCollection<ProfilePojo> collection = database.getCollection("profile", ProfilePojo.class);
//
//        ProfilePojo pojo = new ProfilePojo();
//        pojo.setDeviceId("212345");
//        pojo.setProductKey("productkey");
//        pojo.setd("d");
//        pojo.setCreateTime(new Date());
//        Map<String, Map<String, String>> map = Maps.newHashMap();
//        Map<String, String> objMap = Maps.newHashMap();
//        objMap.put("support", "1");
//        objMap.put("supportKey", "2*2~100");
//        objMap.put("value", "这是名称😄");
//        map.put("title", objMap);
//        pojo.setMap(map);
//
//        collection.updateOne(eq("deviceId", pojo.getDeviceId()),
//                combine(set("d", "abc_001"), set("map.title", objMap), set("createTime", pojo.getCreateTime())),
//                new UpdateOptions().upsert(true));
//    }
//
//    @Test
//    public void distinctTest() {
//        MongoCollection<ProfilePojo> collection = database.getCollection("profile", ProfilePojo.class);
//
//        long pkCount = collection.count(eq("productKey", "productkey"));
//        System.out.println(pkCount);
//
//        DistinctIterable<String> deviceIdList = collection.distinct("deviceId", eq("productKey", "productkey"), String.class);
//        //deviceIdList.batchSize(3);
//
//        AtomicInteger cnt = new AtomicInteger(0);
//        deviceIdList.forEach((Block<? super String>) deviceId -> {
//            System.out.println(deviceId.toString());
//            cnt.incrementAndGet();
//        });
//        System.out.println(cnt.get());
//    }
//}