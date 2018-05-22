package com.gether.research.redis;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;

@Configuration
public class JedisConfiguration {

    @Value("${jedis.clusters}")
    private String clusters;
    @Value("${jedis.isCluster}")
    private boolean isCluster;
    @Value("${jedis.password}")
    private String password;

    @Value("${jedis.maxIdle}")
    private int maxIdle;
    @Value("${jedis.maxTotal}")
    private int maxTotal;
    @Value("${jedis.minIdle}")
    private int minIdle;
    @Value("${jedis.maxWaitMillis}")
    private long maxWaitMillis;
    @Value("${jedis.isBorrow}")
    private boolean isBorrow;


    @Bean
    public RedisConnectionFactory connectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = null;
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(isBorrow);
        poolConfig.setTestOnReturn(true);
        if (isCluster) {
            RedisClusterConfiguration redisCluster = new RedisClusterConfiguration(Arrays.asList(clusters.split(",")));
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
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}