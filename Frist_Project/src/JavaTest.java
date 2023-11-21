import java.util.*;
public class JavaTest {
    static String writeBinary(int i){
        StringBuilder s = new StringBuilder();
        if (i > 1) {
            s.insert(0, i % 2);
            i /=2;
            return s.insert(0, writeBinary(i)).toString();
        }
        else if (i == 1)
            return "" + 1;
        else
            return "" + 0;
    }
    static int factorial (int i){
        if (i == 1)
            return 1;
        else
            return i * factorial(i - 1);
    }
    static String reverse(String e){
        return reverse(e, e.length() - 1);
    }
    static String reverse(String e, int i){
        StringBuilder s = new StringBuilder();
        s.append("" + e.charAt(i));
        if (i == 0)
            return s.toString();
        else
            return s.append(reverse(e, i - 1)).toString();
    }
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        int num = console.nextInt();
        System.out.println(writeBinary(num));
    }
}
