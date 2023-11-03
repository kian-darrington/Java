import java.util.*;
public class JavaTest {
    public static void main(String[] args) {
        int code = 5333;
        for (int place = 4; place > 0; place--)
            System.out.println((code % (int)Math.pow(10, place)) / (int)Math.pow(10, (place - 1)));
    }
}
