package com.gether.research.test.concurrent;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutionException;

/**
 * Created by myp on 2017/8/15.
 */
public class DistributeLockTest {

  @Test
  public void redisLock() throws ExecutionException, InterruptedException {
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
    while (true) {
      Thread.sleep(100);
    }
  }
}