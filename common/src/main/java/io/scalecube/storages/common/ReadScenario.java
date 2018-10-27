package io.scalecube.storages.common;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.metrics.BenchmarkTimer;
import io.scalecube.storages.common.entity.Order;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ReadScenario {

  public static void runWith(
      String[] args, Function<BenchmarkSettings, StorageState> stateSupplier) {
    BenchmarkSettings settings =
        BenchmarkSettings.from(args)
            .numberThreads(4)
            .warmUpDuration(Duration.ofSeconds(1))
            .executionTaskDuration(Duration.ofMinutes(3))
            .durationUnit(TimeUnit.NANOSECONDS)
            .build();

    stateSupplier
        .apply(settings)
        .runForSync(
            state -> {
              Storage<UUID, Order> storage = state.storage();
              BenchmarkTimer timer = state.timer("read");
              int preloadCount = state.preloadCount();

              return iteration -> {
                ThreadLocalRandom rnd = ThreadLocalRandom.current();
                int idx = rnd.nextInt(0, preloadCount);
                UUID key = state.uuid(idx);
                Order read = storage.read(key);
                BenchmarkTimer.Context readTime = timer.time();
                readTime.stop();
                return read;
              };
            });
  }
}
