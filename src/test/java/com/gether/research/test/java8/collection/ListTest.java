package com.gether.research.test.java8.collection;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/2/28 上午10:20
 */
public class ListTest {

  @Test
  public void testRemove() {
    List<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.add("2");
    list.add("2");
    list.add("2");
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equalsIgnoreCase("2")) {
        list.remove(i);
      }
    }
    System.out.println(list);
  }

  @Test(expected = ConcurrentModificationException.class)
  public void testRemoveForEach() {
    List<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.add("2");
    list.add("2");
    list.add("2");
    for (String s : list) {
      if (s.equals("2")) {
        list.remove(s);
      }
    }
    System.out.println(list);
  }

  @Test
  public void testRemoveForEachList() {
    List<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.add("2");
    list.add("2");
    list.add("2");
    list.forEach((s) -> {
      if (s.equals("2")) {
        list.remove(s);
      }
      System.out.println(s);
    });
    System.out.println(list);
  }

  @Test
  public void testRemoveIter() {
    List<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.add("2");
    list.add("2");
    list.add("2");
    ListIterator<String> iter = list.listIterator();
    while (iter.hasNext()) {
      String now = iter.next();
      if (now.equalsIgnoreCase("2")) {
        iter.remove();
      }
    }
    System.out.println(list);
  }
}
