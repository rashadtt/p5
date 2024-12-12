import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private static Config instance;
    public Map<String, List<String>> lines = new HashMap<>();
    public Map<String, List<String>> trips = new HashMap<>();

    public static Config getInstance(){
        if (instance == null){
            instance = new Config();
        }
        return instance;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(lines);
        sb.append(trips);
        return sb.toString();
    }
}
