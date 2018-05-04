package etc;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LmdbJavaWriterTest {

    public static final int n = 2_000_000;

    public static void main(String[] args) throws IOException {
        MetricRegistry registry = new MetricRegistry();

        Storage<Integer, Order> storage = new LmdbStorage();
        StorageWriter storageWriter = new StorageWriter(n, storage, registry.timer("full-writes"));

        // Pre-populate database
        storageWriter.populate();

        // Start Reporter
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertDurationsTo(TimeUnit.NANOSECONDS).build();
        reporter.start(3, TimeUnit.SECONDS);

        // Perform writes
        storageWriter.write();

        reporter.stop();
        System.out.println("DONE");
    }
}
