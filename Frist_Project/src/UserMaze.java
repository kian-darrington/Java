public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(40, 29);
        Maze maze = new Maze(mazeMaker.wilsonMaze());
        maze.printMaze();
        MazeSolver answer = new MazeSolver(maze);
        answer.solveMaze();
        Maze solvedMaze = new Maze(answer.getMaze());
        solvedMaze.printMaze();
    }
}
