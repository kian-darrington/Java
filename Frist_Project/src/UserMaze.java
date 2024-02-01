import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(60, 60);
        Maze maze = new Maze(mazeMaker.wilsonMaze());
        ArrayList<String> unsolved = maze.stringMaze();
        MazeSolver answer = new MazeSolver(maze);
        answer.solveMaze();
        Maze solvedMaze = new Maze(answer.getMaze());
        solvedMaze.printMaze();
        JFrame frame = new JFrame("Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, -3));
        panel.setBackground(Color.BLACK);
        panel.setFont(new Font("Menlo", Font.PLAIN, 12));
        frame.add(panel);
        ArrayList<String> mazeLines = solvedMaze.stringMaze();
        Font font = new Font("Menlo", Font.PLAIN, 8);

        // Apply horizontal and vertical scaling
        AffineTransform transform = new AffineTransform();
        transform.scale(1.75, 1.0); // Adjust the scaling factors as needed
        Font stretchedFont = font.deriveFont(transform);
        for (int i = 0; i < unsolved.size(); i++) {
            JLabel label = new JLabel(unsolved.get(i));
            label.setFont(stretchedFont);
            label.setForeground(Color.WHITE); // Set the color of the font
            panel.add(label);
        }
        frame.setVisible(true);
    }
}
