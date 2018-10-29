package io.scalecube.storages.chronicle;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.storages.common.StorageState;
import io.scalecube.storages.common.WriteScenario;

public class ChronicleMapStorageWriteBenchmark {

  public static void main(String[] args) {
    WriteScenario.runWith(
        args, (BenchmarkSettings settings) -> new StorageState(settings, ChronicleMapStorage::new));
  }
}
