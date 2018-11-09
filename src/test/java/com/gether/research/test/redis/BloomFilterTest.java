package com.gether.research.test.redis;

import com.gether.research.redis.BloomFilter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author myp
 * @date 2018/10/25 下午2:37
 */
public class BloomFilterTest {

  @Test
  public void testFilter() {
    long expectedInsertions = 500000;
    double fpp = 0.001F;

    String host = "127.0.0.1";
    int port = 6379;
    BloomFilter bloomFilter = new BloomFilter(expectedInsertions, fpp, host, port);

    long start = System.currentTimeMillis();
    String key = "bloom_test_" + start;

    Assert.assertFalse(bloomFilter.isExist(key, "gether.me1"));
    Assert.assertTrue(bloomFilter.isExist(key, "gether.me1"));
    Assert.assertFalse(bloomFilter.isExist(key, "gether1"));
    Assert.assertTrue(bloomFilter.isExist(key, "gether1"));
  }

  @Test
  public void testFilte123r() {
    int a = 2;
    System.out.println("a 非的结果是：" + (~a));
    System.out.println("a 非的结果是：" + (~a));
    System.out.println("a 非的结果是：" + (~a));
  }
}
