import net.openhft.chronicle.map.ChronicleMapBuilder;

public class Test {
  public static void main(String[] args) {
    ChronicleMapBuilder.of(Long.class, Long.class);
  }
}
