import java.util.*;
public class MastermindSolver {
    public static final int NUMBER_LENGTH = 4;
    public static final int COLOR_AMOUNT = 6;
    public static int[] scoreCodewords(String codeword1, String codeword2) {
        int whitePegs = 0;
        int blackPegs = 0;
        boolean[] answerGuess = new boolean[NUMBER_LENGTH], guessCheck = new boolean[NUMBER_LENGTH];
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            char currentRealLetter = codeword1.charAt(i);
            char currentGuessLetter = codeword2.charAt(i);
            if (currentGuessLetter == currentRealLetter && !answerGuess[i] && !guessCheck[i]) {
                answerGuess[i] = true;
                guessCheck[i] = true;
                blackPegs++;
            }
        }
        if (blackPegs == NUMBER_LENGTH) {
            return new int[]{4, 0};
        } else {
            for (int i = 0; i < NUMBER_LENGTH; i++) {
                char currentGuessLetter;
                char currentRealLetter = codeword1.charAt(i);
                for (int o = 0; o < NUMBER_LENGTH; o++) {
                    if (o == i)
                        continue;
                    currentGuessLetter = codeword2.charAt(o);
                    if (currentGuessLetter == currentRealLetter && !answerGuess[i] && !guessCheck[o]) {
                        guessCheck[o] = true;
                        answerGuess[i] = true;
                        whitePegs++;
                    }
                }
            }
        }
        return new int[]{blackPegs, whitePegs};
    }
    public static int guess(String answer) {
        while (true) {

        }
    }
    public static int[] compute (int[] guesses, int [] responses, String answer) {
        String guess = "";
        return scoreCodewords(answer, guess);
    }
}
