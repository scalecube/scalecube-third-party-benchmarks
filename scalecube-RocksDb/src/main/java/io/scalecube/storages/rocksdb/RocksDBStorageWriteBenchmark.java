package io.scalecube.storages.rocksdb;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.storages.common.StorageState;
import io.scalecube.storages.common.WriteScenario;

public class RocksDBStorageWriteBenchmark {

  public static void main(String[] args) {
    WriteScenario.runWith(
        args, (BenchmarkSettings settings) -> new StorageState(settings, RocksDBStorage::new));
  }
}
