// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.util.*;
public class Main {
    public static int runFunction(int x) {
        return x * x + - 5;
    }
    public static void main(String[] args) {
        //System initialization and Title for user, code is for a MasterMind simulator
        Scanner console = new Scanner(System.in);
        final int NUMBER_LENGTH = 4;
        final int NUMBER_AMOUNT = 6;
        System.out.println("Welcome to MasterMind!\nGuesses should have "+NUMBER_LENGTH+" digits with numbers ranging from 0 - " + (NUMBER_AMOUNT - 1));
        Random rand = new Random();

        //Establishes number of guesses and initialized the random answer and ensures that it has the required digits
        int guessnum = 1;
        String randomNum = "";
        for (int i = 0; i < NUMBER_LENGTH; i++)
            randomNum += rand.nextInt(NUMBER_AMOUNT);

        //Initialized first guess and error checking
        String guess = "";
        while (true) {
            guess = console.nextLine();
            if (guess.length() == NUMBER_LENGTH)
                break;
            else
                System.out.println("Please type in a number with "+ NUMBER_LENGTH +" digits");
        }


        int numberCorrect;
        while (!guess.equals(randomNum)){
            numberCorrect = 0;
            for (int i = 0; i < NUMBER_LENGTH; i++) {
                char currentRealLetter = randomNum.charAt(i);
                char currentGuessLetter =  guess.charAt(i);
                if (currentGuessLetter == currentRealLetter)
                {
                    numberCorrect++;
                }
                else
                {
                    for (int o = 0; o < NUMBER_LENGTH; o++){
                        if (o == i)
                            continue;
                        currentGuessLetter = guess.charAt(o);

                    }
                }
            }
            System.out.println("You got " + numberCorrect + " pins right");
            System.out.println("That is incorrect, try again!");
            guessnum++;
            while (true) {
                guess = console.nextLine();
                if (guess.length() == NUMBER_LENGTH)
                    break;
                else
                    System.out.println("Please type in a number with "+NUMBER_LENGTH+" digits");
            }
        }
        System.out.println("You are correct!");
        System.out.print("It took you " + guessnum + " guesses");
        /*
        Scanner console = new Scanner(System.in);
        int xMin = -10;
        int xMax = 10;
        int yMin = -10;
        int yMax = 10;
        char t = 9607;
        char yaxis = 9553;
        char center = 9580;
        char xAxis = 9552;
        for(int yLevel = yMax; yLevel >= yMin; yLevel--)
        {
            for (int xLevel = xMin; xLevel <= xMax; xLevel++)
            {

                if (runFunction(xLevel) == yLevel)
                    System.out.print(t);
                else if (xLevel == 0 && yLevel == 0)
                    System.out.print(center);
                else if (yLevel == 0)
                    System.out.print(xAxis);
                else if (xLevel == 0)
                    System.out.print(yaxis);
                else
                    System.out.print(' ');
            }
            System.out.println();
        }

       for (int i = 32; i < 10192; i++) {
           t = (char)i;
           System.out.print(i + ": " + t);
           if ((i - 31) % 6 != 0) {
                System.out.print('\t');
           }
           else {
               System.out.println();
           }
       }
        */
    }
}