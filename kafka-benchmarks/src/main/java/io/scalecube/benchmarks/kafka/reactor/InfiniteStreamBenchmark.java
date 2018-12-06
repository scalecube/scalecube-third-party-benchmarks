package io.scalecube.benchmarks.kafka.reactor;

final class InfiniteStreamBenchmark {

  public static void main(String[] args) {
    InfiniteStreamScenario.runWith(args, ReactorKafkaState::new);
  }
}
