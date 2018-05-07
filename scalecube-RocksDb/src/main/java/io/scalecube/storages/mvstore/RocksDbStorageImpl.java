package io.scalecube.storages.mvstore;

import java.io.IOException;

public class RocksDbStorageImpl implements Storage<Integer, Order> {

  @Override
  public void write(Integer integer, Order order) throws IOException {

  }

  @Override
  public Order read(Integer integer) throws IOException {
    return null;
  }

  @Override
  public void close() {

  }
}
