import java.util.*;

public abstract class Entity {
  final String name;
  public static Map<String, Passenger> passengers = new HashMap<>();
  public static Map<String, Station> stations = new HashMap<>();
  public static Map<String, Train> trains = new HashMap<>();
//  public static MBTA m = new MBTA();

  protected Entity(String name) { this.name = name; }

  public String toString() { return name; }

  public boolean equals(Object o) {
    if (o instanceof Entity e) {
      return e.getClass().equals(getClass()) && name.equals(e.name);
    }
    return false;
  }

  public int hashCode() { return name.hashCode(); }
}
