import java.util.*;
/*
    For the purpose of data collection and the seeking of an optimized strategy of the game "bank"

    I understand this game is all luck, but with some different agents I can try to optimize my
    personal playing style
 */
public class Bank {
    public static Random rand = new Random();
    static Scanner console = new Scanner(System.in);
    static class gameValues {
        public int roundNum = 0;
        public int score = 0;
    }
    static int[] getDice(){
        return new int[]{1 + rand.nextInt(6), 1 + rand.nextInt(6)};
    }
    // This is the entire mechanics of the round scoring mechanism. Everything else is just deciding
    // whether to bank or not
    static boolean playRound(gameValues game){
        game.roundNum++;
        System.out.println("Roll " + game.roundNum + ":");
        int[] dice = getDice();
        int sum = dice[0] + dice[1];
        System.out.println("Rolled a " + dice[0] + " and a " + dice[1]);
        if (game.roundNum > 3){
            if (sum == 7) {
                System.out.println("Round Over!\n");
                console.next();
                return false;
            }
            else if (dice[0] == dice[1])
                game.score *= 2;
            else
                game.score += sum;
        }
        else {
            if (sum == 7)
                game.score += 70;
            else
                game.score += sum;
        }
        System.out.println("The current pot is: " + game.score + "\n");
        return true;
    }
    static int findMax(int[] array){
        int CurrentMax = Integer.MIN_VALUE;
        int topIndex = 0;
        for (int i = 0; i < array.length; i++){
            if(array[i] > CurrentMax){
                topIndex = i;
                CurrentMax = array[i];
            }
        }
        return topIndex;
    }
    static String playGame(int playerNum, int roundNum){
        int[] playerScores = new int[playerNum];
        for (int count = 0; count < roundNum; count++){
            System.out.println("ROUND " + (count + 1) + "!!!!!\n");
            boolean stillGoing = true;
            boolean[] banked = new boolean[playerNum];
            gameValues game = new gameValues();
            for (int i = 0; i < 3; i++)
                stillGoing = playRound(game);
            while (stillGoing){
                for (int i = 0; i < playerNum; i++){
                    if (banked[i])
                        continue;
                    System.out.println("Player " + i + " would you like to bank? (y, n)");
                    char ans = console.next().toLowerCase().toCharArray()[0];
                    if (ans == 'y') {
                        banked[i] = true;
                        playerScores[i] += game.score;
                    }
                    else if (ans == 'n')
                        continue;
                    else{
                        System.out.println("Bro type in the proper input");
                        i--;
                    }
                }
                stillGoing = hasFalse(banked);
                if (stillGoing)
                    stillGoing = playRound(game);
            }
            printScores(playerScores);
        }
        int winner = findMax(playerScores);
        return "Player " + winner +" won with " + playerScores[winner] + " points!";
    }
    static boolean hasFalse(boolean[] array){
        for(int i = 0; i < array.length; i++)
            if (!array[i])
                return true;
        return false;
    }
    static void printScores(int[] scores){
        for (int i = 0; i < scores.length; i++)
            System.out.println("Player " + i + " has " + scores[i] + " points");
        System.out.println();
    }
    public static void main(String[] args){
        System.out.println(playGame(2, 10));
    }
}
