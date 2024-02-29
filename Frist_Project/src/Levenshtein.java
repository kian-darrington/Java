import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class Levenshtein {
    public static final String FILENAME = "src/dictionarySortedLength.txt";
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
            words.add(new Word(f.next().toLowerCase(), i));
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
    static int[] startingDistanceIndexes(ArrayList<Word> words){
        int[] distance = new int[words.get(words.size()-1).getDistance() + 1];
        distance[0] = 0;
        for (int i = 1; i < distance.length; i++)
            distance[i] = firstIndexDistance(i, distance[i - 1], words.size(), words);
        return distance;
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
        ArrayList<ArrayList<Word>> paths = findDistance(findLocation(word1), findLocation(word2));
    }
    static ArrayList<ArrayList<Word>> findDistance(int word1, int word2){
        words1 = setDifference(word1);
        word1Distances = startingDistanceIndexes(words1);

        Word w2 = dictionary.get(word2);
        Word w1 = dictionary.get(word1);
        //System.out.println(words1);
        //System.out.println(Arrays.toString(word1Distances));
        ArrayList<ArrayList<Word>> paths = new ArrayList<>();
        boolean pathFound = false;

        int distance = 0;
        ArrayList<Word> path = new ArrayList<>();
        ArrayList<Word> realPath = new ArrayList<>();
        path.add(w2);
        int timesRan = 0;
        while(!path.isEmpty()) {
            Word current = path.remove(path.size() - 1);
            if (current.equals(w1))
                break;
            distance = numMisMatch(current.toString(), w1.toString());
            ArrayList<Word> nextDistance = new ArrayList<>(words1.subList(word1Distances[distance - 1], word1Distances[distance]));
            //System.out.println(nextDistance);
            ArrayList<Word> neighbors = findNeighbors(current.toString());
            //System.out.println(neighbors);
            neighbors.retainAll(nextDistance);
            //System.out.println(neighbors);
            if (!neighbors.isEmpty()) {
                path.addAll(neighbors);
                realPath.add(current);
            }
        }
        realPath.add(w1);
        System.out.println(realPath);
        return null;
    }
    static ArrayList<Word> setDifference(int word2){
        Word ref = dictionary.get(word2);
        ArrayList<Word> diff = new ArrayList<>();
        for (Word w : dictionary){
            if (dictionary.get(word2) == w)
                continue;
            int temp = numMisMatch(ref.toString(), w.toString());
            Word thing = new Word(w, temp);
            if (!diff.isEmpty()) {
                if (thing.getDistance() < diff.get(0).getDistance())
                    diff.add(0, thing);
                else if (thing.getDistance() > diff.get(diff.size() - 1).getDistance())
                    diff.add(thing);
                else
                    diff.add(firstIndexDistance(thing.getDistance(), 0, diff.size(), diff) + 1, thing);
            }
            else
                diff.add(thing);
        }
        diff.add(0, ref);
        return diff;
    }
    int[] findDistanceRecurse(int word1, int word2, int currentShortest, int[] choices){
        int[] previousChoices = new int[1];
        if (choices != null) {
            previousChoices = new int[choices.length + 1];
            for (int i = 0; i < choices.length; i++)
                previousChoices[i] = choices[i];
            previousChoices[previousChoices.length - 1] = word1;
        } else
            previousChoices[0] = word1;
        ArrayList<Integer> possible = new ArrayList<>();
        for (int i = lengthStarts[word1 - 2]; i < lengthStarts[word1]; i++)
            if (misMatch(dictionary.get(word1).toString(), dictionary.get(i).toString()))
                possible.add(i);
        if (possible.contains(word2))
            return previousChoices;
        else{
            for (int i : previousChoices)
                possible.remove((Integer) i);
        }
        if (possible.isEmpty())
            return null;
        else{
            for (int i : possible) {
                int[] temp = findDistanceRecurse(i, word2, currentShortest, previousChoices);
                if (temp != null)
                    return temp;
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
    static int firstIndexDistance(int distance, int start, int finish, ArrayList<Word> current){
        int mid = (start + finish) / 2;
        if (mid == 0)
            return 0;
        else if (finish - start == 1 && distance != current.get(start).getDistance())
            return finish;
        if ((current.get(mid).getDistance() == current.get(mid - 1).getDistance() + 1) && current.get(mid).getDistance() == distance) {
            return mid;
        } else if (current.get(mid).getDistance() > distance - 1) {
            return firstIndexDistance(distance, start, mid, current);
        } else
            return firstIndexDistance(distance, mid, finish, current);
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
    static int numMisMatch(String word1, String word2){
        int difference = 0;
        byte[] w1 = word1.getBytes(), w2 = word2.getBytes();
        if (word1.length() == word2.length()) {
            for (int i = 0; i < w1.length; i++) {
                if (w1[i] != w2[i]) {
                    difference++;
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
                    } else
                        count++;
                }
                else {
                    difference++;
                }
            }
        }
        return difference;
    }
    static int findLocation(String word){
        int start = lengthStarts[word.length() - 1], end = lengthStarts[word.length()];
        for (int i = start; i < end; i++){
            if (word.equals(dictionary.get(i).toString()))
                return i;
        }
        return -1;
    }
    static int findDistanceLocation(String word, ArrayList<Word> ref){
        int distance = numMisMatch(word, ref.get(0).toString());
        for (int i = word1Distances[distance]; i < word1Distances[distance + 1]; i++)
            if (word.equals(ref.get(i).toString()))
                return i;
        return -1;
    }
    static ArrayList<Word> findNeighbors(String w){
        ArrayList<Word> value = new ArrayList<>();
        byte[] b = w.getBytes();
        int temp = w.length();
        if (temp > 1)
            temp -= 2;
        int max = w.length();
        if (w.length() < lengthStarts.length - 2)
            max++;
        for (int i = lengthStarts[temp]; i < lengthStarts[max]; i++){
            if (misMatch(b, dictionary.get(i).toString().getBytes()))
                value.add(dictionary.get(i));
        }
        return value;
    }
}
class Word{
    private final String word;
    private final int location;
    private int distance = 0;
    Word(String w, int l){
        word = w;
        location = l;
    }
    Word (Word w){
        word = w.word;
        location = w.location;
        distance = w.distance;
    }
    Word (Word w, int d){
        word = w.word;
        location = w.location;
        distance = d;
    }
    void setDistance(int i) {distance = i;}
    int length() {return word.length();}
    int getLocation() {return location;}
    int getDistance() {return distance;}
    public String toString() {return word;}
    public boolean equals(Object o) {return o.toString().equals(word);}
}
