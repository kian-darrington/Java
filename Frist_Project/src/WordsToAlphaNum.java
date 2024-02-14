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
            f = new Scanner(new File("src/dictionarySortedLength.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (f.hasNext()) {
            words.add(f.next().toLowerCase());
        }
        return words;
    }

    static byte[] characterOrganizer(String original) {
        byte[] sequence = original.getBytes();
        for (int i = 0; i < sequence.length; i++) {
            for (int o = 0; o < sequence.length; o++) {
                if (sequence[i] < sequence[o] && i > o) {
                    byte temp = sequence[o];
                    sequence[o] = sequence[i];
                    sequence[i] = temp;
                }
            }
        }
        char[] output = new char[sequence.length];
        for (int i = 0; i < sequence.length; i++) {
            output[i] = (char) sequence[i];
        }
        return sequence;
    }

    public static void main(String[] args) {
        long temp = System.nanoTime();
        ArrayList<String> words = getWords();
        byte[][] wordBytes = new byte[words.size()][];
        for (int i = 0; i < words.size(); i++) {
            wordBytes[i] = words.get(i).getBytes();
        }
        String dog = "dog";
        int count = 0;
        for (int i = 0; i < words.size(); i++) {
            if (misMatch(dog, words.get(i)))
                count++;
                //System.out.println(words.get(i));
            //System.out.println(Math.abs(dogTotal - total(wordBytes[i])));
        }
        System.out.println(count + " Time: " +(float)(System.nanoTime() - temp) / 1000000);
    }

    static int total(byte[] bytes) {
        int sum = 0;
        for (byte b : bytes)
            sum += b;
        return sum;
    }

    static boolean misMatch(String word1, String word2) {
        int difference = 0;
        byte[] w1 = word1.getBytes(), w2 = word2.getBytes();
        if (word1.length() == word2.length()) {
            for (int i = 0; i < w1.length; i++) {
                if (w1[i] != w2[i]) {
                    difference++;
                    if (difference > 1)
                        return false;
                }
            }
        }
        else
        {
            if (word2.length() > word1.length()) {
                byte[] temp = w1;
                w1 = w2;
                w2 = temp;
            }
            int count = 0;
            for (int i = 0; i < w1.length; i++) {
                if (count < w2.length) {
                    if (w1[i] != w2[count]) {
                        difference++;
                        if (difference > 1)
                            return false;
                    } else
                        count++;
                }
                else {
                    difference++;
                    if (difference > 1)
                        return false;
                }
            }
        }
        return difference != 0;
    }
}