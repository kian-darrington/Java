import java.util.ArrayList;
import java.util.Random;

public class MazeSolver{
    int N = 0, E = 1, S = 2, W = 3;
    int[] inverse = new int[] {S, W, N, E};
    private static final Random rand = new Random();
    Room[][] rooms;
    MazeSolver(Room[][] r) {
        rooms = r;
    }
    void setMaze (Room[][] r) {
        rooms = r;
    }
    public void solveMaze(){
        solveMaze(new int[] {0,0}, 0);
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
            default:
                throw new IllegalStateException("Unexpected value: " + moveTo);
        }
    }
    private boolean solveMaze(int[] coord, int directionFrom){
        Room room = rooms[coord[0]][coord[1]];

        if (room.getExit())
            return true;

        boolean[] availablePaths = room.getMove().clone();

        if (availablePaths[inverse[directionFrom]])
            availablePaths[inverse[directionFrom]] = false;
        int pathsAvailable = 0;
        for (boolean b : availablePaths)
            if (b)
                pathsAvailable++;
        if (pathsAvailable == 0)
            return false;
        int[] direction = new int[pathsAvailable];
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < availablePaths.length; i++)
            if (availablePaths[i])
                temp.add(i);
        int size =  temp.size();
        for (int i = 0; i < size; i++) {
            direction[i] = temp.remove(rand.nextInt(temp.size()));
        }
    }
}
