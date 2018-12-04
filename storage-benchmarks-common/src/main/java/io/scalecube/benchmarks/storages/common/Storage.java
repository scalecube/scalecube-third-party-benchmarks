package io.scalecube.benchmarks.storages.common;

public interface Storage<K, V> {

  void start();

  void write(K k, V v);

  V read(K k);

  void close();
}
