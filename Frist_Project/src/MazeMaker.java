import java.util.Random;

public class MazeMaker {
    //Dimensions for the maze
    public static int X_ROOMS = 4;
    public static int Y_ROOMS = 4;
    private static final Random rand = new Random();
    //The maze as a 2D array of rooms
    private static Room[][] rooms = null;
    //For the sake of my poor brain
    public static final int N = 0, E = 1, S = 2, W = 3;

    //Gives the rooms the information they need to know about themselves
    void setUpRooms(){
        for(int i =0; i < X_ROOMS; i++){
            for (int o = 0; o < Y_ROOMS; o++){
                rooms[i][o].setCord(i, o);
                rooms[i][o].reset();
            }
        }
    }

    //Changes all the rooms around the room sent according to the accessibility of the path
    void alterSurrounding(Room room)
    {
        int x = room.xCord, y = room.yCord;
        boolean[] temp = rooms[x][y].getMove();
        if (x != 0)
            rooms[x - 1][y].changeMove(E, temp[W]);
        if (x < X_ROOMS - 1)
            rooms[x + 1][y].changeMove(W, temp[E]);
        if (y != 0)
            rooms[x][y - 1].changeMove(N, temp[S]);
        if (y < Y_ROOMS - 1)
            rooms[x][y + 1].changeMove(S, temp[N]);
    }
    void alterSurrounding(Room room, int direction){
        int x = room.xCord, y = room.yCord;
        boolean[] temp = rooms[x][y].getMove();
        if (x != 0 && direction == W)
            rooms[x - 1][y].changeMove(E, temp[W]);
        if (x < X_ROOMS - 1 && direction == E)
            rooms[x + 1][y].changeMove(W, temp[E]);
        if (y != 0 && direction == S)
            rooms[x][y - 1].changeMove(N, temp[S]);
        if (y < Y_ROOMS - 1 && direction == N)
            rooms[x][y + 1].changeMove(S, temp[N]);
    }

    void changeMove(Room room, int direction){
        boolean t = room.getMove()[direction];
        rooms[room.xCord][room.yCord].changeMove(direction, t);
        alterSurrounding(room, direction);
    }

    void changeMove(Room room){
        rooms[room.xCord][room.yCord].changeMove(room.getMove());
        alterSurrounding(room);
    }

    public Room[][] createMaze(){
        makePath();
        return rooms;
    }

    void makePath(){
        int exit = 0;
        do {
            exit = rand.nextInt(X_ROOMS * 2 + ((Y_ROOMS - 2) * 2));
        } while (exit == 0);
        if (exit < X_ROOMS)
            rooms[exit][0].setExit(true);
        else if (exit < X_ROOMS + Y_ROOMS - 2)
            rooms[X_ROOMS - 1][Y_ROOMS].setExit(true);
    }

    public Room[][] getMaze(){
        return rooms;
    }

    MazeMaker(int x, int y){
        X_ROOMS = x;
        Y_ROOMS = y;
        rooms = new Room[X_ROOMS][Y_ROOMS];
        setUpRooms();
    }
}