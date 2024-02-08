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
            f = new Scanner(new File("src/dictionaryCatDog.txt"));
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
        String dog = "dog";
        byte[] dogStuff = dog.getBytes();
        int dogTotal = total(dogStuff);
        for (int i = 0; i < words.size(); i++){
            if (Math.abs(dogTotal - total(wordBytes[i])) < 26){
                System.out.println(i);
            }
            //System.out.println(Math.abs(dogTotal - total(wordBytes[i])));
        }
    }
    static int total(byte[] bytes){
        int sum = 0;
        for (byte b : bytes)
            sum += b;
        return sum;
    }
    int misMatch(String word1, String word2){
        int w1Max = word1.length(), w2Max = word2.length();
        for (int i = 0; i < w1Max; i++){

        }
        return 0;
    }
}
