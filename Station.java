import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Station extends Entity {
  public ReentrantLock lock = new ReentrantLock();
  public Condition condition = lock.newCondition();
  public Train currentTrain = null;

  private Station(String name) { super(name); }

  public static Station make(String name) {
    if (stations.containsKey(name)){
      return stations.get(name);
    } else {
      Station s = new Station(name);
      stations.put(name, s);
      return s;
    }
  }

}
