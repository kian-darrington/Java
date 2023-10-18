import java.util.*;
public class MastermindSolver {
    public static final int NUMBER_LENGTH = 4;
    public static final int COLOR_AMOUNT = 6;
    public static final String FIRST_GUESS = "0011";
    public static ArrayList<String> reference = new ArrayList<>();
    public static ArrayList<int[]> colorReference = new ArrayList<>();
    Random rand = new Random();

    MastermindSolver(){     initializeList();   }


    public static int[] colorChecker(String str)
    {
        int[] colorCount = new int[COLOR_AMOUNT];
        for (int i = 0; i < str.length(); i++){
            colorCount[Character.getNumericValue(str.charAt(i))]++;
        }
        return colorCount;
    }
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
    public int RandSymmetryGuess(String answer) {
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
            ArrayList<ArrayList<String>> betterAnswerPools = new ArrayList<>();
            ArrayList<ArrayList<int[]>> betterColorPools = new ArrayList<>();
            int bestIndex = 0;
            int currentSize = possibleAnswers.size();
            for (int i = 0; i < reference.size(); i++){
                betterAnswerPools.add(possibleAnswers);
                betterColorPools.add(possibleColors);
                for (int o = 0; o < possibleAnswers.size(); o++){
                    if (!scoreCodewords(betterAnswerPools.get(i).get(o), reference.get(i), betterColorPools.get(i).get(o), colorReference.get(i)).equals(FirstAns)) {
                        betterAnswerPools.get(i).remove(o);
                        betterColorPools.get(i).remove(o);
                    }
                    if (betterAnswerPools.get(i).size() < currentSize)
                        bestIndex = i;
                }
            }
            Guess = reference.get(bestIndex);
        }
        return guessNum;
    }
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
