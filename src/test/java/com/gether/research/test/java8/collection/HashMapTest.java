package com.gether.research.test.java8.collection;

import com.google.common.collect.Maps;
import java.util.Map;
import org.junit.Test;

public class HashMapTest {

  static final int HASH_BITS = 0x7fffffff; // usable bits of normal node hash

  @Test
  public void testHashKey() {
    String key = "key";
    int h = key.hashCode();
    int code = (h ^ (h >>> 16)) & HASH_BITS;
    System.out.println(Integer.toBinaryString(h));
    System.out.println(h >>> 16);
    System.out.println(code);
  }

  @Test
  public void getId() {
    System.out.println(Integer.numberOfLeadingZeros(16) | (1 << (16 - 1)));
  }

  @Test
  public void putIfAbsent() {
    Map<String, String> map = Maps.newConcurrentMap();
    map.put("a", "b");
    System.out.println(map.put("b", "c"));
    System.out.println(map.put("b", "c"));
    System.out.println(map.putIfAbsent("b", "d"));
    System.out.println(map.get("b"));
  }
}