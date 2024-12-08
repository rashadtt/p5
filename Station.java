import java.util.*;

public class Station extends Entity {
  private Station(String name) { super(name); }
  public List<String> passengers = new ArrayList<>();

  public static Station make(String name) {
    if (entities.get("Station").containsKey(name)){
      return (Station) entities.get("Station").get(name);
    } else {
      Station s = new Station(name);
      entities.get("Station").put(name, s);
      return s;
    }
  }
}
