public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(30, 20);
        Maze maze = new Maze(mazeMaker.randDepthFirstSearch());
        maze.printMaze();
        MazeSolver answer = new MazeSolver(maze);
        answer.solveMaze();
        Maze solvedMaze = new Maze(answer.getMaze());
        solvedMaze.printMaze();
    }
}
