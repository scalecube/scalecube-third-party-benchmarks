package io.scalecube.storages.chronicle;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import io.scalecube.storages.common.Order;
import org.objenesis.strategy.SerializingInstantiatorStrategy;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** @author segabriel */
public class KryoImpl {

  /**
   * Is it sad that Kryo insist on using a buffer even though we provide Kryo our own
   * Output-/InputStream?
   *
   * <p>I tested and found no performance impact using the current value 8 versus {@code
   * BUFFER_SIZE}.
   */
  private static final int MIN_BUFFER = 8;

  /** 1361 is the largest byte size I've seen used for a serialized object. */
  private static final int BUFFER_SIZE = 1361;

  private final KryoPool pool;

  public KryoImpl() {
    this.pool =
        new KryoPool.Builder(
                () -> {
                  Kryo kryo = new Kryo();
                  kryo.setRegistrationRequired(true);

                  kryo.register(Order.class);
                  kryo.register(BigDecimal.class);
                  kryo.register(LocalDateTime.class);

                  kryo.setInstantiatorStrategy(new SerializingInstantiatorStrategy());
                  return kryo;
                })
            .softReferences()
            .build();
  }

  public byte[] serialize(Object object) {
    Kryo kryo = pool.borrow();

    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
      Output o = new Output(out, MIN_BUFFER);
      kryo.writeClassAndObject(o, object);
      o.flush();
      return out.toByteArray();
    } finally {
      pool.release(kryo);
    }
  }

  public void serialize(Object object, OutputStream out) {
    Kryo kryo = pool.borrow();

    try (Output o = new Output(out, MIN_BUFFER)) {
      kryo.writeClassAndObject(o, object);
    } finally {
      pool.release(kryo);
    }
  }

  public <T> T deserialize(byte[] bytes) {
    Kryo kryo = pool.borrow();

    try (Input i = new Input(bytes)) {
      @SuppressWarnings("unchecked")
      T t = (T) kryo.readClassAndObject(i);
      return t;
    } finally {
      pool.release(kryo);
    }
  }

  public <T> T deserialize(InputStream in) {
    Kryo kryo = pool.borrow();

    try {
      Input i = new Input(in, MIN_BUFFER);

      @SuppressWarnings("unchecked")
      T t = (T) kryo.readClassAndObject(i);

      return t;
    } finally {
      pool.release(kryo);
    }
  }
}
