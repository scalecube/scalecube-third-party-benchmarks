package etc;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class StorageWriterTest implements Runnable {

  private final int nThreads;
  private final int n;
  private final MetricRegistry registry;
  private final Storage<Integer, Order> storage;
  private final ExecutorService executorService;

  public StorageWriterTest(int nThreads, int n, MetricRegistry registry, Storage<Integer, Order> storage) {
    this.nThreads = nThreads;
    this.n = n;
    this.registry = registry;
    this.storage = storage;
    this.executorService = Executors.newFixedThreadPool(2);
  }

  public void test() throws Exception {
    List<Future> futures = new ArrayList<>();
    IntStream.rangeClosed(1, nThreads).forEach(i -> futures.add(executorService.submit(this)));
    futures.forEach(future -> {
      try {
        future.get();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    executorService.shutdown();
    executorService.awaitTermination(1, TimeUnit.SECONDS);
  }

  @Override
  public void run() {
    StorageWriter storageWriter = new StorageWriter(n, storage, registry.timer("full-writes"));

    System.out.println("###### Starting to populate db with " + n + " elements");
    storageWriter.populate();
    System.out.println("###### Finished to populate db");

    ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertDurationsTo(TimeUnit.NANOSECONDS).build();
    reporter.start(3, TimeUnit.SECONDS);

    storageWriter.write();

    reporter.stop();
    System.out.println("###### DONE");
  }
}
