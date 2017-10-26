import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Zuzanna on 01.11.2016.
 */
public class Helper {

    private static final Map<String, Integer> stats = new TreeMap<String, Integer>();

    public static void increment(String name) {
        if (stats.containsKey(name.toUpperCase())) {
            stats.put(name.toUpperCase(), stats.get(name.toUpperCase()) + 1);
        } else {
            stats.put(name.toUpperCase(), 1);
        }
    }

    private static void printStats() {
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            if (entry.getValue() >= 3) {
                System.out.println(entry.getKey().toUpperCase() + "    " + entry.getValue());
            }
        }
    }

    public static void addShutdownCall() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                printStats();
            }
        }));
    }

}
