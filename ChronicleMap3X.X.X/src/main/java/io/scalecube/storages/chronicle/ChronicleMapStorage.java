package io.scalecube.storages.chronicle;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.storages.common.Storage;
import io.scalecube.storages.common.entity.Order;
import java.io.File;
import java.util.Optional;
import java.util.UUID;
import net.openhft.chronicle.map.ChronicleMap;
import reactor.core.Exceptions;

public class ChronicleMapStorage implements Storage<UUID, Order> {

  private final BenchmarkSettings settings;

  private ChronicleMap<UUID, Order> chronicleMap;
  private File persistenceFile;

  public ChronicleMapStorage(BenchmarkSettings settings) {
    this.settings = settings;
  }

  @Override
  public void start() {
    try {
      Runtime.getRuntime().addShutdownHook(new Thread(this::close));

      persistenceFile = new File(settings.find("persistenceFile", "benchmarks/orders.db"));
      persistenceFile.deleteOnExit();
      persistenceFile.getParentFile().mkdirs();

      int entriesCount =
          Optional.ofNullable(settings.find("entriesCount", null))
              .map(Integer::parseInt)
              .orElse(1_000_000);

      chronicleMap =
          ChronicleMap.of(UUID.class, Order.class)
              .name("chronicleMap")
              .entries(entriesCount)
              .maxBloatFactor(50)
              .averageKey(UUID.randomUUID())
              .averageValue(new Order(UUID.randomUUID()))
              .createOrRecoverPersistedTo(persistenceFile, true);

      System.out.println("ChronicleMap created: " + chronicleMap.toIdentityString());
    } catch (Exception e) {
      throw Exceptions.propagate(e);
    }
  }

  @Override
  public void write(UUID key, Order order) {
    chronicleMap.put(key, order);
  }

  @Override
  public Order read(UUID key) {
    return chronicleMap.get(key);
  }

  @Override
  public void close() {
    try {
      if (chronicleMap != null) {
        chronicleMap.close();
      }
    } finally {
      if (persistenceFile != null) {
        persistenceFile.deleteOnExit();
      }
    }
  }

  @Override
  public String toString() {
    return chronicleMap != null ? chronicleMap.toIdentityString() : "ChronicleMap{ [not started] }";
  }
}
