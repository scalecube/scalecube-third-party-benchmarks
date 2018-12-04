package io.scalecube.benchmarks.storages.chronicle.marshallers;

import io.scalecube.benchmarks.storages.chronicle.utils.BigDecimalUtil;
import io.scalecube.benchmarks.storages.common.entity.Fill;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.hash.serialization.BytesReader;
import net.openhft.chronicle.hash.serialization.BytesWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FillMarshaller implements BytesReader<Fill>, BytesWriter<Fill> {

  @NotNull
  @Override
  public Fill read(Bytes in, @Nullable Fill using) {
    Fill fill = new Fill();
    fill.price(BigDecimalUtil.readObject(in));
    fill.quantity(BigDecimalUtil.readObject(in));
    fill.timestamp(in.readLong());
    return fill;
  }

  @Override
  public void write(Bytes out, @NotNull Fill fill) {
    BigDecimalUtil.writeObject(fill.price(), out);
    BigDecimalUtil.writeObject(fill.quantity(), out);
    out.writeLong(fill.timestamp());
  }
}
