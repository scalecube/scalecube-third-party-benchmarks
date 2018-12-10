package io.scalecube.benchmarks.kafka.reactor;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.kafka.Message;
import io.scalecube.benchmarks.metrics.BenchmarkTimer;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderRecord;
import reactor.util.concurrent.Queues;

final class InfiniteStreamScenario {

  private static final Logger LOGGER = LoggerFactory.getLogger(InfiniteStreamScenario.class);

  private static final int DEFAULT_RATE_LIMIT = Queues.SMALL_BUFFER_SIZE;
  private static final int MAX_IN_FLIGHT = 2048;
  private static final String RATE_LIMIT = "rateLimit";
  private static final String TOPIC_SEND = "benchmark-topic-a";

  public static void runWith(
      String[] args, Function<BenchmarkSettings, ReactorKafkaState> stateSupplier) {

    int numOfThreads = Runtime.getRuntime().availableProcessors();
    Duration rampUpDuration = Duration.ofSeconds(numOfThreads);

    BenchmarkSettings settings =
        BenchmarkSettings.from(args)
            .injectors(1)
            .messageRate(1) // workaround
            .rampUpDuration(rampUpDuration)
            .durationUnit(TimeUnit.MILLISECONDS)
            .build();

    int rateLimit = rateLimit(settings);

    stateSupplier
        .apply(settings)
        .runWithRampUp(
            (rampUpTick, state) -> Mono.just(state.kafkaSender(MAX_IN_FLIGHT)),
            state -> {
              BenchmarkTimer timer = state.timer("reactor-kafka-timer");

              return kafkaSender ->
                  (executionTick, task) ->
                      Flux.defer(
                          () -> {
                            SenderRecord<Integer, Message, Integer> record =
                                SenderRecord.create(
                                    new ProducerRecord<>(TOPIC_SEND, new Message()),
                                    new Message().hashCode());
                            return kafkaSender
                                .send(Mono.just(record))
                                .thenMany(
                                    state
                                        .kafkaReceiver()
                                        .receive()
                                        // .limitRate(rateLimit)
                                        .doOnNext(
                                            r ->
                                                timer.update(
                                                    System.currentTimeMillis()
                                                        - r.value().sendTime(),
                                                    TimeUnit.MILLISECONDS))
                                        .doOnError(th -> LOGGER.error("Error occurred", th)));
                          });
            },
            (state, kafkaSender) -> Mono.empty());
  }

  private static Integer rateLimit(BenchmarkSettings settings) {
    return Optional.ofNullable(settings.find(RATE_LIMIT, null))
        .map(Integer::parseInt)
        .orElse(DEFAULT_RATE_LIMIT);
  }
}
