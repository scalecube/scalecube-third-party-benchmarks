package io.scalecube.storages.mvstore;

import static io.scalecube.storages.common.Constants.N;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.StorageWriterTest;

import com.codahale.metrics.MetricRegistry;

public class MVStoreWriterTest {

  private static final int nThreads = Runtime.getRuntime().availableProcessors();

  public static void main(String[] args) throws Exception {
    MetricRegistry registry = new MetricRegistry();
    Storage<Integer, Order> storage = new MVStoreStorage();
    try {
      new StorageWriterTest(nThreads, N, registry, storage).test();
    } finally {
      storage.close();
    }
  }
}
