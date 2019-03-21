package com.gether.research.test.ms;

import org.junit.Test;

/**
 * <p>启动两个线程, 一个输出 1,3,5,7…99, 另一个输出 2,4,6,8…100 最后 STDOUT 中按序输出 1,2,3,4,5…100</p>
 * <p>用 Java 的wait +notify机制来实现</p>
 *
 * @author myp
 * @date 2019/2/15 下午2:30
 */
public class ABCPrintTest {

  @Test
  public void print() throws InterruptedException {
    Object object = new Object();
    for (int i = 0; i <= 100; i++) {
      Thread threadA = new ThreadA(object);
      Thread threadB = new ThreadB(object);

      threadA.start();
      threadB.start();
      threadA.join();
      threadB.join();
    }
  }

  // 2 4 6 8 10
  public class ThreadA extends Thread {

    private Object shareObj;

    public ThreadA(Object shareObj) {
      this.shareObj = shareObj;
    }

    @Override
    public void run() {
      for (int i = 1; i <= 100; i += 2) {
        synchronized (shareObj) {
          shareObj.notify();
          System.out.println(i);

          if (i > 100) {
            break;
          }

          try {
            shareObj.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  // 1 3 5 7 9
  public class ThreadB extends Thread {

    private Object shareObj;

    public ThreadB(Object shareObj) {
      this.shareObj = shareObj;
    }

    @Override
    public void run() {
      for (int i = 2; i <= 100; i += 2) {
        synchronized (shareObj) {
          shareObj.notify();

          System.out.println(i);

          if (i >= 100) {
            break;
          }

          try {
            shareObj.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}