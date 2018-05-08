package io.scalecube.storages.chronicle;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;

import java.io.File;
import java.io.IOException;

import net.openhft.chronicle.map.ChronicleMap;

public class ChronicleMapStorage implements Storage<Integer, Order> {

  private final ChronicleMap<Integer, Order> storage;

  public ChronicleMapStorage(int entriesCount) throws IOException {
    final File path = new File("ChronicleMapStorage");

    storage = ChronicleMap
        .of(Integer.class, Order.class)
        .name("storage-db")
        .averageValue(new Order())
        .entries(entriesCount)
        .maxBloatFactor(50)
        .createOrRecoverPersistedTo(path, true);
  }

  @Override
  public void write(Integer key, Order order) throws IOException {
    storage.put(key, order);
  }

  @Override
  public Order read(Integer key) throws IOException {
    return storage.get(key);
  }

  @Override
  public void close() {
    storage.close();
  }

}
