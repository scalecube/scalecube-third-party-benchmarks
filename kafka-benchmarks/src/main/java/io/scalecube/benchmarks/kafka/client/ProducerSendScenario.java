package io.scalecube.benchmarks.kafka.client;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.metrics.BenchmarkTimer;
import io.scalecube.benchmarks.metrics.BenchmarkTimer.Context;
import java.time.Duration;
import java.util.function.Function;
import org.apache.kafka.clients.producer.ProducerRecord;
import reactor.core.publisher.Mono;

final class ProducerSendScenario {

  static void runWith(String[] args, Function<BenchmarkSettings, KafkaState> stateSupplier) {
    BenchmarkSettings settings =
        BenchmarkSettings.from(args)
            .numberThreads(1)
            .warmUpDuration(Duration.ofSeconds(1))
            .executionTaskDuration(Duration.ofMinutes(3))
            .build();

    stateSupplier
        .apply(settings)
        .runForAsync(
            state -> {
              BenchmarkTimer timer = state.timer("write");

              return iteration -> {
                byte[] value = "Hello".getBytes();

                ProducerRecord<byte[], byte[]> record =
                    new ProducerRecord<>(KafkaState.TOPIC, value);

                Context time = timer.time();

                state.producer().send(record, (metadata, exception) -> time.stop());

                return Mono.empty();
              };
            });
  }
}
