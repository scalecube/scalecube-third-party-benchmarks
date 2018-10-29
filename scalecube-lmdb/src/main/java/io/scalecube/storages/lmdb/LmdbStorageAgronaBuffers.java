package io.scalecube.storages.lmdb;

import static org.lmdbjava.DirectBufferProxy.PROXY_DB;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.entity.Order;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.UUID;
import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.lmdbjava.Cursor;
import org.lmdbjava.Dbi;
import org.lmdbjava.DbiFlags;
import org.lmdbjava.Env;
import org.lmdbjava.EnvFlags;
import org.lmdbjava.Txn;
import reactor.core.Exceptions;

/**
 * LMDB storage connector with the following configuration:
 *
 * <p>1. Uses Agrona buffers. 2. Don't fsync after commit. 3. Use writable mmap.
 */
public class LmdbStorageAgronaBuffers implements Storage<UUID, Order> {

  public static final String DB_NAME = "LmdbStorageAgronaBuffers";

  private Env<DirectBuffer> env;
  private Dbi<DirectBuffer> db;

  public LmdbStorageAgronaBuffers(BenchmarkSettings settings) {
  }

  @Override
  public void start() {
    File path = new File(".");
    env = Env.create(PROXY_DB)
        .setMapSize(10_485_760_000L)
        .setMaxDbs(1)
        .setMaxReaders(Integer.MAX_VALUE)
        .open(path, EnvFlags.MDB_NOSYNC, EnvFlags.MDB_WRITEMAP);

    db = env.openDbi(DB_NAME, DbiFlags.MDB_CREATE);
    System.getProperties().setProperty(Env.DISABLE_CHECKS_PROP, "true");

    System.out.println("Lmdb (with agrona buffers) created: " + db + ", env: " + env);
  }

  @Override
  public void write(UUID key, Order val) {
    try (Txn<DirectBuffer> txn = env.txnWrite()) {
      try (Cursor<DirectBuffer> cursor = db.openCursor(txn)) {
        byte[] keyBytes = key.toString().getBytes();
        MutableDirectBuffer keyBuffer = new UnsafeBuffer(
            ByteBuffer.allocateDirect(keyBytes.length));
        keyBuffer.putBytes(0, keyBytes);

        byte[] valBytes = val.toBytes();
        MutableDirectBuffer valBuffer = new UnsafeBuffer(
            ByteBuffer.allocateDirect(valBytes.length));
        valBuffer.putBytes(0, valBytes);

        cursor.put(keyBuffer, valBuffer);
      } catch (Exception e) {
        throw Exceptions.propagate(e);
      } finally {
        txn.commit();
      }
    }
  }

  @Override
  public Order read(UUID key) {
    try (Txn<DirectBuffer> txn = env.txnRead()) {
      try {
        byte[] keyBytes = key.toString().getBytes();
        MutableDirectBuffer keyBuffer = new UnsafeBuffer(
            ByteBuffer.allocateDirect(keyBytes.length));
        keyBuffer.putBytes(0, keyBytes);

        DirectBuffer valBuffer = db.get(txn, keyBuffer);
        if (valBuffer == null) {
          return null;
        }

        byte[] valBytes = new byte[valBuffer.capacity()];
        valBuffer.getBytes(0, valBytes);

        return Order.fromBytes(valBytes);
      } catch (Exception e) {
        throw Exceptions.propagate(e);
      } finally {
        txn.commit();
      }
    }
  }

  @Override
  public void close() {
    db.close();
    env.close();
    System.out.println(
        "Lmdb (with agrona buffers) closed: " + db + ", env: " + env + ", thank you, good bye");
  }
}
