/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
public class Guess {

	private static IMastermindStrat strat = new Knuth();
	
	public static int make_guess(Result result) {
		// just a dummy guess
		int myGuess;
		if (result == null)
			myGuess = Guess.strat.reset();
		else
			myGuess = Guess.strat.guess(result);
		return myGuess;
	}
}