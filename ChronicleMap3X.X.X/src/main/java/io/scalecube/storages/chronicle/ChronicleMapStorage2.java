package io.scalecube.storages.chronicle;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import net.openhft.chronicle.map.ChronicleMap;

import java.io.File;
import java.io.IOException;

public class ChronicleMapStorage2 implements Storage<String, Order> {

  private final ChronicleMap<String, Order> chronicleMap;

  public ChronicleMapStorage2(int entriesCount) throws IOException {
    final File path = new File("benchmarks/orders.db");

    path.getParentFile().mkdirs();

    ChronicleMapMarshaller<Order> marshaller = new ChronicleMapMarshaller<>();

    chronicleMap = ChronicleMap
        .of(String.class, Order.class)
        .valueMarshaller(marshaller)
        .name("chronicleMap")
        .entries(entriesCount)
        .maxBloatFactor(50)
        .averageKeySize(128)
        .averageValueSize(512)
        .createOrRecoverPersistedTo(path, true);

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  path.deleteOnExit();
                }));

    System.out.println("ChronicleMap created: " + chronicleMap.toIdentityString());
  }

  public static void main(String[] args) throws IOException {
    ChronicleMapStorage2 storage2 = new ChronicleMapStorage2(100);

    storage2.write("1", new Order(1));

    Order order = storage2.read("1");

    System.out.println(order);
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
