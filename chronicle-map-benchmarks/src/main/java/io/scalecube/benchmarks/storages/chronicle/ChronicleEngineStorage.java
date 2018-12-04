package io.scalecube.benchmarks.storages.chronicle;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.storages.common.Storage;
import io.scalecube.benchmarks.storages.common.entity.Order;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import net.openhft.chronicle.engine.api.tree.AssetTree;
import net.openhft.chronicle.engine.server.ServerEndpoint;
import net.openhft.chronicle.engine.tree.VanillaAssetTree;

public class ChronicleEngineStorage implements Storage<UUID, Order> {

  private ConcurrentMap<UUID, Order> diskMap;
  private ServerEndpoint endpoint;

  ChronicleEngineStorage(BenchmarkSettings settings) {}

  @Override
  public void start() {
    AssetTree serverTree = new VanillaAssetTree().forServer(false);
    try {
      endpoint = new ServerEndpoint("localhost:9090", serverTree);
    } catch (IOException e) {
      e.printStackTrace();
    }
    diskMap = serverTree.acquireMap("localhost:9090", UUID.class, Order.class);
  }

  @Override
  public void write(UUID s, Order order) {
    diskMap.put(s, order);
  }

  @Override
  public Order read(UUID s) {
    return diskMap.get(s);
  }

  @Override
  public void close() {
    endpoint.close();
  }
}
