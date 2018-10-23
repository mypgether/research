package com.gether.research.hashalg;

public class CRC {

  /**
   * Parameters represents set of parameters defining a particular CRC algorithm.
   */
  public static class Parameters {

    private int width;   // Width of the CRC expressed in bits
    private long polynomial; // Polynomial used in this CRC calculation
    private boolean reflectIn;   // Refin indicates whether input bytes should be reflected
    private boolean reflectOut;   // Refout indicates whether input bytes should be reflected
    private long init; // Init is initial value for CRC calculation
    private long finalXor; // Xor is a value for final xor to be applied before returning result

    public Parameters(int width, long polynomial, long init, boolean reflectIn, boolean reflectOut,
        long finalXor) {
      this.width = width;
      this.polynomial = polynomial;
      this.reflectIn = reflectIn;
      this.reflectOut = reflectOut;
      this.init = init;
      this.finalXor = finalXor;
    }

    public Parameters(Parameters orig) {
      width = orig.width;
      polynomial = orig.polynomial;
      reflectIn = orig.reflectIn;
      reflectOut = orig.reflectOut;
      init = orig.init;
      finalXor = orig.finalXor;
    }

    public int getWidth() {
      return width;
    }

    public long getPolynomial() {
      return polynomial;
    }

    public boolean isReflectIn() {
      return reflectIn;
    }

    public boolean isReflectOut() {
      return reflectOut;
    }

    public long getInit() {
      return init;
    }

    public long getFinalXor() {
      return finalXor;
    }

    /**
     * CCITT CRC parameters
     */
    public static final Parameters CCITT = new Parameters(16, 0x1021, 0x00FFFF, false, false, 0x0);
    /**
     * CRC16 CRC parameters, also known as ARC
     */
    public static final Parameters CRC16 = new Parameters(16, 0x8005, 0x0000, true, true, 0x0);
    /**
     * XMODEM is a set of CRC parameters commonly referred as "XMODEM"
     */
    public static final Parameters XMODEM = new Parameters(16, 0x1021, 0x0000, false, false, 0x0);
    /**
     * XMODEM2 is another set of CRC parameters commonly referred as "XMODEM"
     */
    public static final Parameters XMODEM2 = new Parameters(16, 0x8408, 0x0000, true, true, 0x0);

    /**
     * CRC32 is by far the the most commonly used CRC-32 polynom and set of parameters
     */
    public static final Parameters CRC32 = new Parameters(32, 0x04C11DB7, 0x00FFFFFFFFL, true, true,
        0x00FFFFFFFFL);
    /**
     * IEEE is an alias to CRC32
     */
    public static final Parameters IEEE = CRC32;
    /**
     * Castagnoli polynomial. used in iSCSI. And also provided by hash/crc32 package.
     */
    public static final Parameters Castagnoli = new Parameters(32, 0x1EDC6F41L, 0x00FFFFFFFFL, true,
        true, 0x00FFFFFFFFL);
    /**
     * CRC32C is an alias to Castagnoli
     */
    public static final Parameters CRC32C = Castagnoli;
    /**
     * Koopman polynomial
     */
    public static final Parameters Koopman = new Parameters(32, 0x741B8CD7L, 0x00FFFFFFFFL, true,
        true, 0x00FFFFFFFFL);

    /**
     * CRC64ISO is set of parameters commonly known as CRC64-ISO
     */
    public static final Parameters CRC64ISO = new Parameters(64, 0x000000000000001BL,
        0xFFFFFFFFFFFFFFFFL, true, true, 0xFFFFFFFFFFFFFFFFL);
    /**
     * CRC64ECMA is set of parameters commonly known as CRC64-ECMA
     */
    public static final Parameters CRC64ECMA = new Parameters(64, 0x42F0E1EBA9EA3693L,
        0xFFFFFFFFFFFFFFFFL, true, true, 0xFFFFFFFFFFFFFFFFL);

  }

  /**
   * Reverses order of last count bits.
   *
   * @param in value wich bits need to be reversed
   * @param count indicates how many bits be rearranged
   * @return the value with specified bits order reversed
   */
  private static long reflect(long in, int count) {
    long ret = in;
    for (int idx = 0; idx < count; idx++) {
      long srcbit = 1L << idx;
      long dstbit = 1L << (count - idx - 1);
      if ((in & srcbit) != 0) {
        ret |= dstbit;
      } else {
        ret = ret & (~dstbit);
      }
    }
    return ret;
  }

  /**
   * This method implements simple straight forward bit by bit calculation. It is relatively slow
   * for large amounts of data, but does not require any preparation steps. As a result, it might be
   * faster in some cases then building a table required for faster calculation.
   *
   * Note: this implementation follows section 8 ("A Straightforward CRC Implementation") of Ross N.
   * Williams paper as even though final/sample implementation of this algorithm provided near the
   * end of that paper (and followed by most other implementations) is a bit faster, it does not
   * work for polynomials shorter then 8 bits.
   *
   * @param crcParams CRC algorithm parameters
   * @param data data for the CRC calculation
   * @return the CRC value of the data provided
   */
  public static long calculateCRC(Parameters crcParams, byte[] data) {
    long curValue = crcParams.init;
    long topBit = 1L << (crcParams.width - 1);
    long mask = (topBit << 1) - 1;

    for (int i = 0; i < data.length; i++) {
      long curByte = ((long) (data[i])) & 0x00FFL;
      if (crcParams.reflectIn) {
        curByte = reflect(curByte, 8);
      }

      for (int j = 0x80; j != 0; j >>= 1) {
        long bit = curValue & topBit;
        curValue <<= 1;

        if ((curByte & j) != 0) {
          bit ^= topBit;
        }

        if (bit != 0) {
          curValue ^= crcParams.polynomial;
        }
      }

    }

    if (crcParams.reflectOut) {
      curValue = reflect(curValue, crcParams.width);
    }

    curValue = curValue ^ crcParams.finalXor;

    return curValue & mask;
  }

