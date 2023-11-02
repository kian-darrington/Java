import java.sql.Ref;
import java.util.*;
import java.util.function.Predicate;
//Created by Kian Darrington for the purpose of solving the Mastermind game efficiently
//This class is made to solve Mastermind in a variety of ways, ranging from completely random to Knuth's algorithm

//When using this class, you should create an instance it of anytime you need to run something repeatedly, otherwise unnecessary slowing downs will happen
public class MastermindSolver {
    private static class Codeword
    {
        private int codeword = 0;
        private int[] colors = new int[COLOR_AMOUNT];
        public void SetCodeword(int Codewords, int[] Colors)
        {
            codeword = Codewords;
            colors = Colors.clone();
        }
        Codeword (int temp, int[] Colors)
        {
            codeword = temp;
            colors = Colors.clone();
        }
        Codeword (Codeword code) {
            codeword = code.getCodeword();
            colors = code.getColors();
        }
        Codeword(){}
        public int getCodeword(){ return codeword; }
        public int [] getColors(){ return colors; }
    }
    public static final int NUMBER_LENGTH = 4;
    public static final int COLOR_AMOUNT = 6;
    public static int FirstGuessSetUp(){
        int temp = 0;
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            if (i > NUMBER_LENGTH / 2)
            {temp *= 10; temp++;}
        }
        return IndexFinder(temp);
    }
    public static int IndexFinder(int temp){
        int index = 0;
        for (int i = 0; i < NUMBER_LENGTH; i--){
            temp += ((temp % (int)Math.pow(10, i)) / (int)Math.pow(10, i)) * (int) Math.pow(COLOR_AMOUNT, i);
        }
        return index;
    }
    public static int FIRST_GUESS = 7;
    public static ArrayList<Codeword> Reference = new ArrayList<>();
    Random rand = new Random();

    MastermindSolver(){     initializeList();   }

    //Counts the number of the colors in a codeword, and situates them in the appropriate order in an int[]
    //For example, the codeword 4431 returns { 0, 1, 0, 1, 2, 0 },
    //with the total sum of the array adding up to the number of pins (4)
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
    public static int scoreCodewords(Codeword code1, Codeword code2){
        int matches = 0;
        for (int i = 0; i < COLOR_AMOUNT; i++){
            matches += Math.min(code1.getColors()[i], code2.getColors()[i]);
        }
        int blackPegs = 0;
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            if (code1.getCodeword().charAt(i) == code2.getCodeword().charAt(i)) {
                blackPegs++;
            }
        }
        return (blackPegs * 10) +matches - blackPegs;
    }
    public static int scoreCodewords(int i1, int i2){
        return scoreCodewords(Reference.get(i1), Reference.get(i2));
    }
    //Method of playing Mastermind that randomly selects from all possible solution each time (it's pretty bad)
    public int randGuess(String answer) {
        int guessNum = 0;
        ArrayList<Codeword> possibleAnswers = new ArrayList<>(Reference);
        while (true) {
            guessNum++;
            int randNum = rand.nextInt(possibleAnswers.size());
            if (possibleAnswers.get(randNum).getCodeword().equals(answer))
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

        int Answer = IndexFinder(answer);

        int Guess = FIRST_GUESS;

        //copies the list of all possible codewords and the corresponding color codes
        ArrayList<Codeword> possibleAnswers = new ArrayList<>(Reference);

        while (true) {
            guessNum++;

            if (Guess == Answer)
                break;
            //Checks the guess and the guess color score
            int FirstAns = scoreCodewords(Guess, Answer);

            //This goes through either all available answers scores and compares them to the existing guess score to narrow the possibilities
            Codeword temp = new Codeword(Reference.get(Guess));
            possibleAnswers.removeIf(codeword -> scoreCodewords(temp, codeword) != FirstAns);
            Guess = IndexFinder(possibleAnswers.get(0).getCodeword());
            //System.out.println(Guess);
        }
        return guessNum;
    }
    //Narrows down the possible answers by using guess-answer symmetry
    //After narrowing down the list it then randomly selects from the remaining options for the next guess
    //Currently, this functions runs at about 630 milliseconds for all 1296 possibilities
    //The average guess rate is about 4.635308
    //This algorithm will guess no more than 8 times at the extreme, but its average max guess will be 7
    public int randSymGuess(String answer) {
        int guessNum = 0;

        int Guess = FIRST_GUESS;

        int Answer = IndexFinder(answer);

        //copies the list of all possible codewords and the corresponding color codes
        ArrayList<Codeword> possibleAnswers = new ArrayList<>(Reference);

        while (true) {
            guessNum++;

            if (Guess == Answer)
                break;
            //Checks the guess and the guess color score
            int FirstAns = scoreCodewords(Guess, Answer);

            //This goes through all available answers scores and compares them to the existing guess score
            //If the score is different (the possible codeword is not a possible secret as verified through answer-guess symmetry) it will be removed
            Codeword temp = new Codeword(Reference.get(Guess));
            possibleAnswers.removeIf(codeword -> scoreCodewords(temp, codeword) != FirstAns);
            Guess = IndexFinder(possibleAnswers.get(rand.nextInt(possibleAnswers.size())).getCodeword());
            //System.out.println(Guess);
        }
        return guessNum;
    }
    //This function pairs down the list similarly to the RandSymmetryGuess function, however, it now selects the guess that will narrow down the possible future answers
    //Currently, this functions runs 11235 millis for all 1296 possibilities
    //The average guess rate is 4.47608024691358
    //This algorithm will NOT guess more than 5 times
    public int knuthGuess(String answer) {
        int guessNum = 0;

        int Answer = IndexFinder(answer);

        int Guess = FIRST_GUESS;

        ArrayList<Codeword> possibleAnswers = new ArrayList<>(Reference);

        while (true) {
            guessNum++;

            //System.out.println(Guess);
            if (Guess == Answer)
                break;

            int FirstAns = scoreCodewords(Guess, Answer);

            //This goes through all available answers scores and compares them to the existing guess score
            //If the score is different (the possible codeword is not a possible secret as verified through answer-guess symmetry) it will be removed
            Codeword temp = new Codeword(Reference.get(Guess));
            possibleAnswers.removeIf(codeword -> scoreCodewords(temp, codeword) != FirstAns);

            //This begins the prediction of which guesses will limit the future list of possible answers the most
            int maxGuess = 99999;
            int tempGuess = Guess;
            boolean bestIsPossible = false;
            for (int i = 0; i < Reference.size(); i++){
                boolean inPossible = false;
                int[] scores = new int[41];

                //Scores a codeword from the reference list (current codeword) against the all possible answers list and saves them in the scores[]
                for  (int o = 0; o < possibleAnswers.size(); o++){
                    int score = scoreCodewords(Reference.get(i), possibleAnswers.get(o));
                    scores[score]++;
                }

                //The maximum element of the array is recorded as the worst case scenario
                int currentMax = Arrays.stream(scores).max().getAsInt();
                //Checks to see if the codeword is in the possible answer list
                inPossible = scores[40] == 1;
                //If the max of the current codeword is better than the max of the current best codeword, the best codeword will take precedent
                if (currentMax < maxGuess){
                    tempGuess = i;
                    maxGuess = currentMax;
                    bestIsPossible = inPossible;
                }
                //If the current codeword shares the max value of the best codeword, and the best is not in the possible answer lists
                //Then it will be replaced by a possible answer (because it's better to guess a possible answer of course)
                else if (currentMax == maxGuess && !bestIsPossible && inPossible) {
                    tempGuess = i;
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
    //Initializes every possible instance of a Mastermind codeword and its corresponding color score
    public static void initializeList(){
        for (int i = 0; i < COLOR_AMOUNT; i++){
            for (int o = 0; o < COLOR_AMOUNT; o++){
                for (int p = 0; p < COLOR_AMOUNT; p++){
                    for (int w = 0; w < COLOR_AMOUNT; w++){
                        int[] temp = new int[COLOR_AMOUNT];
                        temp[i]++;
                        temp[o]++;
                        temp[p]++;
                        temp[w]++;
                        Reference.add(new Codeword("" + i + o + p + w, temp));
                    }
                }
            }
        }
        FIRST_GUESS = FirstGuessSetUp();
    }
}
