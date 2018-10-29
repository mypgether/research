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

    Assert.assertFalse(bloomFilter.isExist("bloom_test", "gether.me1"));
    Assert.assertTrue(bloomFilter.isExist("bloom_test", "gether.me1"));
    Assert.assertFalse(bloomFilter.isExist("bloom_test", "gether1"));
    Assert.assertTrue(bloomFilter.isExist("bloom_test", "gether1"));
  }
}
