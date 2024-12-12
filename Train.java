import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Train extends Entity implements Runnable {
//  public List<Passenger> passengers = new ArrayList<>();
  public List<Station> stations = new ArrayList<>();
  public int currentStationIndex = 0;
  public Station currentStation;
  public boolean isMovingForward = true;
  public MBTA mbta;
  public Log log;

  private Train(String name) { super(name); }

  public static Train make(String name) {
    if (trains.containsKey(name)){
      return trains.get(name);
    } else {
      Train t = new Train(name);
      trains.put(name, t);
      return t;
    }
  }

  public void run() {
    try {
      while (!mbta.isDone) {
        Station next = getNextStation();
        Station curr = currentStation;
        next.lock.lock();
        try {
          while (next.currentTrain != null) {
            next.condition.await();
          }
          try {
            curr.lock.lock();

            Event move = new MoveEvent(this, curr, next);
            move.replayAndCheck(mbta);
            log.train_moves(this, curr, next);

            curr.condition.signalAll();
          } catch(Exception e) {
            throw new RuntimeException(e);
          } finally {
            curr.lock.unlock();
          }
          if (next.lock.isHeldByCurrentThread()) {
            next.condition.signalAll();
          }
        } finally {
          next.lock.unlock();
        }
        Thread.sleep(20);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }


  public Station getCurrentStation(){
    if (currentStationIndex >=0 && currentStationIndex < stations.size()){
      return stations.get(currentStationIndex);
    }
    return null;
  }

  public Station getNextStation(){
    if (stations.isEmpty()){
      throw new NoSuchElementException();
    }
    if (isMovingForward){
      if (currentStationIndex + 1 < stations.size()){
        return stations.get(currentStationIndex + 1);
      } else {
        isMovingForward = false;
        return stations.get(currentStationIndex - 1);
      }
    } else {
      if (currentStationIndex - 1 >= 0){
        return stations.get(currentStationIndex - 1);
      } else {
        isMovingForward = true;
        return stations.get(currentStationIndex + 1);
      }
    }
  }

  public void move(){
    if (isMovingForward) {
      if (currentStationIndex + 1 < stations.size()){
        currentStationIndex++;
      } else {
        isMovingForward = false;
        currentStationIndex--;
      }
    } else {
      if (currentStationIndex - 1 >= 0){
        currentStationIndex--;
      } else {
        isMovingForward = true;
        currentStationIndex++;
      }
    }
    currentStation.currentTrain = null;
    currentStation = getCurrentStation();
    currentStation.currentTrain = this;
  }
}
