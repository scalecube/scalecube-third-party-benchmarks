package io.scalecube.benchmarks.kafka.client;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.BenchmarkState;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;

final class KafkaState extends BenchmarkState<KafkaState> {

  static final String TOPIC = "benchmark-topic";

  private static final String SERVERS = "127.0.0.1:9092";

  private final KafkaProducer<byte[], byte[]> producer;

  KafkaState(BenchmarkSettings settings) {
    super(settings);

    producer = new KafkaProducer<>(producerProps());
  }

  KafkaProducer<byte[], byte[]> producer() {
    return producer;
  }

  private static Properties producerProps() {
    final Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVERS);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
    return props;
  }
}
