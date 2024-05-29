//Made by Kian Darrington

import java.util.ArrayList;
import java.util.List;

//Contains the entirety of Goal sheet 11 with individual functions being the answers to problems
public class RecursionStuff {
    //Returns a string of * based upon 2^n where n is the input, subdivides for hopeful efficiency
    public static String starString(int n){
        if (n < 0)
            throw new IllegalArgumentException("Negative input detected");
        else if (n == 0)
            return "*";
        else
            return starString(n - 1) + starString(n - 1);
    }

    public static int pow(int a, int b){
        if (b > 1)
            return a * pow(a, b - 1);
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
    public static String writeSquares(int n){
        if (n == 1)
            return "1";
        else if (n == 0)
            return "";
        if (n % 2 == 1)
            return (n * n) + ", " + writeSquares(n-2) + ", "+((n-1) * (n-1));
        else if (n > 2)
            return ((n-1) * (n-1)) + ", " + writeSquares(n-2) + ", " + (n * n) ;
        else
            return "1, 4";
    }
    public static ArrayList<String> mergeSort(ArrayList<String> array){
        if (array.size() > 1) {
            ArrayList<String> left = new ArrayList<>(array.subList(0, array.size() / 2));
            ArrayList<String> right = new ArrayList<>(array.subList(array.size() / 2, array.size()));

            left = mergeSort(left);
            right = mergeSort(right);
            array = merge(left, right);
        }
        return array;
    }
    private static ArrayList<String> merge(ArrayList<String> left, ArrayList<String> right){
        ArrayList<String> result = new ArrayList<>();
        int i = 0;
        while (!left.isEmpty() && !right.isEmpty()){
            if (left.get(0).toLowerCase().getBytes()[i] < right.get(0).toLowerCase().getBytes()[i]) {
                result.add(left.remove(0));
                i = -1;
            }
            else if (left.get(0).toLowerCase().getBytes()[i] > right.get(0).toLowerCase().getBytes()[i]) {
                result.add(right.remove(0));
                i = -1;
            }
            i++;
        }
        while (!left.isEmpty())
            result.add(left.remove(0));
        while (!right.isEmpty())
            result.add(right.remove(0));
        return result;
    }

    //Output stuff
    public static void main(String[] args) {
        ArrayList<String> temp = new ArrayList<>(List.of(new String[]{"boy", "bot", "wow", "abba", "soon", "poop"}));
        //System.out.println(temp);
        for (int i = 0; i < 6; i++)
            System.out.println(starString(i));
    }
}
