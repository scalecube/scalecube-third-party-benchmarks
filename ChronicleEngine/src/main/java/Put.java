import net.openhft.chronicle.engine.Chassis;
import net.openhft.chronicle.engine.api.map.MapView;

public class Put {
  public static void main(String[] args) throws InterruptedException {
    MapView<String, String> mapView = Chassis.acquireMap("map", String.class, String.class);
    mapView.put("s", "val");
    Thread.currentThread().join();

  }
}
