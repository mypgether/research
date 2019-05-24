package com.gether.research.test.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by myp on 2017/6/28.
 */
public class GuavaTest {

  @Test
  public void test() {
    Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1)
        .expireAfterWrite(2, TimeUnit.HOURS).build();
    cache.put("b", "b");

    Assert.assertNull(cache.getIfPresent("a"));
    Assert.assertEquals(true, cache.getIfPresent("a") == null);
    Assert.assertEquals("b", cache.getIfPresent("b"));

    cache.invalidateAll();
    Assert.assertNull(cache.getIfPresent("b"));
    Assert.assertEquals(0, cache.size());
  }
}