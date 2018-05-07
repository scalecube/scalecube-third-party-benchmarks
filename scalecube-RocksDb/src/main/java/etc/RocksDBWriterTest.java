package etc;

import com.codahale.metrics.MetricRegistry;
import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.StorageWriterTest;

public class RocksDBWriterTest {

  private static final int n = (int) 1e+6;

  private static final int nThreads = Runtime.getRuntime().availableProcessors();

  public static void main(String[] args) throws Exception {
    MetricRegistry registry = new MetricRegistry();
    Storage<Integer, Order> storage = new RocksDBStorage();
    try {
      new StorageWriterTest(nThreads, n, registry, storage).test();
    } finally {
      storage.close();
    }
  }
}
