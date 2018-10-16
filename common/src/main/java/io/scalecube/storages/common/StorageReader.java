package io.scalecube.storages.common;

import com.codahale.metrics.Timer;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class StorageReader {

  private final int n;
  private final Storage<UUID, Order> storage;
  private final Timer readTimer;

  public StorageReader(int n, Storage<UUID, Order> storage, Timer readTimer) {
    this.n = n;
    this.storage = storage;
    this.readTimer = readTimer;
  }

  public void read() {
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    IntStream.rangeClosed(1, n).forEach(i -> {
      int idx = rnd.nextInt(1, n);
      UUID key = UUID.fromString(String.valueOf(idx));
      Timer.Context readTime = readTimer.time();
      try {
        storage.read(key);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      readTime.stop();
    });

    throw new UnsupportedOperationException("read");
  }
}
