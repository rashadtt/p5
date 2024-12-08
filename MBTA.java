import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

public class MBTA {

  private Map<String, List<String>> lines = new HashMap<>();
  private Map<String, List<String>> journeys = new HashMap<>();

  // Creates an initially empty simulation
  public MBTA() { }

  // Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) { lines.put(name, stations); }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) {
    journeys.put(name, stations);
  }

  // Return normally if initial simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkStart() {
  }

  // Return normally if final simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkEnd() {
  }

  // reset to an empty simulation
  public void reset() {
    lines.clear();
    journeys.clear();
  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) throws FileNotFoundException, IOException {
    InputStream input = new FileInputStream(filename);
    JsonReader reader = new JsonReader(new InputStreamReader(input));

    reader.beginObject();
    while (reader.hasNext()){
      String name = reader.nextName();
      if (name.equals("lines")){
        reader.beginObject();
        while (reader.hasNext()){
          String color = reader.nextName();
          List<String> stations = new ArrayList<>();
          reader.beginArray();
          while (reader.hasNext()){
            String station = reader.nextString();
            Entity.entities.get("Stations").put(station, Station.make(station));
            stations.add(station);
          }
          lines.put(color, stations);
          reader.endArray();
        }
        reader.endObject();
      } else if (name.equals("trips")){
        reader.beginObject();
        while (reader.hasNext()){
          String passenger = reader.nextString();
          Entity.entities.get("Passengers").put(passenger, Passenger.make(passenger));
          List<String> trips = new ArrayList<>();
          reader.beginArray();
          while (reader.hasNext()){
            trips.add(reader.nextString());
          }
          journeys.put(passenger, trips);
          reader.endArray();
        }
        reader.endObject();
      }
    }
    reader.endObject();
  }

  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append(lines);
    sb.append(journeys);
    return sb.toString();
  }
}
