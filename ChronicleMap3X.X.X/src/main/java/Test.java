import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.util.Map;

public class Test {
  public static void main(String[] args) {
    Map<Integer, CharSequence> map = ChronicleMapBuilder.of(Integer.class,
        CharSequence.class).create();

    map.put(1, "hello world");
    System.out.println(map.get(1));
  }
}
