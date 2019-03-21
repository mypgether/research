package com.gether.research.test.redis;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ScanOptions.ScanOptionsBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * @author myp
 * @date 2018/10/30 下午8:57
 */
public class RedisTest extends BaseRedisTest {

  @Test
  public void testListEncoding() {
    for (int i = 0; i < 512; i++) {
      redisTemplate.opsForList()
          .leftPush("list", "ppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");
    }
  }

  @Test
  public void testHash() {
    // more 64
    String valueLong = "ppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp";
    // 64
    String valueSmall = "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp";

    Map<String, String> map = Maps.newHashMap();
    map.put("b", valueSmall);
    map.put("c", valueSmall);
    redisTemplate.opsForHash().putAll("hash", map);

    map = Maps.newHashMap();
    map.put("b", valueLong);
    map.put("c", valueLong);
    redisTemplate.opsForHash().putAll("hash_long", map);
  }

  @Test
  public void batchSetString() {
    for (int i = 0; i < 50; i++) {
      redisTemplate.opsForValue().set("op_" + i, String.valueOf(System.currentTimeMillis()));
    }
  }

  @Test
  public void testScanRedisTemplate() {
    Cursor<byte[]> result = (Cursor<byte[]>) redisTemplate
        .execute((RedisCallback) redisConnection -> {
          ScanOptionsBuilder scanOption = ScanOptions.scanOptions().match("op_*").count(10);
          return redisConnection.scan(scanOption.build());
        });
    System.out.println(result.getCursorId());
    System.out.println(result.getPosition());

    long count = 0;
    while (result.hasNext()) {
      System.out.println(new String(result.next()));
      count = count + 1;
    }
    System.err.println(count);
  }

  @Test
  public void testScanJedis() throws InterruptedException {
    Jedis jedis = new Jedis();

    String startCursor = "0";
    ScanParams scanParams = new ScanParams();
    scanParams.match("op_*");
    scanParams.count(10);

    long count = 0;
    while (true) {
      ScanResult<String> result = jedis.scan(startCursor, scanParams);
      System.out.println(result.getStringCursor());
      System.out.println(result.getResult());
      count = count + result.getResult().size();

      if (StringUtils.equals(result.getStringCursor(), "0")) {
        break;
      }
      Thread.sleep(1000);
      startCursor = result.getStringCursor();
    }
    System.err.println(count);
  }

  @Test
  public void testInc() throws InterruptedException {
    int n = 10;
    String key = "inc_key_3";
    CountDownLatch latch = new CountDownLatch(n);
    CountDownLatch waitComplete = new CountDownLatch(n);
    for (int i = 0; i < n; i++) {
      new Thread(() -> {
        try {
          latch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        for (int j = 0; j < 10000; j++) {
          redisTemplate.opsForValue().increment(key, 1);
        }
        waitComplete.countDown();
      }).start();
      latch.countDown();
    }

    waitComplete.await();
    System.out.println(redisTemplate.opsForValue().get(key));
  }
}
