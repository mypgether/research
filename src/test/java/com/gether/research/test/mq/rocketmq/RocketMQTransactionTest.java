//package com.gether.research.test.mq.rocketmq;
//
//import java.io.UnsupportedEncodingException;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.apache.rocketmq.client.producer.TransactionListener;
//import org.apache.rocketmq.client.producer.TransactionMQProducer;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.remoting.common.RemotingHelper;
//import org.junit.Test;
//
///**
// * http://127.0.0.1:8180
// * @author myp
// * @date 2019/3/12 下午6:10
// */
//@Slf4j
//public class RocketMQTransactionTest {
//
//  // gs
//  public static final String NAME_SERVER = "172.29.31.100:9876";
//  public static final String TOPIC = "TransactionTest";
//
//  public static void main(String[] args) throws MQClientException, InterruptedException {
////    TransactionListener transactionListener = new TransactionListenerImpl();
//    TransactionListener transactionListener = new TransactionSleepListenerImpl();
//    TransactionMQProducer producer = new TransactionMQProducer("trans-produce");
//    ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
//        new ArrayBlockingQueue<>(2000), r -> {
//      AtomicInteger count = new AtomicInteger();
//      Thread thread = new Thread(r);
//      thread.setName("client-transaction-msg-check-thread" + count.incrementAndGet());
//      return thread;
//    });
//    producer.setNamesrvAddr(NAME_SERVER);
//    producer.setExecutorService(executorService);
//    producer.setTransactionListener(transactionListener);
//    producer.start();
//
//    String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
//    for (int i = 0; i < 10; i++) {
//      try {
//        Message msg =
//            new Message(TOPIC, tags[i % tags.length], "KEY" + i,
//                ("Hello RocketMQ|" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
//        SendResult sendResult = producer.sendMessageInTransaction(msg, null);
//        log.info("{}", sendResult);
//
//        Thread.sleep(10);
//      } catch (MQClientException | UnsupportedEncodingException e) {
//        e.printStackTrace();
//      }
//    }
//
//    for (int i = 0; i < 100000; i++) {
//      Thread.sleep(1000);
//    }
//    producer.shutdown();
//  }
//
//  @Test
//  public void testConsume() throws MQClientException, InterruptedException {
//    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("trans-consumer");
//    consumer.setNamesrvAddr(NAME_SERVER);
//    consumer.setPullBatchSize(10);
//    consumer.setConsumeMessageBatchMaxSize(10);
//    consumer.subscribe(TOPIC, "*");
//    consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
//      if (msgs.size() > 1) {
//        log.info("consumer get more than one, size: {}", msgs.size());
//      }
//      for (MessageExt msg : msgs) {
//        log.info("threadName: {} ,msg: {}", Thread.currentThread().getName(), msg);
//      }
//      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//    });
//    consumer.start();
//
//    System.out.printf("Consumer Started.%n");
//
//    while (true) {
//      Thread.sleep(500);
//    }
//  }
//}
