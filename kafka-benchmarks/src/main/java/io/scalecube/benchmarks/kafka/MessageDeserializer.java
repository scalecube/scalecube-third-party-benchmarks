package io.scalecube.benchmarks.kafka;

import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;

public class MessageDeserializer implements Deserializer<Message> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {}

  @Override
  public Message deserialize(String topic, byte[] data) {
    long sendTime =
        ((long) data[7] << 56)
            | ((long) data[6] & 0xff) << 48
            | ((long) data[5] & 0xff) << 40
            | ((long) data[4] & 0xff) << 32
            | ((long) data[3] & 0xff) << 24
            | ((long) data[2] & 0xff) << 16
            | ((long) data[1] & 0xff) << 8
            | ((long) data[0] & 0xff);
    return new Message(sendTime);
  }

  @Override
  public void close() {}
}
