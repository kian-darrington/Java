// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        // Press Opt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.print("Hello and welcome!\n");

        // Press Ctrl+R or click the green arrow button in the gutter to run the code.
       double f = 111.22222;
       char t = 219;
       for (int i = 32; i < 256; i++) {
           t = (char)i;
           System.out.print(i + " : " + t);
           if ((i - 32) % 6 == 0) {

           }
       }
    }
}