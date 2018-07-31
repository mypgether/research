package com.gether.research.test.jmm;

import java.util.concurrent.atomic.AtomicInteger;

public class HeapBean {

  private static final AtomicInteger count = new AtomicInteger();

  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public AtomicInteger getCount() {
    return count;
  }
}
