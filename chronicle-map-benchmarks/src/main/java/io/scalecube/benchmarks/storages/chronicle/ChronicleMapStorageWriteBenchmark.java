package io.scalecube.benchmarks.storages.chronicle;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.storages.common.StorageState;
import io.scalecube.benchmarks.storages.common.WriteScenario;

public class ChronicleMapStorageWriteBenchmark {

  /**
   * Runs benchmark with the write scenario.
   *
   * @param args args
   */
  public static void main(String[] args) {
    WriteScenario.runWith(
        args, (BenchmarkSettings settings) -> new StorageState(settings, ChronicleMapStorage::new));
  }
}
