package com.gether.research.test.java8.aqs;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.LongAdder;
import org.junit.Test;

public class CyclicBarrierTest {

  @Test
  public void testLongAdder() {
    LongAdder longAdder = new LongAdder();
    longAdder.add(1000);
    longAdder.add(100);
    longAdder.add(10);
    longAdder.add(9);
    System.out.println(longAdder.sumThenReset());
    System.out.println(longAdder.longValue());
  }

  @Test
  public void testCyclicBarrier() throws InterruptedException {
    int threads = 10;
    CyclicBarrier cb = new CyclicBarrier(threads);
    for (int i = 0; i < threads; i++) {
      final int index = i;
      new Thread(() -> {
        System.out.println("thread " + String.valueOf(index) + " ready");
        int result = -1;
        try {
          result = cb.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (BrokenBarrierException e) {
          e.printStackTrace();
        }
        System.out.println("thread " + String.valueOf(index) + " start , result is " + result);
      }).start();
    }

    Thread.sleep(1000);
  }

  @Test
  public void testCyclicBarrierTimeOut() throws InterruptedException {
    int threads = 10;
    CyclicBarrier cb = new CyclicBarrier(threads);
    for (int i = 0; i < threads; i++) {
      final int index = i;
      new Thread(() -> {
        int result = -1;
        System.out.println("thread " + String.valueOf(index) + " ready");
        try {
          if (index == threads - 1) {
            Thread.sleep(3000);
          }

          if (index == 0 || index == 1) {
            result = cb.await(((long) index), TimeUnit.SECONDS);
          } else {
            result = cb.await();
          }
        } catch (InterruptedException e) {
          System.err.println("thread " + String.valueOf(index) + " InterruptedException");
        } catch (BrokenBarrierException e) {
          System.err.println("thread " + String.valueOf(index) + " BrokenBarrierException");
        } catch (TimeoutException e) {
          System.err.println("thread " + String.valueOf(index) + " TimeoutException");
        }
        if (result != -1) {
          System.out.println("thread " + String.valueOf(index) + " start , result is " + result);
        }
      }).start();
    }

    Thread.sleep(5000);
  }

  @Test
  public void testCyclicBarrierMainRun() throws InterruptedException {
    int threads = 10;
    CyclicBarrier cb = new CyclicBarrier(threads, () -> System.out.println("all is ready"));
    for (int i = 0; i < threads; i++) {
      final int index = i;
      new Thread(() -> {
        int result = -1;
        System.out.println("thread " + String.valueOf(index) + " ready");
        try {
          result = cb.await();
        } catch (InterruptedException e) {
          System.err.println("thread " + String.valueOf(index) + " InterruptedException");
        } catch (BrokenBarrierException e) {
          System.err.println("thread " + String.valueOf(index) + " BrokenBarrierException");
        }
        if (result != -1) {
          System.out.println("thread " + String.valueOf(index) + " start , result is " + result);
        }
      }).start();
    }

    Thread.sleep(3000);
  }


  @Test
  public void testCyclicBarrierMainRu123n() throws InterruptedException {
    int threads = 2;
    CyclicBarrier cb = new CyclicBarrier(threads, () -> System.out.println("all is ready"));
    for (int i = 0; i < threads; i++) {
      final int index = i;
      new Thread(() -> {
        int result = -1;
        System.out.println("thread " + String.valueOf(index) + " ready");
        try {
          result = cb.await();

          if (index == 1) {
            cb.reset();
          }
        } catch (InterruptedException e) {
          System.err.println("thread " + String.valueOf(index) + " InterruptedException");
        } catch (BrokenBarrierException e) {
          System.err.println("thread " + String.valueOf(index) + " BrokenBarrierException");
        }
        if (result != -1) {
          System.out.println("thread " + String.valueOf(index) + " start , result is " + result);
        }
      }).start();
    }

    Thread.sleep(3000);
  }
}