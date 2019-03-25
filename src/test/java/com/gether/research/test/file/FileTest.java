package com.gether.research.test.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 网络传输：大端，易于读取；小端，适合cpu
 *
 * java使用大端
 *
 * @author myp
 * @date 2019/3/21 下午7:30
 */
public class FileTest {

  int write = 1392;

  public static final String filepath = "/Users/myp/a.txt";
  public static File f;

  @Before
  public void before() throws IOException {
    f = new File(filepath);
    RandomAccessFile randomAccessFile = new RandomAccessFile(f, "rw");
    randomAccessFile.writeInt(write);
    randomAccessFile.close();
  }

  @After
  public void after() {
    f.delete();
  }

  @Test
  public void getIntTest() throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(f, "r");
    int a = randomAccessFile.readInt();
    System.out.println(a);
    Assert.assertEquals(write, a);
  }

  @Test
  public void getIntBytesTest() throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(f, "r");
    byte[] bytes = new byte[4];
    bytes[0] = randomAccessFile.readByte();
    bytes[1] = randomAccessFile.readByte();
    bytes[2] = randomAccessFile.readByte();
    bytes[3] = randomAccessFile.readByte();
    int res = byte2int(bytes);
    System.out.println(res);
    Assert.assertEquals(write, res);
  }

  @Test
  public void seekTest() throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(f, "r");
    randomAccessFile.seek(3);
    byte[] bytes = new byte[4];
    bytes[3] = randomAccessFile.readByte();
    System.out.println(byte2int(bytes));
  }

  public int byte2int(byte[] b) {
    return ((b[3] & 0xFF) << 0) +
        ((b[2] & 0xFF) << 8) +
        ((b[1] & 0xFF) << 16) +
        ((b[0]) << 24);
  }
}