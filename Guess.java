import java.util.*;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
public class Guess {

	private static int first = 0;
	protected final static int NUMBER_LENGTH = 4;
	public final static List<Integer> allNumbers = createAllNumber();;
	public final static List<Result> allResults = createAllResult(NUMBER_LENGTH);;
	protected static List<Integer> remainingNumbers = new ArrayList<>();
	protected static Integer lastGuess;
	
	public static int make_guess(int hits, int strike) {
		int myGuess;
		if (first == 0) {
			myGuess = lastGuess;
		}
		else
			myGuess = guess(new Result(hits, strike));
		first++;
		return myGuess;
	}


//	Reset the all initial value
	public static void reset(int firstGuess) {
		remainingNumbers.clear();
		remainingNumbers.addAll(allNumbers);
		first = 0;
		lastGuess = firstGuess;
	}

//	Knuth mix max guess
	public static Integer guess(Result result){
		remainingNumbers = removeRemainingNumber(remainingNumbers, lastGuess, result);
//		List<Integer> entropyGuess = entropy(remainingNumbers, result);
//		first++;
		List<Integer> bestGuess = miniMax(remainingNumbers, result);

		List<Integer> maxPartsGuess = maxParts(bestGuess, result);

//

		lastGuess = maxPartsGuess.get(0);

		return lastGuess;
	}

//	get remaining number base on the previous guess and result of previous guess either from previous cache or from new array
	protected static List<Integer> getRemainingNumber(List<Integer> numbers, Integer lastGuess, Result result) {
		List<Integer> remainResult = new ArrayList<>();
		if (first == 1 && Cache.cache2.containsKey(lastGuess) && Cache.cache2.get(lastGuess).containsKey(result))
			return Cache.cache2.get(lastGuess).get(result);
		for (Integer number : numbers) {
			Result temp = checkGuess(lastGuess, number);
			if (temp.getHits() == result.getHits() && temp.getStrikes() == result.getStrikes())
				remainResult.add(number);
		}
		Map<Result, List<Integer>> temp1 = new HashMap<>();
		if (first == 1 && !Cache.cache2.containsKey(lastGuess)) {
			Cache.cache2.put(lastGuess, temp1);
		}
		temp1 = Cache.cache2.get(lastGuess);
		if (first == 1 && !Cache.cache2.get(lastGuess).containsKey(result))
			temp1.put(result, remainResult);
		return remainResult;
	}

//	remove from an array all unfit guesses base on previous guess and result
	public static List<Integer> removeRemainingNumber(List<Integer> numbers, Integer lastGuess, Result result){
		if (first == 1 && Cache.cache.containsKey(result)){
//			List<Integer> r = new ArrayList<>(Cache.cache.get(result));
			return Cache.cache.get(result);
		}
		for (int i = numbers.size() - 1; i >= 0; i--) {
			Result temp = checkGuess(lastGuess, numbers.get(i));
			if (temp.getStrikes() != result.getStrikes() || temp.getHits() != result.getHits())
				numbers.remove(i);
		}
		return numbers;
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
				if (sum <= length && !(strikes == length && hits == length)) {
					result.add(new Result(hits, strikes));
					System.out.println(hits + " " + strikes);
				}
			}
		}
		return result;
	}

	protected static class EncodedPart {

		private int size;
		private Integer firstCode;

		public EncodedPart(int size, Integer firstCode) {
			this.size = size;
			this.firstCode = firstCode;
		}

		public Boolean equals(EncodedPart other) {
			return this.size == other.size && this.firstCode.equals(other.firstCode);
		}

	}

	protected static List<Integer> maxParts(List<Integer> startingArray, Result result){
		List<Integer> maxPartsGuess = new ArrayList<>();
		maxPartsGuess.add(startingArray.get(0));
		int mostParts = 1;
		for(Integer number: allNumbers) {
			Map<EncodedPart, Boolean> parts = new HashMap<>();
			for (Result r: allResults) {
				List<Integer> partition = getRemainingNumber(startingArray, number, r);
				if (!partition.isEmpty()){
					int size = partition.size();
					Integer firstCode = partition.get(0);
					parts.put(new EncodedPart(size, firstCode), true);
				}
			}

			int numberOfParts = parts.size();
			if (numberOfParts == mostParts && numberOfParts > 1) {
				maxPartsGuess.add(number);
			}
			if (numberOfParts > mostParts) {
				mostParts = numberOfParts;
				maxPartsGuess.clear();
				maxPartsGuess.add(number);
			}

			List<Integer> consistentBestMaxPartsGuesses = getRemainingNumber(maxPartsGuess, lastGuess, result);
			if (!consistentBestMaxPartsGuesses.isEmpty())
				maxPartsGuess = consistentBestMaxPartsGuesses;
		}
		return maxPartsGuess;
	}

	protected static List<Integer> miniMax(List<Integer> startingArray,Result result) {
		List<Integer> bestGuess = new ArrayList<>();
		bestGuess.add(startingArray.get(0));
		int minMaximum = Integer.MAX_VALUE;
		for (Integer number: allNumbers){
			int maximum = 0;
			for (Result r: allResults){
				int removedIntegerSize = getRemainingNumber(startingArray, number, r).size();
				maximum = Math.max(removedIntegerSize, maximum);
			}
			if (maximum == minMaximum)
				bestGuess.add(number);

			if (maximum < minMaximum){
				minMaximum = maximum;
				bestGuess.clear();
				bestGuess.add(number);
			}
		}

		List<Integer> consistentBestGuesses = getRemainingNumber(
				bestGuess, lastGuess, result);
		if(!consistentBestGuesses.isEmpty()) {
			bestGuess = consistentBestGuesses;
		}
		return bestGuess;
	}

	protected static List<Integer> entropy(List<Integer> startingArray, Result result) {
		double maxEntropy = 0;
		int totalSize = startingArray.size();
		List<Integer> entropyGuess = new ArrayList<>();
		entropyGuess.add(startingArray.get(0));
		for(Integer number: allNumbers){
			double entropy = 0;
			for (Result r: allResults){
				int partSize = getRemainingNumber(startingArray, number, r).size();
				if (partSize != 0){
					double I = Math.log(1.0 * totalSize/partSize) / Math.log(2);
					double P = 1.0 * partSize/totalSize;
					entropy += entropy + I * P;
				}
			}
			if (entropy == maxEntropy && entropy > 0)
				entropyGuess.add(number);

			if (entropy > maxEntropy){
				maxEntropy = entropy;
				entropyGuess.clear();
				entropyGuess.add(number);
			}
		}

		List<Integer> consistentBestEntropyGuesses = getRemainingNumber(entropyGuess, lastGuess, result);
		if (!consistentBestEntropyGuesses.isEmpty())
			entropyGuess = consistentBestEntropyGuesses;

		return entropyGuess;
	}
}