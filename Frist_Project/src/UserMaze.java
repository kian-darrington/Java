public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(4, 4);
        Room[][] maze = mazeMaker.createMaze();
        mazeMaker.printMaze();
    }
}
