package etc;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

import java.io.File;
import java.io.IOException;

public class MvStorage implements Storage<Integer, Order> {

    private final MVMap<Integer, Order> map;
    private final MVStore s;

    public MvStorage() {
        File path = new File(".");
        s = new MVStore.Builder()
                .fileName(new File(path, "mvstore.db").getAbsolutePath())
                .autoCommitDisabled()
                .open();
        map = s.openMap("MvStorage");
    }

    @Override
    public void write(Integer integer, Order order) throws IOException {
        map.put(integer, order);
    }

    @Override
    public Order read(Integer integer) throws IOException {
        return map.get(integer);
    }

    @Override
    public void close() {
        s.close();
    }
}
