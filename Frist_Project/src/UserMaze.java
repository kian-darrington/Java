public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(20, 5);
        Room[][] maze = mazeMaker.createMaze();
        mazeMaker.printMaze();
    }
}
