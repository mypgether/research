package com.gether.research.test.java8.aqs;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import org.junit.Test;

public class SemaphoreTest {

  @Test
  public void testSemaphore() {
    int maxCount = 10;
    Semaphore semaphore = new Semaphore(maxCount);
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
          semaphore.release();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    }

    try {
      Thread.sleep(12000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSemaphoreSelf() {
    int maxCount = 10;
    SemaphoreSelf semaphore = new SemaphoreSelf(maxCount);
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
          semaphore.release();
        }
      }).start();
    }

    try {
      Thread.sleep(12000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
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
