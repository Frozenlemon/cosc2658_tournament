import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Generator {

    public static List<Integer> createAllNumber(){
        final List<Integer> result = new LinkedList<>();
        for (int i = 1000; i < 10000; i++){
            result.add(i);
        }
        return result;
    }

    public static List<Result> createAllResult(int length){
        List<Result> result = new ArrayList<>();
        for (int hits = 0; hits <= length; hits++){
            for (int strikes = 0; strikes < length; strikes++){
                int sum = hits + strikes;
                if (sum <= length && !(strikes == length - 1 && hits == length)) {
                    result.add(new Result(hits, strikes));
                }
            }
        }
        return result;
    }
}
