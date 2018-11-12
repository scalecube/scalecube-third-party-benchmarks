package io.scalecube.storages.common.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import net.openhft.chronicle.bytes.Bytes;

/**
 * todo see https://github.com/OpenHFT/Chronicle-Wire.
 * {@link net.openhft.chronicle.wire.RawWire.RawValueOut#dateTime(java.time.LocalDateTime)}
 */
public final class LocalDateTimeUtil {

  /**
   * Writes LocalDateTime to the given output.
   *
   * @param value value
   * @param out output
   */
  public static void writeObject(LocalDateTime value, Bytes out) {
    out.writeStopBit(value.toLocalDate().toEpochDay());
    out.writeLong(value.toLocalTime().toNanoOfDay());
  }

  /**
   * Reads LocalDateTime from the given input.
   *
   * @param in input
   * @return LocalDateTime value
   */
  public static LocalDateTime readObject(Bytes in) {
    long epochDay = in.readStopBit();
    long nanoOfDay = in.readLong();

    return LocalDateTime.of(LocalDate.ofEpochDay(epochDay), LocalTime.ofNanoOfDay(nanoOfDay));
  }
}
