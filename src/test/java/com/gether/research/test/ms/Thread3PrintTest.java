package com.gether.research.test.ms;

import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/3 下午9:30
 */
public class Thread3PrintTest {

  @Test
  public void print() throws InterruptedException {
    Object objA = new Object();
    Object objB = new Object();
    Object objC = new Object();
    Thread a = new ThreadA(objA, objB, objC);
    Thread b = new ThreadB(objA, objB, objC);
    Thread c = new ThreadC(objA, objB, objC);
    a.start();
    b.start();
    c.start();
    a.join();
    b.join();
    c.join();
  }

  public class ThreadA extends Thread {

    private Object objA;
    private Object objB;
    private Object objC;

    public ThreadA(Object objA, Object objB, Object objC) {
      this.objA = objA;
      this.objB = objB;
      this.objC = objC;
    }

    @Override
    public void run() {
      for (int i = 1; i <= 102; i += 3) {
        synchronized (objB) {
          System.out.println(i);
          objB.notify();
        }

        if (i == 100) {
          break;
        }

        synchronized (objA) {
          try {
            objA.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public class ThreadB extends Thread {

    private Object objA;
    private Object objB;
    private Object objC;

    public ThreadB(Object objA, Object objB, Object objC) {
      this.objA = objA;
      this.objB = objB;
      this.objC = objC;
    }

    @Override
    public void run() {
      for (int i = 2; i <= 102; i += 3) {
        synchronized (objC) {
          System.out.println(i);
          objC.notify();
        }

        if (i == 101) {
          break;
        }

        synchronized (objB) {
          try {
            objB.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public class ThreadC extends Thread {

    private Object objA;
    private Object objB;
    private Object objC;

    public ThreadC(Object objA, Object objB, Object objC) {
      this.objA = objA;
      this.objB = objB;
      this.objC = objC;
    }

    @Override
    public void run() {
      for (int i = 3; i <= 102; i += 3) {
        synchronized (objA) {
          System.out.println(i);
          objA.notify();
        }

        if (i == 102) {
          break;
        }

        synchronized (objC) {
          try {
            objC.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
