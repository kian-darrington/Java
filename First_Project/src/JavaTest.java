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
    public static int CodewordFinder(int index){
        int temp = 0;
        for (int i = 0; i < 4 ; i++){
            temp += (temp % 6) * (int) Math.pow(10, i);
            index /= 6;
        }
        return temp;
    }
    public static void main(String[] args) {
        Random rand = new Random();
        int[] sixes = new int[13];
        int[] eleven = new int[13];
        for (int i = 0; i < 100000; i++){
            sixes[2 + rand.nextInt(6) + rand.nextInt(6)]++;
            eleven[2 + rand.nextInt(11)]++;
        }
        System.out.println(Arrays.toString(sixes));
        System.out.println(Arrays.toString(eleven));
    }
}
