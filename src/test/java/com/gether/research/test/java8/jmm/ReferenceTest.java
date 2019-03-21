package com.gether.research.test.java8.jmm;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import org.junit.Test;

/**
 * <p>-XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCDetails</p>
 *
 * @author myp
 * @date 2019/2/17 下午4:21
 */
public class ReferenceTest {

  @Test
  public void testSoftRef() throws InterruptedException {
    SoftReference<ReferenceTest> softReference = new SoftReference<>(new ReferenceTest());
    // 第一次打印软引用所引用的对象
    System.err.println(softReference.get());
    // 进行一次GC
    System.gc();
    // 由于GC进行需要时间，这里等一秒钟
    Thread.sleep(500);
    // 再次打印软引用所引用的对象
    System.err.println(softReference.get());
  }

  @Test
  public void testWeakRef() throws InterruptedException {
    WeakReference<ReferenceTest> weakReference = new WeakReference<>(new ReferenceTest());
    // 第一次打印弱引用所引用的对象
    System.err.println(weakReference.get());
    // 进行一次GC
    System.gc();
    // 由于GC进行需要时间，这里等一秒钟
    Thread.sleep(500);
    // 再次打印弱引用所引用的对象
    System.err.println(weakReference.get());
  }

  @Override
  protected void finalize() throws Throwable {
    System.err.println("finalized");
    if (true) {
      throw new RuntimeException("finalized throw exception");
    }
  }
}
