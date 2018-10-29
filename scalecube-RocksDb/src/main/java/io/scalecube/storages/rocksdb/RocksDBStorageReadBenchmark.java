package io.scalecube.storages.rocksdb;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.storages.common.ReadScenario;
import io.scalecube.storages.common.StorageState;

public class RocksDBStorageReadBenchmark {

  public static void main(String[] args) {
    ReadScenario.runWith(
        args, (BenchmarkSettings settings) -> new StorageState(settings, RocksDBStorage::new));
  }
}
