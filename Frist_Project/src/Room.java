public class Room {
    final int X_ROOM = MazeMaker.X_ROOMS;
    final int Y_ROOM = MazeMaker.Y_ROOMS;
    int numMove = 4;
    int xCord;
    int yCord;
    boolean onPath, canPath, isExit;
    public static final int N = 0, E = 1, S = 2, W = 3;
    boolean[] canMove = new boolean[4];
    //True or false for N, E, S, W
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
        if (xCord == 0) {
            canMove[E] = false;
            numMove--;
        }
        else if (xCord == X_ROOM -1) {
            canMove[W] = false;
            numMove--;
        }
        if (yCord == 0) {
            canMove[N] = false;
            numMove--;
        }
        else if (yCord == Y_ROOM -1) {
            canMove[S] = false;
            numMove--;
        }
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
    public boolean[] getMove() { return canMove; }
}
