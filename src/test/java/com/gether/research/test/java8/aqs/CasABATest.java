package com.gether.research.test.java8.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class CasABATest {

  private static AtomicInteger atomicInteger = new AtomicInteger(100);
  private static AtomicStampedReference atomicStampedReference = new AtomicStampedReference(100, 1);

  public static void main(String[] args) throws InterruptedException {

    //AtomicInteger
    Thread at1 = new Thread(() -> {
      atomicInteger.compareAndSet(100, 110);
      atomicInteger.compareAndSet(110, 100);
    });

    Thread at2 = new Thread(() -> {
      try {
        TimeUnit.SECONDS.sleep(2);      // at1,执行完
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("AtomicInteger:" + atomicInteger.compareAndSet(100, 120));
    });

    at1.start();
    at2.start();

    at1.join();
    at2.join();

    //AtomicStampedReference
    Thread tsf1 = new Thread(() -> {
      try {
        //让 tsf2先获取stamp，导致预期时间戳不一致
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      // 预期引用：100，更新后的引用：110，预期标识getStamp() 更新后的标识getStamp() + 1
      atomicStampedReference.compareAndSet(100, 110, atomicStampedReference.getStamp(),
          atomicStampedReference.getStamp() + 1);
      atomicStampedReference.compareAndSet(110, 100, atomicStampedReference.getStamp(),
          atomicStampedReference.getStamp() + 1);
    });

    Thread tsf2 = new Thread(() -> {
      int stamp = atomicStampedReference.getStamp();

      try {
        TimeUnit.SECONDS.sleep(4);      //线程tsf1执行完
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("AtomicStampedReference:" + atomicStampedReference
          .compareAndSet(100, 120, stamp, stamp + 1));
    });

    tsf1.start();
    tsf2.start();
    tsf1.join();
    tsf2.join();
  }
}