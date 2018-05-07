package io.scalecube.storages.common;

import com.codahale.metrics.Timer;

import java.io.IOException;
import java.util.stream.IntStream;

public class StorageWriter {

    private final int n;
    private final Storage<Integer, Order> storage;
    private Timer writeTimer;

    public StorageWriter(int n, Storage<Integer, Order> storage, Timer writeTimer) {
        this.n = n;
        this.storage = storage;
        this.writeTimer = writeTimer;
    }

    void populate() {
        IntStream.rangeClosed(1, n).forEach(i -> {
            Order order1 = new Order();
            try {
                storage.write(order1.getId(), order1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    void write() {
        // Write to full
        IntStream.rangeClosed(1, n).forEach(i -> {
            Order order = new Order();
            Timer.Context time = writeTimer.time();
            try {
                storage.write(order.getId(), order);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            time.stop();
        });
    }
}
