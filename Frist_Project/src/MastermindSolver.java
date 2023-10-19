import java.util.*;
public class MastermindSolver {
    public static final int NUMBER_LENGTH = 4;
    public static final int COLOR_AMOUNT = 6;
    public static final String FIRST_GUESS = "0011";
    public static ArrayList<String> reference = new ArrayList<>();
    public static ArrayList<int[]> colorReference = new ArrayList<>();
    Random rand = new Random();

    MastermindSolver(){     initializeList();   }

    //Counts the number of the colors in a codeword, and situates them in the appropriate order in an int[]
    //For example, the codeword 4431 returns { 0, 1, 0, 1, 2, 0 },
    //with the total value of the array adding up to the number of pins
    public static int[] colorChecker(String str)
    {
        int[] colorCount = new int[COLOR_AMOUNT];
        for (int i = 0; i < str.length(); i++){
            colorCount[Character.getNumericValue(str.charAt(i))]++;
        }
        return colorCount;
    }
    //Scores codewords by method of total hits (minimum of the shared colors), and subtracting total hits from black pins
    //Returns an int[] of two values {black pegs, white pegs}
    public static int[] scoreCodewords(String codeword1, String codeword2, int[] color1, int[] color2){
        int matches = 0;
        for (int i = 0; i < COLOR_AMOUNT; i++){
            matches += Math.min(color1[i], color2[i]);
        }
        int blackPegs = 0;
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            char currentRealLetter = codeword1.charAt(i);
            char currentGuessLetter = codeword2.charAt(i);
            if (currentGuessLetter == currentRealLetter) {
                blackPegs++;
            }
        }
        return new int[]{blackPegs, matches - blackPegs};
    }
    public static int[] scoreCodewords(String codeword1, String codeword2) {
        int[] color1 = colorChecker(codeword1);
        int[] color2 = colorChecker(codeword2);
        return scoreCodewords(codeword1, codeword2, color1, color2);
    }
    //Method of playing Mastermind that randomly selects from all possible solution each time (it's pretty bad)
    public int Randguess(String answer) {
        int guessNum = 0;
        ArrayList<String> possibleAnswers = new ArrayList<>(reference);
        while (true) {
            guessNum++;
            int randNum = rand.nextInt(possibleAnswers.size());
            if (possibleAnswers.get(randNum).equals(answer))
                break;
            else
                possibleAnswers.remove(randNum);
        }
        return guessNum;
    }
    //Narrows down the possible answers by using guess-answer symmetry
    //After narrowing down the list it then randomly selects from the remaining options for the next guess
    public int RandSymmetryGuess(String answer) {
        int guessNum = 0;

        String Guess = FIRST_GUESS;

        //copies the list of all possible codewords and the corresponding color codes
        ArrayList<String> possibleAnswers = new ArrayList<>(reference);
        ArrayList<int[]> possibleColors = new ArrayList<>(colorReference);

        while (true) {
            guessNum++;

            if (Guess.equals(answer))
                break;
            //Checks the guess and the guess color score
            int[] FirstAns = scoreCodewords(Guess, answer);
            int[] FirstAnsColor = colorChecker(Guess);

            //This goes through either all available answers and compares them to the existing code score to narrow the possibilities
            for (int i = 0; i < possibleAnswers.size(); i++) {
                if (!Arrays.equals(scoreCodewords(possibleAnswers.get(i), Guess, possibleColors.get(i), FirstAnsColor), FirstAns)) {
                    possibleAnswers.remove(i);
                    possibleColors.remove(i);
                }
            }

            Guess = possibleAnswers.get(rand.nextInt(possibleAnswers.size()));
            System.out.println(Guess);
        }
        return guessNum;
    }
    //
    public int KnuthGuess(String answer) {
        int guessNum = 0;

        String Guess = FIRST_GUESS;

        ArrayList<String> possibleAnswers = new ArrayList<>(reference);
        ArrayList<int[]> possibleColors = new ArrayList<>(colorReference);

        while (true) {
            guessNum++;

            if (Guess.equals(answer))
                break;

            int[] FirstAns = scoreCodewords(Guess, answer);
            int[] FirstAnsColor = colorChecker(Guess);

            for (int i = 0; i < possibleAnswers.size(); i++) {
                if (!Arrays.equals(scoreCodewords(possibleAnswers.get(i), Guess, possibleColors.get(i), FirstAnsColor), FirstAns)) {
                    possibleAnswers.remove(i);
                    possibleColors.remove(i);
                }
            }

            //This begins the prediction of which guesses will limit the future list of possible answers the most
            ArrayList<ArrayList<String>> betterAnswerPools = new ArrayList<>();
            ArrayList<ArrayList<int[]>> betterColorPools = new ArrayList<>();
            int bestIndex = possibleAnswers.size();
            for (int i = 0; i < possibleAnswers.size(); i++){
                betterAnswerPools.add(new ArrayList<>(possibleAnswers));
                betterColorPools.add(new ArrayList<>(possibleColors));
                for (int o = 0; o < possibleAnswers.size(); o++){
                    if (!Arrays.equals(scoreCodewords(betterAnswerPools.get(i).get(o), possibleAnswers.get(i), betterColorPools.get(i).get(o), possibleColors.get(i)), scoreCodewords(Guess, possibleAnswers.get(i), FirstAnsColor, possibleColors.get(i)))) {
                        betterAnswerPools.get(i).remove(o);
                        betterColorPools.get(i).remove(o);
                    }
                    if (betterAnswerPools.get(i).size() < bestIndex)
                        bestIndex = i;
                }
            }
            Guess = possibleAnswers.get(bestIndex);
            System.out.println(Guess);
        }
        return guessNum;
    }
    //Initializes every possible instance of a Mastermind codeword and its corresponding color score
    public static void initializeList(){
        for (int i = 0; i < COLOR_AMOUNT; i++){
            for (int o = 0; o < COLOR_AMOUNT; o++){
                for (int p = 0; p < COLOR_AMOUNT; p++){
                    for (int w = 0; w < COLOR_AMOUNT; w++){
                        reference.add("" + i + o + p + w);
                        int[] temp = new int[COLOR_AMOUNT];
                        temp[i]++;
                        temp[o]++;
                        temp[p]++;
                        temp[w]++;
                        colorReference.add(temp);
                    }
                }
            }
        }
    }
}
