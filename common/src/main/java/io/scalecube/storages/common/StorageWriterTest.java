package io.scalecube.storages.common;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class StorageWriterTest implements Runnable {

  private final int nThreads;
  private final int n;
  private final MetricRegistry registry;
  private final StorageWriter storageWriter;
  private final ExecutorService executorService;

  public StorageWriterTest(int nThreads, int n, MetricRegistry registry, Storage<UUID, Order> storage) {
    this.nThreads = nThreads;
    this.n = n;
    this.registry = registry;
    this.storageWriter = new StorageWriter(n, storage, registry.timer("full-writes"));
    this.executorService = Executors.newFixedThreadPool(nThreads);
  }

  public void test() throws Exception {
    try {
      System.out.println("###### Starting to populate db with " + n + " elements");
      storageWriter.populate(true);
      System.out.println("###### Finished to populate db");

      ConsoleReporter reporter =
          ConsoleReporter.forRegistry(registry).convertDurationsTo(TimeUnit.NANOSECONDS).build();
      reporter.start(1, TimeUnit.SECONDS);
      CompletableFuture[] futures = new CompletableFuture[nThreads];
      IntStream.range(0, nThreads).forEach(i -> futures[i] = CompletableFuture.runAsync(this, executorService));
      CompletableFuture.allOf(futures).join();
      reporter.stop();
    } finally {
      executorService.shutdown();
      executorService.awaitTermination(1, TimeUnit.SECONDS);
    }
  }

  @Override
  public void run() {
    System.out.println("###### STARTED");
    storageWriter.write();
    System.out.println("###### DONE");
  }
}
