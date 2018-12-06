package io.scalecube.benchmarks.kafka.reactor;

import static io.scalecube.benchmarks.kafka.ConfigConstants.SERVERS;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.BenchmarkState;
import io.scalecube.benchmarks.kafka.Message;
import io.scalecube.benchmarks.kafka.MessageDeserializer;
import io.scalecube.benchmarks.kafka.MessageSerializer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

final class ReactorKafkaState extends BenchmarkState<ReactorKafkaState> {

  private static final String TOPIC_RECEIVE = "benchmark-topic-b";

  ReactorKafkaState(BenchmarkSettings settings) {
    super(settings);
  }

  KafkaSender<Integer, Message> kafkaSender(int maxInFlight) {
    return KafkaSender.create(
        SenderOptions.<Integer, Message>create(producerProps()).maxInFlight(maxInFlight));
  }

  KafkaReceiver<Integer, Message> kafkaReceiver() {
    return KafkaReceiver.create(
        ReceiverOptions.<Integer, Message>create(consumerProps())
            .subscription(Collections.singleton(TOPIC_RECEIVE)));
  }

  private Map<String, Object> producerProps() {
    Map<String, Object> producerProps = new HashMap<>();
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVERS);
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class);
    return producerProps;
  }

  private Map<String, Object> consumerProps() {
    Map<String, Object> consumerProps = new HashMap<>();
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVERS);
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "benchmark-consumer");
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
    return consumerProps;
  }
}
