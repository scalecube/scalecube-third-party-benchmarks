package io.scalecube.storages.rocksdb;

import static io.scalecube.storages.common.Constants.nEntries;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.StorageWriterTest;

import com.codahale.metrics.MetricRegistry;
import java.util.UUID;

public class RocksDBWriterTest {

  private static final int nThreads = Runtime.getRuntime().availableProcessors();

  public static void main(String[] args) throws Exception {
    MetricRegistry registry = new MetricRegistry();
    Storage<UUID, Order> storage = new RocksDBStorage();
    try {
      new StorageWriterTest(nThreads, nEntries, registry, storage).test();
    } finally {
      storage.close();
    }
  }
}
