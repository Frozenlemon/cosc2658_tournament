public class Knuth extends Base {

    @Override
    public Integer guess(Result result){
        removeRemainingNumber(remainingNumbers, lastGuess, result);
        lastGuess = remainingNumbers.get(0);
        int minMaximum = Integer.MAX_VALUE;
        for (Integer number: allNumbers){
            int maximum = 0;
            for (Result r: allResults){
                int removedIntegerSize = getRemainingNumber(remainingNumbers, number, r).size();
                maximum = Math.max(removedIntegerSize, maximum);
            }
            if (maximum < minMaximum){
                minMaximum = maximum;
                lastGuess = number;
            }
        }
        return lastGuess;
    }
}
