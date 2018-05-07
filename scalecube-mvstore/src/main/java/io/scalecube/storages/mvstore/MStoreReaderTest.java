package io.scalecube.storages.mvstore;

import com.codahale.metrics.MetricRegistry;
import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.StorageReaderTest;

import static io.scalecube.storages.common.Constants.N;

public class MStoreReaderTest {

    private static final int nThreads = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws Exception {
        MetricRegistry registry = new MetricRegistry();
        Storage<Integer, Order> storage = new MVStoreStorage();
        try {
            new StorageReaderTest(nThreads, N, registry, storage).test();
        } finally {
            storage.close();
        }
    }
}
