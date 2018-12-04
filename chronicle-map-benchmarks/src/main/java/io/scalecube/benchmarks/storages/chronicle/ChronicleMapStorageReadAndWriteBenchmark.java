package io.scalecube.benchmarks.storages.chronicle;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.storages.common.ReadAndWriteScenario;
import io.scalecube.benchmarks.storages.common.StorageState;

public class ChronicleMapStorageReadAndWriteBenchmark {

  /**
   * Runs benchmark with the read scenario.
   *
   * @param args args
   */
  public static void main(String[] args) {
    ReadAndWriteScenario.runWith(
        args, (BenchmarkSettings settings) -> new StorageState(settings, ChronicleMapStorage::new));
  }
}
