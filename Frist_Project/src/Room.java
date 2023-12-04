public class Room {
    final int X_ROOM = MazeMaker.X_ROOMS;
    final int Y_ROOM = MazeMaker.Y_ROOMS;
    int xCord;
    int yCord;
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
        if (xCord == 0)
            canMove[E] = false;
        else if (xCord == X_ROOM -1)
            canMove[W] = false;
        if (yCord == 0)
            canMove[N] = false;
        if (yCord == Y_ROOM -1)
            canMove[S] = false;
    }
    void changeMove(boolean[] t){
        canMove = t;
        edgeCheck();
    }
    void changeMove(boolean t, int direction){
        canMove[direction] = t;
        edgeCheck();
    }
    public boolean[] getMove() { return canMove; }
}
