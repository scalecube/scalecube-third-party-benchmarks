package io.scalecube.benchmarks.storages.chronicle;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.storages.common.StorageStatePerThread;
import io.scalecube.benchmarks.storages.common.WriteScenario;

public class ChronicleMapStoragePerThreadWriteBenchmark {

  /**
   * Runs benchmark with the write scenario.
   *
   * @param args args
   */
  public static void main(String[] args) {
    WriteScenario.runWith(
        args,
        (BenchmarkSettings settings) ->
            new StorageStatePerThread(settings, ChronicleMapStorage::new));
  }
}
