package com.gether.research.test.mq.rocketmq;

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
public class MQFileParseTest {

  private static final String RMQ_FILE_PATH = System.getProperty("user.dir") + "/file/rmq/";

  @Test
  public void testParseRocketMQFile() throws Exception {
    String filePath = RMQ_FILE_PATH + "00000000000000000000";
    RandomAccessFile raf = new RandomAccessFile(filePath, "r");
    log.info("filePath is {} ,file length {}", filePath, raf.length());
    byte[] offsetByte = new byte[8];
    while (raf.read(offsetByte) != -1) {
      // read offset
      long offset = bytes2long(offsetByte);
      log.info("offset {}", offset);
      if (offset == 0) {
        // no more data
        break;
      }

      // read size
      byte[] sizeByte = new byte[4];
      raf.read(sizeByte);
      int size = byte2int(sizeByte);
      log.info("size {}", size);

      // read hash
      byte[] hashByte = new byte[8];
      raf.read(hashByte);
      long hashCode = bytes2long(hashByte);
      log.info("hashCode {}", hashCode);
    }
  }

  @Test
  public void testWriteQueueFileWithRAF() throws Exception {
    String filePath = RMQ_FILE_PATH + "00000000000000000001";
    RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
    log.info("filePath is {} ,file length {}", filePath, raf.length());
    Long offset = 0L;
    int size = 5;
    String body = "thisa";
    log.info("body size: {}", body.getBytes().length);
    for (int i = 0; i < 10; i++) {
      raf.writeLong(offset);
      raf.writeInt(size);
      raf.writeBytes(body);
      offset = offset + 8 + 4 + body.getBytes().length;
    }
  }

  @Test
  public void testReadQueueFileWithRAF() throws Exception {
    String filePath = RMQ_FILE_PATH + "00000000000000000001";
    RandomAccessFile raf = new RandomAccessFile(filePath, "r");
    log.info("filePath is {} ,file length {}", filePath, raf.length());
    byte[] offsetByte = new byte[8];
    while (raf.read(offsetByte) != -1) {
      log.info("offset {}", bytes2long(offsetByte));

      byte[] sizeByte = new byte[4];
      raf.read(sizeByte);
      int size = byte2int(sizeByte);
      log.info("size {}", size);

      byte[] bodyByte = new byte[size];
      raf.read(bodyByte);
      log.info("body {}", new String(bodyByte));
    }
  }

  @Test
  public void testReadQueueFileWithFileChannel() throws Exception {
    String filePath = RMQ_FILE_PATH + "00000000000000000001";
    RandomAccessFile raf = new RandomAccessFile(filePath, "r");
    FileChannel fileChannel = raf.getChannel();
    log.info("filePath is {} ,file length {}", filePath, raf.length());

    ByteBuffer offsetBF = ByteBuffer.allocate(8);
    while (fileChannel.read(offsetBF) != -1) {
      offsetBF.flip();
      log.info("offset {}", bytes2long(offsetBF.array()));
      offsetBF.clear();

      ByteBuffer sizeBF = ByteBuffer.allocate(4);
      fileChannel.read(sizeBF);
      sizeBF.flip();
      log.info("size {}", byte2int(sizeBF.array()));
      sizeBF.clear();

      ByteBuffer bodyBF = ByteBuffer.allocate(byte2int(sizeBF.array()));
      fileChannel.read(bodyBF);
      bodyBF.flip();
      log.info("body {}", new String(bodyBF.array()));
      bodyBF.clear();
    }
  }

  public static int byte2int(byte[] b) {
    return ((b[3] & 0xFF) << 0) +
        ((b[2] & 0xFF) << 8) +
        ((b[1] & 0xFF) << 16) +
        ((b[0]) << 24);
  }

  public static long bytes2long(byte[] bytes) {
    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.put(bytes);
    buffer.flip();
    return buffer.getLong();
  }
}