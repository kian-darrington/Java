import java.util.ArrayList;
import java.util.Random;

public class MazeSolver{
    static final int N = 0, E = 1, S = 2, W = 3;
    int[] inverse = new int[] {S, W, N, E};
    private static final Random rand = new Random();
    Room[][] rooms;
    MazeSolver(Room[][] r) {
        rooms = r;
    }
    MazeSolver(Maze maze) {rooms = maze.getRooms();}
    void setMaze (Room[][] r) {
        rooms = r;
    }
    public void solveMaze(){
        solveMaze(new int[] {0,0}, S);
    }
    Room[][] getMaze(){return rooms;}
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
        //
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
}
