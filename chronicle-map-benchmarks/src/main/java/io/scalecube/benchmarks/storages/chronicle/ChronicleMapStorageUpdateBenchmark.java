package io.scalecube.benchmarks.storages.chronicle;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.storages.common.StorageState;
import io.scalecube.benchmarks.storages.common.UpdateScenario;

public class ChronicleMapStorageUpdateBenchmark {

  /**
   * Runs benchmark with the read scenario.
   *
   * @param args args
   */
  public static void main(String[] args) {
    UpdateScenario.runWith(
        args, (BenchmarkSettings settings) -> new StorageState(settings, ChronicleMapStorage::new));
  }
}
