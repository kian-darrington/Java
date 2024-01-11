public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(35, 29);
        Room[][] maze = mazeMaker.createMaze();
        mazeMaker.printMaze();
    }
}
