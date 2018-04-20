package sheet_1;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;

public class MorphingFrame extends JFrame {

    private MorphingPanel morphingPanel;
    private final int WIDTH = 500;
    private final int HEIGHT = 500;

    public MorphingFrame() {
        super("Morphing Frame");
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        morphingPanel = new MorphingPanel(WIDTH, HEIGHT);
        this.add(morphingPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public void morphLinear(AbstractList<Point> origin, AbstractList<Point> target, double step) {
        morphingPanel.morphLinear(origin, target, step);
    }


    public static void main(String[] args) {
        MorphingFrame frame = new MorphingFrame();
        Point[] points2 = new Point[] {
                new Point(100, 100),
                new Point(100, 200),
                new Point(200, 200),
                new Point(200, 100)};
        Point[] points1 = new Point[] {
                new Point(300, 100),
                new Point(400, 450),
                new Point(250, 50),
                new Point(350, 100)};

        ArrayList<Point> origin1 = new ArrayList<>(Arrays.asList(points1));
        ArrayList<Point> target1 = new ArrayList<>(Arrays.asList(points2));

        frame.morphLinear(origin1, target1, 0.0005);

    }

}
