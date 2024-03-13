import java.io.*;
import java.util.*;

public class Levenshtein {
    public static final String FILENAME = "src/dictionarySortedLength.txt";
    public static final ArrayList<Word> dictionary = getWords();
    private static final Scanner console = new Scanner(System.in);
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
        System.out.println("Input your two words:");
        String word1 = console.nextLine();
        String word2 = console.nextLine();
        ArrayList<String> temp = getWordsStrings();

        while (!temp.contains(word1) || !temp.contains(word2)){
            System.out.println("One or both of your words is not found in dictionary");
            System.out.println("Input another word");
            if (!temp.contains(word1))
                word1 = console.nextLine();
            else if (!temp.contains(word2))
                word2 = console.nextLine();
        }

        System.out.println("Both words are A OK");
        //System.out.println(findLocation(word1)+ " " + findLocation(word2));
        Word w = dictionary.get(findLocation(word1)), j = dictionary.get(findLocation(word2));
        long startTime = System.nanoTime();
        ArrayList<ArrayList<Word>> paths = findDistance(w, j);
        System.out.println("It took " + (float)(System.nanoTime() - startTime) / 1000000);
        for (Word word : paths.get(paths.size() - 1)){
            if (word.equals(j)){
                Word ans = word;
                while (ans.getParent() != null){
                    System.out.print(ans + ", ");
                    ans = ans.getParent();
                }
                System.out.println(w);
            }
        }
    }
    static ArrayList<ArrayList<Word>> findDistance(Word word1, Word word2){
        ArrayList<ArrayList<Word>> tree = new ArrayList<>();
        tree.add(new ArrayList<Word>());
        tree.get(0).add(word1);
        if (word1.equals(word2))
            return tree;
        HashSet<Word> seen = new HashSet<>();

        int level = 0;
        boolean found = false;

        while (!found && !tree.get(level).isEmpty()) {
            System.out.println("New level added with size of " + tree.get(level).size() +", words computed: " + seen.size());
            tree.add(new ArrayList<Word>());
            for (Word word : tree.get(level)) {
                if (!seen.contains(word)) {
                    if (!found) {
                        HashSet<Word> choices = findNeighbors(word);
                        choices.removeAll(seen);
                        choices.removeIf(t -> !misMatch(t, word));
                        tree.get(level + 1).addAll(choices);
                        if (choices.contains(word2)) {
                            found = true;
                            System.out.println("Found a path!");
                        }
                    }
                    else if (misMatch(word, word2))
                        tree.get(level + 1).add(new Word (word2, word));
                    seen.add(word);
                }
            }
            level++;
        }
        System.out.println("Number of words computed " +seen.size());
        if (!found)
            System.out.println("Wow... That was a waste of time");
        return tree;
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
    static boolean misMatch(String w1, String w2) {
        int difference = 0;
        int w1l = w1.length(), w2l = w2.length();
        if (w1l == w2l) {
            for (int i = 0; i < w1l && difference < 2; i++) {
                if (w1.charAt(i) != w2.charAt(i)) {
                    difference++;
                }
            }
        }
        else
        {
            if (w2l > w1l) {
                String temp = w1;
                w1 = w2;
                w2 = temp;
                w1l = w2l;
                w2l = w2.length();
            }
            int count = 0;
            for (int i = 0; i < w1l && difference < 2; i++) {
                if (count < w2l) {
                    if (w1.charAt(i) != w2.charAt(count)) {
                        difference++;
                    } else
                        count++;
                }
                else {
                    difference++;
                }
            }
        }
        return difference < 2;
    }
    static boolean misMatch (Word w1, Word w2) { return misMatch(w1.toString(), w2.toString());}
    static int findLocation(String word){
        int start = lengthStarts[word.length() - 1], end = lengthStarts[word.length()];
        for (int i = start; i < end; i++){
            if (word.equals(dictionary.get(i).toString()))
                return i;
        }
        return -1;
    }
    static HashSet<Word> findNeighbors(Word w){
        int temp = w.length();
        if (temp > 1)
            temp -= 2;
        int max = w.length();
        if (w.length() < lengthStarts.length - 2)
            max++;
        HashSet<Word> value = new HashSet<>(dictionary.subList(lengthStarts[temp], lengthStarts[max]));
        value.removeIf(t -> !misMatch(w, t));
        value.forEach(t -> t.setParent(w));
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
    public Word getParent() {return parent;}
}
