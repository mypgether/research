package com.gether.research.test.java8.aqs;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Test;

public class ConditionTest {

  private ReentrantLock lock = new ReentrantLock();
  private Condition condition = lock.newCondition();

  @Test
  public void testCondition() {
    Thread a = new Thread(() -> {
      lock.lock();
      try {
        System.out.println("Thread A await start");
        condition.awaitUninterruptibly();
        System.out.println("Thread A await end");
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        System.out.println("Thread A release lock");
        lock.unlock();
      }
    }, "A");
    Thread b = new Thread(() -> {
      lock.lock();
      try {
        System.out.println("Thread B await start");
        condition.awaitUninterruptibly();
        System.out.println("Thread B await end");
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        System.out.println("Thread B release lock");
        lock.unlock();
      }
    }, "B");

    a.start();
    b.start();

    try {
      System.out.println("waiting Thread A & B ready");

      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    lock.lock();
    try {
      System.out.println("Thread main signalAll start and sleeping 2s");
      condition.signalAll();
      Thread.sleep(2000);
      System.out.println("Thread main signalAll end");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }

    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
