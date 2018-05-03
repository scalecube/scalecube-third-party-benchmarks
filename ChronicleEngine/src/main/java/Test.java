import net.openhft.chronicle.engine.Chassis;
import net.openhft.chronicle.engine.api.map.MapView;

public class Test {
  public static void main(String[] args) throws InterruptedException {
    MapView<String, String> mapView = Chassis.acquireMap("map", String.class, String.class);
// register a subscriber for any topic in a group on another machine.
    Chassis.registerTopicSubscriber("map",
        String.class,
        String.class,
        (k, v) -> System.out.println("key: " + k + ", value: " + v));
    Thread.currentThread().join();
  }
}
