import java.util.*;

public class Train extends Entity {
  private Train(String name) { super(name); }
  public List<String> passengers = new ArrayList<>();
  public List<String> stations = new ArrayList<>();

  public static Train make(String name) {
    if (entities.get("Train").containsKey(name)){
      return (Train) entities.get("Train").get(name);
    } else {
      Train t = new Train(name);
      entities.get("Train").put(name, t);
      return t;
    }
  }
}
