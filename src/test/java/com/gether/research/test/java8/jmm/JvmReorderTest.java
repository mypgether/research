package com.gether.research.test.java8.jmm;

/**
 * JVM的重排序测试
 */
public class JvmReorderTest {

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
      JvmReorderTest a = new JvmReorderTest();
      Thread write = new Thread(() -> {
        a.writer();
      });

      Thread read = new Thread(() -> {
        a.read();
      });
      write.start();
      read.start();

    }
    Thread.sleep(1000);
  }
}