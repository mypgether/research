package com.gether.research.test.concurrent;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.BiConsumer;


/**
 * Created by myp on 2017/9/12.
 */
public class RedisTest {

  private static final long LOCK_TIMEOUT = 60 * 1000;
  private String clusters = "localhost:6379";
  private boolean isCluster = false;
  private String password;
  private int maxIdle = 5;
  private int maxTotal = 80;
  private int minIdle = 5;
  private long maxWaitMillis = 10000;
  private boolean isBorrow = true;

  RedisTemplate redisTemplate = null;


  @Before
  public void before() {
    JedisConnectionFactory jedisConnectionFactory = null;
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxIdle(maxIdle);
    poolConfig.setMaxTotal(maxTotal);
    poolConfig.setMinIdle(minIdle);
    poolConfig.setMaxWaitMillis(maxWaitMillis);
    poolConfig.setTestOnBorrow(isBorrow);
    poolConfig.setTestOnReturn(true);
    if (isCluster) {
      RedisClusterConfiguration redisCluster = new RedisClusterConfiguration(
          Arrays.asList(clusters.split(",")));
      jedisConnectionFactory = new JedisConnectionFactory(redisCluster, poolConfig);
      if (StringUtils.isNotBlank(password)) {
        jedisConnectionFactory.setPassword(password);
      }
    } else {
      jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
      String[] splits = clusters.split(",");
      jedisConnectionFactory.setHostName(splits[0].split(":")[0]);
      jedisConnectionFactory.setPort(Integer.valueOf(splits[0].split(":")[1]));
      if (StringUtils.isNotBlank(password)) {
        jedisConnectionFactory.setPassword(password);
      }
    }
    jedisConnectionFactory.afterPropertiesSet();

    redisTemplate = new RedisTemplate();
    redisTemplate.setConnectionFactory(jedisConnectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new StringRedisSerializer());
    redisTemplate.afterPropertiesSet();
  }

  @Test
  public void testMap() {
    String key = "a";
    Map<String, String> map = Maps.newHashMap();
    map.put("b", "hgehe");
    map.put("c", "false");
    redisTemplate.opsForHash().putAll(key, map);

    Map<String, String> result = redisTemplate.opsForHash().entries(key);
    result.forEach(new BiConsumer<String, String>() {
      @Override
      public void accept(String s, String s2) {
        System.out.println(s + ":" + s2);
      }
    });

    Object res = redisTemplate.opsForHash().get(key, "c");
    System.out.println(res);
  }

  @Test
  public void testString() {
    String key = "string";
    redisTemplate.opsForValue().set(key, "hehe123");

    Object result = redisTemplate.opsForValue().get(key);
    System.out.println(result);
  }

  @Test
  public void getSet() {
    String cacheKey = "cache_key_1";
    String expireValue = "expireValue_123123";
    Object oldValue = redisTemplate.opsForValue().getAndSet(cacheKey, expireValue);
    System.out.println(oldValue);
  }

  @Test
  public void getLock() {
    ForkJoinPool forkJoinPool = new ForkJoinPool(10);
    for (int i = 0; i < 2; i++) {
      forkJoinPool.submit(new NeverStopAction());
    }
    while (true) {

    }
  }

  @Test
  public void testGetlock() {
    String cacheKey = "hehe_key";
    boolean getLock = redisTemplate.opsForValue()
        .setIfAbsent(cacheKey, String.valueOf(System.currentTimeMillis()));
    System.out.println(getLock);
    getLock = redisTemplate.opsForValue()
        .setIfAbsent(cacheKey, String.valueOf(System.currentTimeMillis()));
    System.out.println(getLock);
  }

  public class NeverStopAction extends RecursiveAction {

    @Override
    protected void compute() {
      //startGetLock();
      startGetLockV2();
      invokeAll(new NeverStopAction());
    }
  }

  private void startGetLock() {
    String cacheKey = "lock_key";
    try {
      boolean getLock = redisTemplate.opsForValue()
          .setIfAbsent(cacheKey, String.valueOf(System.currentTimeMillis()));
      if (getLock) {
        System.err.println("ok getlock");
      } else {
        System.out.println("not get lock");
      }
    } finally {
      redisTemplate.delete(cacheKey);
    }
  }


  private void startGetLockV2() {
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