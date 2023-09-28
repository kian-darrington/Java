import java.util.ArrayList;

public class MastermindRunner {
    public static final int COLOR_AMOUNT = 6;
    public static void main(String[] args) {
        int guessNum = 0;
        for (int i = 0; i < COLOR_AMOUNT; i++){
            for (int o = 0; o < COLOR_AMOUNT; o++){
                for (int p = 0; p < COLOR_AMOUNT; p++){
                    for (int w = 0; w < COLOR_AMOUNT; w++){
                        String answer = "" + i + o + p + w;
                        guessNum += MastermindSolver.guess(answer);
                    }
                }
            }
        }
    }
}
