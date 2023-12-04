public class MazeMaker {
    public static final int X_ROOMS = 4;
    public static final int Y_ROOMS = 4;
    private static Room[][] rooms = new Room[X_ROOMS][Y_ROOMS];
    public static final int N = 0, E = 1, S = 2, W = 3;
    void setUpRooms(){
        for(int i =0; i < X_ROOMS; i++){
            for (int o = 0; o < Y_ROOMS; o++){
                rooms[i][o].setCord(i, o);
            }
        }
    }
    public void alterSurrounding(boolean[] t, int x, int y)
    {
        boolean[] temp = rooms[x][y].getMove();
        if (x != 0)
            rooms[x - 1][y].changeMove(temp[W], E);
        if (x < X_ROOMS)
            rooms[x + 1][y].changeMove(temp[E], W);
        if (y != 0)
            rooms[x][y - 1].changeMove(temp[S], N);
        if (y < Y_ROOMS)
            rooms[x][y + 1].changeMove(temp[N], S);
    }
    public void alterSurrounding(boolean t, int direction, int x, int y){
        if (x != 0 && direction == W)
            rooms[x - 1][y].changeMove(t, E);
        if (x < X_ROOMS && direction == E)
            rooms[x + 1][y].changeMove(t, W);
        if (y != 0 && direction == S)
            rooms[x][y - 1].changeMove(t, N);
        if (y < Y_ROOMS && direction == N)
            rooms[x][y + 1].changeMove(t, S);
    }
    public void changeMove(boolean t, int direction, int x, int y){
        rooms[x][y].changeMove(t, direction);
        alterSurrounding(t, direction, x, y);
    }
    public void changeMove(boolean[] t, int x, int y){
        rooms[x][y].changeMove(t);
        alterSurrounding(t, x, y);
    }
    public Room[][] getMaze(){
        return rooms;
    }
    MazeMaker(){
        setUpRooms();
    }
}