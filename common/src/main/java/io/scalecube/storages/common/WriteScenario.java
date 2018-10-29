package io.scalecube.storages.common;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.metrics.BenchmarkTimer;
import io.scalecube.storages.common.entity.Order;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WriteScenario {

  /**
   * Runs write scenario.
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
              Storage<UUID, Order> storage = state.storage();
              BenchmarkTimer timer = state.timer("write");

              return iteration -> {
                Order order = new Order(UUID.randomUUID());
                BenchmarkTimer.Context writeTime = timer.time();
                storage.write(order.id(), order);
                writeTime.stop();
                return order;
              };
            });
  }
}
