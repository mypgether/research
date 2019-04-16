package com.gether.research.test.java8.nio;


import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author myp
 * @date 2019/3/28 下午3:57
 */
@Slf4j
public class FileChannelTest {

  private static final String NIO_FILE_PATH = System.getProperty("user.dir") + "/file/nio/";
  public static final String MSG = "01234567";
  public static final String MSG2 = "91234567";

  @Test
  public void testFileChannelWrite() throws Exception {
    String filePath = NIO_FILE_PATH + "test";
    RandomAccessFile aFile = new RandomAccessFile(filePath, "rw");
    FileChannel channel = aFile.getChannel();
    ByteBuffer buffer = ByteBuffer.allocate(16);
    buffer.put(MSG.getBytes());
    buffer.put(MSG2.getBytes());
    channel.write(buffer);
  }

  @Test
  public void testFileChannelRead() throws Exception {
    String filePath = NIO_FILE_PATH + "test";
    RandomAccessFile aFile = new RandomAccessFile(filePath, "r");
    FileChannel channel = aFile.getChannel();
    ByteBuffer buffer = ByteBuffer.allocate(8);
    channel.read(buffer);
    log.info("{}", new String(buffer.array()));

    ByteBuffer buffer2 = ByteBuffer.allocate(8);
    channel.read(buffer2);
    log.info("{}", new String(buffer2.array()));
  }
}