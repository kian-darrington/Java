import java.util.*;
import java.io.*;
public class Hangman {
    public  final int MAX_GUESS = 6;
        public static void printGallows(int errors)
        {
            System.out.println(" ________  ");
            System.out.println(" |       | ");
            if (errors < 1) {
                System.out.println(" |         ");
                System.out.println(" |         ");
            }
            else
                System.out.println(" |       o ");
            switch (errors) {
                case 1:
                    System.out.println(" |         ");
                    System.out.println(" |         ");
                    break;
                case 2:
                    System.out.println(" |       | ");
                    System.out.println(" |         ");
                    break;
                case 3:
                    System.out.println(" |      /| ");
                    System.out.println(" |         ");
                    break;
                case 4:
                    System.out.println(" |      /|\\");
                    System.out.println(" |         ");
                    break;
                case 5:
                    System.out.println(" |      /|\\");
                    System.out.println(" |      /  ");
                    break;
                case 6:
                    System.out.println(" |      /|\\");
                    System.out.println(" |      / \\");
                    break;
            }

            System.out.println("___________");
        }
        public static ArrayList<String> getWords(){
            ArrayList<String> words = new ArrayList<>();
            Scanner f;
            try {
                f = new Scanner(new File("src/words.txt"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            while(f.hasNext()){
                words.add(f.next().toLowerCase());
            }
            return words;
        }
        public static void main(String[] args) {
            ArrayList<String> words = getWords();
            Random rand = new Random();
            Scanner console = new Scanner(System.in);
            while (true){
                int guessNum = 0;
                int wrongNum = 0;
                String answer = words.get(rand.nextInt(words.size()));
                boolean[] revealedLetters = new boolean[answer.length()];
                boolean hasWon = false;
                while (true) {
                    printGallows(wrongNum);
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
                    if (hasWon || wrongNum > 6)
                        break;
                    System.out.print("\nGuess: ");
                    char guess = console.nextLine().toLowerCase().charAt(0);
                    guessNum++;
                    wrongNum++;
                    boolean[]prev = revealedLetters.clone();
                    for (int i = 0; i < answer.length(); i++)
                        if (guess == answer.charAt(i))
                            revealedLetters[i] = true;
                    if (Arrays.toString(revealedLetters).compareTo(Arrays.toString(prev)) != 0)
                        wrongNum--;
                }
                if (hasWon)
                    System.out.println("\nYou won!\nIt took you " + guessNum + " guesses");
                else
                    System.out.println("\nYou lost...\nThe answer was: " + answer);
            }
        }
    }
