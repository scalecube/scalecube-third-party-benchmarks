package etc;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

public class StorageWriterTest {

  private final int n;
  private final MetricRegistry registry;
  private final Storage<Integer, Order> storage;
  private final boolean prePopulate;

  public StorageWriterTest(int n, MetricRegistry registry, Storage<Integer, Order> storage, boolean prePopulate) {
    this.n = n;
    this.registry = registry;
    this.storage = storage;
    this.prePopulate = prePopulate;
  }

  public void test() {
    StorageWriter storageWriter = new StorageWriter(n, storage, registry.timer("full-writes"));

    if (prePopulate) {
      System.out.println("###### Starting to populate db with " + n + " elements");
      storageWriter.populate();
      System.out.println("###### Finished to populate db");
    }

    ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertDurationsTo(TimeUnit.NANOSECONDS).build();
    reporter.start(3, TimeUnit.SECONDS);

    storageWriter.write();

    reporter.stop();
    System.out.println("###### DONE");
  }
}
