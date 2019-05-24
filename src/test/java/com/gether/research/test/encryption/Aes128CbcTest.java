package com.gether.research.test.encryption;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author myp
 * @date 2019/4/29 上午10:23
 */
@Slf4j
public class Aes128CbcTest {

  @Test
  public void encrypt() throws Exception {
    String content = "foobar";
    SecretKey secretKey = generateSecretKey();

    String stringSecretKey = new BASE64Encoder().encode(secretKey.getEncoded());
    log.info("secretkey: {}", stringSecretKey);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] iv = cipher.getIV();
    try (FileOutputStream fileOut = new FileOutputStream("/Users/myp/Desktop/en.txt");
        CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
      fileOut.write(iv);
      cipherOut.write(content.getBytes());
    }
  }

  @Test
  public void decrypt() throws Exception {
    String content;

    byte[] buffer = new BASE64Decoder().decodeBuffer("j1AT4M+F2aAMpN/KV/WF+A==");
    SecretKey originalKey = generateSecretKey(buffer);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    try (FileInputStream fileIn = new FileInputStream("/Users/myp/Desktop/en.txt")) {
      byte[] fileIv = new byte[16];
      fileIn.read(fileIv);
      cipher.init(Cipher.DECRYPT_MODE, originalKey, new IvParameterSpec(fileIv));

      CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
      InputStreamReader inputReader = new InputStreamReader(cipherIn);
      BufferedReader reader = new BufferedReader(inputReader);

      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      content = sb.toString();
    }
    log.info("after decrypt content: {}", content);
  }

  public SecretKey generateSecretKey(byte[] bytes) {
    SecretKey secretKey = new SecretKeySpec(bytes, "AES");
    return secretKey;
  }

  public SecretKey generateSecretKey() throws NoSuchAlgorithmException {
    try {
      KeyGenerator generator = KeyGenerator.getInstance("AES");
      generator.init(128, new SecureRandom());
      return generator.generateKey();
    } catch (Exception e) {
      throw e;
    }
  }
}
