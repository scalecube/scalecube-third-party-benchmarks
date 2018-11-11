package io.scalecube.storages.chronicle;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.storages.common.StorageStatePerThread;
import io.scalecube.storages.common.WriteScenario;

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
