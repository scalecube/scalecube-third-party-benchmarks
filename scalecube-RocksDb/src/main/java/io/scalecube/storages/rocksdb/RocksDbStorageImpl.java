package io.scalecube.storages.rocksdb;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;

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
