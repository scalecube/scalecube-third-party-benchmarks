package io.scalecube.storages.chronicle;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;

import java.io.File;
import java.io.IOException;

import net.openhft.chronicle.map.ChronicleMap;

public class ChronicleMapStorage implements Storage<String, Order> {

  private final ChronicleMap<String, Order> chronicleMap;

  public ChronicleMapStorage(int entriesCount) throws IOException {
    final File path = new File("/mnt/efs/ChronicleMapStorage");

    chronicleMap = ChronicleMap
        .of(String.class, Order.class)
        .name("chronicleMap")
        .entries(entriesCount)
        .maxBloatFactor(50)
        .averageKeySize(128)
        .averageValueSize(512)
        .createOrRecoverPersistedTo(path, true);

    System.out.println("ChronicleMap created: " + chronicleMap.toIdentityString());
  }

  @Override
  public void write(String key, Order order) throws IOException {
    chronicleMap.put(key, order);
  }

  @Override
  public Order read(String key) throws IOException {
    return chronicleMap.get(key);
  }

  @Override
  public void close() {
    chronicleMap.close();
  }
}
