import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class WordsToAlphaNum {
    public static ArrayList<String> getWords() {
        ArrayList<String> words = new ArrayList<>();
        Scanner f;
        try {
            f = new Scanner(new File("src/alphaCatDog.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (f.hasNext()) {
            words.add(f.next().toLowerCase());
        }
        return words;
    }
    static byte[] characterOrganizer(String original){
        byte[] sequence = original.getBytes();
        for (int i = 0; i < sequence.length; i++){
            for (int o = 0; o < sequence.length; o++){
                if (sequence[i] < sequence[o] && i > o){
                    byte temp = sequence[o];
                    sequence[o] = sequence[i];
                    sequence[i] = temp;
                }
            }
        }
        char[] output = new char[sequence.length];
        for (int i = 0; i < sequence.length; i++){
            output[i] = (char) sequence[i];
        }
        return sequence;
    }
    public static void main(String[] args) {
        ArrayList<String> words = getWords();
        byte[][] wordBytes = new byte[words.size()][];
        for (int i = 0; i < words.size(); i ++){
            wordBytes[i] = words.get(i).getBytes();
        }
        System.out.println(Arrays.toString("z".getBytes()));
        String dog = "dgo";
        for (int i = 0; i < words.size(); i++){
            if (misMatch(dog, words.get(i)))
                System.out.println(words.get(i));
            //System.out.println(Math.abs(dogTotal - total(wordBytes[i])));
        }
    }
    static int total(byte[] bytes){
        int sum = 0;
        for (byte b : bytes)
            sum += b;
        return sum;
    }
    //MUST be inputted with alphanumerical Strings
    static boolean misMatch(String word1, String word2){
        int difference = 0, w1count = 0, w2count = 0;
        byte[] w1 = word1.getBytes(), w2 = word2.getBytes();
        while (w1count < word1.length() && w2count < word2.length()){
            if (w1[w1count] == w2[w2count]) {
                w1count++;
                w2count++;
            }
            else{
                difference++;
                if (w1[w1count] < w2[w2count])
                    w1count++;
                else
                    w2count++;
            }
            if (difference > 1)
                return false;
        }
        return difference != 0;
    }
}
