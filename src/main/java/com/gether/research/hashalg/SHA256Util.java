package com.gether.research.hashalg;

import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SHA256Util {

  private static final String salt_key1 = "key1";
  private static final String salt_key2 = "key2";
  private static final String salt_key3 = "key3";

  public static String hmac(String str, String message) throws IOException {
    String yymmdd = getUTCTimeStr();
    // 原始参数基本sha
    byte[] messageByte = DigestUtils.sha256(message);

    // 第1次用时间与str做HMAC签名
    byte[] kDate = HmacUtils.hmacSha256(str, yymmdd);

    // 第2次拿签名后的2进制与salt_key1 再做一次 hmac签名
    byte[] kRegion = HmacUtils.hmacSha256(kDate, salt_key1.getBytes("utf-8"));

    // 第3次拿签名后的2进制kRegion与salt_key2 再做一次 hmac签名
    byte[] kService = HmacUtils.hmacSha256(kRegion, salt_key2.getBytes("utf-8"));

    // 第3次拿签名后的2进制kService与salt_key3 再做一次 hmac签名
    byte[] sigkey = HmacUtils.hmacSha256(kService, salt_key3.getBytes("utf-8"));

    // 计算最后签名，sigkey 和 原始参数基本sha的值计算
    String sig = HmacUtils.hmacSha256Hex(sigkey, messageByte);
    return sig;
  }

  private static String getUTCTimeStr() {
    // 1、取得本地时间：
    Calendar cal = Calendar.getInstance();
    // 2、取得时间偏移量：
    int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
    // 3、取得夏令时差：
    int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
    // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
    cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

    DateTimeFormatter dtf1 = DateTimeFormat.forPattern("yyyyMMdd");
    return dtf1.print(cal.getTimeInMillis());
  }


  public static void main(String[] args) {
    try {
      String utc = getUTCTimeStr();
      System.out.println(utc);
      long time = System.currentTimeMillis();
      System.out.println(time);
      String message = "start" + time + "end";
      System.out.println("message:" + message);
      System.out.println("sig:" + SHA256Util.hmac("heheheheh", message));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}