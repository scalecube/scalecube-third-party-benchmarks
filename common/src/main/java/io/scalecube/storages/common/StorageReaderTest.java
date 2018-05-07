package io.scalecube.storages.common;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class StorageReaderTest implements Runnable {

    private final int nThreads;
    private final int n;
    private final MetricRegistry registry;

    private final StorageWriter storageWriter;
    private final StorageReader storageReader;
    private final ExecutorService executorService;

    public StorageReaderTest(int nThreads, int n, MetricRegistry registry, Storage<Integer, Order> storage) {
        this.nThreads = nThreads;
        this.n = n;
        this.registry = registry;
        this.storageReader = new StorageReader(n, storage, registry.timer("full-reads"));
        this.storageWriter = new StorageWriter(n, storage, null);
        this.executorService = Executors.newFixedThreadPool(nThreads);
    }

    public void test() throws Exception {
        try {
            System.out.println("###### Starting to populate db with " + n + " elements");
            storageWriter.populate();
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
        System.out.println("###### STARTED READ");
        storageReader.read();
        System.out.println("###### DONE WITH READ");
    }
}
