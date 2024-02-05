//Made by Kian Darrington

//Contains the entirety of Goal sheet 11 with individual functions being the answers to problems
public class RecursionStuff {
    //Returns a string of * based upon 2^n where n is the input, subdivides for hopeful efficiency
    public static String starString(int n){
        if (n < 0)
            throw new IllegalArgumentException("Negative input detected");
        else
            return starStringMod(pow(2, n));
    }
    private static String starStringMod(int n){
        if (n == 1)
            return "*";
        else
            return starStringMod(n / 2) + starStringMod(n / 2);
    }

    //Positive integer recursive power multiplier made because why not and for starString
    private static int pow2 (int a, int b, int track){
        if (b > 1)
            return pow2(a * track, b - 1, track);
        else
            return a;
    }
    public static int pow(int a, int b){
        if (b > 1)
            return pow2(a * a, b - 1, a);
        else if (b == 1)
            return a;
        else if (b == 0)
            return 1;
        else
            throw new IllegalArgumentException("Negative input detected, this don't work with that");
    }

    //This function writes whole numbers up to inputted n
    public static String writeNums(int n){
        if (n == 1)
            return "1";
        else
            return writeNums(n -1) +", "+ n;
    }

    //Writes a mirrored sequence of numbers based off of inputted n
    //Ex. n = 3 results in 212 and n = 4 results in 2112
    public static String writeSequence(int n){
        if (n == 2)
            return "1 1 ";
        else if (n == 1)
            return "1 ";
        else if (n % 2 == 0)
            return n / 2 + " " + writeSequence(n - 2) + n / 2 + " ";
        else
            return (n / 2 + 1) + " " + writeSequence(n - 2) + (n / 2 + 1) + " ";
    }

    //Adds the sum of all the reciprocals of integers up to inputted n
    public static float sumTo(int n){
        if (n == 0)
            return 0;
        else if (n > 0)
            return sumTo(n - 1) + (float)1 / n;
        else
            throw new IllegalArgumentException("Negative input detected");
    }

    //Modified fibonacci recursive element finder. It finds the element of fibonacci inputted
    public static int fibonacci(int n){
        if (n < 0)
            throw new IllegalArgumentException("Negative input detected");
        else if (n < 3)
            return 1;
        else
        {
            return fibonacci2(n, 2, 1, 1);
        }
    }
    private static int fibonacci2(int n, int place, int last, int last2){
        place++;
        if (place == n)
            return last + last2;
        else
            return fibonacci2(n, place, last2, last + last2);
    }
    //Output stuff
    public static void main(String[] args) {
        System.out.println(fibonacci(27));
    }
}
