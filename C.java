import java.util.*;
import com.google.gson.*;

public class C {
    public List<String> l;
    public Map<String, String> m;

    public static void main(String[] args) throws Exception{
      MBTA m = new MBTA();
      m.loadConfig("sample.json");
      System.out.println(m.toString());
    }
}
