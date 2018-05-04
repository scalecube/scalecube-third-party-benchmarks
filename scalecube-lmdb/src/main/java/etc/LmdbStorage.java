package etc;

import org.lmdbjava.Dbi;
import org.lmdbjava.Env;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import static org.lmdbjava.DbiFlags.MDB_CREATE;

public class LmdbStorage implements Storage<Integer, Order> {

    private final Env<ByteBuffer> env;
    private final Dbi<ByteBuffer> db;

    public LmdbStorage() throws IOException {
        // We always need an Env. An Env owns a physical on-disk storage file. One
        // Env can store many different databases (ie sorted maps).
        // LMDB also needs to know how large our DB might be. Over-estimating is OK.
        // LMDB also needs to know how many DBs (Dbi) we want to store in this Env.
        // Now let's open the Env. The same path can be concurrently opened and
        // used in different processes, but do not open the same path twice in
        // the same process at the same time.

        File path = new File(".");

        env = Env.create()
                // LMDB also needs to know how large our DB might be. Over-estimating is OK.
                .setMapSize(10_485_760)
                // LMDB also needs to know how many DBs (Dbi) we want to store in this Env.
                .setMaxDbs(1)
                // Now let's open the Env. The same path can be concurrently opened and
                // used in different processes, but do not open the same path twice in
                // the same process at the same time.
                .open(path);

        db = env.openDbi("LmdbStorage", MDB_CREATE);
    }

    @Override
    public void write(Integer k, Order v) throws IOException {
        // We want to store some data, so we will need a direct ByteBuffer.
        // Note that LMDB keys cannot exceed maxKeySize bytes (511 bytes by default).
        // Values can be larger.

        ByteBuffer keyBuffer = (ByteBuffer) ByteBuffer.allocateDirect(4).putInt(k).flip();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        v.writeExternal(oos);
        oos.flush();
        byte[] byteArray = baos.toByteArray();
        ByteBuffer valBuffer = (ByteBuffer) ByteBuffer.allocateDirect(byteArray.length).put(byteArray).flip();

        // Now store it. Dbi.put() internally begins and commits a transaction (Txn).
        db.put(keyBuffer, valBuffer);
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
