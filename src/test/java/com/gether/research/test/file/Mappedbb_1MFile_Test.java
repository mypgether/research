package com.gether.research.test.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import org.junit.After;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/21 下午7:30
 */
public class Mappedbb_1MFile_Test {

  public static final String filepath_from = "/tmp/tmp.log";
  public static final String filepath_to = "/tmp/file_to.txt";
  long sum = 0;
  int count = 2;

  @After
  public void after() {
    File outFile = new File(filepath_to);
    outFile.delete();
  }

  /**
   * copy costTime: 6576
   * copy costTime: 4218
   * copy costTime: 3930
   * copy costTime: 5083
   * copy costTime: 5851
   * copy costTime: 6197
   * copy costTime: 8005
   * copy costTime: 5586
   * copy costTime: 6046
   * copy costTime: 5284
   * copy costTime: 6418
   * copy costTime: 6120
   * copy costTime: 6275
   * copy costTime: 5941
   * copy costTime: 4268
   * copy costTime: 3986
   * copy costTime: 3999
   * copy costTime: 4427
   * copy costTime: 7304
   * copy costTime: 5146
   * count: 20 totalTime: 110660
   * @throws IOException
   */
  @Test
  public void testCopyMappedByteBuffer() throws IOException {
    for (int c = 0; c < count; c++) {
      RandomAccessFile rafi = new RandomAccessFile(filepath_from, "r");
      RandomAccessFile rafo = new RandomAccessFile(filepath_to, "rw");
      FileChannel fci = rafi.getChannel();
      FileChannel fco = rafo.getChannel();
      long size = fci.size();
      long start = System.currentTimeMillis();

      MappedByteBuffer mbbi = fci.map(FileChannel.MapMode.READ_ONLY, 0, size);
      MappedByteBuffer mbbo = fco.map(FileChannel.MapMode.READ_WRITE, 0, size);
      for (int i = 0; i < size; i++) {
        byte b = mbbi.get(i);
        mbbo.put(i, b);
      }
//      // this is prefer
//      fco.write(mbbi);
      fci.close();
      fco.close();
      rafi.close();
      rafo.close();
      long nowCost = System.currentTimeMillis() - start;
      sum += nowCost;
      System.out.println("copy costTime: " + nowCost);
    }
    System.out.println("count: " + count + " totalTime: " + sum);
  }

  /**
   * copy costTime: 8338
   * copy costTime: 5990
   * copy costTime: 4705
   * copy costTime: 4041
   * copy costTime: 5279
   * copy costTime: 7866
   * copy costTime: 4868
   * copy costTime: 6853
   * copy costTime: 4497
   * copy costTime: 3825
   * copy costTime: 5480
   * copy costTime: 5667
   * copy costTime: 3966
   * copy costTime: 7505
   * copy costTime: 4862
   * copy costTime: 2941
   * copy costTime: 3855
   * copy costTime: 3772
   * copy costTime: 2587
   * copy costTime: 2534
   * count: 20 totalTime: 99431
   * @throws IOException
   */
  @Test
  public void testCopyFileChannel() throws IOException {
    for (int c = 0; c < count; c++) {
      RandomAccessFile rafi = new RandomAccessFile(filepath_from, "r");
      RandomAccessFile rafo = new RandomAccessFile(filepath_to, "rw");
      FileChannel fci = rafi.getChannel();
      FileChannel fco = rafo.getChannel();
      long size = fci.size();
      long start = System.currentTimeMillis();

      fci.transferTo(0, size, fco);
      fci.close();
      fco.close();
      rafi.close();
      rafo.close();
      long nowCost = System.currentTimeMillis() - start;
      sum += nowCost;
      System.out.println("copy costTime: " + nowCost);
    }
    System.out.println("count: " + count + " totalTime: " + sum);
  }
}