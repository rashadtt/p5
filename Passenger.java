import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Passenger extends Entity implements Runnable {
  public List<Station> journey = new ArrayList<>();
  public int currentStationIndex = 0;
  public Station currentStation = null;
  public Train currentTrain = null;
  public Train nextTrain = null;
  public boolean isOnTrain = false;
  public boolean arrived = false;
  public MBTA mbta;
  public Log log;

  private Passenger(String name) { super(name); }

  public static Passenger make(String name) {
    if (passengers.containsKey(name)){
      return passengers.get(name);
    } else {
      Passenger p = new Passenger(name);
      passengers.put(name, p);
      return p;
    }
  }

  public void run(){
    while (!arrived) {
      Station nextStation = getNextStation();
      Train currT = currentTrain;
      if (isOnTrain){
        nextStation.lock.lock();
        try {
          while (nextStation.currentTrain != currentTrain) {
            nextStation.condition.await();
          }
          Event deboard = new DeboardEvent(this, currentTrain, nextStation);
          deboard.replayAndCheck(mbta);
          log.passenger_deboards(this, currT, nextStation);
          nextStation.condition.signalAll();
        } catch (Exception e){
          throw new RuntimeException(e);
        } finally {
          nextStation.lock.unlock();
        }
      } else {
        currentStation.lock.lock();
        if (nextTrain == null) {
          for (Train train : mbta.lines.keySet()) {
            if (train.stations.contains(getCurrentStation()) && train.stations.contains(getNextStation())) {
              nextTrain = train;
            }
          }
        }
        try {
          while (currentStation.currentTrain != nextTrain) {
            currentStation.condition.await();
          }
          Event board = new BoardEvent(this, nextTrain, currentStation);
          board.replayAndCheck(mbta);
          log.passenger_boards(this, nextTrain, currentStation);
          currentStation.condition.signalAll();
        } catch (Exception e){
          throw new RuntimeException(e);
        } finally {
          currentStation.lock.unlock();
        }
      }
    }
  }



  public Station getCurrentStation(){
    if (currentStationIndex >= 0 && currentStationIndex < journey.size()){
      return journey.get(currentStationIndex);
    }
    return null;
  }

  public Station getNextStation(){
    if (currentStationIndex + 1 < journey.size()){
      return journey.get(currentStationIndex + 1);
    }
    return null;
  }

  public void boardTrain(Train train){
    if (!isOnTrain){
      currentTrain = train;
      isOnTrain = true;
    }
  }

  public void deboardTrain(){
    currentStationIndex++;
    currentStation = getCurrentStation();
    if (currentStation == journey.getLast()){
      arrived = true;
      mbta.arrived.put(this, true);
      if (!mbta.arrived.containsValue(false)){
        mbta.isDone = true;
      }
    } else {
      for (Train train : mbta.lines.keySet()) {
        if (train.stations.contains(getCurrentStation()) && train.stations.contains(getNextStation())) {
          nextTrain = train;
        }
      }
    }
    isOnTrain = false;
    currentTrain = null;
  }

}
