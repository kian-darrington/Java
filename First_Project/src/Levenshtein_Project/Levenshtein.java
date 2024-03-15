package Levenshtein_Project;

import java.io.*;
import java.util.*;
//This class is for Mr Miyoshi's Levenshtein project, in which you find the shortest path between two words
//going a single character edit at a time to other words inside a dictionary
//Made by Kian Darrington
public class Levenshtein {
    public static final String FILENAME = "src/Levenshtein_Project/dictionarySortedLength.txt";
    public static final ArrayList<Word> dictionary = getWords();
    private static final Scanner console = new Scanner(System.in);
    //Gets the dictionary in a Word class form
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
    //Gets the dictionary in a String class form to allow for different application
    //I should get rid of one or the other since it's redundant, but I'm kinda lazy
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
    //This contains a list of all the starting values of a length inside the dictionary to allow for quick references
    //to appropriate chunks of data
    static final int[] lengthStarts = startingIndexes();
    //This finds all the Starting indexes of the dictionary according to length
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
    //Main, gathers the two words, finds the tree in-between, and outputs all shortest paths
    public static void main(String[] args) {
        System.out.println("Input your two words:");

        String word1 = console.nextLine();
        String word2 = console.nextLine();

        ArrayList<String> temp = getWordsStrings();

        //Error check
        while (!temp.contains(word1) || !temp.contains(word2)){
            System.out.println("One or both of your words is not found in dictionary");
            System.out.println("Input another word");
            if (!temp.contains(word1))
                word1 = console.nextLine();
            else if (!temp.contains(word2))
                word2 = console.nextLine();
        }

        System.out.println("Both words are A OK");

        //Finds the location of the words inside the dictionary, putting them in the system for future use
        Word w = dictionary.get(findLocation(word1)), j = dictionary.get(findLocation(word2));

        //Starts the stopwatch of my bad code
        long startTime = System.nanoTime();

        //This gets the tree in-between the two words
        ArrayList<ArrayList<Word>> paths = findDistance(w, j);

        System.out.println("It took " + (float)(System.nanoTime() - startTime) / 1000000);

        //This outputs all the shortest paths as found in the tree
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
    //Creates a 2D array of all the neighbors of a word, and then the neighbors of the neighbors of the word
    static ArrayList<ArrayList<Word>> findDistance(Word word1, Word word2){
        ArrayList<ArrayList<Word>> tree = new ArrayList<>();
        tree.add(new ArrayList<Word>());
        tree.get(0).add(word1);

        if (word1.equals(word2))
            return tree;

        HashSet<Word> seen = new HashSet<>();

        int level = 0;
        boolean found = false;
        //This finds the neighbors of a level in the tree, making sure to never calculate the same word twice
        while (!found && !tree.get(level).isEmpty()) {
            System.out.println("New level added with size of " + tree.get(level).size() +", words computed: " + seen.size());
            tree.add(new ArrayList<Word>());
            for (Word word : tree.get(level)) {
                //If this is a new word, find the neighbors and put them in
                if (!seen.contains(word)) {
                    //If we haven't found the shortest path, we do a lot of work to find all the neighbors
                    if (!found) {
                        HashSet<Word> choices = findNeighbors(word);
                        choices.removeAll(seen);

                        if (choices.contains(word2)) {
                            found = true;
                            System.out.println("Found a path!");
                        }

                        ArrayList<Word> holder = tree.get(level + 1);
                        for (Word temp : choices)
                            if (misMatch(temp, word))
                                holder.add(new Word(temp, word));
                    }
                    //If there is a shortest path, the task is easier to just reference it off the word we're looking at
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
    //Finds the first occurrence of a specific length in an inputted ArrayList
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

    //Ah yes, the all important function.
    //This function looks at two strings and returns true if the individual character difference between the two is < 2
    //This function gets called probably tens of thousands to millions of times on longer runs, so it must be efficent
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
    //Wrapper function with different arguments for ease of use
    static boolean misMatch (Word w1, Word w2) { return misMatch(w1.toString(), w2.toString());}
    //Locates the index of an inputted String in the dictionary
    static int findLocation(String word){
        int start = lengthStarts[word.length() - 1], end = lengthStarts[word.length()];
        for (int i = start; i < end; i++){
            if (word.equals(dictionary.get(i).toString()))
                return i;
        }
        return -1;
    }
    //Finds all the words one edit distance away from an inputted word
    static HashSet<Word> findNeighbors(Word w){
        int temp = w.length();
        int max = temp;
        if (temp > 1)
            temp -= 2;
        if (max < lengthStarts.length - 2)
            max++;
        HashSet<Word> value = new HashSet<>(dictionary.subList(lengthStarts[temp], lengthStarts[max]));
        value.removeIf(t -> !misMatch(w, t));
        return value;
    }
}
//This class is basically a String that points to another String, allowing for the mapping of words to take place
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
