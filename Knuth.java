public class Knuth extends Base {

    @Override
    public Integer guess(Result result){
        this.removeRemainingNumber(this.remainingNumbers, this.lastGuess, result);
        this.lastGuess = this.remainingNumbers.get(0);
        int maxMinimum = 0;
        for (Integer number: this.allNumbers){
            int minimum = Integer.MAX_VALUE;
            for (Result r: this.allResults){
                int removedIntegerSize = getRemainingNumber(this.remainingNumbers, number, r).size();
                minimum = Math.min(removedIntegerSize, minimum);
            }
            if (minimum > maxMinimum){
                maxMinimum = minimum;
                this.lastGuess = number;
            }
        }
        return this.lastGuess;
    }
}
