package io.scalecube.benchmarks.storages.chronicle.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import net.openhft.chronicle.bytes.Bytes;

public final class BigDecimalUtil {

  /**
   * Writes BigDecimal to the given output.
   *
   * @param value value
   * @param out output
   */
  public static void writeObject(BigDecimal value, Bytes out) {
    BigInteger bigInteger = value.unscaledValue();
    out.writeInt(value.scale());
    if (bigInteger.bitLength() > 63) { // put as a bytes' array
      byte[] bytes = bigInteger.toByteArray();
      out.writeInt(bytes.length);
      out.write(bytes);
    } else { // put as a long value
      out.writeInt(0);
      out.writeLong(bigInteger.longValue());
    }
  }

  /**
   * Reads BigDecimal from the given input.
   *
   * @param in input
   * @return BigDecimal value
   */
  public static BigDecimal readObject(Bytes in) {
    int scale = in.readInt();
    int bytesLength = in.readInt();
    if (bytesLength == 0) { // value as a long
      long unscaledValue = in.readLong();
      return BigDecimal.valueOf(unscaledValue, scale);
    }
    // value as a bytes' array
    byte[] bytes = new byte[bytesLength];
    in.read(bytes);
    return new BigDecimal(new BigInteger(bytes), scale);
  }
}
