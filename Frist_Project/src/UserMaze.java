public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(10, 10);
        Room[][] maze = mazeMaker.createMaze();
        mazeMaker.printMaze();
    }
}
