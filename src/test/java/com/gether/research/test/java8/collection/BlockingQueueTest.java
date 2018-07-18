package com.gether.research.test.java8.collection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Test;

public class BlockingQueueTest {

  volatile boolean flag = false;

  @Test
  public void testArrayBlockingQueue() throws InterruptedException {
    BlockingQueue blockingQueue = new ArrayBlockingQueue(100);
    blockingQueue.put("123");
    blockingQueue.put("456");
    blockingQueue.put("7");
    blockingQueue.put("8");
    blockingQueue.put("9");

    blockingQueue.take();
    blockingQueue.take();
    blockingQueue.take();
  }

  @Test
  public void testSyncQueue() throws InterruptedException {
    BlockingQueue blockingQueue = new SynchronousQueue();
    int num = 10;

    ReentrantLock lock = new ReentrantLock();
    Condition produceCondition = lock.newCondition();
    Condition consumeCondition = lock.newCondition();

    Thread takeThread = new Thread(() -> {
      try {
        for (int i = 0; i < num; i++) {
          lock.lock();
          try {
            if (flag) {
              consumeCondition.await();
            }
            Thread.sleep(500);
            produceCondition.signalAll();
            flag = true;
            Object take = blockingQueue.take();
            System.out.println("queue take obj " + (take == null));
          } finally {
            lock.unlock();
          }
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "take-Thread");
    takeThread.start();

    for (int i = 0; i < num; i++) {
      lock.lock();
      try {
        if (!flag) {
          produceCondition.await();
        }
        blockingQueue.add(String.valueOf(i));
        flag = false;
        consumeCondition.signalAll();
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("queue add error, i: " + i);
        throw e;
      } finally {
        lock.unlock();
      }
    }
    takeThread.join();
  }

  @Test
  public void testTransferQueue() throws InterruptedException {
    LinkedTransferQueue queue = new LinkedTransferQueue();

    int num = 10;
    CountDownLatch latch = new CountDownLatch(num);
    Thread pollThread = new Thread(() -> {
      try {
        latch.await();
        for (; ; ) {
          Object res = queue.poll(3, TimeUnit.SECONDS);
          if (res == null) {
            break;
          }
          System.out.println("queue get: " + res);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    pollThread.start();

    for (int i = 0; i < num; i++) {
      queue.offer("this is " + i);
      latch.countDown();
    }
    pollThread.join();
  }
}
