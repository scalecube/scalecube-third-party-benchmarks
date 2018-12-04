package io.scalecube.benchmarks.storages.common;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.metrics.BenchmarkTimer;
import io.scalecube.benchmarks.storages.common.entity.Order;
import io.scalecube.benchmarks.storages.common.entity.OrderStatus;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ReadAndWriteScenario {

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
            .warmUpDuration(Duration.ofSeconds(10))
            .executionTaskDuration(Duration.ofMinutes(3))
            .durationUnit(TimeUnit.NANOSECONDS)
            .build();

    stateSupplier
        .apply(settings)
        .runForSync(
            state -> {
              BenchmarkTimer timer = state.timer("readAndWrite");
              int preloadCount = state.preloadCount();
              if (preloadCount <= 0) {
                throw new IllegalArgumentException("preloadCount [" + preloadCount + "] <= 0");
              }

              return iteration -> {
                Storage<UUID, Order> storage = state.storage();
                UUID id = state.uuid(ThreadLocalRandom.current().nextInt(0, preloadCount));

                BenchmarkTimer.Context writeTime = timer.time();

                Order order = storage.read(id);
                storage.write(order.id(), order.withNewStatus(OrderStatus.Filled));

                writeTime.stop();
                return order;
              };
            });
  }
}
