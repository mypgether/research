package com.gether.research.test.java8.jmm;

import java.nio.ByteBuffer;

/**
 * 每个方法的参数m都是表示对应区间分配多少M内存
 */
public class MemoryTest {

  private static final int _1m = 1024 * 1024;

  private static final long THREAD_SLEEP_MS = 10000 * 1000;

  public static void main(String[] args) throws Exception {
//    youngAllocate(1000);
//    oldAllocate(1000);
//    directMemoryAllocate(400);
    threadStackAllocate(400);
    Thread.sleep(6000000);
  }


  /**
   * @param m 分配多少M direct memory
   */
  private static void directMemoryAllocate(int m) {
    System.out.println("direct memory: " + m + "m");
    for (int i = 0; i < m; i++) {
      ByteBuffer.allocateDirect(_1m);
    }
    System.out.println("direct memory end");
  }

  /**
   * @param m 给young区分配多少M的数据
   */
  private static void youngAllocate(int m) {
    System.out.println("young: " + m + "m");
    for (int i = 0; i < m; i++) {
      byte[] test = new byte[_1m];
    }
    System.out.println("young end");
  }

  /**
   * 需要配置参数: -XX:PretenureSizeThreshold=2M, 并且结合CMS
   *
   * @param m 给old区分配多少M的数据
   */
  private static void oldAllocate(int m) {
    System.out.println("old:   " + m + "m");
    for (int i = 0; i < m / 5; i++) {
      byte[] test = new byte[5 * _1m];
    }
    System.out.println("old end");
  }

  // 需要配置参数: -Xss10240k, 这里的实验以失败告终
  private static void threadStackAllocate(int m) {
    int threadCount = m / 10;
    System.out.println("thread stack count:" + threadCount);
    for (int i = 0; i < threadCount; i++) {
      new Thread(() -> {
        System.out.println("thread name: " + Thread.currentThread().getName());
        try {
          while (true) {
            Thread.sleep(THREAD_SLEEP_MS);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    }
    System.out.println("thread stack end:" + threadCount);
  }
}