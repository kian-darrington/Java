public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(70, 8);
        Room[][] maze = mazeMaker.createMaze();
        mazeMaker.printMaze();
    }
}
