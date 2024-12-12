import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

public class MBTA {
  public Log log = new Log();
  public Map<Train, List<Station>> lines = new HashMap<>();
  public Map<Passenger, List<Station>> trips = new HashMap<>();
  public Map<Passenger, Boolean> arrived = new ConcurrentHashMap<>();
  public Config config = new Config();
  public volatile boolean isDone = false;

  // Creates an initially empty simulation
  public MBTA() { }

  // Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) {
    Train train = Train.make(name);
    List<Station> stationList = new ArrayList<>();
    for (String station : stations){
      stationList.add(Station.make(station));
    }

    train.stations = stationList;
    train.currentStation = stationList.getFirst();
    train.currentStation.currentTrain = train;
    lines.put(train, stationList);
  }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) {
    Passenger passenger = Passenger.make(name);
    List<Station> stationList = new ArrayList<>();
    for (String station : stations){
      stationList.add(Station.make(station));
    }

    passenger.currentStation = stationList.getFirst();
    passenger.journey = stationList;
    for (Train train : lines.keySet()) {
      if (train.stations.contains(passenger.getCurrentStation()) && train.stations.contains(passenger.getNextStation())) {
        passenger.nextTrain = train;
      }
    }
    trips.put(passenger, stationList);
    arrived.put(passenger, false);
  }

  // Return normally if initial simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkStart() {
    for (Passenger passenger : trips.keySet()){
      if (passenger.currentStation != passenger.journey.getFirst()){
        throw new RuntimeException();
      }
    }

    for (Train train : lines.keySet()){
      if (train.currentStation != train.stations.getFirst() || train.stations.getFirst().currentTrain != train){
        throw new RuntimeException();
      }
    }
  }

  // Return normally if final simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkEnd() {
    if (arrived.containsValue(false)){
      throw new UnsupportedOperationException();
    }
  }

  // reset to an empty simulation
  public void reset() {
    lines.clear();
    trips.clear();
    for (Passenger passenger : Entity.passengers.values()){
      passenger.journey = new ArrayList<>();
      passenger.currentStationIndex = 0;
      passenger.currentStation = null;
      passenger.currentTrain = null;
      passenger.nextTrain = null;
      passenger.isOnTrain = false;
      passenger.arrived = false;
      passenger.mbta = new MBTA();
      passenger.log = new Log();
    }
    for (Station station : Entity.stations.values()){
      station.currentTrain = null;
    }
    for (Train train : Entity.trains.values()){
      train.stations = new ArrayList<>();
      train.currentStationIndex = 0;
      train.currentStation = null;
      train.isMovingForward = true;
      train.mbta = new MBTA();
      train.log = new Log();
    }

    config.lines.clear();
    config.trips.clear();
    config = new Config();
    arrived = new ConcurrentHashMap<>();
  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) throws IOException {
    InputStream input = new FileInputStream(filename);
    JsonReader reader = new JsonReader(new InputStreamReader(input));
    Gson gson = new Gson();
    config = gson.fromJson(reader, Config.class);

    for (String line : config.lines.keySet()){
      addLine(line, config.lines.get(line));
    }

    for (String passenger : config.trips.keySet()){
      addJourney(passenger, config.trips.get(passenger));
    }
  }

  public void setMBTAandLog(){
    for (Passenger passenger : trips.keySet()){
      passenger.mbta = this;
      passenger.log = log;
    }

    for (Train train : lines.keySet()){
      train.mbta = this;
      train.log = log;
    }
  }
}
