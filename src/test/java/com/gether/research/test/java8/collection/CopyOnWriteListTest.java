package com.gether.research.test.java8.collection;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class CopyOnWriteListTest {

  @Test(expected = UnsupportedOperationException.class)
  public void testCopyOnWrite() {
    List<String> lists = new CopyOnWriteArrayList<>();
    lists.add("1");
    lists.add("2");
    lists.add("3");

    // copy on write not support iter.remove
    for (Iterator<String> iter = lists.iterator(); iter.hasNext(); ) {
      String service = iter.next();
      if (StringUtils.equalsIgnoreCase(service, "2")) {
        iter.remove();
      }
    }
  }
}