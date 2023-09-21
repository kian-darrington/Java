// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.util.*;
public class Main {
    public static void main(String[] args) {
        //System initialization and Title for user, code is for a MasterMind simulator
        Scanner console = new Scanner(System.in);
        final int NUMBER_LENGTH = 4;
        final int NUMBER_AMOUNT = 6;
        System.out.println("Welcome to MasterMind!\nGuesses should have "+NUMBER_LENGTH+" digits with numbers ranging from 0 - " + (NUMBER_AMOUNT - 1));
        Random rand = new Random();

        boolean play = true;
        while (play) {
            //Establishes number of guesses and initialized the random answer and ensures that it has the required digits
            int guessNum = 1;
            StringBuilder randomNumBuilder = new StringBuilder();
            for (int i = 0; i < NUMBER_LENGTH; i++)
                randomNumBuilder.append(rand.nextInt(NUMBER_AMOUNT));
            String randomNum = randomNumBuilder.toString();
            //System.out.println(randomNum);
            //Initialized first guess and error checking
            System.out.println("Please input guess");
            String guess;
            while (true) {
                guess = console.nextLine();
                if (guess.length() == NUMBER_LENGTH)
                    break;
                else
                    System.out.println("Please type in a number with " + NUMBER_LENGTH + " digits");
            }

            int numberCorrect = 0;
            int numberKindOfCorrect = 0;
            while (numberCorrect < NUMBER_LENGTH) {
                boolean[] answerGuess = new boolean[NUMBER_LENGTH], guessCheck = new boolean[NUMBER_LENGTH];
                numberKindOfCorrect = 0;
                numberCorrect = 0;
                for (int i = 0; i < NUMBER_LENGTH; i++) {
                    char currentRealLetter = randomNum.charAt(i);
                    char currentGuessLetter = guess.charAt(i);
                    if (currentGuessLetter == currentRealLetter && !answerGuess[i] && !guessCheck[i]) {
                        answerGuess[i] = true;
                        guessCheck[i] = true;
                        numberCorrect++;
                    }
                }
                if (numberCorrect == NUMBER_LENGTH) {
                    break;
                }

                else {
                    for (int i = 0; i < NUMBER_LENGTH; i++) {
                        char currentGuessLetter;
                        char currentRealLetter = randomNum.charAt(i);
                        for (int o = 0; o < NUMBER_LENGTH; o++) {
                            if (o == i)
                                continue;
                            currentGuessLetter = guess.charAt(o);
                            if (currentGuessLetter == currentRealLetter && !answerGuess[i] && !guessCheck[o]) {
                                guessCheck[o] = true;
                                answerGuess[i] = true;
                                numberKindOfCorrect++;
                            }
                        }
                    }
                }
                System.out.println("You got " + numberCorrect + " white pins and " + numberKindOfCorrect + " black pins");
                System.out.println("That is incorrect, try again!");
                guessNum++;
                while (true) {
                    guess = console.nextLine();
                    if (guess.length() == NUMBER_LENGTH)
                        break;
                    else if (guess.equals("g")) {
                        System.out.println(randomNum);
                        break;
                    } else
                        System.out.println("Please type in a number with " + NUMBER_LENGTH + " digits");
                }
            }
            System.out.println("You are correct! The sequence was " + randomNum);
            System.out.println("It took you " + guessNum + " guesses");
            System.out.println("Play again? (y/n)");
            while (true) {
                guess = console.nextLine();
                if (guess.equals("y")) {
                    break;
                }
                else if (guess.equals("n")) {
                    play = false;
                    break;
                } else
                    System.out.println("Please type in a 'y' or 'n'");
            }
        }
    }
}