public class Room {
    final int X_ROOM = MazeMaker.X_ROOMS;
    final int Y_ROOM = MazeMaker.Y_ROOMS;
    int numMove = 4;
    int xCord;
    int yCord;
    boolean onPath, canPath, isExit;
    public static final int N = 0, E = 1, S = 2, W = 3;

    // select * from user_table where firstname = "mike"; drop user_table; "x";


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
        numMove = 4;
        if (xCord == 0) {
            canMove[W] = false;
            numMove--;
        }
        if (xCord == X_ROOM -1) {
            canMove[E] = false;
            numMove--;
        }
        if (yCord == 0) {
            canMove[N] = false;
            numMove--;
        }
        if (yCord == Y_ROOM -1) {
            canMove[S] = false;
            numMove--;
        }
    }
    boolean[] alterablePaths(){
        boolean[] directions = new boolean[] {true, true, true, true};
        numMove = 4;
        if (xCord == 0) {
            directions[W] = false;
            numMove--;
        }
        if (xCord == X_ROOM -1) {
            directions[E] = false;
            numMove--;
        }
        if (yCord == 0) {
            directions[N] = false;
            numMove--;
        }
        if (yCord == Y_ROOM -1) {
            directions[S] = false;
            numMove--;
        }
        for (int i = 0; i < directions.length; i++)
            if (canMove[i])
                directions[i] = false;
        return directions;
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
        canPath = false;
        isExit = false;
    }
    public int numMove(){
        int num = 0;
        for (boolean t : canMove)
            if (t)
                num++;
        return num;
    }
    public int[] alterablePathsInt(){
        boolean[] temp = alterablePaths();
        int trueNum = 0;
        for (boolean t : temp)
            if (t)
                trueNum++;
        int[] directions = new int[trueNum];
        int count = 0;
        for (int i = 0; i < temp.length; i++){
            if (temp[i]) {
                directions[count] = i;
                count++;
            }
        }
        return directions;
    }
    public String toString(){
        if (isExit)
            return "F";
        else if (xCord == 0 && yCord == 0)
            return "S";
        else
            return "R";
    }
    public boolean[] getMove() { return canMove; }
    public int[] getDirections() {
        int[] temp = new int[numMove];
        int count = 0;
        for (int i =0; i < canMove.length; i++){
            if (canMove[i])
                temp[count] = i;
        }
        return temp;
    }
    public boolean getMove(int direction) { return canMove[direction]; }
}
