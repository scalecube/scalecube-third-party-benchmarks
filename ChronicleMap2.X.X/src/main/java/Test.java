import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.File;
import java.io.IOException;

public class Test {
  public static void main(String[] args) throws IOException {
    String tmp = System.getProperty("java.io.tmpdir");
    String pathname = tmp + "/shm-test/myfile.dat";

    ChronicleMap<String, String> map = ChronicleMapBuilder
        .of(String.class, String.class)
        .instance()
        .persistedTo(new File("foile"))
        .create();

    map.put("k", "v");
    String k = map.get("k");
    System.out.println(k);

  }
}
