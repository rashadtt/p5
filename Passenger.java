import java.util.*;

public class Passenger extends Entity {
  private Passenger(String name) { super(name); }
  public List<String> journey = new ArrayList<String>();

  public static Passenger make(String name) {
    if (entities.get("Passenger").containsKey(name)){
      return (Passenger) entities.get("Passenger").get(name);
    } else {
      Passenger p = new Passenger(name);
      entities.get("Passenger").put(name, p);
      return p;
    }
  }
}
