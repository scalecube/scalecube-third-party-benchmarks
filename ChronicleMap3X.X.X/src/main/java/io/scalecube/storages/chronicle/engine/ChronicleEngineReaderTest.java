package io.scalecube.storages.chronicle.engine;

import com.codahale.metrics.MetricRegistry;
import io.scalecube.storages.chronicle.ChronicleMapStorage;
import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.StorageReaderTest;

import static io.scalecube.storages.common.Constants.nEntries;

public class ChronicleEngineReaderTest{

    private static final int nThreads = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws Exception {
        MetricRegistry registry = new MetricRegistry();
        Storage<String, Order> storage = new ChronicleEngineStorage();
        try {
            new StorageReaderTest(nThreads, nEntries, registry, storage).test();
        } finally {
            storage.close();
        }
    }
}

