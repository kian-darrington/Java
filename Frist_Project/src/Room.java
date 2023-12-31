public class Room {
    final int X_ROOM = MazeMaker.X_ROOMS;
    final int Y_ROOM = MazeMaker.Y_ROOMS;
    int numMove = 4, timesSeen = 0;
    int xCord;
    int yCord;
    boolean onPath, isExit;
    public static final int N = 0, E = 1, S = 2, W = 3;

    Room[] neighbors = new Room[4];

    // select * from user_table where firstname = "mike"; drop user_table; "x";
    void onPath(){
        onPath = true;
    }
    void assignNeighbors(Room[] r){
        neighbors = r;
    }
    int getTimesSeen(){return timesSeen;}
    //True or false for N, E, S, W
    boolean[] canMove = new boolean[4];
    Room(){ reset(); }
    Room(int x, int y, boolean[] t)
    {
        xCord = x;
        yCord = y;
        canMove = t;
    }
    public void setCord(int x, int y){
        xCord = x;
        yCord = y;
    }
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
        if (yCord == Y_ROOM -1)
            t[S] = false;
    }
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
    void changeMove(int direction, boolean t){
        canMove[direction] = t;
        edgeCheck();
    }
    void setExit(boolean t) { isExit = t; }
    boolean getExit(){return isExit;}
    void reset(){
        canMove = new boolean[] {false, false, false, false};
        onPath = false;
        isExit = false;
    }
    public int numMove(){ return numMove; }
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
    public String toString(){
        if (isExit)
            return "F";
        else if (xCord == 0 && yCord == 0)
            return "S";
        else
            return " ";
            //return "" + xCord % 10;
    }
    public boolean[] getMove() { return canMove; }
    public int[] getDirections() {
        int[] temp = new int[numMove];
        int count = 0;
        for (int i =0; i < canMove.length; i++){
            if (canMove[i])
                temp[count++] = i;
        }
        return temp;
    }
    boolean getPath() {return onPath;}
    public boolean getMove(int direction) { return canMove[direction]; }
}
