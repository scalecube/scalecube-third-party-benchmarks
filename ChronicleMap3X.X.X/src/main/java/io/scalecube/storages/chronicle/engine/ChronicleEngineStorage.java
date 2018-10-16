package io.scalecube.storages.chronicle.engine;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import java.util.UUID;
import net.openhft.chronicle.engine.api.tree.AssetTree;
import net.openhft.chronicle.engine.server.ServerEndpoint;
import net.openhft.chronicle.engine.tree.VanillaAssetTree;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

public class ChronicleEngineStorage implements Storage<UUID, Order> {

    private final ConcurrentMap<UUID, Order> diskMap;
    private ServerEndpoint endpoint;

    public ChronicleEngineStorage() {

        AssetTree serverTree = new VanillaAssetTree().forServer(false);
        try {
            endpoint = new ServerEndpoint("localhost:9090", serverTree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        diskMap = serverTree.acquireMap("localhost:9090", UUID.class, Order.class);
    }

    @Override
    public void write(UUID s, Order order) throws IOException {
        diskMap.put(s, order);
    }

    @Override
    public Order read(UUID s) throws IOException {
        return diskMap.get(s);
    }

    @Override
    public void close() {
        endpoint.close();
    }

    public static void main(String[] args) throws IOException {
        ChronicleEngineStorage storage = new ChronicleEngineStorage();
        Order o = new Order(42);
        storage.write(o.id(), o);
        Order o1 = storage.read(o.id());
        System.out.println(o1);
    }
}
