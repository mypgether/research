package com.gether.research.test.concurrent;

import com.gether.research.test.redis.BaseRedisTest;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;


/**
 * Created by myp on 2017/9/12.
 */
public class DistributeLockRedisTest extends BaseRedisTest {

  private static final long LOCK_TIMEOUT = 60 * 1000;

  @Test
  public void getLockWithRedisTemplate() throws InterruptedException {
    ForkJoinPool forkJoinPool = new ForkJoinPool(10);
    for (int i = 0; i < 2; i++) {
      forkJoinPool.submit(new NeverStopAction());
    }
    Thread.sleep(20 * 1000);
  }

  @Test
  public void getLockWithJedis() throws InterruptedException {
    String ip = "localhost";
    String lockName = "lock";
    for (int i = 0; i < 4; i++) {
      new Thread(() -> {
        long start = System.currentTimeMillis();

        Jedis jedis = new Jedis(ip, 6379);
        while (true) {
          String result = jedis.set(lockName, "lock value", "NX", "EX", 5);
          if (StringUtils.equalsIgnoreCase(result, "OK")) {
            System.out.println(
                "Thread " + Thread.currentThread().getName() + " get lock cost time " + (
                    System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
          } else {
            System.out.println("Thread " + Thread.currentThread().getName() + " start get lock ");
            try {
              Thread.sleep(500);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }).start();
    }
    Thread.sleep(20 * 1000);
  }

  public class NeverStopAction extends RecursiveAction {

    @Override
    protected void compute() {
      startGetLock();
      invokeAll(new NeverStopAction());
    }
  }

  private void startGetLock() {
    String cacheKey = "lock_key";
    boolean getLock = false;
    try {
      String expireValue = String.valueOf(System.currentTimeMillis() + LOCK_TIMEOUT + 1);
      getLock = redisTemplate.opsForValue().setIfAbsent(cacheKey, expireValue);
      if (!getLock) {
        Object currentValue = redisTemplate.opsForValue().get(cacheKey);
        if (null != currentValue && System.currentTimeMillis() > Long
            .valueOf(String.valueOf(currentValue))) {
          System.out.println("getlock with timeout timestamp start");
          Object oldValue = redisTemplate.opsForValue().getAndSet(cacheKey, expireValue);
          if (oldValue != null && StringUtils
              .equalsIgnoreCase(String.valueOf(oldValue), expireValue)) {
            getLock = true;
            System.out.println("getlock with timeout timestamp success");
          }
        }
      }

      if (getLock) {
        System.err.println("getlock success ");
      } else {
      }
    } finally {
      if (getLock) {
        redisTemplate.delete(cacheKey);
      }
    }
  }
}