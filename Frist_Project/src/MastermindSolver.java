import java.util.*;
public class MastermindSolver {
    public static final int NUMBER_LENGTH = 4;
    public static final int COLOR_AMOUNT = 6;
    public static ArrayList<String> reference = new ArrayList<>();
    public static int[][] colorReference = new int[(int)Math.pow(COLOR_AMOUNT, NUMBER_LENGTH)][COLOR_AMOUNT];
    Random rand = new Random();

    MastermindSolver(){     initializeList();   }


    public static int[] colorChecker(String str)
    {
        int[] colorCount = new int[COLOR_AMOUNT];
        for (int i = 0; i < COLOR_AMOUNT; i++){
            colorCount[Character.getNumericValue(str.charAt(i))]++;
        }
        return colorCount;
    }
    public static int[] scoreCodewords(String codeword1, String codeword2) {
        int[] color1 = colorChecker(codeword1);
        int[] color2 = colorChecker(codeword2);
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
    public int guess(String answer) {
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
    public static void initializeList(){
        int e = 0;
        for (int i = 0; i < COLOR_AMOUNT; i++){
            for (int o = 0; o < COLOR_AMOUNT; o++){
                for (int p = 0; p < COLOR_AMOUNT; p++){
                    for (int w = 0; w < COLOR_AMOUNT; w++){
                        reference.add("" + i + o + p + w);
                        colorReference[e][i]++;
                        colorReference[e][o]++;
                        colorReference[e][p]++;
                        colorReference[e][w]++;
                        e++;
                    }
                }
            }
        }
    }
}
