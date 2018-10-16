package io.scalecube.storages.common;

public interface Storage<K, V> {

  void write(K k, V v) throws Exception;

  V read(K k) throws Exception;

  void close();
}
