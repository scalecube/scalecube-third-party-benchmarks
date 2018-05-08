package io.scalecube.storages.mvstore;

import io.scalecube.storages.common.Order;
import io.scalecube.storages.common.Storage;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import org.h2.mvstore.OffHeapStore;

import java.io.File;

public class MVStoreStorage implements Storage<String, Order> {

  private final MVMap<String, Order> mvMap;
  private final MVStore mvStore;

  public MVStoreStorage() {
    File path = new File(".");
    mvStore = new MVStore.Builder()
        .fileName(new File(path, "mvstore.db").getAbsolutePath())
        .fileStore(new OffHeapStore())
        .open();
    System.out.println("MVStore instance created: " + mvStore);
    mvMap = mvStore.openMap("MVStoreStorage");
    System.out.println("MVMap instance opened: " + mvStore);
  }

  @Override
  public void write(String key, Order order) {
    mvMap.put(key, order);
  }

  @Override
  public Order read(String key) {
    return mvMap.get(key);
  }

  @Override
  public void close() {
    mvStore.close();
    System.out.println("MVStore instance closed: " + mvStore + ", thank you, good bye");
  }
}
