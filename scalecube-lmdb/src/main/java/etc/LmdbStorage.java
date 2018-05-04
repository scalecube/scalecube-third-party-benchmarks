package etc;

import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.lmdbjava.Dbi;
import org.lmdbjava.Env;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static java.nio.ByteBuffer.allocateDirect;
import static org.lmdbjava.DbiFlags.MDB_CREATE;
import static org.lmdbjava.DirectBufferProxy.PROXY_DB;

public class LmdbStorage implements Storage<Integer, Order> {

    public static final String DB_NAME = "LmdbStorage";
    private final Env<DirectBuffer> env;
    private final Dbi<DirectBuffer> db;

    public LmdbStorage() {
        File path = new File(".");
        env = Env.create(PROXY_DB)
                .setMapSize(10_485_760)
                .setMaxDbs(1)
                .open(path);

        db = env.openDbi(DB_NAME, MDB_CREATE);
    }

    @Override
    public void write(Integer k, Order v) throws IOException {

        final ByteBuffer keyBb = allocateDirect(env.getMaxKeySize());
        final MutableDirectBuffer key = new UnsafeBuffer(keyBb);
        key.putInt(0, v.getId());
        final MutableDirectBuffer val = new UnsafeBuffer(allocateDirect(1000));
        val.putBytes(0, v.serialized());
        db.put(key, val);
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
