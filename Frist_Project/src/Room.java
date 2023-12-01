public class Room {
    final int X_ROOM = MazeMaker.X_ROOMS;
    final int Y_ROOM = MazeMaker.Y_ROOMS;
    int xCord;
    int yCord;
    boolean[] accessable = new boolean[4]; //True or false for N, E, S, W
    Room(int x, int y, boolean[] t)
    {
        xCord = x;
        yCord = y;
        accessable = t;
    }
}
