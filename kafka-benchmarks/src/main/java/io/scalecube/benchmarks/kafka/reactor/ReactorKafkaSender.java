package io.scalecube.benchmarks.kafka.reactor;

import static io.scalecube.benchmarks.kafka.ConfigConstants.SERVERS;
import static io.scalecube.benchmarks.kafka.ConfigConstants.TOPIC;

import io.scalecube.benchmarks.kafka.Message;
import io.scalecube.benchmarks.kafka.MessageSerializer;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

public class ReactorKafkaSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReactorKafkaSender.class);

  private static final int MESSAGE_COUNT = 10_000_000;

  public static void main(String[] args) throws InterruptedException {
    Map<String, Object> producerProps = new HashMap<>();
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVERS);
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class);

    SenderOptions<Integer, Message> senderOptions =
        SenderOptions.<Integer, Message>create(producerProps).maxInFlight(1024);

    KafkaSender<Integer, Message> sender = KafkaSender.create(senderOptions);

    Flux<SenderRecord<Integer, Message, Integer>> outboundFlux =
        Flux.range(1, MESSAGE_COUNT)
            .map(i -> new ProducerRecord<>(TOPIC, i, new Message()))
            .map(r -> SenderRecord.create(r, r.key()));

    sender
        .send(outboundFlux.doOnNext(r -> r.value().sendTime(System.currentTimeMillis())))
        .then()
        .doOnError(e -> LOGGER.error("Sending failed", e))
        .doOnSuccess(s -> LOGGER.info("Sending completed"))
        .subscribe();

    Thread.currentThread().join();
  }
}
