package io.scalecube.storages.mvstore;

import com.codahale.metrics.MetricRegistry;
import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.StorageWriterTest;

public class MvStoreWriterTest {


    private static final int n = (int) 1e+6;

//    private static final int nThreads = Runtime.getRuntime().availableProcessors();
    private static final int nThreads = 1;

    public static void main(String[] args) throws Exception {
        MetricRegistry registry = new MetricRegistry();
        Storage<Integer, Order> storage = new MVStoreStorage();
        try {
            new StorageWriterTest(nThreads, n, registry, storage).test();
        } finally {
            storage.close();
        }
    }
}
