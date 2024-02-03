import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class UserMaze {
    public static void main(String[] args) {
        MazeMaker mazeMaker = new MazeMaker(50, 34);
        Maze maze = new Maze(mazeMaker.randDepthFirstSearch());
        ArrayList<String> unsolved = maze.stringMaze();
        MazeSolver answer = new MazeSolver(maze);
        answer.distanceAssigner();
        Maze solvedMaze = new Maze(answer.getMaze());
        solvedMaze.printMaze();
        JFrame frame = new JFrame("Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);

        Font font = new Font("Consolas", Font.PLAIN, 10);
        AffineTransform transform = new AffineTransform();
        transform.scale(1.75, 1.0); // Adjust the scaling factors as needed
        Font stretchedFont = font.deriveFont(transform);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, -3));
        panel.setBackground(Color.BLACK);
        panel.setFont(stretchedFont);
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, -3));
        panel2.setBackground(Color.BLACK);
        panel2.setFont(stretchedFont);
        frame.add(panel2);
        String mazeLines = solvedMaze.justRooms();
        for (int i = 0; i < unsolved.size(); i++) {
            JLabel label2 = new JLabel(unsolved.get(i));
            label2.setFont(stretchedFont);
            label2.setForeground(Color.WHITE); // Set the color of the font
            panel2.add(label2);
        }

        // Apply horizontal and vertical scaling

        JLabel label = new JLabel(mazeLines);
        label.setFont(stretchedFont);
        label.setForeground(Color.WHITE); // Set the color of the font
        panel.add(label);
        frame.setVisible(true);
    }
}
