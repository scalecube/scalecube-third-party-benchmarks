package io.scalecube.storages.common;

import com.codahale.metrics.Timer;
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

  void populate(boolean needToGenerateId) {
    IntStream.rangeClosed(1, n).forEach(i -> {
      UUID id = null;
      if (!needToGenerateId) {
        id = UUID.fromString(String.valueOf(i));
      }
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
      Order order = new Order(null);
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
