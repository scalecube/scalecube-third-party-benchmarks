package io.scalecube.benchmarks.kafka;

public final class Message {

  private long sendTime;

  public Message() {}

  public Message(long sendTime) {
    this.sendTime = sendTime;
  }

  public long sendTime() {
    return sendTime;
  }

  public Message sendTime(long sendTime) {
    this.sendTime = sendTime;
    return this;
  }
}
