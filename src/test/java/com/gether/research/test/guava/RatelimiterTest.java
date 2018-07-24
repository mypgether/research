package com.gether.research.test.guava;

import com.google.common.util.concurrent.RateLimiter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by myp on 2017/6/28.
 */
public class RatelimiterTest {

  @Test
  public void test() throws InterruptedException {
    RateLimiter limiter = RateLimiter.create(5);
    AtomicInteger index = new AtomicInteger(0);
    ForkJoinPool fjPool = new ForkJoinPool(100);
    for (int i = 0; i < 10; i++) {
      fjPool.submit(new NeverStopAction(limiter, index));
    }

    fjPool.awaitTermination(10, TimeUnit.SECONDS);
    fjPool.shutdown();
  }

  public class NeverStopAction extends RecursiveAction {

    RateLimiter limiter;
    AtomicInteger index;

    public NeverStopAction(RateLimiter limiter, AtomicInteger index) {
      this.limiter = limiter;
      this.index = index;
    }

    @Override
    protected void compute() {
      while (true) {
        boolean getLock = limiter.tryAcquire(1);
        if (getLock) {
          System.err.println(
              new DateTime().toString("yyyy-MM-dd HH:mm:ss") + " get access i:" + index
                  .incrementAndGet() + " :" + getLock);
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

        }
      }
    }
  }
}