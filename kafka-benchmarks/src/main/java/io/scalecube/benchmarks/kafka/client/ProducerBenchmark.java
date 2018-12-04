package io.scalecube.benchmarks.kafka.client;

final class ProducerBenchmark {

  public static void main(String[] args) {
    ProducerSendScenario.runWith(args, KafkaState::new);
  }
}
