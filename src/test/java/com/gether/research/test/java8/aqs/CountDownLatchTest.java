package com.gether.research.test.java8.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import org.junit.Test;

public class CountDownLatchTest {

  @Test
  public void testCountDownLatch() {
    int threads = 10;
    CountDownLatch cdl = new CountDownLatch(threads);

    Thread main = new Thread(() -> {
      System.out.println("thread main is ready");
      try {
        cdl.await();
        System.out.println("thread main start");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("thread main end");
    });
    main.start();

    for (int i = 0; i < threads; i++) {
      int finalI = i;
      new Thread(() -> {
        try {
          Thread.sleep(finalI * 300);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        cdl.countDown();
        System.out.println("thread " + finalI + " countDown");
      }).start();
    }

    try {
      main.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void countDownLSelf() {
    int threads = 10;
    CountDownL cdl = new CountDownL(threads);

    Thread main = new Thread(() -> {
      System.out.println("thread main is ready");
      cdl.await();
      System.out.println("thread main start");
      System.out.println("thread main end");
    });
    main.start();

    for (int i = 0; i < threads; i++) {
      int finalI = i;
      new Thread(() -> {
        try {
          Thread.sleep(finalI * 300);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        cdl.countDown();
        System.out.println("thread " + finalI + " countDown");
      }).start();
    }

    try {
      main.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public class CountDownL {

    private Sync sync;

    public void await() {
      sync.acquireShared(1);
    }

    public void countDown() {
      sync.releaseShared(1);
    }

    public CountDownL(int count) {
      sync = new Sync(count);
    }

    public class Sync extends AbstractQueuedSynchronizer {

      public Sync(int total) {
        super();
        setState(total);
      }

      @Override
      protected int tryAcquireShared(int arg) {
        return getState() == 0 ? arg : -1;
      }

      @Override
      protected boolean tryReleaseShared(int arg) {
        for (; ; ) {
          int c = getState();
          if (compareAndSetState(c, c - arg)) {
            return true;
          }
        }
      }
    }
  }
}