//package com.gether.research.test.mq.rocketmq;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.producer.LocalTransactionState;
//import org.apache.rocketmq.client.producer.TransactionListener;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.common.message.MessageExt;
//
//@Slf4j
//public class TransactionSleepListenerImpl implements TransactionListener {
//
//  private AtomicInteger transactionIndex = new AtomicInteger(0);
//
//  private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();
//
//  private AtomicInteger counter = new AtomicInteger();
//
//  @Override
//  public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//    String body = new String(msg.getBody());
//    log.info("executeLocalTransaction message: {} ,arg: {}", body, arg);
//    int value = transactionIndex.getAndIncrement();
//    int status = value % 3;
//    localTrans.put(msg.getTransactionId(), status);
//    return LocalTransactionState.UNKNOW;
//  }
//
//  @Override
//  public LocalTransactionState checkLocalTransaction(MessageExt msg) {
//    String body = new String(msg.getBody());
//    log.info("checkLocalTransaction message: {}", body);
//    int index = Integer.parseInt(body.split("\\|")[1]);
//    if (index == 2 || index == 3) {
//      int now = counter.incrementAndGet();
//      if (now % 4 == 0) {
//        return LocalTransactionState.COMMIT_MESSAGE;
//      }
//      return LocalTransactionState.UNKNOW;
//    }
//    return LocalTransactionState.COMMIT_MESSAGE;
//  }
//}