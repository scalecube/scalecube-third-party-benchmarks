package io.scalecube.benchmarks.storages.rocksdb;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.storages.common.StorageState;
import io.scalecube.benchmarks.storages.common.WriteScenario;

public class RocksdbStorageWriteBenchmark {

  /**
   * Runs benchmark with the write scenario.
   *
   * @param args args
   */
  public static void main(String[] args) {
    WriteScenario.runWith(
        args, (BenchmarkSettings settings) -> new StorageState(settings, RocksdbStorage::new));
  }
}
