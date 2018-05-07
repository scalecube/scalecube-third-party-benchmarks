package etc;

import com.codahale.metrics.MetricRegistry;

import java.io.IOException;

public class ChronicleMapWriterTest {

  private static final int n = (int) 1e+6;

  public static void main(String[] args) throws IOException {
    MetricRegistry registry = new MetricRegistry();
    Storage<Integer, Order> storage = new ChronicleMapStorage(n);
    new StorageWriterTest(n, registry, storage, true).test();
  }
}
