package com.gether.research.test.java8.single;

import java.util.concurrent.CountDownLatch;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class DCLTest {

  @Test
  public void testDoubleCheck() throws InterruptedException {
    for (int j = 0; j < 100; j++) {
      CountDownLatch competion = new CountDownLatch(4);
      for (int i = 0; i < 4; i++) {
        Thread a = new Thread(() -> {
          competion.countDown();
          try {
            competion.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Singleton singleton = Singleton.getSingleton();
          if (singleton == null || !StringUtils.equalsIgnoreCase(singleton.getName(), "name")) {
            System.err.println("name is error, name is " + singleton.getName());
          }
        });
        a.start();

      }
      Thread.sleep(10);
    }
  }
}