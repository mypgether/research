package com.gether.research.test.encryption;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import sun.misc.BASE64Decoder;

/**
 * @author myp
 * @date 2019/4/29 上午10:23
 */
@Slf4j
public class Aes128GcmTest {

  //generate key, possible values  128 bit key (16), 192(24), 256(32)
  private static final int AES_KEY_SIZE = 32;
  private static final int AES_TAG_LEN = 128;
  private static final int IV_SIZE = 12;

  @Test
  public void decrypt() throws Exception {
    String key = "UjpRBXswDcfhjqyIxZISDg==";

    String realContent = "foobar";
    byte[] encryptByte = encrypt(realContent.getBytes(), key);
    byte[] decryptByte = decrypt(encryptByte, key);
    log.info("after decrypt content: {}", new String(decryptByte));
  }

  public byte[] encrypt(byte[] data, String key) throws GeneralSecurityException, IOException {
    Cipher aes = Cipher.getInstance("AES/GCM/NoPadding");

    log.info("cipher.getBlockSize(): {}", aes.getBlockSize());
    byte[] keyBytes = new BASE64Decoder().decodeBuffer(key);
    SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
    aes.init(Cipher.ENCRYPT_MODE, secretKey);
    GCMParameterSpec parameterSpec = aes.getParameters().getParameterSpec(GCMParameterSpec.class);
    byte[] encrypted = aes.doFinal(data);
    if (encrypted.length != aes.getOutputSize(data.length)) {
      throw new GeneralSecurityException(
          "Encrypted Output Size does not match cipher.getOutputSize");
    }
    byte[] iv = parameterSpec.getIV();
    log.info("iv size: {}", iv.length);
    return ByteBuffer.allocate(IV_SIZE + encrypted.length).put(iv).put(encrypted).array();
  }

  public byte[] decrypt(byte[] encryptedData, String key)
      throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    ByteBuffer buffer = ByteBuffer.wrap(encryptedData);
    byte[] iv = new byte[IV_SIZE];
    buffer.get(iv);
    GCMParameterSpec parameterSpec = new GCMParameterSpec(AES_TAG_LEN, iv);

    byte[] keyBytes = new BASE64Decoder().decodeBuffer(key);
    SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

    Cipher aes = Cipher.getInstance("AES/GCM/NoPadding");
    aes.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

    byte[] encryptedMessage = new byte[encryptedData.length - IV_SIZE];
    buffer.get(encryptedMessage);
    return aes.doFinal(encryptedMessage);
  }
}
