package io.scalecube.benchmarks.kafka.reactor;

import static io.scalecube.benchmarks.kafka.ConfigConstants.SERVERS;
import static io.scalecube.benchmarks.kafka.ConfigConstants.TOPIC;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import io.scalecube.benchmarks.kafka.Message;
import io.scalecube.benchmarks.kafka.MessageDeserializer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

public class ReactorKafkaReceiver {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReactorKafkaReceiver.class);

  /**
   * Runs benchmark.
   *
   * @param args program arguments
   */
  public static void main(String[] args) throws InterruptedException {
    MetricRegistry metricRegistry = new MetricRegistry();
    ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).build();

    reporter.start(3, TimeUnit.SECONDS);

    Map<String, Object> consumerProps = new HashMap<>();
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVERS);
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);

    ReceiverOptions<Integer, Message> receiverOptions =
        ReceiverOptions.<Integer, Message>create(consumerProps)
            .subscription(Collections.singleton(TOPIC));

    KafkaReceiver.create(receiverOptions)
        .receive()
        .subscribe(
            r -> {
              metricRegistry
                  .timer("reactor-kafka")
                  .update(System.currentTimeMillis() - r.value().sendTime(), TimeUnit.MILLISECONDS);
              r.receiverOffset().acknowledge();
            },
            th -> LOGGER.error("Exception occurred", th));

    Thread.currentThread().join();
  }
}
