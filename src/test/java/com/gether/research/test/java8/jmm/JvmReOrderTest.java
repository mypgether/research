package com.gether.research.test.java8.jmm;

import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * JVM的重排序测试
 */
public class JvmReOrderTest {

  int a = 0;
  boolean flag = false;

  /**
   * A线程执行
   */
  public void writer() {
    a = 1;                  // 1
    flag = true;            // 2
  }

  /**
   * B线程执行
   */
  public void read() {
    if (flag) {                  // 3
      int i = a + a;          // 4
      if (i != 2) {
        System.err.println(i);
      } else {
//        System.out.println(i);
      }
    }
  }


  /**
   * X86CPU不支持写写重排序，如果是在x86上面操作，这个一定会是a=1
   */
  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 1000; i++) {
      JvmReOrderTest a = new JvmReOrderTest();
      Thread write = new Thread(() -> a.writer());

      Thread read = new Thread(() -> a.read());
      write.start();
      read.start();

    }
    Thread.sleep(1000);
  }

  private boolean stopRunning = false;

  @Test
  public void testReorder() throws InterruptedException {
    Thread nobreakThread = new Thread(() -> {
      int a = 0;
      while (!stopRunning) {
        a++;
//        System.out.println("this is " + a);
      }
      System.err.println("nobreakThread stop at: " + a);
    });
    nobreakThread.start();

    TimeUnit.SECONDS.sleep(1);

    System.err.println("stopRunning set to true");
    stopRunning = true;

    nobreakThread.join(5 * 1000);
  }
}