package io.scalecube.benchmarks.storages.common.entity;

import java.util.UUID;

public final class UuidUtil {

  /**
   * Converts the given uuid to a byte array.
   *
   * @return byte array
   */
  public static byte[] toBytes(UUID uuid) {
    long mostSignificantBits = uuid.getMostSignificantBits();
    long leastSignificantBits = uuid.getLeastSignificantBits();

    byte[] b = new byte[16];

    for (int i = 7; i > 0; i--) {
      b[i] = (byte) mostSignificantBits;
      mostSignificantBits >>>= 8;

      b[i + 8] = (byte) leastSignificantBits;
      leastSignificantBits >>>= 8;
    }
    b[0] = (byte) mostSignificantBits;
    b[8] = (byte) leastSignificantBits;
    return b;
  }

  /**
   * Converts the given byte array to an uuid.
   *
   * @return uuid
   */
  public static UUID toUuid(byte[] bytes) {
    long msb = 0;
    long lsb = 0;

    for (int i = 0; i < 8; i++) {
      msb = (msb << 8) | (bytes[i] & 0xff);
      lsb = (lsb << 8) | (bytes[i + 8] & 0xff);
    }
    return new UUID(msb, lsb);
  }
}
