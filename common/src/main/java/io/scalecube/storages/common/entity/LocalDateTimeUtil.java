package io.scalecube.storages.common.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import net.openhft.chronicle.bytes.Bytes;

/**
 * todo see https://github.com/OpenHFT/Chronicle-Wire. {@link net.openhft.chronicle.wire.RawWire.RawValueOut#dateTime(java.time.LocalDateTime)}
 */
public final class LocalDateTimeUtil {

  /**
   * Writes LocalDateTime to the given output.
   *
   * @param value value
   * @param out output
   */
  public static void writeObject(LocalDateTime value, Bytes out) {
    writeLocalDate(out, value.toLocalDate());
    writeLocalTime(out, value.toLocalTime());
  }

  /**
   * Reads LocalDateTime from the given input.
   *
   * @param in input
   * @return LocalDateTime value
   */
  public static LocalDateTime readObject(Bytes in) {
    LocalDate date = readLocalDate(in);
    LocalTime time = readLocalTime(in);
    return LocalDateTime.of(date, time);
  }

  private static LocalDate readLocalDate(Bytes in) {
    int year = in.readInt();
    int month = in.readByte();
    int dayOfMonth = in.readByte();
    return LocalDate.of(year, month, dayOfMonth);
  }

  private static LocalTime readLocalTime(Bytes in) {
    int hour = in.readByte();
    int minute = 0;
    int second = 0;
    int nano = 0;
    if (hour < 0) {
      hour = ~hour;
    } else {
      minute = in.readByte();
      if (minute < 0) {
        minute = ~minute;
      } else {
        second = in.readByte();
        if (second < 0) {
          second = ~second;
        } else {
          nano = in.readInt();
        }
      }
    }
    return LocalTime.of(hour, minute, second, nano);
  }

  private static void writeLocalDate(Bytes out, LocalDate localDate) {
    out.writeInt(localDate.getYear());
    out.writeByte((byte) localDate.getMonthValue());
    out.writeByte((byte) localDate.getDayOfMonth());
  }

  private static void writeLocalTime(Bytes out, LocalTime localTime) {
    int nano = localTime.getNano();
    int second = localTime.getSecond();
    int minute = localTime.getMinute();
    int hour = localTime.getHour();
    if (nano == 0) {
      if (second == 0) {
        if (minute == 0) {
          out.writeByte((byte) ~hour);
        } else {
          out.writeByte((byte) hour);
          out.writeByte((byte) ~minute);
        }
      } else {
        out.writeByte((byte) hour);
        out.writeByte((byte) minute);
        out.writeByte((byte) ~second);
      }
    } else {
      out.writeByte((byte) hour);
      out.writeByte((byte) minute);
      out.writeByte((byte) second);
      out.writeInt(nano);
    }
  }
}
