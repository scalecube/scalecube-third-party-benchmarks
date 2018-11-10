package io.scalecube.storages.common;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.storages.common.entity.Order;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Exceptions;

public class StorageStatePerThread extends StorageState {

  private static final Logger LOGGER = LoggerFactory.getLogger(StorageStatePerThread.class);

  private final BlockingQueue<StorageState> storageStates = new LinkedBlockingQueue<>();

  private final ThreadLocal<Storage<UUID, Order>> storage =
      ThreadLocal.withInitial(
          () -> {
            try {
              StorageState storageState = storageStates.take();
              Storage<UUID, Order> storage = storageState.storage();
              storageStates.add(storageState);
              LOGGER.info(
                  "set storage {} for thread [{}]", storage, Thread.currentThread().getName());
              return storage;
            } catch (Exception e) {
              throw Exceptions.propagate(e);
            }
          });

  public StorageStatePerThread(
      BenchmarkSettings settings, Function<BenchmarkSettings, Storage<UUID, Order>> supplier) {
    super(settings, supplier);
  }

  @Override
  protected void beforeAll() {
    for (int i = 0; i < settings.numberThreads(); i++) {
      StorageState storageState = new StorageState(settings, supplier);
      storageStates.add(storageState);
      storageState.beforeAll();
    }
  }

  @Override
  protected void afterAll() {
    storageStates.forEach(StorageState::afterAll);
  }

  @Override
  public Storage<UUID, Order> storage() {
    return storage.get();
  }

  @Override
  public int preloadCount() {
    return Integer.parseInt(settings.find("preloadCount", "0"));
  }
}
