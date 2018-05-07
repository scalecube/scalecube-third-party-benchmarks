package etc;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RocksDBStorage implements Storage<Integer, Order> {

  private final RocksDB rocksDb;

  public RocksDBStorage() {
    RocksDB.loadLibrary();
    System.out.println("RocksDB.loadLibrary DONE");

    try (Options options = new Options()
        .setAllowMmapReads(true)
        .setAllowMmapWrites(true)
        .setAllowConcurrentMemtableWrite(true)
        .setIncreaseParallelism(Runtime.getRuntime().availableProcessors())
        .setCreateIfMissing(true)) {

      rocksDb = RocksDB.open(options, "RocksDBStorage");

      System.out.println("RocksDB opened: " + rocksDb);
    } catch (RocksDBException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void write(Integer i, Order order) throws IOException {
    final ByteBuffer key = ByteBuffer.allocate(4);
    key.putInt(0, i);

    final ByteBuffer val = ByteBuffer.allocate(order.toBytes().length);
    val.put(order.toBytes());

    try {
      rocksDb.put(key.array(), val.array());
    } catch (RocksDBException e) {
      throw new IOException(e);
    }
  }

  @Override
  public Order read(Integer integer) throws IOException {
    return null;
  }

  @Override
  public void close() {

  }
}
