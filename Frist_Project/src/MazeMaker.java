import java.util.*;
//This class makes a maze and can print it too
public class MazeMaker {
    //Default dimensions for the maze, they are changed by the user later
    public static int X_ROOMS = 9;
    public static int Y_ROOMS = 9;
    public static final int[] INVERSE = { 2, 3, 0, 1 };
    public static final char[] DIRECTIONS = {'N', 'E', 'S', 'W'};
    private static final Random rand = new Random();
    //The maze as a 2D array of rooms
    private static Room[][] rooms = new Room[X_ROOMS][Y_ROOMS];
    //For the sake of my poor brain
    public static final int N = 0, E = 1, S = 2, W = 3;
    //Used for printing the maze
    public static final char block = 0x2588;

    boolean generatedMaze = false;

    //Gives the rooms the information they need to know about themselves
    void setUpRooms(){
        generatedMaze = false;
        for(int i =0; i < X_ROOMS; i++){
            for (int o = 0; o < Y_ROOMS; o++){
                rooms[i][o] = new Room();
                rooms[i][o].setMaxes(X_ROOMS, Y_ROOMS);
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
        rooms[x][y].changeMove(direction, true);
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
    //Takes a coordinate (x, y) and modifies the value based upon the direction given
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
    //Populates the maze with paths
    public Room[][] randDepthFirstSearch(){
        if (generatedMaze)
            setUpRooms();
        cornerExit();
        int[] coord = new int[] {rand.nextInt(X_ROOMS), rand.nextInt(Y_ROOMS)}; //I used to have it be x and y, but for code simplicity it is now coord[0] coord[1]
        ArrayList<Room> unConnected = new ArrayList<>();
        for (int i = 0; i < X_ROOMS; i++)
            unConnected.addAll(Arrays.asList(rooms[i]).subList(0, Y_ROOMS));

        ArrayList<Room> currentRooms = new ArrayList<>();
        //Makes the initial path to exit
        while (!unConnected.isEmpty()) {
            //System.out.println(coord[0] + " " + coord[1]);
            rooms[coord[0]][coord[1]].onPath();
            unConnected.remove(rooms[coord[0]][coord[1]]);
            int[] direction = rooms[coord[0]][coord[1]].alterablePathsInt(); //Gets the possible directions of path making
            int moveTo = 0;
            while (true) {
                int[] tempCoord = coord.clone();
                //If the given room does not have a path to create, it will backtrack
                if (direction.length < 1) {
                    System.out.println(currentRooms.size());
                    coord = currentRooms.remove(currentRooms.size()-1).getCoord();
                    break;
                }
                //If the given room CAN create a path, then this is how it works
                else {
                    moveTo = direction[rand.nextInt(direction.length)];

                    alterCoord(tempCoord, moveTo);

                    if (tempCoord[1] < 0 || tempCoord[1] >= Y_ROOMS || tempCoord[0] < 0 || tempCoord[0] >= X_ROOMS) {
                        continue;
                    }
                    rooms[coord[0]][coord[1]].changeMove(moveTo, true);

                    alterSurrounding(rooms[coord[0]][coord[1]]);
                    currentRooms.add(rooms[coord[0]][coord[1]]);

                    coord = tempCoord;
                    break;
                }
            }
        }
        generatedMaze = true;
        return rooms;
    }
    public Room[][] wilsonMaze(){
        if (generatedMaze)
            setUpRooms();
        cornerExit();
        int[] coord = new int[] {rand.nextInt(X_ROOMS), rand.nextInt(Y_ROOMS)}; //I used to have it be x and y, but for code simplicity it is now coord[0] coord[1]
        ArrayList<Room> unConnected = new ArrayList<>();
        for (int i = 0; i < X_ROOMS; i++)
            unConnected.addAll(Arrays.asList(rooms[i]).subList(0, Y_ROOMS));

        Room first = rooms[coord[0]][coord[1]];
        first.onPath();
        unConnected.remove(first);
        while (!unConnected.isEmpty()) {
            ArrayList<Room> currentRooms = new ArrayList<>();
            Room current = unConnected.get(rand.nextInt(unConnected.size()));
            coord = current.getCoord();
            while (!current.getPath()) {
                if (current.getNewPath()){
                    //printMaze();
                    final int snipTo =  currentRooms.indexOf(current) + 1;
                    for (int i = currentRooms.size() - 1; snipTo < i;) {
                        i = currentRooms.size() - 1;
                        if (i >= 0) {
                            currentRooms.get(i).roomWipe();
                            currentRooms.remove(i);
                        }
                    }
                    if (currentRooms.size() > 0) {
                        //alterSurrounding(currentRooms.get(currentRooms.size() - 1));
                        currentRooms.get(currentRooms.size() - 1).roomWipe();
                        currentRooms.get(currentRooms.size() - 1).onNewPath();
                        if  (currentRooms.size() > 2)
                            alterSurrounding(currentRooms.get(currentRooms.size() - 2));
                    }
                    //System.out.println("\n\n\n\n\n");
                    //printMaze();
                }
                currentRooms.add(current);
                current.onNewPath();

                int[] directions = current.edgeCheckReturn();
                int dir = directions[rand.nextInt(directions.length)];
                alterSurrounding(current, dir);
                alterCoord(coord, dir);
                current = rooms[coord[0]][coord[1]];
            }
            //System.out.println("Connected!");
            //printMaze();
            for (Room r : currentRooms)
                r.onPath();
            unConnected.removeIf(Room::getPath);
        }
        generatedMaze = true;
        return rooms;
    }

    //Prints the maze to the screen
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
    //Checks the current room and down neighbor path of a room to determine what is printed
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
    //Checks the current room and left neighbor path of a room to determine what is printed
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
    //Constructor
    MazeMaker(int x, int y){
        X_ROOMS = x;
        Y_ROOMS = y;
        rooms = new Room[X_ROOMS][Y_ROOMS];
        setUpRooms();
    }
    MazeMaker(){
        rooms = new Room[X_ROOMS][Y_ROOMS];
        setUpRooms();
    }
}