import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Levenshtein {
    public static final ArrayList<String> dictionary = getWords();;
    private static final Scanner console = new Scanner(System.in);
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
    public static ArrayList<byte[]> getAlphaWords() {
        ArrayList<byte[]> words = new ArrayList<>();
        Scanner f;
        try {
            f = new Scanner(new File("src/dictionaryCatDog.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (f.hasNext()) {
            words.add(f.next().toLowerCase().getBytes());
        }
        return words;
    }

    public static void main(String[] args) {
        System.out.println("Input your two words:");
        String word1 = console.nextLine();
        String word2 = console.nextLine();
        while (!dictionary.contains(word1) || !dictionary.contains(word2)){
            System.out.println("One or both of your words is not found in dictionary");
            System.out.println("Input another word");
            if (!dictionary.contains(word1))
                word1 = console.nextLine();
            else if (dictionary.contains(word2))
                word2 = console.nextLine();
        }
        findDistance(word1, word2);
    }
    static boolean findDistance(String word1, String word2){
        ArrayList<String> previousChoices = new ArrayList<>();
        previousChoices.add(word1);
        int bigStart = startingIndex(word1.length(), 0, dictionary.size() - 1);
        int bigFinish = endingIndex(word1.length(), bigStart, dictionary.size() - 1);
        ArrayList<String> possible = new ArrayList<>(dictionary.subList(bigStart, bigFinish + 1));
        int first = firstIndex(word1.length(), bigStart, possible.size() - 1, possible);
        int last = lastIndex(word1.length(), first, possible.size() - 1, possible);
        System.out.println(first +" "+ last + ", " + bigStart + " " + bigFinish);
        return false;
    }
    ArrayList<String> binarySearchLength(int length){
        return null;
    }
    static int startingIndex(int length, int start, int finish){
        int mid = (start + finish) / 2;
        if (mid == 0)
            return 0;
        if ((dictionary.get(mid).length() == dictionary.get(mid - 1).length() + 1) && dictionary.get(mid).length() == length - 1) {
            return mid;
        } else if (dictionary.get(mid).length() > length - 2) {
            return startingIndex(length, start, mid);
        } else
            return startingIndex(length, mid, finish);
    }
    static int endingIndex(int length, int start, int finish){
        int mid = (start + finish) / 2;
        if (mid + 1 == dictionary.size()-1)
            return mid + 1;
        if ((dictionary.get(mid).length() == dictionary.get(mid + 1).length() - 1) && dictionary.get(mid).length() == length + 1) {
            return mid;
        } else if (dictionary.get(mid).length() > length + 1) {
            return endingIndex(length, start, mid);
        } else
            return endingIndex(length, mid, finish);
    }
    static int firstIndex(int length, int start, int finish, ArrayList<String> current){
        int mid = (start + finish) / 2;
        if (mid == 0)
            return 0;
        if ((current.get(mid).length() == current.get(mid - 1).length() + 1) && current.get(mid).length() == length) {
            return mid;
        } else if (current.get(mid).length() > length - 1) {
            return firstIndex(length, start, mid, current);
        } else
            return firstIndex(length, mid, finish, current);
    }
    static int lastIndex(int length, int start, int finish, ArrayList<String> current){
        int mid = (start + finish) / 2;
        if (mid + 1 == current.size() - 1)
            return mid + 1;
        if ((current.get(mid).length() == current.get(mid + 1).length() - 1) && current.get(mid).length() == length) {
            return mid;
        } else if (current.get(mid).length() > length) {
            return lastIndex(length, start, mid, current);
        } else
            return lastIndex(length, mid, finish, current);
    }
    int compareStrings(String word1, String word2){
        return 0;
    }
}
