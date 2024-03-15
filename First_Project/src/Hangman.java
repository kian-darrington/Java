import java.util.*;
import java.io.*;
//Created by Kian Darrington for the purpose of playing a good game of hangman
//This class administers the game of Hangman, allowing for 7 total wrong guesses
//I believe this is project 3
public class Hangman {
    //Constant for the number of Guesses
    public static final int MAX_GUESS = 6;
    //This function prints the gallows based off of the number of wrong guesses
    final static Random rand = new Random();
    final static Scanner console = new Scanner(System.in);
    //A spaghetti esq function that I wrote because I was too lazy to write more concise code (it makes sense though)
    public static void printGallows(int errors, String letters)
    {
        if (errors < 1) {
            System.out.println(" ________  ");
            System.out.println(" |       | ");
            System.out.println(" |         ");
            System.out.println(" |         ");
        }
        else {
            System.out.print("Previous guesses: ");
            for(int i = 0; i < letters.length(); i++){
                System.out.print("" + letters.charAt(i) + ' ');
            }
            System.out.println("\n ________  ");
            System.out.println(" |       | ");
            System.out.println(" |       o ");
        }
        if (errors > 3)
            System.out.println(" |      /|\\");

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
                System.out.println(" |         ");
                break;
            case 5:
                System.out.println(" |      /  ");
                break;
            case 6:
                System.out.println(" |      / \\");
                break;
        }

        System.out.println("___________");
    }
    //Returns all the words from the file
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
    //Main, runs the playing function
    public static void main(String[] args) {
        ArrayList<String> words = getWords();
        playGame(words);
    }
    //Allows for a user based game of hangman to be played until the user wants to stop
    static void playGame(ArrayList<String> words){
        while (true){
            int guessNum = 0;
            int wrongNum = 0;
            //Selects random word
            String answer = words.get(rand.nextInt(words.size()));
            //Create the array that tracks the revealed Letters
            boolean[] revealedLetters = new boolean[answer.length()];
            boolean hasWon = false;
            //Keeps track of what the user has already guessed, as well as the incorrect letters
            StringBuilder wrongLetters = new StringBuilder();
            StringBuilder allLetters = new StringBuilder();
            //Initiates the game
            while (true) {
                if (wrongNum > MAX_GUESS)
                    break;
                //Sets up the visual hangman
                printGallows(wrongNum, wrongLetters.toString().toUpperCase());
                //This prints the dashed lines of the unknown word, as well as the revealed letters in their proper place
                //Ex: B _ L L _ _ N for Balloon if the user has guesses have B, L, and N
                for (int i = 0; i < answer.length(); i++) {
                    if (!revealedLetters[i])
                        System.out.print("_ ");
                    else
                        System.out.print("" + answer.toUpperCase().charAt(i) + ' ');
                }
                //Win check to see if all letters have been guessed
                hasWon = true;
                for (boolean t : revealedLetters){
                    if (!t) {
                        hasWon = false;
                        break;
                    }
                }
                if (hasWon)
                    break;

                //Gets the user's next guess and checks if it has already been guessed
                System.out.print("\nGuess: ");
                char guess = console.nextLine().toLowerCase().charAt(0);
                while (allLetters.indexOf("" + guess) != -1){
                    System.out.println("You already guessed that!");
                    System.out.print("Guess: ");
                    guess = console.nextLine().toLowerCase().charAt(0);
                }
                //Adds the guess to the total guess list
                allLetters.append(guess);
                guessNum++;
                wrongNum++;

                //Checks to see if the guess is part of the secret word, and adjusts the error counter appropriately
                boolean[]prev = revealedLetters.clone();
                for (int i = 0; i < answer.length(); i++)
                    if (guess == answer.charAt(i))
                        revealedLetters[i] = true;
                //If the old Boolean array is different then the new boolean array, it adjusts the error counter because a letter has been guessed
                if (Arrays.toString(revealedLetters).compareTo(Arrays.toString(prev)) != 0)
                    wrongNum--;
                else
                    wrongLetters.append(guess);
            }
            //UI and checking for another game
            if (hasWon)
                System.out.println("\nYou won!\nIt took you " + guessNum + " guesses");
            else
                System.out.println("\nYou lost...\nThe answer was: " + answer);
            System.out.println("Play again?");
            char play = console.nextLine().toLowerCase().charAt(0);
            if (play == 'n')
                break;
        }
    }
}
