package io.scalecube.storages.chronicle;

import io.scalecube.storages.common.entity.Order;
import io.scalecube.storages.common.Storage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import net.openhft.chronicle.map.ChronicleMap;

public class ChronicleMapStorage implements Storage<UUID, Order> {

  private final ChronicleMap<UUID, Order> chronicleMap;

  public ChronicleMapStorage(int entriesCount) throws IOException {
    File file = new File("benchmarks/orders.db");

    file.getParentFile().mkdirs();

    chronicleMap = ChronicleMap
        .of(UUID.class, Order.class)
        .name("chronicleMap")
        .entries(entriesCount)
        .maxBloatFactor(50)
        .averageKey(UUID.randomUUID())
        .averageValue(new Order(null))
        .averageValueSize(512)
        .createOrRecoverPersistedTo(file, true);

    Runtime.getRuntime().addShutdownHook(new Thread(file::deleteOnExit));

    System.out.println("ChronicleMap created: " + chronicleMap.toIdentityString());
  }

  @Override
  public void write(UUID key, Order order) throws Exception {
    chronicleMap.put(key, order);
  }

  @Override
  public Order read(UUID key) throws Exception {
    return chronicleMap.get(key);
  }

  @Override
  public void close() {
    chronicleMap.close();
  }
}
