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
        int randomNum = rand.nextInt(100000);
        int guess = console.nextInt();
        /*
        Scanner console = new Scanner(System.in);
        int xmin = -10;
        int xmax = 10;
        int ymin = -10;
        int ymax = 10;
        char t = 9607;
        char yaxis = 9553;
        char center = 9580;
        char xaxis = 9552;
        for(int ylevel = ymax; ylevel >= ymin; ylevel--)
        {
            for (int xlevel = xmin; xlevel <= xmax; xlevel++)
            {

                if (runFunction(xlevel) == ylevel)
                    System.out.print(t);
                else if (xlevel == 0 && ylevel == 0)
                    System.out.print(center);
                else if (ylevel == 0)
                    System.out.print(xaxis);
                else if (xlevel == 0)
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