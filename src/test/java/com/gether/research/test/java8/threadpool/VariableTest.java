package com.gether.research.test.java8.threadpool;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class VariableTest {

  private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
  private static final int COUNT_BITS = Integer.SIZE - 3;
  private static final int CAPACITY = (1 << COUNT_BITS) - 1;

  // runState is stored in the high-order bits
  private static final int RUNNING = -1 << COUNT_BITS;
  private static final int SHUTDOWN = 0 << COUNT_BITS;
  private static final int STOP = 1 << COUNT_BITS;
  private static final int TIDYING = 2 << COUNT_BITS;
  private static final int TERMINATED = 3 << COUNT_BITS;

  // Packing and unpacking ctl
  private static int runStateOf(int c) {
    return c & ~CAPACITY;
  }

  private static int workerCountOf(int c) {
    return c & CAPACITY;
  }

  private static int ctlOf(int rs, int wc) {
    return rs | wc;
  }

  @Test
  public void testCtl() {
    ctl.set(100);
    System.out.println(runStateOf(ctl.get()));
    System.out.println(workerCountOf(ctl.get()));

    // 非 运算符
    System.out.println(~CAPACITY);
    System.out.println(CAPACITY);

    System.err.println(RUNNING);
    System.err.println(SHUTDOWN);
    System.err.println(STOP);
    System.err.println(TIDYING);
    System.err.println(TERMINATED);
  }
}
