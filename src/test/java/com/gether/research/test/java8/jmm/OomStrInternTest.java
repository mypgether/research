package com.gether.research.test.java8.jmm;

import java.util.ArrayList;
import java.util.List;

public class OomStrInternTest {

  static String base = "string";

  public static void main(String[] args) throws InterruptedException {
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < Integer.MAX_VALUE; i++) {
      String str = base + base;
      base = str;
      list.add(str.intern());
      Thread.sleep(100);
    }
  }
}