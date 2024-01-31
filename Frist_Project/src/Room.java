import java.util.*;
//This is the Room class which populates the maze
public class Room {
    //Gets some information from the original class, should have set this up in the Constructor
    int X_ROOM = 0;
    int Y_ROOM = 0;
    //establishes necessary variables
    int numMove = 4, timesSeen = 0;
    int xCord;
    int yCord;
    //True or false for N, E, S, W accordingly
    boolean[] canMove = new boolean[4];
    boolean onPath, isExit, onNewPath;
    //For the sake of my poor brain
    public static final int N = 0, E = 1, S = 2, W = 3;
    boolean partOfSolvePath = false, onSolvePath = false;
    private static final Random rand = new Random();
    //Current neighbors
    Room[] neighbors = new Room[4];
    //Run-of-the-mill set functions
    void onPath(){onPath = true;}
    void offPath() {onPath = false;}
    void onNewPath() {onNewPath = true;}
    void offNewPath() {onNewPath = false;}
    void assignNeighbors(Room[] r){
        neighbors = r;
    }
    int getTimesSeen(){return timesSeen;}
    //default Constructor
    Room(){ reset(); }
    //Constructor which allows for the copying of a room
    Room(int x, int y, boolean[] t)
    {
        xCord = x;
        yCord = y;
        canMove = t;
    }
    //Sets the Coord of the room for reference
    public void setCord(int x, int y){
        xCord = x;
        yCord = y;
    }
    //Checks if the room is on the perimeter of the maze, and adjusts accordingly
    void edgeCheck()
    {
        if (xCord == 0)
            canMove[W] = false;
        if (xCord == X_ROOM -1)
            canMove[E] = false;
        if (yCord == 0)
            canMove[N] = false;
        if (yCord == Y_ROOM -1)
            canMove[S] = false;
        numMoveCheck();
    }
    void edgeCheck (boolean[] t){
        if (xCord == 0)
            t[W] = false;
        if (xCord == X_ROOM -1)
            t[E] = false;
        if (yCord == 0)
            t[N] = false;
        if (yCord == Y_ROOM - 1)
            t[S] = false;
    }
    int[] edgeCheckReturn(int from){
        boolean[] ableToChange = new boolean[4];

        for (int i = 0; i < 4; i++)
            ableToChange[i] = !canMove[i];
        edgeCheck(ableToChange);
        ableToChange[from] = false;
        //System.out.println(Arrays.toString(ableToChange));
        int pathNum = 0;
        for (boolean t : ableToChange)
            if (t)
                pathNum++;

        int[] alterable = new int[pathNum];

        int count = 0;
        for(int i = 0; i < ableToChange.length; i++)
            if (ableToChange[i])
                alterable[count++] = i;

        return alterable;
    }
    int[] edgeCheckReturn(){
        boolean[] ableToChange = new boolean[4];

        for (int i = 0; i < 4; i++)
            ableToChange[i] = !canMove[i];
        edgeCheck(ableToChange);
        //System.out.println(Arrays.toString(ableToChange));
        int pathNum = 0;
        for (boolean t : ableToChange)
            if (t)
                pathNum++;

        int[] alterable = new int[pathNum];

        int count = 0;
        for(int i = 0; i < ableToChange.length; i++)
            if (ableToChange[i])
                alterable[count++] = i;

        return alterable;
    }
    //Generates a number of how many rooms you can move into currently
    void numMoveCheck(){
        numMove = 0;
        for (boolean t : canMove)
            if (t)
                numMove++;
    }
    void changeMove(boolean[] t){
        canMove = t;
        edgeCheck();
    }
    //Allows to alter the availability of the path according to the direction inputted
    void changeMove(int direction, boolean t){
        canMove[direction] = t;
        edgeCheck();
    }
    //more gets and sets
    void setExit(boolean t) { isExit = t; }
    boolean getExit(){return isExit;}
    //Wipes the room afresh
    void reset(){
        canMove = new boolean[] {false, false, false, false};
        onPath = false;
        isExit = false;
        onNewPath = false;
        X_ROOM = 0;
        Y_ROOM = 0;
    }
    void roomWipe(){
        canMove = new boolean[] {false, false, false, false};
        onPath = false;
        onNewPath = false;
    }

