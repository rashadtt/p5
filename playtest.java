import java.io.*;
import java.util.*;
import com.google.gson.*;


public class playtest {
    public static void main(String[] args) throws FileNotFoundException {
        StringBuilder s = new StringBuilder();
        Object o = new JsonParser().parse(new FileReader("sample.json"));
        JsonObject obj = (JsonObject) o;

        Map<String, List<String>> lines = new HashMap<>();
        JsonObject listObject = obj.get("lines").getAsJsonObject();
        JsonArray listArray = listObject.get("red").getAsJsonArray();
        System.out.println(listArray.size());

        List<String> stations = new ArrayList<>();

        for (int i = 0; i < listArray.size(); i++) {
            JsonElement line = listArray.get(i);
            stations.add(line.toString());
        }

        System.out.println(stations);

        for (int i = 0; i < stations.size(); i++) {
            String station = stations.get(i);
            station = station.replace("\"", "");
            if (station.equals("Davis")){
                System.out.println("working");
            }
            System.out.println(station);
        }
    }
}

