package com.gether.research.test.java8.collection;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.Test;

public class ConcureentSkipListTest {

  @Test
  public void putTest() {
    Map<Integer, String> map = new ConcurrentSkipListMap<>();
    for (int i = 0; i <= 100; i++) {
      map.put(i + 2, String.valueOf(i + 2));
    }
    System.out.println(map);

    map.put(20, "20");
    map.put(20, "20");

    ThreadLocalRandom random = ThreadLocalRandom.current();
    System.out.println(random.nextInt());
    System.out.println(random.nextInt() & 0x80000001);

    int rnd = random.nextInt() & 0x80000001;
    int level = 1;
    while (((rnd >>>= 1) & 1) != 0) {
      ++level;
    }
    System.out.println(rnd >>>= 1);
    System.out.println(level);
  }
}
