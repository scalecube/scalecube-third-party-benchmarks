package io.scalecube.storages.common;

import com.codahale.metrics.Timer;
import io.scalecube.storages.common.entity.Order;
import java.util.UUID;
import java.util.stream.IntStream;

public class StorageWriter {

  private final int n;
  private final Storage<UUID, Order> storage;
  private Timer writeTimer;

  public StorageWriter(int n, Storage<UUID, Order> storage, Timer writeTimer) {
    this.n = n;
    this.storage = storage;
    this.writeTimer = writeTimer;
  }

  void populate() {
    IntStream.rangeClosed(1, n).forEach(i -> {
      UUID id = UUID.fromString("00000000-0000-0000-0000-" + String.format("%012d", n));
      Order order1 = new Order(id);
      try {
        storage.write(order1.id(), order1);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  void write() {
    IntStream.rangeClosed(1, n).forEach(i -> {
      Order order = new Order(UUID.randomUUID());
      Timer.Context time = writeTimer.time();
      try {
        storage.write(order.id(), order);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      time.stop();
    });
  }
}
