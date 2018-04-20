package sheet_1;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractList;

public class MorphingPanel extends JPanel {

    private int DURATION = 5000;
    private Point[] currentPoints;
    private int width;
    private int height;

    public MorphingPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;
    }

    public void morphLinear(AbstractList<Point> origin, AbstractList<Point> target, double step) {
        currentPoints = origin.toArray(new Point[origin.size()]);

        double t = 1;
        int waitingTime = (int) Math.ceil(DURATION * step);

        this.waitFor(1000);
        while (t > 0) {

            for (int i = 0; i < currentPoints.length; i++) {
                int newX = (int) (t * origin.get(i).getX() + (1 - t) * target.get(i).getX());
                int newY = (int) (t * origin.get(i).getY() + (1 - t) * target.get(i).getY());
                currentPoints[i] = new Point(newX, newY);
            }
            t -= step;
            this.repaint();
            this.waitFor(waitingTime);
        }

    }


    private void waitFor(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {}
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);

        if (this.currentPoints != null) {
            int len = this.currentPoints.length;
            for (int i = 0; i < len; i++) {
                g.drawLine(this.currentPoints[i].getX(), this.currentPoints[i].getY(),
                        this.currentPoints[(i + 1) % len].getX(), this.currentPoints[(i+1) % len].getY());
            }
        }
    }
}


