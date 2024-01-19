import java.util.*;
//This class makes a maze and can print it too
public class MazeMaker {
    //Default dimensions for the maze, they are changed by the user later
    public static int X_ROOMS = 9;
    public static int Y_ROOMS = 9;
    private static final Random rand = new Random();
    //The maze as a 2D array of rooms
    private static Room[][] rooms = new Room[X_ROOMS][Y_ROOMS];
    //For the sake of my poor brain
    public static final int N = 0, E = 1, S = 2, W = 3;
    //Used for printing the maze
    public static final char block = 0x2588;

    //Gives the rooms the information they need to know about themselves
    void setUpRooms(){
        for(int i =0; i < X_ROOMS; i++){
            for (int o = 0; o < Y_ROOMS; o++){
                rooms[i][o] = new Room();
                rooms[i][o].setCord(i, o);
            }
        }
        //Gives the rooms their neighbors for reference in making the maze
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

    //Changes all the rooms around the room given according to the accessibility of the path
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
    //Takes the empty unpaved maze and makes a path and makes all rooms accessible to all other rooms (no islands cut off)
    public Room[][] createMaze(){
        makePath();
        return rooms;
    }
    //Sets the location of the exit to the bottom right corner
    void cornerExit(){
        rooms[X_ROOMS-1][Y_ROOMS-1].setExit(true);
    }
    //Sets the location of the exit to a random point on the perimeter of the maze
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
    void alterCoord(int[] tempCoord, int moveTo){
        switch (moveTo) {
            case N:
                tempCoord[1]--;
                break;
            case E:
                tempCoord[0]++;
                break;
            case S:
                tempCoord[1]++;
                break;
            case W:
                tempCoord[0]--;
                break;
        }
    }
    void makePath(){
        cornerExit();
        int[] coord = new int[2];
        while (!rooms[coord[0]][coord[1]].getExit()) {
            rooms[coord[0]][coord[1]].onPath();
            int[] direction = rooms[coord[0]][coord[1]].alterablePathsInt();
            int moveTo = 0;
            while (true) {
                int[] tempCoord = coord.clone();
                if (direction.length < 1) {
                    int[] possible = rooms[coord[0]][coord[1]].getDirections();
                    moveTo = possible[rand.nextInt(possible.length)];

                    alterCoord(tempCoord, moveTo);

                    if (tempCoord[1] < 0 || tempCoord[1] >= Y_ROOMS || tempCoord[0] < 0 || tempCoord[0] >= X_ROOMS) {
                        continue;
                    }

                    coord = tempCoord;
                    break;
                }
                else {
                    moveTo = direction[rand.nextInt(direction.length)];

                    alterCoord(tempCoord, moveTo);

                    if (tempCoord[1] < 0 || tempCoord[1] >= Y_ROOMS || tempCoord[0] < 0 || tempCoord[0] >= X_ROOMS) {
                        continue;
                    }
                    rooms[coord[0]][coord[1]].changeMove(moveTo, true);

                    alterSurrounding(rooms[coord[0]][coord[1]]);

                    coord = tempCoord;
                    break;
                }
            }
        }
        ArrayList<Room> unConnected = new ArrayList<>();
        // This checks all the rooms to see if they are connected to the path
        for (int i = 0; i < X_ROOMS; i++){
            for (int o = 0; o < Y_ROOMS; o++) {
                if (!rooms[i][o].getPath()) {
                    unConnected.add(rooms[i][o]);
                }
            }
        }
        while (!unConnected.isEmpty()) {
            int num = rand.nextInt(unConnected.size());
            if (!unConnected.get(num).getPath()) {
                coord[0] = unConnected.get(num).getX();
                coord[1] = unConnected.get(num).getY();
                ArrayList<Integer> Xs = new ArrayList<>();
                ArrayList<Integer> Ys = new ArrayList<>();
                while (!rooms[coord[0]][coord[1]].getPath()) {
                    Xs.add(coord[0]);
                    Ys.add(coord[1]);
                    rooms[coord[0]][coord[1]].onNewPath();
                    int[] direction = rooms[coord[0]][coord[1]].alterablePathsIntCleanUp();
                    int moveTo = 0;
                    while (true) {
                        int[] tempCoord = coord.clone();
                        if (direction.length < 1) {
                            int[] possible = rooms[coord[0]][coord[1]].getDirections();
                            moveTo = possible[rand.nextInt(possible.length)];
                            alterCoord(tempCoord, moveTo);
                            if (tempCoord[1] < 0 || tempCoord[1] >= Y_ROOMS || tempCoord[0] < 0 || tempCoord[0] >= X_ROOMS) {
                                continue;
                            }
                            coord = tempCoord;
                            break;
                        } else {
                            if (!rooms[coord[0]][coord[1]].neighborOnPath())
                                moveTo = direction[rand.nextInt(direction.length)];
                            else
                                moveTo = rooms[coord[0]][coord[1]].neighborToPath();
                            alterCoord(tempCoord, moveTo);
                            if (tempCoord[1] < 0 || tempCoord[1] >= Y_ROOMS || tempCoord[0] < 0 || tempCoord[0] >= X_ROOMS) {
                                continue;
                            }
                            rooms[coord[0]][coord[1]].changeMove(moveTo, true);
                            alterSurrounding(rooms[coord[0]][coord[1]]);
                            coord = tempCoord;
                            break;
                        }
                    }
                }
                for (int j = 0; j < Xs.size(); j++) {
                    rooms[Xs.get(j)][Ys.get(j)].onPath();
                    unConnected.remove(rooms[Xs.get(j)][Ys.get(j)]);
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