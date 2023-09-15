// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.util.*;
public class Main {
    public static int runFunction(int x) {
        return x * x + - 5;
    }
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.println("Guess this 5 digit number!");
        int guessnum = 1;
        Random rand = new Random();
        String randomNum = "";
        randomNum += rand.nextInt(100000);
        while (true) {
            String guess = "" + console.nextInt();
            if (guess.length() == 5)
                break;
            else
                System.out.println("Please type in a number with 5 digits");
        }
        while (guess != randomNum){
            System.out.println("Incorrect");
            guessnum++;
            int[] correct;
        }
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