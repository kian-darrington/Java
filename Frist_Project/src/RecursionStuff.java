import java.util.*;
public class RecursionStuff {
    public static String StarStuff(int n){
        return StarStuffModified(pow(2, n));
    }
    private static String StarStuffModified(int n){
        if (n == 1)
            return "*";
        else
            return StarStuffModified(n / 2) + StarStuffModified(n / 2);
    }


    private static int pow (int a, int b, int track){
        if (b > 1)
            return pow(a * track, b - 1, track);
        else
            return a;
    }
    public static int pow(int a, int b){
        if (b > 1)
            return pow(a * a, b - 1, a);
        else if (b == 1)
            return a;
        else if (b == 0)
            return 1;
        else
            return -1;
    }
    public static String writeNums(int n){
        if (n == 1)
            return "1";
        else
            return writeNums(n -1) +", "+ n;
    }
    public static String writeSequence(int n){
        if (n == 2)
            return "1 1 ";
        else if (n == 1)
            return "1 ";
        else if (n % 2 == 0)
            return n / 2 + " " + writeSequence(n - 2) + n / 2 + " ";
        else
            return (n / 2 + 1) + " " + writeSequence(n - 2) + (n / 2 + 1)+" ";
    }
    public static float sumTo(int n){
        if (n == 0)
            return 0;
        else
            return sumTo(n - 1) + (float)1 / n;
    }
    public static void main(String[] args) {
        System.out.println(sumTo(2));
    }
}