    public int numMove(){ return numMove; }
    //Returns an array of the possible directions for which a maze maker could travel/create new paths
    public int[] alterablePathsInt(){
        numMoveCheck();
        boolean[] ableToChange = new boolean[4];

        for (int i = 0; i < 4; i++)
            ableToChange[i] = !canMove[i];
        edgeCheck(ableToChange);

        neighborCheck(ableToChange);

        int pathNum = 0;
        for (boolean t : ableToChange)
            if (t)
                pathNum++;

        int[] alterable = new int[pathNum];

        int count = 0;
        for(int i = 0; i < ableToChange.length; i++)
            if (ableToChange[i])
                alterable[count++] = i;

        return alterable;
    }
    int randAltDirection(){
        int[] temp = edgeCheckReturn();
        return temp[rand.nextInt(temp.length)];
    }
    //Returns an array of the possible directions for which a maze maker could travel/create new paths, accounting for neighbors not yet on the traditional path
    public int[] alterablePathsIntCleanUp(){
        numMoveCheck();
        boolean[] ableToChange = new boolean[4];

        for (int i = 0; i < 4; i++)
            ableToChange[i] = !canMove[i];

        edgeCheck(ableToChange);
        neighborCheckCleanUp(ableToChange);

        int pathNum = 0;
        for (boolean t : ableToChange)
            if (t)
                pathNum++;

        int[] alterable = new int[pathNum];

        int count = 0;
        for(int i = 0; i < ableToChange.length; i++)
            if (ableToChange[i])
                alterable[count++] = i;

        return alterable;
    }
    //Checks to see if the neighbors exist, then whether they're on the path
    void neighborCheck(boolean[] t){
        for (int i = 0; i < t.length; i++){
            if (t[i]){
                if (neighbors[i] != null) {
                    if (neighbors[i].getPath())
                        t[i] = false;
                }
                else
                    t[i] = false;
            }
        }
    }
    //more gets
    int getX(){return xCord;}
    int getY(){return yCord;}
    //if any neighbor is on the path it will return true
    boolean neighborOnPath(){
        for (Room n : neighbors){
            if (n != null)
                if (n.getPath())
                    return true;
        }
        return false;
    }
    //If any neighbor is on the path, then it will randomly choose a direction to get to a room with a path
    int neighborToPath() {
        int count = 0;
        for (Room n : neighbors){
            if (n != null)
                if (n.getPath())
                    count++;
        }
        if (count == 0) {
            return -1;
        }
        int[] pathChoice = new int[count];
        int q = 0;
        for (int i = 0; i < 4; i++){
            if (neighbors[i] != null)
                if (neighbors[i].getPath())
                    pathChoice[q++] = i;
        }
        return pathChoice[rand.nextInt(count)];
    }
    //Checks if unconnected rooms neighboring this room have already been visited by the path maker
    void neighborCheckCleanUp(boolean[] t){
        for (int i = 0; i < t.length; i++){
            if (t[i]){
                if (neighbors[i] != null) {
                    if (neighbors[i].getNewPath() && !neighbors[i].getPath())
                        t[i] = false;
                }
                else
                    t[i] = false;
            }
        }
    }
    //For the readability of the user
    public String toString(){
        if (isExit)
            return "F";
        else if (xCord == 0 && yCord == 0)
            return "S";
        else if (onSolvePath)
            return "*";
        else if (onPath)
            return " ";
        else if (onNewPath)
            return "N";
        else {
            return " ";
            //return "" + xCord % 10; //Debug
        }
    }
    public boolean[] getMove() { return canMove; }
    //Returns the legal moves that have been determined for the sake of solving
    public int[] getDirections() {
        int[] temp = new int[numMove];
        int count = 0;
        for (int i =0; i < canMove.length; i++){
            if (canMove[i])
                temp[count++] = i;
        }
        return temp;
    }
    //Gets and stuff
    void setOnSolvePath() {onSolvePath = true;}
    int[] getCoord(){
        return new int[] {xCord, yCord};
    }
    void setMaxes(int xRoom, int yRoom){
        X_ROOM = xRoom;
        Y_ROOM = yRoom;
    }
    boolean getPath() {return onPath;}
    boolean getNewPath() {return onNewPath;}
    public boolean getMove(int direction) { return canMove[direction]; }
}
