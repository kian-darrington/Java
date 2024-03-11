import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class Levenshtein {
    public static final String FILENAME = "src/dictionaryCatDog.txt";
    public static final ArrayList<Word> dictionary = getWords();
    private static final Scanner console = new Scanner(System.in);
    static ArrayList<Word> words1 = null;
    static int[] word1Distances = null;
    public static ArrayList<Word> getWords() {
        ArrayList<Word> words = new ArrayList<>();
        Scanner f;
        try {
            f = new Scanner(new File(FILENAME));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int i = 0;
        while (f.hasNext()) {
            words.add(new Word(f.next().toLowerCase()));
            i++;
        }
        return words;
    }
    static void saveData(ArrayList<Word> words) throws FileNotFoundException{
        PrintStream output = new PrintStream("src/SaveData");
        for (int i = 0; i < words.size(); i++){
            output.println(words.get(i).toString());
        }
    }
    public static ArrayList<String> getWordsStrings() {
        ArrayList<String> words = new ArrayList<>();
        Scanner f;
        try {
            f = new Scanner(new File(FILENAME));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int i = 0;
        while (f.hasNext()) {
            words.add(f.next().toLowerCase());
            i++;
        }
        return words;
    }
    static final int[] lengthStarts = startingIndexes();
    static int[] startingIndexes(){
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(0);
        for (int i = 2; i <= dictionary.get(dictionary.size()-1).length(); i++)
            temp.add(firstIndex(i, temp.get(i - 2), dictionary.size()-1, dictionary));
        int[] finish = new int[temp.size()];
        for (int i = 0; i <temp.size(); i++)
            finish[i] = temp.get(i);
        return finish;
    }
    public static void main(String[] args) {
        System.out.println(Arrays.toString(lengthStarts));
        System.out.println("Input your two words:");
        String word1 = console.nextLine();
        String word2 = console.nextLine();
        ArrayList<String> temp = getWordsStrings();
        while (!temp.contains(word1) || !temp.contains(word2)){
            System.out.println("One or both of your words is not found in dictionary");
            System.out.println("Input another word");
            if (!temp.contains(word1))
                word1 = console.nextLine();
            else if (temp.contains(word2))
                word2 = console.nextLine();
        }
        System.out.println("Both words are A OK");
        //System.out.println(findLocation(word1)+ " " + findLocation(word2));
        Word w = dictionary.get(findLocation(word1)), j = dictionary.get(findLocation(word2));
        ArrayList<ArrayList<Word>> paths = findDistance(w, j);
    }
    static ArrayList<ArrayList<Word>> findDistance(Word word1, Word word2){
        ArrayList<ArrayList<Word>> tree = new ArrayList<>();
        tree.add(new ArrayList<Word>());
        tree.get(0).add(word1);
        if (word1.equals(word2))
            return tree;
        HashSet<Word> seen = new HashSet<>(tree.get(0));

        tree.add(new ArrayList<Word>());

        int level = 0;
        int found = 0;

        for(Word word : tree.get(level)){
            if (!seen.contains(word)) {
                HashSet<Word> choices = findNeighbors(word);
                for (Word temp : choices) {
                    if (temp.equals(word1))
                        found++;
                    if (misMatch(word, temp))
                        tree.get(level + 1).add(temp);
                }
                seen.add(word);
            }

        }

        return null;
    }
    static int firstIndex(int length, int start, int finish, ArrayList<Word> current){
        int mid = (start + finish) / 2;
        if (mid == 0)
            return 0;
        else if (finish - start == 1 && length != current.get(start).length() && current.get(finish).length() != length)
            return -1;
        else if (finish - start == 1 && length != current.get(start).length())
            return finish;
        if ((current.get(mid).length() == current.get(mid - 1).length() + 1) && current.get(mid).length() == length) {
            return mid;
        } else if (current.get(mid).length() > length - 1) {
            return firstIndex(length, start, mid, current);
        } else
            return firstIndex(length, mid, finish, current);
    }
    static boolean misMatch(String word1, String word2) {
        return misMatch(word1.getBytes(), word2.getBytes());
    }
    static boolean misMatch(byte[] w1, byte[] w2) {
        int difference = 0;
        if (w1.length == w2.length) {
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
            if (w2.length > w1.length) {
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
    static boolean misMatch (Word w1, Word w2) { return misMatch(w1.toString().getBytes(), w2.toString().getBytes());}
    static int findLocation(String word){
        int start = lengthStarts[word.length() - 1], end = lengthStarts[word.length()];
        for (int i = start; i < end; i++){
            if (word.equals(dictionary.get(i).toString()))
                return i;
        }
        return -1;
    }
    static HashSet<Word> findNeighbors(Word w){
        HashSet<Word> value = new HashSet<>();
        int temp = w.length();
        if (temp > 1)
            temp -= 2;
        int max = w.length();
        if (w.length() < lengthStarts.length - 2)
            max++;
        for (int i = lengthStarts[temp]; i < lengthStarts[max]; i++){
            if (misMatch(w, dictionary.get(i)))
                value.add(new Word (dictionary.get(i), w));
        }
        return value;
    }
}
class Word{
    private Word parent = null;
    private final String word;
    Word (Word w){
        word = w.word;
    }
    Word (Word w, Word p){
        word = w.word;
        parent = p;
    }
    Word (String s){
        word = s;
    }
    void setParent(Word w) {parent = w;}
    int length() {return word.length();}
    public String toString() {return word;}
    public boolean equals(Object o) {return o.toString().equals(word);}
    public Word clone() {return new Word(this, parent);}
    public Word getParent() {return parent;}
}
