package io.scalecube.benchmarks.storages.common;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.metrics.BenchmarkTimer;
import io.scalecube.benchmarks.storages.common.entity.Order;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class ReadScenario {

  /**
   * Runs read scenario.
   *
   * @param args args
   * @param stateSupplier state supplier
   */
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
              BenchmarkTimer timer = state.timer("read");
              int preloadCount = state.preloadCount();

              Supplier<UUID> uuidSupplier =
                  preloadCount > 0
                      ? () -> state.uuid(ThreadLocalRandom.current().nextInt(0, preloadCount))
                      : () -> state.uuid(0); // not found case

              return iteration -> {
                Storage<UUID, Order> storage = state.storage();
                UUID key = uuidSupplier.get();
                Order order = storage.read(key);
                BenchmarkTimer.Context readTime = timer.time();
                readTime.stop();
                return key;
              };
            });
  }
}
