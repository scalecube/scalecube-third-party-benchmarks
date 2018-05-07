package io.scalecube.storages.rocksdb;

import com.codahale.metrics.MetricRegistry;
import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.StorageWriterTest;

import static io.scalecube.storages.common.Constants.N;

public class RocksDBWriterTest {

  private static final int nThreads = Runtime.getRuntime().availableProcessors();

  public static void main(String[] args) throws Exception {
    MetricRegistry registry = new MetricRegistry();
    Storage<Integer, Order> storage = new RocksDBStorage();
    try {
      new StorageWriterTest(nThreads, N, registry, storage).test();
    } finally {
      storage.close();
    }
  }
}