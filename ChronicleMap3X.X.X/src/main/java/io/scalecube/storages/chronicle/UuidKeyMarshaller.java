package io.scalecube.storages.chronicle;

import java.util.UUID;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.hash.serialization.BytesReader;
import net.openhft.chronicle.hash.serialization.BytesWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UuidKeyMarshaller implements BytesReader<UUID>, BytesWriter<UUID> {

  @NotNull
  @Override
  public UUID read(Bytes in, @Nullable UUID using) {
    long msb = in.readLong();
    long lsb = in.readLong();
    return new UUID(msb, lsb);
  }

  @Override
  public void write(Bytes out, @NotNull UUID uuid) {
    out.writeLong(uuid.getMostSignificantBits()).writeLong(uuid.getLeastSignificantBits());
  }
}
