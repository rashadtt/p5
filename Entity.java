import java.util.*;

public abstract class Entity {
  private final String name;

  public static Map<String, Map<String, Entity>> entities = Map.of(
          "Train", new HashMap<>(),
          "Passengers", new HashMap<>(),
          "Stations", new HashMap<>()
  );

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
