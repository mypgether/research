package com.gether.research.test.java8.collection;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
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

  @Test
  public void getCapacity() {
    int cap = 5;
    int n = cap - 1;
    System.out.println(n >>> 1);
    System.out.println(n | n >>> 1);
    System.out.println(n >>> 2);
    System.out.println(n >>> 4);
    System.out.println(n >>> 8);
    System.out.println(n >>> 16);
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    int result = (n < 0) ? 1 : (n >= Integer.MAX_VALUE) ? Integer.MAX_VALUE : n + 1;
    System.out.println(result);
  }

  @Test
  public void testLinkedHashMap() {
    LinkedHashMap map = new LinkedHashMap();
    map.put("b", "b");
    map.put("a", "1");
    map.put("a", "1");
    System.out.println(map);
  }
}