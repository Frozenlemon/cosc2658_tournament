public class GuessRunner {

	static Result processGuess(int target, int guess) {
		char des[] = Integer.toString(target).toCharArray();
		char src[] = Integer.toString(guess).toCharArray();
		int hits=0;
		int strikes=0;

		// process strikes
		for (int i=0; i<4; i++) {
			if (src[i] == des[i]) {
				strikes++;
				des[i] = 'a';
				src[i] = 'a';
			}
		}
		// process hits
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				if (src[i]!='a') {
					if (src[i]==des[j]) {
						hits++;
						des[j] = 'a';
						break;
					}
				}
			}
		}
		if (strikes==4)	{ // game over
			System.out.printf("4 strikes - Game over\n");
			return new Result(hits, strikes);
		}
		if (hits==0 && strikes==0)
			System.out.printf("Miss\n");
		else if(hits>0 && strikes==0)
			System.out.printf("%d hits\n", hits);
		else if(hits==0 && strikes>0)
			System.out.printf("%d strikes\n", strikes);
		else if(hits>0 && strikes>0)
			System.out.printf("%d strikes and %d hits\n", strikes, hits);

		return new Result(hits, strikes);
	}

	private static int getTargetNumber() {
		int target = (int) (Math.random() * (9999-1000) + 1000);
		char targetDigits[] = Integer.toString(target).toCharArray();
		// Dodging AABB target values since other teams might use
		// them as initial guess
		if (targetDigits[0] == targetDigits[1] && targetDigits[2] == targetDigits[3]) {
			return getTargetNumber();
		}
		return target;
	}

	public static void main(String[] args) {
		int guess_cnt = 0;
		/* A dummy value, you need to code here
		 * to get a target number for your oponent
		 * should be a random number between [1000-9999]
		 */
		int target = getTargetNumber();
		Result res = new Result();
		System.out.println("Guess\tResponse");
		for (int i = 0; i < 100; i++) {
			System.out.print(getTargetNumber() + ",");
		}
		while(res.getStrikes() < 4) {
			/* take a guess from user provided class
			 * the user provided class must be a Guess.class file
			 * that has implemented a static function called make_guess()
			 */
			int guess = Guess.make_guess(res.getHits(), res.getStrikes());
			System.out.printf("%d\t", guess);

			if (guess == -1) {	// user quits
				System.out.printf("you quit: %d\n", target);
				return;
			}
			guess_cnt++;

			/* You need to code this method to process a guess
			 * provided by your oponent
			 */
			res = processGuess(target, guess);
		}
		System.out.printf("Target: %d - Number of guesses: %d\n", target, guess_cnt);
	}
}
