package com.gether.research.test.redis;

import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author myp
 * @date 2018/10/30 下午8:58
 */
public abstract class BaseRedisTest {

  private String clusters = "localhost:6379";
  private boolean isCluster = false;
  private String password;
  private int maxIdle = 5;
  private int maxTotal = 80;
  private int minIdle = 5;
  private long maxWaitMillis = 10000;
  private boolean isBorrow = true;

  protected RedisTemplate redisTemplate = null;

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
}
