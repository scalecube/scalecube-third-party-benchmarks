package io.scalecube.storages.chronicle;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.storages.common.ReadAndWriteScenario;
import io.scalecube.storages.common.StorageState;

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
