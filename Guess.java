import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
public class Guess {

	private static boolean first = true;
	protected final static int NUMBER_LENGTH = 4;
	protected final static List<Integer> allNumbers = createAllNumber();;
	protected final static List<Result> allResults = createAllResult(NUMBER_LENGTH);;
	protected static List<Integer> remainingNumbers = new ArrayList<>();
	protected static Integer lastGuess;
	protected static boolean cache = false;
	
	public static int make_guess(int hits, int strike) {
		int myGuess;
		if (first) {
			myGuess = lastGuess;
			first = false;
			cache = true;
		}
		else
			myGuess = guess(new Result(hits, strike));
		return myGuess;
	}


//	Reset the all initial value
	public static void reset(int firstGuess) {
		remainingNumbers.clear();
		remainingNumbers.addAll(allNumbers);
		first = true;
		lastGuess = firstGuess;
	}

//	Knuth mix max guess
	public static Integer guess(Result result){
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

//	get remaining number base on the previous guess and result of previous guess either from previous cache or from new array
	protected static List<Integer> getRemainingNumber(List<Integer> numbers, Integer lastGuess, Result result) {
		if (cache && Cache.cache.containsKey(result)) {
			numbers = Cache.cache.get(result);
			cache = false;
		}
		List<Integer> remainResult = new ArrayList<>();
		for (Integer number : numbers) {
			Result temp = checkGuess(lastGuess, number);
			if (temp.getHits() == result.getHits() && temp.getStrikes() == result.getStrikes())
				remainResult.add(number);
		}
		return remainResult;
	}

//	remove from an array all unfit guesses base on previous guess and result
	protected static void removeRemainingNumber(List<Integer> numbers, Integer lastGuess, Result result){
		if (cache && Cache.cache.containsKey(result)) {
			numbers = Cache.cache.get(result);
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

	private static Result checkGuess(int target, int guess) {
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

//	create all possible guesses from 1000 to 9999
	public static List<Integer> createAllNumber(){
		final List<Integer> result = new LinkedList<>();
		for (int i = 1000; i < 10000; i++){
			result.add(i);
		}
		System.out.println(result.size());
		return result;
	}

//	create all possible result for any guess
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