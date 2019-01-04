package com.gether.research.test.java8.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.Test;

public class FutureTest {

  @Test
  public void testFutureWithInterrupted() {
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(100));
    Future<String> future = poolExecutor.submit(() -> {
      long start = System.currentTimeMillis();
      while (true) {
        System.out.println("thread is doing activeCount: " + poolExecutor.getActiveCount());
        Thread.sleep(500);
        if (System.currentTimeMillis() - start >= 4000) {
          break;
        }
      }
      return "thread done";
    });
    try {
      String result = future.get(2, TimeUnit.SECONDS);
      System.out.println(result);
    } catch (InterruptedException e) {
      System.err.println("thread error with InterruptedException");
    } catch (ExecutionException e) {
      System.err.println("thread error with ExecutionException");
    } catch (TimeoutException e) {
      System.err.println("thread error with TimeoutException");
      // 可以取消任务
      future.cancel(true);
    }

    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("poolSize : " + poolExecutor.getActiveCount());
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
