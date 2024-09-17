import java.util.*;
/*
    For the purpose of data collection and the seeking of an optimized strategy of the game "bank"

    I understand this game is all luck, but with some different agents I can try to optimize my
    personal playing style
 */
public class Bank {
    public static final int HUMAN = 0, RANDOM = 1, GREEDY = 2, LAST_BANK = 3, LATE_ROUND = 4;
    public static final int HIGH_DOUBLES = 5;
    public static String[] NAMES = new String[] { "Player ", "Random ", "Greedy ", "LastBank ",
        "LateRound "
    };
    public static Random rand = new Random();
    static Scanner console = new Scanner(System.in);
    static class gameValues {
        public int roundNum = 0;
        public int score = 0;
        public boolean wasDoubles = false;
        static int[] getDice(){
            return new int[]{1 + rand.nextInt(6), 1 + rand.nextInt(6)};
        }
        public boolean playRound(){
            wasDoubles = false;
            roundNum++;
            System.out.println("Roll " + roundNum + ":");
            int[] dice = getDice();
            int sum = dice[0] + dice[1];
            System.out.println("Rolled a " + dice[0] + " and a " + dice[1]);
            if (sum == 7) {
                System.out.println("Round Over!\n");
                console.next();
                return false;
            }
            else if (dice[0] == dice[1]) {
                score *= 2;
                wasDoubles = true;
            }
            else
                score += sum;
            System.out.println("The current pot is: " + score + "\n");
            return true;
        }
        void playRoundSafe(){
            roundNum++;
            System.out.println("Roll " + roundNum + ":");
            int[] dice = getDice();
            int sum = dice[0] + dice[1];
            System.out.println("Rolled a " + dice[0] + " and a " + dice[1]);
            if (sum == 7)
                score += 70;
            else
                score += sum;
            System.out.println("The current pot is: " + score + "\n");
        }
    }

    // This is the entire mechanics of the round scoring mechanism. Everything else is just deciding
    // whether to bank or not
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
    static String playGame(int roundNum, int[] playerTypes){
        int playerNum = playerTypes.length;
        int[] playerScores = new int[playerNum];
        for (int count = 0; count < roundNum; count++){
            System.out.println("ROUND " + (count + 1) + "!!!!!\n");
            boolean stillGoing = true;
            boolean[] isBanked = new boolean[playerNum];
            gameValues game = new gameValues();
            for (int i = 0; i < 3; i++)
                game.playRoundSafe();
            while (stillGoing){
                for (int i = 0; i < playerNum; i++){
                    if (isBanked[i])
                        continue;
                    switch(playerTypes[i]){
                        case HUMAN:
                            isBanked[i] = humanCheck(i);
                            break;
                        case RANDOM:
                            isBanked[i] = randomCheck();
                            break;
                        case GREEDY:
                            isBanked[i] = greedyCheck(game.score);
                            break;
                        case LAST_BANK:
                            isBanked[i] = lastBankCheck(playerTypes, isBanked);
                            break;
                        case LATE_ROUND:
                            isBanked[i] = lateRoundCheck(game.roundNum);
                            break;
                    }
                    if (isBanked[i]) {
                        playerScores[i] += game.score;
                        System.out.println(NAMES[playerTypes[i]] + i + " has banked\n");
                    }
                }
                stillGoing = hasFalse(isBanked);
                if (stillGoing)
                    stillGoing = game.playRound();
            }
            printScores(playerScores, playerTypes);
        }
        int winner = findMax(playerScores);
        return NAMES[playerTypes[winner]] + winner +" won with " + playerScores[winner] + " points!";
    }
    static boolean humanCheck(int i){
        while(true) {
            System.out.println("Player " + i + " would you like to bank? (y, n)");
            char ans = console.next().toLowerCase().toCharArray()[0];
            if (ans == 'y')
                return true;
            else if (ans == 'n')
                return false;
            else
                System.out.println("Please type in a proper input");
        }
    }
    static boolean randomCheck(){
        return rand.nextBoolean();
    }
    static boolean greedyCheck(int score){
        return score > 250 + rand.nextInt(100);
    }
    static boolean lastBankCheck(int[] playerTypes, boolean[] isBanked){
            for (int i = 0; i < playerTypes.length; i++){
                if (!isBanked[i] && playerTypes[i] != LAST_BANK)
                    return false;
            }
        return true;
    }
    static boolean lateRoundCheck(int roundNum) { return roundNum > 8 + rand.nextInt(4);}
    static boolean highDoubleCheck(int roundNum, boolean wasDoubles) { return wasDoubles && roundNum > 7; }
    static boolean hasFalse(boolean[] array){
        for(int i = 0; i < array.length; i++)
            if (!array[i])
                return true;
        return false;
    }
    static void printScores(int[] scores, int[] playerTypes){
        for (int i = 0; i < scores.length; i++)
            System.out.println(NAMES[playerTypes[i]] + i + " has " + scores[i] + " points");
        System.out.println();
    }
    public static void main(String[] args){
        int[] playerTypes = new int[]{HUMAN, RANDOM, GREEDY, LAST_BANK, LATE_ROUND};
        System.out.println(playGame( 10, playerTypes));
    }
}
