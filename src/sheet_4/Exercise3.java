package sheet_4;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Exercise3 extends GLCanvas implements GLEventListener {


    /**
     * KEYMAP:
     * Increase time            arrow right
     * Decrease time            arrow left
     * rotate around x axis     a/d
     */

    private final float SCALE = 1f;
    private double angle = Math.toRadians(35);
    private double speed = 10;
    private double y_0;
    private double x_0;
    private double g = -9.81;
    private double time = 0;

    private float rotate = 0;
    private GLUT glut;
    private GL2 gl;
    private GLU glu;

    public Exercise3() {
        this.addGLEventListener(this);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'a') {
                    rotate += 2;
                } else if (e.getKeyChar() == 'd') {
                    rotate -= 2;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    time += 0.01;
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (time > 0)
                        time -= 0.01;
                }
                display();
            }
        });

    }

    public void initSpeed() {
        this.y_0 = Math.sin(angle) * speed;
        this.x_0 = Math.cos(angle) * speed;
    }

    public void setAngle(double angle) {
        this.angle = Math.toRadians(angle);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void display(GLAutoDrawable gLDrawable) {
        this.initSpeed();
        gl = gLDrawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();


        glu.gluLookAt(10, 5, 5, 0, 1, 5, 0, 1, 0);
        gl.glPushMatrix();

        gl.glRotatef(rotate, 1, 0, 0);
        this.drawAxis(1, 0, 0, 255, 0, 0); // x-Achse
        this.drawAxis(0, 1, 0, 0, 255, 0); // y-Achse
        this.drawAxis(0, 0, 1, 0, 0, 255); // z-Achse

        this.showSpeer(time);

        gl.glPopMatrix();
        gl.glFlush();
    }


    private void drawSpeer(float y, float z, float angle) {
        gl.glPushMatrix();
        gl.glTranslatef(0, y * SCALE,
                z * SCALE);
        gl.glRotatef(angle, 1, 0, 0);
        gl.glScaled(1, 1, 10);
        glut.glutWireCube(0.05f);
        gl.glPopMatrix();
//        glu.gluLookAt(10, 10, 10, x, y, z, 0,1,0);
    }


    private void showSpeer(double time) {
        float x = (float) (x_0 * time);
        float y = (float) (y_0 * time + 0.5 * g * time * time);
//        float y = (float) ((y_0/x_0) * x - (g/Math.pow(x_0, 2)) * x * x);
        float angle = (float) -Math.toDegrees(Math.atan((y_0 + g * time) / x_0));
        this.drawSpeer(y, x, angle);
    }

    private void drawAxis(int x, int y, int z, float red, float green, float blue) {
        gl.glBegin(gl.GL_LINE_LOOP);
        gl.glColor3f(red, green, blue);
        gl.glVertex3f(0f, 0f, 0f);
        gl.glVertex3f(x * 20f, y * 20f, z * 20f);
        gl.glColor3f(255, 255, 255);
        gl.glEnd();
    }


    public void init(GLAutoDrawable gLDrawable) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    public void reshape(GLAutoDrawable gLDrawable, int x, int y,
                        int width, int height) {
        gl = gLDrawable.getGL().getGL2();
        glu = new GLU();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(60, width / height, 1, 100);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public static JFrame createSettingsFrame(Exercise3 canvas) {
        JFrame frame = new JFrame("Settings");
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Speed: "));
        JTextField speed = new JTextField("10");
        speed.setColumns(20);
        panel.add(speed);
        panel.add(new JLabel("Angle:"));
        JTextField angle = new JTextField("35");
        angle.setColumns(20);
        panel.add(angle);
        panel.add(new JLabel("Gravitation:"));
        JTextField gravitation = new JTextField("-9.81");
        gravitation.setColumns(20);
        panel.add(gravitation);
        JButton show = new JButton("Show");
        show.addActionListener(e -> {
            try {
                canvas.setSpeed(Double.parseDouble(speed.getText()));
                canvas.setAngle(Double.parseDouble((angle.getText())));
                canvas.setG(Double.parseDouble(gravitation.getText()));
                canvas.display();
            } catch (NumberFormatException ex) {
            }
        });
        panel.add(show);
        for (Component c : panel.getComponents())
            c.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.add(panel);
        frame.setSize(new Dimension(500, 150));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    public static void main(String[] args) {
        Frame frame = new Frame("Speer");
        Exercise3 canvas = new Exercise3();
        frame.add(canvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(800, 600);
        frame.setVisible(true);

        JFrame settings = Exercise3.createSettingsFrame(canvas);
        settings.setLocation((int) frame.getLocation().getX() + frame.getWidth() + 30, 30);
        settings.setVisible(true);

        canvas.requestFocus();
    }

}
