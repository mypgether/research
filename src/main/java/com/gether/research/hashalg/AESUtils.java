package com.gether.research.hashalg;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 */
public class AESUtils {

  private static final String IV_PARAM = "dda639ffb96d8724";

  /**
   * 加密
   */
  public static String Encrypt(String sSrc, String sKey) throws Exception {
    if (sKey == null) {
      System.out.print("Key为空null");
      return null;
    }
    // 判断Key是否为16位
    if (sKey.length() != 16) {
      System.out.print("Key长度不是16位");
      return null;
    }
    byte[] raw = sKey.getBytes();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    //"算法/模式/补码方式"
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
    IvParameterSpec iv = new IvParameterSpec(IV_PARAM.getBytes());
    // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
    byte[] encrypted = cipher.doFinal(sSrc.getBytes());
    //此处使用BASE64做转码功能，同时能起到2次加密的作用。
    return new BASE64Encoder().encode(encrypted);
  }

  /**
   * 解密
   */
  public static String Decrypt(String sSrc, String sKey) throws Exception {
    try {
      // 判断Key是否正确
      if (sKey == null) {
        System.out.print("Key为空null");
        return null;
      }
      // 判断Key是否为16位
      if (sKey.length() != 16) {
        System.out.print("Key长度不是16位");
        return null;
      }
      byte[] raw = sKey.getBytes("ASCII");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
      IvParameterSpec iv = new IvParameterSpec(IV_PARAM.getBytes());
      cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
      //先用base64解密
      byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
      try {
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
      } catch (Exception e) {
        System.out.println(e.toString());
        return null;
      }
    } catch (
        Exception ex) {
      System.out.println(ex.toString());
      return null;
    }
  }

  public static void main(String[] args) throws Exception {
    /*         * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定         * 此处使用AES-128-CBC加密模式，key需要为16位。         */
    // 需要加密的字串
    String cKey = "1290c17708b0351c";
    String cSrc = "hehehehasldjkldfklp12908312uy13e.'as";
    System.out.println(cSrc);
    long lStart = System.currentTimeMillis();
    String enString = Encrypt(cSrc, cKey);
    System.out.println("加密后的字串是：" + enString);
    long lUseTime = System.currentTimeMillis() - lStart;
    System.out.println("加密耗时：" + lUseTime
        + "毫秒");
    System.out.println(
        enString.equals("AkE2eORKmPdJAO20pV/sxJdqkAMaep2mF+S4ckd6Zj+/vXzNJ1Uh805AUkTbM2++"));
    lStart = System.currentTimeMillis();
    String DeString = Decrypt(enString, cKey);
    System.out.println("解密后的字串是：" + DeString);
    lUseTime = System.currentTimeMillis() - lStart;
    System.out.println("解密耗时：" + lUseTime + "毫秒");
  }
}