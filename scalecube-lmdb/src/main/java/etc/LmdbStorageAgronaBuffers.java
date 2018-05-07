package etc;

import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.lmdbjava.Cursor;
import org.lmdbjava.Dbi;
import org.lmdbjava.Env;
import org.lmdbjava.EnvFlags;
import org.lmdbjava.Txn;

import java.io.File;
import java.io.IOException;

import static java.nio.ByteBuffer.allocateDirect;
import static org.lmdbjava.DbiFlags.MDB_CREATE;
import static org.lmdbjava.DirectBufferProxy.PROXY_DB;

/**
 * LMDB storage connector with the following configuration:<p/>
 * 1. Uses Agrona buffers.
 * 2. Don't fsync after commit.
 * 3. Use writable mmap.
 */
public class LmdbStorageAgronaBuffers implements Storage<Integer, Order> {

    public static final String DB_NAME = "LmdbStorageAgronaBuffers";
    private final Env<DirectBuffer> env;
    private final Dbi<DirectBuffer> db;

    public LmdbStorageAgronaBuffers() {
        File path = new File(".");
        env = Env.create(PROXY_DB)
                .setMapSize(10_485_760_000L)
                .setMaxDbs(1)
                .open(path, EnvFlags.MDB_NOSYNC, EnvFlags.MDB_WRITEMAP);

        db = env.openDbi(DB_NAME, MDB_CREATE);
        System.getProperties().setProperty(Env.DISABLE_CHECKS_PROP, "true");
    }

    @Override
    public void write(Integer k, Order v) throws IOException {
        try (Txn<DirectBuffer> txn = env.txnWrite()) {
            final Cursor<DirectBuffer> c = db.openCursor(txn);

            final MutableDirectBuffer key = new UnsafeBuffer(allocateDirect(4));
            key.putInt(0, v.getId());

            byte[] valBytes = v.serialized();
            final MutableDirectBuffer val = new UnsafeBuffer(allocateDirect(valBytes.length));
            val.putBytes(0, valBytes);
            c.put(key, val);

            c.close();
            txn.commit();
        }
    }

    @Override
    public Order read(Integer o) {
        return null;
    }

    @Override
    public void close() {
        db.close();
        env.close();
    }
}
