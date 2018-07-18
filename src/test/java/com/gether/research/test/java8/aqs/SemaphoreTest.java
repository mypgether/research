package com.gether.research.test.java8.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import org.junit.Test;

public class SemaphoreTest {

  @Test
  public void testSemaphore() throws InterruptedException {
    int maxCount = 10;
    int count = maxCount * 2;

    Semaphore semaphore = new Semaphore(maxCount);
    CountDownLatch latch = new CountDownLatch(count);
    ExecutorService pool = Executors.newFixedThreadPool(count);
    for (int i = 0; i < count; i++) {
      int finalI = i;
      Thread t = new Thread(() -> {
        try {
          long start = System.currentTimeMillis();
          System.out.println("Thread " + finalI + " get semaphore start");
          semaphore.acquire();
          if (finalI < maxCount) {
            Thread.sleep((finalI + 2) * 1000);
          } else {
            Thread.sleep(finalI * 50);
          }
          System.out.println(
              "Thread " + finalI + " get semaphore end, cost time " + (System.currentTimeMillis()
                  - start));
          semaphore.release();
          latch.countDown();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      pool.submit(t);
    }

    latch.await();
  }

  @Test
  public void testSemaphoreSelf() throws InterruptedException {
    int maxCount = 10;
    int count = maxCount * 2;

    SemaphoreSelf semaphore = new SemaphoreSelf(maxCount);
    CountDownLatch latch = new CountDownLatch(count);
    for (int i = 0; i < maxCount * 2; i++) {
      int finalI = i;
      new Thread(() -> {
        try {
          long start = System.currentTimeMillis();
          System.out.println("Thread " + finalI + " get semaphore start");
          semaphore.acquire();
          if (finalI < maxCount) {
            Thread.sleep((finalI + 2) * 1000);
          } else {
            Thread.sleep(finalI * 50);
          }
          System.out.println(
              "Thread " + finalI + " get semaphore end, cost time " + (System.currentTimeMillis()
                  - start));
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          latch.countDown();
          semaphore.release();
        }
      }).start();
    }

    latch.await();
  }

  public class SemaphoreSelf {

    private Sync sync;

    public SemaphoreSelf(int limits) {
      sync = new Sync(limits);
    }

    public void acquire() {
      sync.acquireShared(1);
    }

    public void release() {
      sync.releaseShared(1);
    }

    public class Sync extends AbstractQueuedSynchronizer {

      public Sync(int limits) {
        setState(limits);
      }

      @Override
      protected int tryAcquireShared(int arg) {
        int c = getState();
        if (c <= 0) {
          return -1;
        }
        if (compareAndSetState(c, c - 1)) {
          return 1;
        }
        return -1;
      }

      @Override
      protected boolean tryReleaseShared(int arg) {
        for (; ; ) {
          int c = getState();
          if (compareAndSetState(c, c + 1)) {
            return true;
          }
        }
      }
    }
  }
}
