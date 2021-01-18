import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache {

    static Map<Result, List<Integer>> cache = new HashMap<>();
    static Map<Integer, Map<Result, List<Integer>>> cache2 = new HashMap<>();

    public static void init(int firstGuess){

        for (Result r: Guess.allResults) {
            List<Integer> temp = Generator.createAllNumber();
            Guess.removeRemainingNumber(temp, firstGuess, r);
            cache.put(r, temp);
        }
    }
}
