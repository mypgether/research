package com.gether.research.test.java8.jmm;

/**
 * <p>-Xms86m -Xmx86m -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCDetails</p>
 * <p>2019-02-17T15:51:47.368-0800: 0.651: [GC (Allocation Failure) [PSYoungGen: 21683K->1497K(25600K)] 21683K->15367K(84480K), 0.0129812 secs] [Times: user=0.02 sys=0.01, real=0.02 secs]</p>
 * <p>2019-02-17T15:51:47.414-0800: 0.697: [Full GC (Ergonomics) [PSYoungGen: 5265K->3696K(25600K)] [ParOldGen: 58870K->58870K(58880K)] 64135K->62566K(84480K), [Metaspace: 3010K->3010K(1056768K)], 0.0074207 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]</p>
 * <p>2019-02-17T15:51:47.421-0800: 0.705: [Full GC (Allocation Failure) [PSYoungGen: 3696K->3679K(25600K)] [ParOldGen: 58870K->58870K(58880K)] 62566K->62549K(84480K), [Metaspace: 3010K->3010K(1056768K)], 0.0060477 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]</p>
 * <p>Heap</p>
 * <p>PSYoungGen      total 25600K, used 4546K [0x00000007be380000, 0x00000007c0000000, 0x00000007c0000000)</p>
 * <p> eden space 22016K, 20% used [0x00000007be380000,0x00000007be7f0bf8,0x00000007bf900000)</p>
 * <p>from space 3584K, 0% used [0x00000007bf900000,0x00000007bf900000,0x00000007bfc80000)</p>
 * <p>to   space 3584K, 0% used [0x00000007bfc80000,0x00000007bfc80000,0x00000007c0000000)</p>
 * <p>ParOldGen       total 58880K, used 58870K [0x00000007baa00000, 0x00000007be380000, 0x00000007be380000)</p>
 * <p>object space 58880K, 99% used [0x00000007baa00000,0x00000007be37d820,0x00000007be380000)</p>
 * <p>Metaspace       used 3042K, capacity 4568K, committed 4864K, reserved 1056768K</p>
 * <p>class space    used 321K, capacity 392K, committed 512K, reserved 1048576K</p>
 */
public class GCTest {

  public static void main(String[] args) throws InterruptedException {
    byte[] allocation1, allocation2, allocation3, allocation4, allocation5, allocation6;
    allocation1 = new byte[13870 * 1024];
    // minor gc
    allocation2 = new byte[900 * 1024];
    allocation3 = new byte[1000 * 1024];
    allocation4 = new byte[1000 * 1024];
    allocation5 = new byte[45000 * 1024];

    // full gc
    allocation6 = new byte[20000 * 1024];
  }
}