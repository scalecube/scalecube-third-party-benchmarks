package io.scalecube.storages.mvstore;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;
import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import org.h2.mvstore.OffHeapStore;

import java.io.File;

public class MVStoreStorage implements Storage<Integer, Order> {

    private final MVMap<Integer, Order> map;
    private final MVStore s;

    public MVStoreStorage() {
        File path = new File(".");
        s = new MVStore.Builder()
                .fileName(new File(path, "mvstore.db").getAbsolutePath())
                .fileStore(new OffHeapStore())
                .open();
        map = s.openMap("MVStoreStorage");
    }

    @Override
    public void write(Integer integer, Order order) {
        map.put(integer, order);
    }

    @Override
    public Order read(Integer integer) {
        return map.get(integer);
    }

    @Override
    public void close() {
        s.close();
    }
}
