package com.gether.research.test.java8.aqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 验证读写锁逻辑：当前如果有读锁，那就不允许加上写锁
 *
 * @author myp
 * @date 2018/11/26 下午2:33
 */
@Slf4j
public class ReadWriteLockTest {

  @Test
  public void testRead() throws InterruptedException {
    ExecutorService executors = Executors.newFixedThreadPool(10);
    ReadWriteLock lock = new ReentrantReadWriteLock();

    executors.submit(() -> {
      Lock readLock = lock.readLock();
      readLock.lock();
      try {
        log.info("readLock thread get lock");
        TimeUnit.SECONDS.sleep(300);
      } catch (InterruptedException e) {
        log.info("readLock InterruptedException");
      } finally {
        readLock.unlock();
        log.info("readLock thread release lock");
      }
    });
    executors.submit(() -> {
      Lock writeLock = lock.writeLock();
      writeLock.lock();
      try {
        log.info("writeLock thread get lock");
        TimeUnit.SECONDS.sleep(300);
      } catch (InterruptedException e) {
        log.info("writeLock InterruptedException");
      } finally {
        writeLock.unlock();
        log.info("writeLock thread release lock");
      }
    });

    TimeUnit.SECONDS.sleep(3);
    executors.shutdown();
    System.out.println("executors is shutdown");
  }
}
