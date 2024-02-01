import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(90, 40);
        Maze maze = new Maze(mazeMaker.wilsonMaze());
        String unsolved = maze.stringMaze();
        MazeSolver answer = new MazeSolver(maze);
        answer.distanceAssigner();
        Maze solvedMaze = new Maze(answer.getMaze());
        solvedMaze.printMaze();
        JFrame frame = new JFrame("Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);

        Font font = new Font("Menlo", Font.PLAIN, 9);
        AffineTransform transform = new AffineTransform();
        transform.scale(1.75, 1.0); // Adjust the scaling factors as needed
        Font stretchedFont = font.deriveFont(transform);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, -3));
        panel.setBackground(Color.BLACK);
        panel.setFont(stretchedFont);
        frame.add(panel);
        String mazeLines = solvedMaze.stringMaze();

        // Apply horizontal and vertical scaling

        JLabel label = new JLabel(mazeLines);
        label.setFont(stretchedFont);
        label.setForeground(Color.WHITE); // Set the color of the font
        panel.add(label);
        frame.setVisible(true);
    }
}
