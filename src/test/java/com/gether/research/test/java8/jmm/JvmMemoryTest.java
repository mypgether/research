package com.gether.research.test.java8.jmm;

import org.junit.Assert;
import org.junit.Test;

public class JvmMemoryTest {

  @Test
  public void testFinalFiled() {
    int newValue = 100;

    HeapBean bean1 = new HeapBean();
    bean1.getCount().set(newValue);

    Assert.assertEquals(newValue, bean1.getCount().get());

    HeapBean bean2 = new HeapBean();
    Assert.assertEquals(newValue, bean2.getCount().get());

    HeapBean bean3 = new HeapBean();
    Assert.assertEquals(newValue, bean3.getCount().get());
  }
}
