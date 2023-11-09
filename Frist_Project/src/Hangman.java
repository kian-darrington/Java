import java.util.*;
import java.io.*;
public class Hangman {
        public static ArrayList<String> getWords(){
            ArrayList<String> words = new ArrayList<>();
            Scanner f;
            try {
                f = new Scanner(new File("src/words.txt"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            while(f.hasNext()){
                words.add(f.next());
            }
            return words;
        }
        public static void main(String[] args) {
            ArrayList<String> words = getWords();
            Random rand = new Random();
            Scanner console = new Scanner(System.in);
            while (true){
                int guessNum = 0;
                String answer = words.get(rand.nextInt(words.size()));
                boolean[] revealedLetters = new boolean[answer.length()];
                boolean hasWon = false;
                while (true) {
                    for (int i = 0; i < answer.length(); i++) {
                        if (!revealedLetters[i])
                            System.out.print("_ ");
                        else
                            System.out.print("" + answer.toUpperCase().charAt(i) + ' ');
                    }
                    hasWon = true;
                    for (boolean t : revealedLetters){
                        if (!t) {
                            hasWon = false;
                            break;
                        }
                    }
                    if (hasWon)
                        break;
                    System.out.print("\nGuess: ");
                    char guess = console.nextLine().toLowerCase().charAt(0);
                    guessNum++;
                    for (int i = 0; i < answer.length(); i++)
                        if (guess == answer.charAt(i))
                            revealedLetters[i] = true;
                }
                System.out.println("\nYou won!\nIt took you " + guessNum + "guesses");
            }
        }
    }
