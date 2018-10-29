package io.scalecube.storages.lmdb;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.storages.common.StorageState;
import io.scalecube.storages.common.WriteScenario;

public class LmdbStorageWriteBenchmark {

  /**
   * Runs benchmark with the write scenario.
   *
   * @param args args
   */
  public static void main(String[] args) {
    WriteScenario.runWith(
        args,
        (BenchmarkSettings settings) -> new StorageState(settings, LmdbStorageAgronaBuffers::new));
  }
}
