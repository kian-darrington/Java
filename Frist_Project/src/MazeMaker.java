import java.util.*;

public class MazeMaker {
    //Dimensions for the maze
    public static int X_ROOMS = 9;
    public static int Y_ROOMS = 9;
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
        for (int i = 0; i < X_ROOMS; i++){
            for (int o = 0; o < Y_ROOMS; o++){
                Room[] neighbors = new Room[4];
                if (o != 0)
                    neighbors[N] = rooms[i][o - 1];
                else
                    neighbors[N] = null;
                if (i < X_ROOMS - 1)
                    neighbors[E] = rooms[i + 1][o];
                else
                    neighbors[E] = null;
                if (o < Y_ROOMS - 1)
                    neighbors[S] = rooms[i][o + 1];
                else
                    neighbors[S] = null;
                if ( i != 0)
                    neighbors[W] = rooms[i - 1][o];
                else
                    neighbors[W] = null;
                rooms[i][o].assignNeighbors(neighbors);
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
            rooms[x][y - 1].changeMove(S, temp[N]);
        if (y < Y_ROOMS - 1)
            rooms[x][y + 1].changeMove(N, temp[S]);
    }
    void alterSurrounding(Room room, int direction){
        int x = room.xCord, y = room.yCord;
        boolean[] temp = rooms[x][y].getMove();
        if (x != 0 && direction == W)
            rooms[x - 1][y].changeMove(E, temp[W]);
        if (x < X_ROOMS - 1 && direction == E)
            rooms[x + 1][y].changeMove(W, temp[E]);
        if (y < Y_ROOMS - 1 && direction == S)
            rooms[x][y + 1].changeMove(N, temp[S]);
        if (y != 0 && direction == N)
            rooms[x][y - 1].changeMove(S, temp[N]);
    }

    void changeMove(Room room, int direction){
        boolean t = room.getMove()[direction];
        rooms[room.xCord][room.yCord].changeMove(direction, t);
        alterSurrounding(room, direction);
    }

    void changeMove(Room room){
        alterSurrounding(room);
    }

    public Room[][] createMaze(){
        makePath();
        return rooms;
    }
    void cornerExit(){
        rooms[X_ROOMS-1][Y_ROOMS-1].setExit(true);
    }
    void randSetExit(){
        int exitCount = 0;
        do {
            exitCount = 0;
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
                    rooms[0][exit - (X_ROOMS - 2)].setExit(true);
            }
            //Error check for multiple exits and if the Start is the exit
            for (Room[] r : rooms){
                for (Room room : r){
                    if (room.getExit()){
                        if (room.xCord !=0 || room.yCord != 0) {
                            if (exitCount > 0)
                                room.setExit(false);
                            exitCount++;
                        }
                        else {
                            room.setExit(false);
                        }
                    }
                }
            }
        } while (exitCount < 1);
    }
    void makePath(){
        cornerExit();
        int x = 0, y = 0;
        while (!rooms[x][y].getExit()) {
            rooms[x][y].onPath();
            int[] direction = rooms[x][y].alterablePathsInt();
            int moveTo = 0;
            while (true) {
                int tempX = x, tempY = y;
                if (direction.length < 1) {
                    int[] possible = rooms[x][y].getDirections();
                    moveTo = possible[rand.nextInt(possible.length)];
                    switch (moveTo) {
                        case N:
                            tempY--;
                            break;
                        case E:
                            tempX++;
                            break;
                        case S:
                            tempY++;
                            break;
                        case W:
                            tempX--;
                    }
                    if (tempY < 0 || tempY >= Y_ROOMS)
                        continue;
                    if (tempX < 0 || tempX >= X_ROOMS)
                        continue;
                    x = tempX;
                    y = tempY;
                    break;
                }
                else {
                    moveTo = direction[rand.nextInt(direction.length)];
                    switch (moveTo) {
                        case N:
                            tempY--;
                            break;
                        case E:
                            tempX++;
                            break;
                        case S:
                            tempY++;
                            break;
                        case W:
                            tempX--;
                    }
                    if (tempY < 0 || tempY >= Y_ROOMS || tempX < 0 || tempX >= X_ROOMS) {
                        continue;
                    }
                    rooms[x][y].changeMove(moveTo, true);
                    alterSurrounding(rooms[x][y]);
                    x = tempX;
                    y = tempY;
                    break;
                }
            }
        }
        // This checks all the rooms to see if they are connected to the path
        for (int i = 0; i < X_ROOMS; i++){
            for (int o = 0; o < Y_ROOMS; o++){
                if (!rooms[i][o].getPath()){
                    x = i;
                    y = o;
                    ArrayList<Integer> Xs = new ArrayList<>();
                    ArrayList<Integer> Ys = new ArrayList<>();
                    while (!rooms[x][y].getPath()) {
                        Xs.add(x);
                        Ys.add(y);
                        rooms[x][y].onNewPath();
                        int[] direction = rooms[x][y].alterablePathsIntCleanUp();
                        int moveTo = 0;
                        while (true) {
                            int tempX = x, tempY = y;
                            if (direction.length < 1) {
                                int[] possible = rooms[x][y].getDirections();
                                moveTo = possible[rand.nextInt(possible.length)];
                                switch (moveTo) {
                                    case N:
                                        tempY--;
                                        break;
                                    case E:
                                        tempX++;
                                        break;
                                    case S:
                                        tempY++;
                                        break;
                                    case W:
                                        tempX--;
                                }
                                if (tempY < 0 || tempY >= Y_ROOMS)
                                    continue;
                                if (tempX < 0 || tempX >= X_ROOMS)
                                    continue;
                                x = tempX;
                                y = tempY;
                                break;
                            } else {
                                moveTo = direction[rand.nextInt(direction.length)];
                                switch (moveTo) {
                                    case N:
                                        tempY--;
                                        break;
                                    case E:
                                        tempX++;
                                        break;
                                    case S:
                                        tempY++;
                                        break;
                                    case W:
                                        tempX--;
                                }
                                if (tempY < 0 || tempY >= Y_ROOMS || tempX < 0 || tempX >= X_ROOMS) {
                                    continue;
                                }
                                if (rooms[tempX][tempY].getPath())
                                    rooms[x][y].onPath();
                                rooms[x][y].changeMove(moveTo, true);
                                alterSurrounding(rooms[x][y]);
                                x = tempX;
                                y = tempY;
                                break;
                            }
                        }
                    }
                    for (int j = 0; j < Xs.size(); j++){
                        rooms[Xs.get(j)][Ys.get(j)].onPath();
                    }
                }
            }
        }
    }
    public void printMaze(){
        for (int i = 0; i < Y_ROOMS * 2 + 1; i++){
            for (int o = 0; o < X_ROOMS * 2 + 1; o++){
                if (o % 2 == 0 && i % 2 == 0)
                    System.out.print(block);
                else if (o % 2 == 1 && i % 2 == 1)
                    System.out.print(rooms[o / 2][i / 2]);
                else{
                    if (i % 2 == 0)
                        System.out.print(upDownCheck(o /2, i / 2));
                    else {
                        System.out.print(sideSideCheck(o /2, i / 2));
                    }
                }
            }
            System.out.println();
        }
        /*for (int i = 0; i < Y_ROOMS; i++){
            for (int o = 0; o < X_ROOMS; o++) {
                System.out.print(o + " " + i + " ");
                System.out.println(Arrays.toString(rooms[o][i].canMove));
            }
        }*/ //for Debug
    }
    
    char upDownCheck(int x, int y){
        if (y == 0)
            return block;
        else if (y == Y_ROOMS)
            return block;
        else if (y == Y_ROOMS - 1){
            if (rooms[x][y].getMove(N))
                return ' ';
            else
                return block;
        }
        else if (rooms[x][y].getMove(N) && rooms[x][y - 1].getMove(S))
            return ' ';
        else
            return block;
    }
    char sideSideCheck(int x, int y){
        if (x == 0)
            return block;
        else if (x == X_ROOMS)
            return block;
        else if (x == X_ROOMS - 1) {
            if (rooms[x][y].getMove(W))
                return ' ';
            else
                return block;
        }
        else if (rooms[x][y].getMove(W) && rooms[x - 1][y].getMove(E))
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