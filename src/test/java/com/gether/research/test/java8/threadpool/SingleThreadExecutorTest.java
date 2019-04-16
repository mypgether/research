package com.gether.research.test.java8.threadpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/17 上午9:54
 */
@Slf4j
public class SingleThreadExecutorTest {

  private final ScheduledExecutorService scheduledExecutorService = Executors
      .newSingleThreadScheduledExecutor(r -> new Thread(r, "singleThread"));

  @Test
  public void testSingle() throws InterruptedException {
    scheduledExecutorService.scheduleAtFixedRate(() -> {
      log.info("start working a ,threadId: {},threadName: {}", Thread.currentThread().getId(),
          Thread.currentThread().getName());
    }, 10, 2000 * 1, TimeUnit.MILLISECONDS);

    scheduledExecutorService.scheduleAtFixedRate(() -> {
      log.info("start working b ,threadId: {},threadName: {}", Thread.currentThread().getId(),
          Thread.currentThread().getName());

      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, 10, 1000 * 1, TimeUnit.MILLISECONDS);

    scheduledExecutorService.scheduleAtFixedRate(() -> {
      log.info("start working c ,threadId: {},threadName: {}", Thread.currentThread().getId(),
          Thread.currentThread().getName());
    }, 10, 2000 * 1, TimeUnit.MILLISECONDS);

    Thread.sleep(10000);
  }

  @Test
  public void testExecutorCancel() throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(1);
    CountDownLatch over = new CountDownLatch(1);
    long start = System.currentTimeMillis();
    Future<String> f = executor.submit(() -> {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
      }
      System.out.println("task cost times: " + (System.currentTimeMillis() - start));
      over.countDown();
      return "SUCCESS";
    });

    try {
      String res = f.get(100, TimeUnit.MILLISECONDS);
      System.out.println("run ok, result: " + res);
    } catch (ExecutionException e) {
      // 执行报错，可以不返回timeout信息
      System.err.println("ExecutionException error");
    } catch (TimeoutException e) {
      System.err.println("TimeoutException error");
      f.cancel(true);
    }

    over.await(5, TimeUnit.SECONDS);
  }
}