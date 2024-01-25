public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(10, 10);
        Maze maze = new Maze(mazeMaker.createMaze());
        maze.printMaze();
    }
}
