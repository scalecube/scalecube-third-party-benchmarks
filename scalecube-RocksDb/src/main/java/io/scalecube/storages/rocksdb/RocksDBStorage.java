package io.scalecube.storages.rocksdb;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.IOException;

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
    try {
      rocksDb.put(key.getBytes(), order.toBytes());
    } catch (RocksDBException e) {
      throw new IOException(e);
    }
  }

  @Override
  public Order read(String key) throws IOException {
    try {
      byte[] valBytes = rocksDb.get(key.getBytes());
      return valBytes != null ? Order.fromBytes(valBytes) : null;
    } catch (RocksDBException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void close() {
    rocksDb.close();
    System.out.println("RocksDB instance closed: " + rocksDb + ", thank you, good bye");
  }
}
