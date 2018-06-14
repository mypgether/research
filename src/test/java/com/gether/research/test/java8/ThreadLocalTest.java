package com.gether.research.test.java8;

import io.netty.util.HashedWheelTimer;
import io.netty.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLocalTest {

  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  @Test
  public void testTime() throws InterruptedException {
    HashedWheelTimer timer = new HashedWheelTimer();
    timer.start();

    log.info("task1 start");
    TimerTask task1 = timeout -> log.info("timeout: {}", timeout.toString());
    timer.newTimeout(task1, 2, TimeUnit.SECONDS);

    Thread.sleep(1000);

    log.info("task2 start");
    TimerTask task2 = timeout -> log.info("timeout: {}", timeout.toString());
    timer.newTimeout(task2, 3, TimeUnit.SECONDS);

    Thread.sleep(10000);
  }

  ThreadLocal<String> threadLocal = new ThreadLocal<>();
  ThreadLocal<String> threadLocalb = new ThreadLocal<>();

  @Test
  public void threadLocalTest() throws InterruptedException {
    threadLocal.set("nihao");
    threadLocalb.set("heheh ");

    new Thread(() -> {
      threadLocal.set("hehe string");
      threadLocalb.set("b string");

      Thread a = Thread.currentThread();
      System.out.println(a.getName());
      System.out.println("slave thread get -->" + threadLocal.get());
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "slave thread 1").start();

    while (true) {
      Thread main = Thread.currentThread();
      System.out.println(main.getName());

      System.out.println("main thread get -->" + threadLocal.get());
      System.out.println("main thread get -->" + threadLocalb.get());
      Thread.sleep(5000);
    }
  }
}
