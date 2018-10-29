package io.scalecube.storages.rocksdb;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.entity.Order;
import java.util.UUID;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import reactor.core.Exceptions;

public class RocksdbStorage implements Storage<UUID, Order> {

  private RocksDB rocksDb;

  public RocksdbStorage(BenchmarkSettings settings) {
  }

  @Override
  public void start() {
    RocksDB.loadLibrary();
    System.out.println("RocksDB.loadLibrary DONE");

    try (Options options = new Options()
        .setAllowMmapReads(true)
        .setAllowMmapWrites(true)
        .setAllowConcurrentMemtableWrite(true)
        .setIncreaseParallelism(Runtime.getRuntime().availableProcessors())
        .setCreateIfMissing(true)) {

      rocksDb = RocksDB.open(options, "RocksdbStorage");

      System.out.println("RocksDB opened: " + rocksDb);
    } catch (RocksDBException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void write(UUID key, Order order) {
    try {
      rocksDb.put(key.toString().getBytes(), order.toBytes());
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
  }

  @Override
  public Order read(UUID key) {
    try {
      byte[] valBytes = rocksDb.get(key.toString().getBytes());
      return valBytes != null ? Order.fromBytes(valBytes) : null;
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
  }

  @Override
  public void close() {
    rocksDb.close();
    System.out.println("RocksDB instance closed: " + rocksDb + ", thank you, good bye");
  }
}
