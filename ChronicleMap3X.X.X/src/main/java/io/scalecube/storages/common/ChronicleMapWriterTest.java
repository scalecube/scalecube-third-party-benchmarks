package io.scalecube.storages.common;

import com.codahale.metrics.MetricRegistry;

public class ChronicleMapWriterTest {

  private static final int n = (int) 1e+6;

  private static final int nThreads = Runtime.getRuntime().availableProcessors();

  public static void main(String[] args) throws Exception {
    MetricRegistry registry = new MetricRegistry();
    Storage<Integer, Order> storage = new ChronicleMapStorage(n);
    try {
      new StorageWriterTest(nThreads, n, registry, storage).test();
    } finally {
      storage.close();
    }
  }
}
