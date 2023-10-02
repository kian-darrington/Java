import java.util.*;
public class JavaTest {
    public static boolean isSuch (int n){
        return n > 2 && !isSuch(n-2);
    }
    public static void main(String[] args) {
        System.out.println(isSuch(99) + " " +isSuch(100));
    }
}
