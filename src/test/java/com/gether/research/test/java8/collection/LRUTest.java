package com.gether.research.test.java8.collection;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/4/28 下午3:32
 */
@Slf4j
public class LRUTest {

  private static class LruCache<K, V> extends LinkedHashMap<K, V> {

    private final int MAX_ENTRIES;

    public LruCache(int maxEntries) {
      super(maxEntries + 1, 1.0F, true);
      this.MAX_ENTRIES = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
      return this.size() > this.MAX_ENTRIES;
    }
  }

  @Test
  public void testLRU() {
    LruCache<String, String> lruCache = new LruCache<>(5);
    for (int i = 0; i <= 10; i++) {
      lruCache.put(String.valueOf(i), String.valueOf(i));
    }
    log.info("cache size: {}", lruCache.size());
    log.info("cache content: {}", lruCache);
  }
}
