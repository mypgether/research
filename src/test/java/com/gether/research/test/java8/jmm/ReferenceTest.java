package com.gether.research.test.java8.jmm;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import org.junit.Test;

/**
 * 强引用 软引用  内存不够才回收 弱引用  gc后回收 虚引用
 * <p>-XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCDetails</p>
 *
 * @author myp
 * @date 2019/2/17 下午4:21
 */
public class ReferenceTest {

  @Test
  public void testSoftRef() throws InterruptedException {
    ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
    SoftReference<ReferenceTest> softReference = new SoftReference(new ReferenceTest(),
        referenceQueue);
    // 第一次打印软引用所引用的对象
    System.err.println(softReference.get());
    // 进行一次GC
    System.gc();
    // 由于GC进行需要时间，这里等一秒钟
    Thread.sleep(1000);
    // 再次打印软引用所引用的对象
    System.err.println(softReference.get());

    Reference<? extends String> reference = referenceQueue.poll();
    System.out.println(reference); //null
  }

  @Test
  public void testWeakRef() throws InterruptedException {
    WeakReference<ReferenceTest> weakReference = new WeakReference<>(new ReferenceTest());
    // 第一次打印弱引用所引用的对象
    System.err.println(weakReference.get());
    // 进行一次GC
    System.gc();
    // 由于GC进行需要时间，这里等一秒钟
    Thread.sleep(1000);
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
