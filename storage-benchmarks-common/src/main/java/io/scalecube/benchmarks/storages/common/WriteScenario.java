package io.scalecube.benchmarks.storages.common;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.metrics.BenchmarkTimer;
import io.scalecube.benchmarks.storages.common.entity.Order;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import reactor.core.Exceptions;

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
            .numberThreads(1)
            .warmUpDuration(Duration.ofSeconds(1))
            .executionTaskDuration(Duration.ofMinutes(3))
            .durationUnit(TimeUnit.NANOSECONDS)
            .build();

    stateSupplier
        .apply(settings)
        .runForSync(
            state -> {
              BenchmarkTimer timer = state.timer("write");

              return iteration -> {
                try {
                  Order order = new Order(UUID.randomUUID());
                  BenchmarkTimer.Context writeTime = timer.time();
                  state.storage().write(order.id(), order);
                  writeTime.stop();
                  return order;
                } catch (Exception e) {
                  // todo change it in benchmarks-api
                  e.printStackTrace();
                  throw Exceptions.propagate(e);
                }
              };
            });
  }
}
