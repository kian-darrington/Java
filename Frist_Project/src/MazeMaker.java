import java.util.Random;

public class MazeMaker {
    //Dimensions for the maze
    public static int X_ROOMS = 4;
    public static int Y_ROOMS = 4;
    private static final Random rand = new Random();
    //The maze as a 2D array of rooms
    private static Room[][] rooms = new Room[X_ROOMS][Y_ROOMS];
    //For the sake of my poor brain
    public static final int N = 0, E = 1, S = 2, W = 3;
    public static final char block = 0x2588;

    //Gives the rooms the information they need to know about themselves
    void setUpRooms(){
        for(int i =0; i < X_ROOMS; i++){
            for (int o = 0; o < Y_ROOMS; o++){
                rooms[i][o] = new Room();
                rooms[i][o].setCord(i, o);
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
    void randSetExit(){
        int exit = 0;
        while (exit == 0)
            exit = rand.nextInt(X_ROOMS * 2 + ((Y_ROOMS - 2) * 2));
        if (exit < X_ROOMS)
            rooms[exit][0].setExit(true);
        else if (exit < X_ROOMS + Y_ROOMS - 1)
            rooms[X_ROOMS - 1][1 + exit - X_ROOMS].setExit(true);
        else {
            exit -= X_ROOMS + Y_ROOMS - 2;
            if (exit < X_ROOMS - 2)
                rooms[exit][Y_ROOMS - 1].setExit(true);
            else
                rooms[0][exit - (X_ROOMS - 3)].setExit(true);
        }
    }
    void makePath(){
        randSetExit();
        for (int i = 0; i < rooms.length; i++){
            for (int o = 0; o < rooms[i].length; o++){
                rooms[i][o].changeMove(new boolean[] {true, true, true, true});
            }
        }
    }
    public void printMaze(){
        for (int i = 0; i < Y_ROOMS * 2 + 1; i++){
            for (int o = 0; o < X_ROOMS * 2 + 1; o++){
                if (!(o % 2 != 0 || i % 2 != 0))
                    System.out.print(block);
                else if (o % 2 == 1 && i % 2 == 1)
                    System.out.print(rooms[o / 2][i / 2]);
                else{
                    if (i % 2 == 0)
                        System.out.print(upDownCheck(o /2, i / 2));
                    else if (o % 2 == 0){
                        System.out.print(sideSideCheck(o /2, i / 2));
                    }
                }
            }
            System.out.println();
        }
    }
    char upDownCheck(int x, int y){
        if (y == 0)
            return block;
        else if (y == Y_ROOMS || y == Y_ROOMS - 1)
            return block;
        else if (!(!rooms[x][y].getMove(N) || !rooms[x][y + 1].getMove(S)))
            return ' ';
        else
            return block;
    }
    char sideSideCheck(int x, int y){
        if (x == X_ROOMS)
            return block;
        else if (x == X_ROOMS - 1) {
            if (rooms[x][y].getMove(W))
                return ' ';
            else
                return block;
        }
        else if (!(!rooms[x][y].getMove(E) || !rooms[x + 1][y].getMove(W)))
            return ' ';
        else
            return block;
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