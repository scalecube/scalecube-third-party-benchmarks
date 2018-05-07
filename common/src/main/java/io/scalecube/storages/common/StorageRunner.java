package io.scalecube.storages.common;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class StorageRunner {

    public static final int RECORDS_CNT = 1_000_000;

    public static void main(String[] args) {
        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertDurationsTo(TimeUnit.NANOSECONDS).build();
        reporter.start(3, TimeUnit.SECONDS);

        Timer timeFullWrite = registry.timer("full-writes");
        Timer timeFullRead = registry.timer("full-reads");

        Storage<Integer, Order> storage = storage();

        StorageWriter writer = new StorageWriter(RECORDS_CNT, storage, timeFullWrite);
        writer.populate();
        writer.write();

        StorageReader reader = new StorageReader(RECORDS_CNT, storage, timeFullRead);
        reader.read();
    }

    private static Storage<Integer, Order> storage() {
        return new HashMapStorage<>(RECORDS_CNT * 2);
    }

    private static class HashMapStorage<K, V> implements Storage<K, V> {

        private ConcurrentHashMap<K, V> map;

        HashMapStorage(int capacity) {
            this.map = new ConcurrentHashMap<>(capacity);
        }

        @Override
        public void write(K k, V v) {
            map.put(k, v);
        }

        @Override
        public V read(K k) {
            return map.get(k);
        }

        @Override
        public void close() {
            // no-0op[
        }
    }
}
