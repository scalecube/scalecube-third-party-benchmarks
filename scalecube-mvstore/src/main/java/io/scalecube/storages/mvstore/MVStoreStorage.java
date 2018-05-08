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
    mvMap = mvStore.openMap("MVStoreStorage");
  }

  @Override
  public void write(String str, Order order) {
    mvMap.put(str, order);
  }

  @Override
  public Order read(String str) {
    return mvMap.get(str);
  }

  @Override
  public void close() {
    mvStore.close();
  }
}
