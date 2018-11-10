package io.scalecube.storages.common;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.metrics.BenchmarkTimer;
import io.scalecube.storages.common.entity.Order;
import io.scalecube.storages.common.entity.OrderStatus;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class UpdateScenario {

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
            .numberThreads(1)
            .warmUpDuration(Duration.ofSeconds(10))
            .executionTaskDuration(Duration.ofMinutes(3))
            .durationUnit(TimeUnit.NANOSECONDS)
            .build();

    stateSupplier
        .apply(settings)
        .runForSync(
            state -> {
              BenchmarkTimer timer = state.timer("update");
              int preloadCount = state.preloadCount();
              if (preloadCount <= 0) {
                throw new IllegalArgumentException("preloadCount [" + preloadCount + "] <= 0");
              }

              return iteration -> {
                Storage<UUID, Order> storage = state.storage();
                UUID id = state.uuid(ThreadLocalRandom.current().nextInt(0, preloadCount));
                Order order = storage.read(id);

                BenchmarkTimer.Context writeTime = timer.time();

                storage.write(order.id(), order.withNewStatus(OrderStatus.Filled));

                writeTime.stop();
                return order;

              };
            });
  }
}
