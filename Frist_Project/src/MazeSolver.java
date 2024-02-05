import java.util.ArrayList;
import java.util.Random;

//This class takes an inputted maze (a Room[][] or Maze Class) and solves it, detailing solve path rooms with asterisks
public class MazeSolver{
    static final int N = 0, E = 1, S = 2, W = 3;
    int[] inverse = new int[] {S, W, N, E};
    private static final Random rand = new Random();
    Room[][] rooms;
    //Constructors
    MazeSolver(Room[][] r) {
        rooms = r;
    }
    MazeSolver(Maze maze) {rooms = maze.getRooms();}
    void setMaze (Room[][] r) {
        rooms = r;
    }
    //Initializes the recursive solver
    public void solveMaze(){
        solveMaze(new int[] {0,0}, S);
    }
    //Initializes the recursive distance assigner
    public void distanceAssigner() { distanceAssigner(new int[] {0, 0}, S, 0); }
    //Output of maze
    Room[][] getMaze(){return rooms;}
    //Alters coordinates for a less brain break in code
    int[] alterCoord(int[] tempCoord, int moveTo){
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
            default:
                throw new IllegalStateException("Unexpected value: " + moveTo);
        }
        return tempCoord;
    }
    //Creates a solve path by making an element of the Room class true
    //Solves the Maze through recursion directed by randomness
    private boolean solveMaze(int[] coord, int directionFrom){
        Room room = rooms[coord[0]][coord[1]];
        //System.out.println(coord[0] + " " + coord[1]);

        //Default true statement
        if (room.getExit()) {
            room.setOnSolvePath();
            System.out.println("Found the Exit!");
            return true;
        }

        boolean[] availablePaths = room.getMove().clone();

        //Stops from going backward
        if (availablePaths[inverse[directionFrom]])
            availablePaths[inverse[directionFrom]] = false;
        int pathsAvailable = 0;
        //Gets the number of possible moves
        for (boolean b : availablePaths)
            if (b)
                pathsAvailable++;
        //Dead end
        if (pathsAvailable == 0)
            return false;
        int[] direction = new int[pathsAvailable];
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < availablePaths.length; i++)
            if (availablePaths[i])
                temp.add(i);

        for (int i = 0; i < pathsAvailable; i++) {
            direction[i] = temp.remove(rand.nextInt(temp.size()));
        }
        for (int i = 0; i < pathsAvailable; i++){
            int[] newCoord = alterCoord(coord.clone(), direction[i]);
            if (solveMaze(newCoord, direction[i])) {
                room.setOnSolvePath();
                return true;
            }
        }
        return false;
    }
    //Uses a near identical method to the solver, but it goes through every room available in the maze
    //This tells the rooms how far they are from the start. This data is used in the color projection of the maze
    private void distanceAssigner(int[] coord, int directionFrom, int distance){
        Room room = rooms[coord[0]][coord[1]];
        //System.out.println(coord[0] + " " + coord[1]);
        room.setDistance(distance);
        boolean[] availablePaths = room.getMove().clone();

        //Stops from going backward
        if (availablePaths[inverse[directionFrom]])
            availablePaths[inverse[directionFrom]] = false;
        int pathsAvailable = 0;
        //Gets the number of possible moves
        for (boolean b : availablePaths)
            if (b)
                pathsAvailable++;
        //Dead end
        if (pathsAvailable == 0)
            return;

        int[] direction = new int[pathsAvailable];
        int count = 0;
        for (int i = 0; i < availablePaths.length; i++)
            if (availablePaths[i])
                direction[count++] = i;

        //Moves to the next room, biased in the order of N, E, S, then W
        for (int i = 0; i < pathsAvailable; i++){
            int[] newCoord = alterCoord(coord.clone(), direction[i]);
            distanceAssigner(newCoord, direction[i], distance + 1);
        }
        return;
    }
}
