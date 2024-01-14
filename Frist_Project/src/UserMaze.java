public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(41, 27);
        Room[][] maze = mazeMaker.createMaze();
        mazeMaker.printMaze();
    }
}
