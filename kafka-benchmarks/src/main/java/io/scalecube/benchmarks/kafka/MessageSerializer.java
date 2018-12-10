package io.scalecube.benchmarks.kafka;

import java.util.Map;
import org.apache.kafka.common.serialization.Serializer;

public class MessageSerializer implements Serializer<Message> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {}

  @Override
  public byte[] serialize(String topic, Message data) {
    long sendTime = data.sendTime();
    return new byte[] {
      (byte) sendTime,
      (byte) (sendTime >> 8),
      (byte) (sendTime >> 16),
      (byte) (sendTime >> 24),
      (byte) (sendTime >> 32),
      (byte) (sendTime >> 40),
      (byte) (sendTime >> 48),
      (byte) (sendTime >> 56)
    };
  }

  @Override
  public void close() {}
}
