import java.util.*;
public class JavaTest {
    public static void main(String[] args) {
        Random rand = new Random(1);
        int[] amount = new int[6];
        for (int i = 0; i < 1000; i++)
        {
            int tempRand = rand.nextInt(6)+1;
            amount[tempRand - 1]++;
            System.out.println(tempRand);
        }
        System.out.println(Arrays.toString(amount));
    }
}
