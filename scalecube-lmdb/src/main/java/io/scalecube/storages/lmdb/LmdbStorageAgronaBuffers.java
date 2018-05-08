package io.scalecube.storages.lmdb;

import static java.nio.ByteBuffer.allocateDirect;
import static org.lmdbjava.DbiFlags.MDB_CREATE;
import static org.lmdbjava.DirectBufferProxy.PROXY_DB;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;

import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.lmdbjava.Cursor;
import org.lmdbjava.Dbi;
import org.lmdbjava.Env;
import org.lmdbjava.EnvFlags;
import org.lmdbjava.Txn;

import java.io.File;
import java.io.IOException;

/**
 * LMDB storage connector with the following configuration:
 * <p/>
 * 1. Uses Agrona buffers. 2. Don't fsync after commit. 3. Use writable mmap.
 */
public class LmdbStorageAgronaBuffers implements Storage<String, Order> {

  public static final String DB_NAME = "LmdbStorageAgronaBuffers";

  private final Env<DirectBuffer> env;
  private final Dbi<DirectBuffer> db;

  public LmdbStorageAgronaBuffers() {
    File path = new File(".");
    env = Env.create(PROXY_DB)
        .setMapSize(10_485_760_000L)
        .setMaxDbs(1)
        .open(path, EnvFlags.MDB_NOSYNC, EnvFlags.MDB_WRITEMAP);

    db = env.openDbi(DB_NAME, MDB_CREATE);
    System.getProperties().setProperty(Env.DISABLE_CHECKS_PROP, "true");
  }

  @Override
  public void write(String key, Order value) throws IOException {
    try (Txn<DirectBuffer> txn = env.txnWrite()) {
      try (Cursor<DirectBuffer> c = db.openCursor(txn)) {
        byte[] keyBytes = key.getBytes();
        MutableDirectBuffer keyBuffer = new UnsafeBuffer(allocateDirect(keyBytes.length));
        keyBuffer.putBytes(0, keyBytes);

        byte[] valBytes = value.toBytes();
        MutableDirectBuffer valueBuffer = new UnsafeBuffer(allocateDirect(valBytes.length));
        valueBuffer.putBytes(0, valBytes);
        c.put(keyBuffer, valueBuffer);
      } finally {
        txn.commit();
      }
    }
  }

  @Override
  public Order read(String key) throws IOException {
    try (Txn<DirectBuffer> txn = env.txnRead()) {
      try {
        byte[] keyBytes = key.getBytes();
        MutableDirectBuffer keyBuffer = new UnsafeBuffer(allocateDirect(keyBytes.length));
        keyBuffer.putBytes(0, keyBytes);
        DirectBuffer valBuffer = db.get(txn, keyBuffer);

        return Order.fromBytes(valBuffer.byteArray());
      } finally {
        txn.commit();
      }
    }
  }

  @Override
  public void close() {
    db.close();
    env.close();
  }
}