  private Parameters crcParams;
  private long initValue;
  private long[] crctable;
  private long mask;

  /**
   * Returns initial value for this CRC intermediate value This method is used when starting a new
   * iterative CRC calculation (using init, update and finalCRC methods, possibly supplying data in
   * chunks).
   *
   * @return initial value for this CRC intermediate value
   */
  public long init() {
    return initValue;
  }

  /**
   * This method is used to feed data when performing iterative CRC calculation (using init, update
   * and finalCRC methods, possibly supplying data in chunks). It can be called multiple times per
   * CRC calculation to feed data to be processed in chunks.
   *
   * @param curValue CRC intermediate value so far
   * @param chunk data chunk to b processed by this call
   * @param offset is 0-based offset of the data to be processed in the array supplied
   * @param length indicates number of bytes to be processed.
   * @return updated intermediate value for this CRC
   */
  public long update(long curValue, byte[] chunk, int offset, int length) {
    if (crcParams.reflectIn) {
      for (int i = 0; i < length; i++) {
        byte v = chunk[offset + i];
        curValue = crctable[(((byte) curValue) ^ v) & 0x00FF] ^ (curValue >>> 8);
      }
    } else if (crcParams.width < 8) {
      for (int i = 0; i < length; i++) {
        byte v = chunk[offset + i];
        curValue =
            crctable[((((byte) (curValue << (8 - crcParams.width))) ^ v) & 0xFF)] ^ (curValue << 8);
      }
    } else {
      for (int i = 0; i < length; i++) {
        byte v = chunk[offset + i];
        curValue =
            crctable[((((byte) (curValue >>> (crcParams.width - 8))) ^ v) & 0xFF)] ^ (curValue
                << 8);
      }
    }

    return curValue;
  }

  /**
   * A convenience method for feeding a complete byte array of data.
   *
   * @param curValue CRC intermediate value so far
   * @param chunk data chunk to b processed by this call
   * @return updated intermediate value for this CRC
   */
  public long update(long curValue, byte[] chunk) {
    return update(curValue, chunk, 0, chunk.length);
  }

  /**
   * This method should be called to retrieve actual CRC for the data processed so far.
   *
   * @param curValue CRC intermediate value so far
   * @return calculated CRC
   */
  public long finalCRC(long curValue) {
    long ret = curValue;
    if (crcParams.reflectOut != crcParams.reflectIn) {
      ret = reflect(ret, crcParams.width);
    }
    return (ret ^ crcParams.finalXor) & mask;
  }

  /**
   * A convenience method allowing to calculate CRC in one call.
   *
   * @param data is data to calculate CRC on
   * @return calculated CRC
   */
  public long calculateCRC(byte[] data) {
    long crc = init();
    crc = update(crc, data);
    return finalCRC(crc);
  }

  /**
   * Constructs a new CRC processor for table based CRC calculations. Underneath, it just calls
   * finalCRC() method.
   *
   * @param crcParams CRC algorithm parameters
   * @throws RuntimeException if CRC sum width is not divisible by 8
   */
  public CRC(Parameters crcParams) {
    this.crcParams = new Parameters(crcParams);

    initValue = (crcParams.reflectIn) ? reflect(crcParams.init, crcParams.width) : crcParams.init;
    this.mask = ((crcParams.width >= 64) ? 0 : (1L << crcParams.width)) - 1;
    this.crctable = new long[256];

    byte[] tmp = new byte[1];

    Parameters tableParams = new Parameters(crcParams);

    tableParams.init = 0;
    tableParams.reflectOut = tableParams.reflectIn;
    tableParams.finalXor = 0;
    for (int i = 0; i < 256; i++) {
      tmp[0] = (byte) i;
      crctable[i] = CRC.calculateCRC(tableParams, tmp);
    }
  }

  /**
   * Is a convenience method to spare end users from explicit type conversion every time this
   * package is used. Underneath, it just calls finalCRC() method.
   *
   * @param curValue current intermediate crc state value
   * @return the final CRC value
   * @throws RuntimeException if crc being calculated is not 8-bit
   */
  public byte finalCRC8(long curValue) {
    if (crcParams.width != 8) {
      throw new RuntimeException("CRC width mismatch");
    }
    return (byte) finalCRC(curValue);
  }

  /**
   * Is a convenience method to spare end users from explicit type conversion every time this
   * package is used. Underneath, it just calls finalCRC() method.
   *
   * @param curValue current intermediate crc state value
   * @return the final CRC value
   * @throws RuntimeException if crc being calculated is not 16-bit
   */
  public short finalCRC16(long curValue) {
    if (crcParams.width != 16) {
      throw new RuntimeException("CRC width mismatch");
    }
    return (short) finalCRC(curValue);
  }

  /**
   * Is a convenience method to spare end users from explicit type conversion every time this
   * package is used. Underneath, it just calls finalCRC() method.
   *
   * @param curValue current intermediate crc state value
   * @return the final CRC value
   * @throws RuntimeException if crc being calculated is not 32-bit
   */
  public int finalCRC32(long curValue) {
    if (crcParams.width != 32) {
      throw new RuntimeException("CRC width mismatch");
    }
    return (int) finalCRC(curValue);
  }

}