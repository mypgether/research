package com.gether.research.test.java8.jmm;

import java.lang.reflect.Field;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import sun.misc.Unsafe;

/**
 * @author myp
 * @date 2019/4/26 下午3:44
 */
@Slf4j
public class UnsafeTest {

  @Test
  public void swapLong() throws Exception {
    Long random = 100L;
    Unsafe unsafe = getUnsafe();
    long offset = unsafe.objectFieldOffset(Long.class.getDeclaredField("value"));
    System.out.println("value offset " + offset);
    unsafe.getAndSetLong(random, offset, 20L);
    System.out.println("random=" + random);
  }

  @Test
  public void unlock() throws Exception {
    ReentrantLock lock = new ReentrantLock();
    Thread firstThread = new Thread(() -> {
      lock.lock();
      try {
        log.info("thread a getlock");
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
        log.info("thread a release lock");
      }
    });
    Thread secondThread = new Thread(() -> {
      lock.lock();
      try {
        log.info("thread b getlock");
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
        log.info("thread b release lock");
      }
    });
    firstThread.start();
    secondThread.start();

    firstThread.join();
    secondThread.join();
  }

  private Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
    Field theUnsafeFiled = Unsafe.class.getDeclaredField("theUnsafe");
    theUnsafeFiled.setAccessible(true);
    return (Unsafe) theUnsafeFiled.get(null);
  }
}