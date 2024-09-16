import java.util.*;
/*
    For the purpose of data collection and the seeking of an optimized strategy of the game "bank"

    I understand this game is all luck, but with some different agents I can try to optimize my
    personal playing style
 */
public class Bank {
    public static Random rand = new Random();
    class gameValues {
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
        System.out.println("Round " + game.roundNum + ":");
        int[] dice = getDice();
        int sum = dice[0] + dice[1];
        System.out.println("Rolled a " + dice[0] + " and a " + dice[1]);
        if (game.roundNum > 3){
            if (sum == 7) {
                System.out.println("Round Over!");
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
        System.out.println("The round score is: " + game.score);
        return true;
    }
    static int findMax(int[] array){
        int CurrentMax = Integer.MIN_VALUE;
        int topIndex = 0;
        for (int i = 0; i < array.length; i++){
            
        }
    }
    static String playGame(int playerNum, int roundNum){
        int[] playerScores = new int[playerNum];
        for (int count = 0; count < roundNum; count++){

        }
    }
    public static void main(String[] args){
        System.out.println(playGame(1, 10));
    }
}
