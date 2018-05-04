package etc;

import com.codahale.metrics.Timer;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class StorageReader {

  private final int n;
  private final Storage<Integer, Order> storage;
  private final Timer readTimer;

  public StorageReader(int n, Storage<Integer, Order> storage, Timer readTimer) {
    this.n = n;
    this.storage = storage;
    this.readTimer = readTimer;
  }

  public void read() {
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    IntStream.rangeClosed(1, n).forEach(i -> {
      int idx = rnd.nextInt(1, n);
      Timer.Context readTime = readTimer.time();
      try {
        storage.read(idx);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      readTime.stop();
    });
  }

}
