import java.util.ArrayList;
import java.util.List;

public class Base implements IMastermindStrat {

    protected final int NUMBER_LENGTH = 4;
    protected final List<Integer> allNumbers;
    protected final List<Result> allResults;
    protected List<Integer> remainingNumbers;
    protected Integer lastGuess;
    protected boolean cache = false;

    public Base(){
        this.allNumbers = Generator.createAllNumber();
        this.remainingNumbers = new ArrayList<>();
        this.allResults = Generator.createAllResult(NUMBER_LENGTH);
    }

    @Override
    public Integer reset() {
        this.remainingNumbers.clear();
        this.remainingNumbers.addAll(this.allNumbers);
        this.lastGuess = 1111;
        return this.lastGuess;
    }

    @Override
    public Integer guess(Result result) {
        return null;
    }

    protected List<Integer> getRemainingNumber(List<Integer> numbers, Integer lastGuess, Result result) {
        List<Integer> remainResult = new ArrayList<>();
        for (Integer number : numbers) {
            Result temp = checkGuess(lastGuess, number);
            if (temp.getHits() == result.getHits() && temp.getStrikes() == result.getStrikes())
                remainResult.add(number);
        }
        return remainResult;
    }

    protected void removeRemainingNumber(List<Integer> numbers, Integer lastGuess, Result result){
        if (cache && Cache.cache.containsKey(result)) {
            numbers = Cache.cache.get(result);
            cache = false;
        }
        else {
            for (int i = numbers.size() - 1; i >= 0; i--) {
                Result temp = checkGuess(lastGuess, numbers.get(i));
                if (temp.getStrikes() != result.getStrikes() || temp.getHits() != result.getHits())
                    numbers.remove(i);
            }
            Cache.cache.put(result, numbers);
        }
    }

    private Result checkGuess(int target, int guess) {
        char des[] = Integer.toString(target).toCharArray();
        char src[] = Integer.toString(guess).toCharArray();
        int hits = 0;
        int strikes = 0;

        // process strikes
        for (int i = 0; i < 4; i++) {
            if (src[i] == des[i]) {
                strikes++;
                des[i] = 'a';
                src[i] = 'a';
            }
        }
        // process hits
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (src[i] != 'a') {
                    if (src[i] == des[j]) {
                        hits++;
                        des[j] = 'a';
                        break;
                    }
                }
            }
        }
        return new Result(hits, strikes);
    }
}
