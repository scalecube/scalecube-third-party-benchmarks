package io.scalecube.storages.chronicle.engine;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import net.openhft.chronicle.engine.Chassis;
import net.openhft.chronicle.engine.api.tree.AssetTree;
import net.openhft.chronicle.engine.server.ServerEndpoint;
import net.openhft.chronicle.engine.tree.VanillaAssetTree;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

public class ChronicleEngineStorage implements Storage<String, Order> {

    private final ConcurrentMap<String, Order> diskMap;
    private ServerEndpoint endpoint;

    public ChronicleEngineStorage() {

        AssetTree serverTree = new VanillaAssetTree().forServer(false);
        try {
            endpoint = new ServerEndpoint("localhost:9090", serverTree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        diskMap = serverTree.acquireMap("localhost:9090", String.class, Order.class);
    }

    @Override
    public void write(String s, Order order) throws IOException {
        diskMap.put(s, order);
    }

    @Override
    public Order read(String s) throws IOException {
        return diskMap.get(s);
    }

    @Override
    public void close() {
        endpoint.close();
    }

    public static void main(String[] args) throws IOException {
        ChronicleEngineStorage storage = new ChronicleEngineStorage();
        Order o = new Order(42);
        storage.write(o.getId(), o);
        Order o1 = storage.read(o.getId());
        System.out.println(o1);
    }
}
