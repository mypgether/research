package com.gether.research.test.ms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Test;

//编写两个线程，一个线程打印1~25，另一个线程打印字母A~Z，打印顺序为12A34B56C……5152Z，要求使用线程间的通信。
public class ThreadPrintTest {

  @Test
  public void print() {
    for (int i = 1; i <= 100; i++) {
      ExecutorService tPool = Executors.newFixedThreadPool(2);
      ReentrantLock lock = new ReentrantLock();
      Condition numberPrint = lock.newCondition();
      Condition charPrint = lock.newCondition();

      Thread numberThread = new Thread(() -> {
        lock.lock();
        int indexNumber = 0;

        try {
          while (true) {
            numberPrint.awaitUninterruptibly();

            System.out.print(indexNumber + 1);
            System.out.print(indexNumber + 2);
            indexNumber = indexNumber + 2;

            charPrint.signalAll();
            if (indexNumber >= 52) {
              break;
            }
          }
        } finally {
          lock.unlock();
        }
      });
      Thread charThread = new Thread(() -> {
        lock.lock();
        int indexChar = 0;
        try {
          while (true) {
            charPrint.awaitUninterruptibly();

            char c = (char) ('A' + indexChar);
            System.out.print(c);

            indexChar++;

            numberPrint.signalAll();
            if (indexChar >= 26) {
              System.out.println();
              break;
            }
          }
        } finally {
          lock.unlock();
        }
      });

      tPool.submit(charThread);
      tPool.submit(numberThread);

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      lock.lock();
      try {
        numberPrint.signalAll();
      } finally {
        lock.unlock();
      }
      tPool.shutdown();

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  volatile boolean flag = false;

  @Test
  public void testThreadprint() throws InterruptedException {
    ReentrantLock lock = new ReentrantLock();
    Condition produceCondition = lock.newCondition();
    Condition consumeCondition = lock.newCondition();

    Thread takeThread = new Thread(() -> {
      try {
        int i = 1;
        while (i <= 51) {
          lock.lock();
          try {
            if (flag) {
              consumeCondition.await();
            }
            System.out.print(i);
            System.out.print(i + 1);
            i = i + 2;
            produceCondition.signalAll();
            flag = true;
          } finally {
            lock.unlock();
          }
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "take-Thread");
    takeThread.start();

    int i = 0;
    while (i <= 25) {
      lock.lock();
      try {
        if (!flag) {
          produceCondition.await();
        }
        char c = (char) (i + 'A');
        System.out.print(c);
        flag = false;
        i = i + 1;
        consumeCondition.signalAll();
      } finally {
        lock.unlock();
      }
    }
    takeThread.join();
  }
}