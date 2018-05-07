package etc;

import com.codahale.metrics.MetricRegistry;

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
