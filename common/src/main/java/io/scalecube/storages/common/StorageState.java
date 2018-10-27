package io.scalecube.storages.common;

import io.scalecube.benchmarks.BenchmarkSettings;
import io.scalecube.benchmarks.BenchmarkState;
import io.scalecube.storages.common.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.function.Function;

public class StorageState extends BenchmarkState<StorageState> {

  private static final Logger LOGGER = LoggerFactory.getLogger(StorageState.class);

  private final Function<BenchmarkSettings, Storage<UUID, Order>> supplier;

  private Storage<UUID, Order> storage;
  private int preloadCount;

  public StorageState(
      BenchmarkSettings settings, Function<BenchmarkSettings, Storage<UUID, Order>> supplier) {
    super(settings);
    this.supplier = supplier;
  }

  @Override
  protected void beforeAll() {
    storage = supplier.apply(settings);
    storage.start();
    LOGGER.info("Storage created: {}", storage);
    preloadCount = Integer.parseInt(settings.find("preloadCount", "0"));
    LOGGER.info("Starting to populate db with {} elements", preloadCount);
    populate(preloadCount);
    LOGGER.info("Finished to populate db");
  }

  @Override
  protected void afterAll() {
    storage.close();
  }

  public Storage<UUID, Order> storage() {
    return storage;
  }

  public int preloadCount() {
    return preloadCount;
  }

  public UUID uuid(int n) {
    return new UUID(Integer.MAX_VALUE, n);
  }

  private void populate(int n) {
    if (n > 0) {
      Flux.range(0, n)
          .parallel()
          .runOn(scheduler())
          .doOnNext(
              i -> {
                Order order = new Order(uuid(i));
                try {
                  storage.write(order.id(), order);
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              })
          .groups()
          .flatMap(Function.identity())
          .blockLast();
    }
  }
}
