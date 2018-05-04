package etc;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class StorageRunner {

    public static final int RECORDS_CNT = 2_000_000;

    public static void main(String[] args) throws InterruptedException {
        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertDurationsTo(TimeUnit.NANOSECONDS).build();
        reporter.start(3, TimeUnit.SECONDS);

        Timer timeFullWrite = registry.timer("full-writes");
        Timer timeFullRead = registry.timer("full-reads");

        Storage<Integer, Order> storage = storage();

        IntStream.rangeClosed(1, RECORDS_CNT).forEach(i -> {
            Order order1 = new Order();
            try {
                storage.write(order1.getId(), order1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        IntStream.rangeClosed(1, RECORDS_CNT).forEach(i -> {
                    int idx = rnd.nextInt(1, RECORDS_CNT);
                    Timer.Context readTime = timeFullRead.time();
                    try {
                        storage.read(idx);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    readTime.stop();
                }
        );

        // Write to full
        IntStream.rangeClosed(1, RECORDS_CNT).forEach(i -> {
            Order order = new Order();
            Timer.Context time = timeFullWrite.time();
            try {
                storage.write(order.getId(), order);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            time.stop();
            if (i == RECORDS_CNT) {
                reporter.stop();
                System.out.println("DONE");
            }
        });

        //        Thread.currentThread().join();
    }

    private static Storage<Integer, Order> storage() {
        return new HashMapStorage(RECORDS_CNT * 2);
    }

    private static class HashMapStorage<K, V> implements Storage<K, V> {

        private ConcurrentHashMap<K, V> map;

        public HashMapStorage(int capacity) {
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
