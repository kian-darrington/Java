import java.util.ArrayList;
import java.util.Scanner;

public class MastermindRunner {
    public static final int COLOR_AMOUNT = 6;
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        int guessNum = 0;
        int timesRan = 0;
        MastermindSolver m = new MastermindSolver();
        /*for (int i = 0; i < COLOR_AMOUNT; i++){
            for (int o = 0; o < COLOR_AMOUNT; o++){
                for (int p = 0; p < COLOR_AMOUNT; p++){
                    for (int w = 0; w < COLOR_AMOUNT; w++){
                        String answer = "" + i + o + p + w;
                        guessNum += m.RandSymmetryGuess(answer);
                        timesRan++;
                    }
                }
            }
        }*/
        String guess = console.nextLine();
        guessNum = m.RandSymmetryGuess(guess);
        System.out.println("It took " + guessNum + " tries");
    }
}
