package io.scalecube.benchmarks.kafka.reactor;

import static io.scalecube.benchmarks.kafka.ConfigConstants.SERVERS;

import io.scalecube.benchmarks.kafka.Message;
import io.scalecube.benchmarks.kafka.MessageDeserializer;
import io.scalecube.benchmarks.kafka.MessageSerializer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

public class ExampleService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleService.class);

  private static final String TOPIC_RECEIVE = "benchmark-topic-a";
  private static final String TOPIC_SEND = "benchmark-topic-b";
  private static final int MAX_IN_FLIGHT = 256;

  public static void main(String[] args) throws InterruptedException {
    KafkaSender<Integer, Message> kafkaSender = kafkaSender();

    kafkaReceiver()
        .receive()
        .log(">>>")
        .subscribe(
            r -> {
              kafkaSender
                  .send(records())
                  .then()
                  .doOnError(e -> LOGGER.error("Sending failed", e))
                  .doOnSuccess(s -> LOGGER.info("Sending completed"))
                  .subscribe();
              r.receiverOffset().acknowledge();
            },
            th -> LOGGER.error("Exception occurred", th));

    Thread.currentThread().join();
  }

  private static KafkaReceiver<Integer, Message> kafkaReceiver() {
    Map<String, Object> consumerProps = new HashMap<>();
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVERS);
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "service-consumer");
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);

    ReceiverOptions<Integer, Message> receiverOptions =
        ReceiverOptions.<Integer, Message>create(consumerProps)
            .subscription(Collections.singleton(TOPIC_RECEIVE));

    return KafkaReceiver.create(receiverOptions);
  }

  private static KafkaSender<Integer, Message> kafkaSender() {
    Map<String, Object> producerProps = new HashMap<>();
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVERS);
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class);

    SenderOptions<Integer, Message> senderOptions =
        SenderOptions.<Integer, Message>create(producerProps).maxInFlight(MAX_IN_FLIGHT);

    return KafkaSender.create(senderOptions);
  }

  private static Flux<SenderRecord<Integer, Message, Integer>> records() {
    return Mono.fromCallable(
            () -> {
              Message message = new Message(System.currentTimeMillis());
              int correlationMetadata = message.hashCode();
              ProducerRecord<Integer, Message> record =
                  new ProducerRecord<>(TOPIC_SEND, correlationMetadata, message);
              return SenderRecord.create(record, correlationMetadata);
            })
        .repeat();
  }
}
