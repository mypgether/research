package com.gether.research.test.rocketmq;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/12 下午6:10
 */
public class RocketMQTest {

  // gs
  public static final String NAME_SERVER = "172.29.31.100:9876";

  public static final String MSG = "this is msg";

  @Test
  public void testProduce()
      throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
    //Instantiate with a producer group name.
    DefaultMQProducer producer = new DefaultMQProducer("producer_sync");
    // Specify name server addresses.
    producer.setNamesrvAddr(NAME_SERVER);
    //Launch the instance.
    producer.start();
    AtomicInteger count = new AtomicInteger();
    for (int i = 0; i < 2000; i++) {
      //Create a message instance, specifying topic, tag and message body.
      String body = String.format("%s %s", MSG, count.incrementAndGet());
      Message msg = new Message("TopicTest" /* Topic */,
          "Tag" /* Tag */,
          body.getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
      );
      System.out.println(body);
      //Call send message to deliver message to one of brokers.
      try {
        SendResult sendResult = producer.send(msg, new SelectMessageQueueByHash(), i);
        System.out.printf("%s | %s\n", sendResult, new String(msg.getBody()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    //Shut down once the producer instance is not longer in use.
    producer.shutdown();
  }

  @Test
  public void testProduceAsync()
      throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
    //Instantiate with a producer group name.
    DefaultMQProducer producer = new DefaultMQProducer("producer_async");
    // Specify name server addresses.
    producer.setNamesrvAddr(NAME_SERVER);
    //Launch the instance.
    producer.start();
    Message msg = new Message("TopicTest" /* Topic */,
        "Tag" /* Tag */,
        MSG.getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
    );

    int size = 1;
    for (int i = 0; i < size; i++) {
      new Thread(() -> {
        while (true) {
          try {
            producer.send(msg, new SendCallback() {
              @Override
              public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult);
              }

              @Override
              public void onException(Throwable e) {
                System.err.println("send error" + e);
              }
            });
          } catch (MQClientException e) {
            e.printStackTrace();
          } catch (RemotingException e) {
            e.printStackTrace();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }).start();
    }

    while (true) {
      Thread.sleep(500);
    }
  }


  @Test
  public void testConsume() throws MQClientException, InterruptedException {
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer1");
    consumer.setNamesrvAddr(NAME_SERVER);
    consumer.setPullBatchSize(10);
    consumer.setConsumeMessageBatchMaxSize(10);
    // Subscribe one more more topics to consume.
    consumer.subscribe("TopicTest", "*");
    AtomicInteger count = new AtomicInteger();
    // Register callback to execute on arrival of messages fetched from brokers.
    consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
//      int cnt = count.incrementAndGet();
//      if (cnt % 10 == 0) {
//        try {
//          Thread.sleep(1000);
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//      }
//      for (MessageExt msg : msgs) {
//        String body = new String(msg.getBody());
//        System.out.println(
//            Thread.currentThread().getName() + "|" + context.getMessageQueue() + "|" + body);
//      }
      System.out.println(
          Thread.currentThread().getName() + "|" + context.getMessageQueue() + "|" + msgs.size());
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
//    consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
//      int cnt = count.incrementAndGet();
//      if (cnt % 10 == 0) {
//        try {
//          Thread.sleep(100);
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//      }
//      for (MessageExt msg : msgs) {
//        String body = new String(msg.getBody());
//        System.out.println(context.getMessageQueue() + "|" + body);
//      }
//      return ConsumeOrderlyStatus.SUCCESS;
//    });

    //Launch the consumer instance.
    consumer.start();

    System.out.printf("Consumer Started.%n");

    while (true) {
      Thread.sleep(500);
    }
  }
}
