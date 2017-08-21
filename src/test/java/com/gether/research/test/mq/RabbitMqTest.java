//package com.gether.research.test.mq;
//
//import com.gether.research.common.MQConstants;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.MessageProperties;
//import org.junit.Before;
//import org.openjdk.jmh.annotations.*;
//import org.openjdk.jmh.runner.Runner;
//import org.openjdk.jmh.runner.RunnerException;
//import org.openjdk.jmh.runner.options.Options;
//import org.openjdk.jmh.runner.options.OptionsBuilder;
//
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//
///**
// * Created by myp on 2017/3/8.
// */
//@BenchmarkMode(Mode.Throughput)
//@OutputTimeUnit(TimeUnit.SECONDS)
//@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS )
//@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS )
//@Threads(1)
//@State(Scope.Thread)
//public class RabbitMqTest {
//
//    Channel channel = null;
//
//    int times = 10000;
//
//    @Setup
//    @Before
//    public void setUp() throws IOException, TimeoutException {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        factory.setUsername("guest");
//        factory.setPassword("guest");
//        factory.setPort(5672);
//        Connection connection = factory.newConnection();
//        channel = connection.createChannel();
//        //创建exchange
//        channel.exchangeDeclare(MQConstants.EXCHANGE_FANOUT, "fanout", true, false, null);
//        channel.exchangeDeclare(MQConstants.EXCHANGE_FANOUT2, "fanout", true, false, null);
//        //创建队列
//        //channel.queueDeclare(MQConstants.QUEUE_LAZY, true, false, false, null);
//        //绑定exchange和queue
//        channel.queueBind(MQConstants.QUEUE_LAZY, MQConstants.EXCHANGE_FANOUT, "");
//        channel.queueBind(MQConstants.QUEUE_SELF_CREATE, MQConstants.EXCHANGE_FANOUT2, "");
//        //channel.confirmSelect();
//    }
//
//    //@Test
//    //@Benchmark
//    //public void produceLazyQueue() throws InterruptedException, IOException {
//    //    for (int i = 0; i < times; i++) {
//    //        channel.basicPublish(MQConstants.EXCHANGE_FANOUT, "", MessageProperties.PERSISTENT_BASIC, "hehe".getBytes());
//    //    }
//    //}
//
//    @Benchmark
//    public void produceDefaultQueue() throws InterruptedException, IOException {
//        for (int i = 0; i < times; i++) {
//            channel.basicPublish(MQConstants.EXCHANGE_FANOUT2, "", MessageProperties.PERSISTENT_BASIC, "hehe".getBytes());
//        }
//    }
//
//    public static void main(String[] args) throws RunnerException {
//        Options opt = new OptionsBuilder()
//                .include(".*" + RabbitMqTest.class.getSimpleName() + ".*")
//                .forks(1)
//                .build();
//        new Runner(opt).run();
//    }
//}