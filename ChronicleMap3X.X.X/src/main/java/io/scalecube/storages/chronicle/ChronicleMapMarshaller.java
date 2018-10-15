package io.scalecube.storages.chronicle;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.hash.serialization.BytesReader;
import net.openhft.chronicle.hash.serialization.BytesWriter;
import net.openhft.chronicle.hash.serialization.StatefulCopyable;
import net.openhft.chronicle.wire.ValueOut;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireOut;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

/**
 * Is an adapter from a Chronicle Map marshaller to a {@code SerializationFramework}.
 *
 * @param <T> type of thing being marshalled
 * @author Martin Andersson (webmaster at martinandersson.com)
 */
public final class ChronicleMapMarshaller<T>
    implements BytesWriter<T>, BytesReader<T>, StatefulCopyable<ChronicleMapMarshaller<T>> {

  private KryoImpl kryo = new KryoImpl();

  /** {@inheritDoc} */
  @Override
  public void write(Bytes out, @NotNull T toWrite) {
    try (OutputStream os = out.outputStream()) {
      kryo.serialize(toWrite, os);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /** {@inheritDoc} */
  @NotNull
  @Override
  public T read(Bytes in, T using) {
    if (using != null) {
      throw new UnsupportedOperationException();
    }

    try (InputStream is = in.inputStream()) {
      return kryo.deserialize(is);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void writeMarshallable(WireOut wire) {
//    wire.write(() -> "serializer").asEnum(null);
  }

  /** {@inheritDoc} */
  @Override
  public void readMarshallable(WireIn wire) throws IORuntimeException {
//    wire.read(() -> "serializer").asEnum(null);
  }

  /** {@inheritDoc} */
  @Override
  public void writeValue(@NotNull ValueOut out) {
    throw new UnsupportedOperationException("Have no idea what this method is supposed to do");
  }

  /** {@inheritDoc} */
  @Override
  public ChronicleMapMarshaller<T> copy() {
    return this;
  }
}
