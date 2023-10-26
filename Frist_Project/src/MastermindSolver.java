import java.util.*;
//When using this class, you should create an instance it of anytime you need to run something repeatedly, otherwise unnecessary slowing downs will happen
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
    public int randGuess(String answer) {
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
    //After narrowing down the list it then chooses the 1 possible answer
    //Currently, this functions run at about 580 milliseconds for all 1296 possibilities
    //The average guess rate of this is 5.021604938271605
    //This algorithm will guess no more than 8 times
    public int firstSymGuess(String answer)
    {
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

            //This goes through either all available answers scores and compares them to the existing guess score to narrow the possibilities
            for (int i = 0; i < possibleAnswers.size(); i++) {
                if (!Arrays.equals(scoreCodewords(possibleAnswers.get(i), Guess, possibleColors.get(i), FirstAnsColor), FirstAns)) {
                    possibleAnswers.remove(i);
                    possibleColors.remove(i);
                    i--;
                }
            }

            Guess = possibleAnswers.get(0);
            //System.out.println(Guess);
        }
        return guessNum;
    }
    //Narrows down the possible answers by using guess-answer symmetry
    //After narrowing down the list it then randomly selects from the remaining options for the next guess
    //Currently, this functions runs at about 630 milliseconds for all 1296 possibilities
    //The average guess rate is about 4.635308
    //This algorithm will guess no more than 8 times, but its average max guess will be 7
    public int randSymGuess(String answer) {
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

            //This goes through either all available answers scores and compares them to the existing guess score to narrow the possibilities
            for (int i = 0; i < possibleAnswers.size(); i++) {
                if (!Arrays.equals(scoreCodewords(possibleAnswers.get(i), Guess, possibleColors.get(i), FirstAnsColor), FirstAns)) {
                    possibleAnswers.remove(i);
                    possibleColors.remove(i);
                    i--;
                }
            }

            Guess = possibleAnswers.get(rand.nextInt(possibleAnswers.size()));
            //System.out.println(Guess);
        }
        return guessNum;
    }
    //This function pairs down the list similarly to the RandSymmetryGuess function, however, it now selects the guess that will narrow down the possible future answers
    //Currently, this functions runs just under 13 seconds for all 1296 possibilities
    //The average guess rate is 4.47608024691358
    //This algorithm will NOT guess more than 5 times
    public int knuthGuess(String answer) {
        int guessNum = 0;

        String Guess = FIRST_GUESS;

        ArrayList<String> possibleAnswers = new ArrayList<>(reference);
        ArrayList<int[]> possibleColors = new ArrayList<>(colorReference);

        while (true) {
            guessNum++;

            //System.out.println(Guess);
            if (Guess.equals(answer))
                break;

            int[] FirstAns = scoreCodewords(Guess, answer);
            int[] FirstAnsColor = colorChecker(Guess);


            for (int i = 0; i < possibleAnswers.size(); i++) {
                if (!Arrays.equals(scoreCodewords(possibleAnswers.get(i), Guess, possibleColors.get(i), FirstAnsColor), FirstAns)) {
                    possibleAnswers.remove(i);
                    possibleColors.remove(i);
                    i--;
                }
            }

            //This begins the prediction of which guesses will limit the future list of possible answers the most
            int maxGuess = 99999;
            String tempGuess = Guess;
            boolean bestIsPossible = false;
            for (int i = 0; i < reference.size(); i++){
                boolean inPossible = false;
                int[] scores = new int[41];
                for  (int o = 0; o < possibleAnswers.size(); o++){
                    int[] temp = scoreCodewords(reference.get(i), possibleAnswers.get(o), colorReference.get(i), possibleColors.get(o));
                    scores[temp[0] * 10 + temp[1]]++;
                }
                int currentMax = Arrays.stream(scores).max().getAsInt();
                inPossible = possibleAnswers.contains(reference.get(i));
                if (currentMax < maxGuess){
                    tempGuess = reference.get(i);
                    maxGuess = currentMax;
                    bestIsPossible = inPossible;
                }
                else if (currentMax == maxGuess && !bestIsPossible && inPossible) {
                    tempGuess = reference.get(i);
                    bestIsPossible = inPossible;
                }
            }
            //if (!bestIsPossible){
            //    System.out.print('*');
            //}
            Guess = tempGuess;
        }
        //System.out.println(' ');
        return guessNum;
    }
    boolean knuthMaxGuess(String answer){
        return knuthGuess(answer) < 6;
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
