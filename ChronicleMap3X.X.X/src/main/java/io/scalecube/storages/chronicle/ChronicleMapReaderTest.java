package io.scalecube.storages.chronicle;

import static io.scalecube.storages.common.Constants.nEntries;

import io.scalecube.storages.common.entity.Order;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.StorageReaderTest;

import com.codahale.metrics.MetricRegistry;
import java.util.UUID;

public class ChronicleMapReaderTest {

  private static final int nThreads = Runtime.getRuntime().availableProcessors();

  public static void main(String[] args) throws Exception {
    MetricRegistry registry = new MetricRegistry();
    Storage<UUID, Order> storage = new ChronicleMapStorage(nEntries);
    try {
      new StorageReaderTest(nThreads, nEntries, registry, storage).test();
    } finally {
      storage.close();
    }
  }
}
