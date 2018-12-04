package io.scalecube.benchmarks.storages.lmdb;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.storages.common.ReadScenario;
import io.scalecube.benchmarks.storages.common.StorageState;

public class LmdbStorageReadBenchmark {

  /**
   * Runs benchmark with the read scenario.
   *
   * @param args args
   */
  public static void main(String[] args) {
    ReadScenario.runWith(
        args,
        (BenchmarkSettings settings) -> new StorageState(settings, LmdbStorageAgronaBuffers::new));
  }
}
