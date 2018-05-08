package io.scalecube.storages.rocksdb;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RocksDBStorage implements Storage<String, Order> {

  private final RocksDB rocksDb;

  public RocksDBStorage() {
    RocksDB.loadLibrary();
    System.out.println("RocksDB.loadLibrary DONE");

    try (Options options = new Options()
        .setAllowMmapReads(true)
        .setAllowMmapWrites(true)
        .setAllowConcurrentMemtableWrite(true)
        .setIncreaseParallelism(Runtime.getRuntime().availableProcessors())
        .setCreateIfMissing(true)) {

      rocksDb = RocksDB.open(options, "RocksDBStorage");

      System.out.println("RocksDB opened: " + rocksDb);
    } catch (RocksDBException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void write(String key, Order order) throws IOException {
    ByteBuffer keyBuffer = ByteBuffer.allocate(key.getBytes().length);
    keyBuffer.put(key.getBytes());

    ByteBuffer valBuffer = ByteBuffer.allocate(order.toBytes().length);
    valBuffer.put(order.toBytes());

    try {
      rocksDb.put(keyBuffer.array(), valBuffer.array());
    } catch (RocksDBException e) {
      throw new IOException(e);
    }
  }

  @Override
  public Order read(String key) throws IOException {
    try {
      return Order.fromBytes(rocksDb.get(key.getBytes()));
    } catch (RocksDBException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void close() {
    rocksDb.close();
  }
}
