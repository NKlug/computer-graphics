package sheet_3;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

import static javafx.scene.input.KeyCode.LEFT;


public class Exercise3 extends GLCanvas implements GLEventListener {

    /**
     * KEYMAP:
     * Drehen                       a, d
     * Anzahl Ecken erhöhen         +
     * Anzahl Ecken verringern      -
     * Anzahl Segmente erhöhen      Pfeil oben
     * Anzahl Segmente verringern   Pfeil unten
     *
     */

    private float rotate = 0;
    private int n = 4;
    private int m = 4;
    public enum MODES {Stumpf, Pyramide};
    private MODES mode = MODES.Pyramide;


    public Exercise3() {
        this.addGLEventListener(this);
        this.addGLEventListener(this);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'a') {
                    rotate += 2;
                } else if (e.getKeyChar() == 'd') {
                    rotate -= 2;
                } else if (e.getKeyChar() == '+') {
                    n += 1;
                } else if (e.getKeyChar() == '-') {
                    if (n > 0)
                        n -= 1;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (m > 0)
                        m -= 1;
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    m += 1;
                }
                display();
            }
        });
    }

    private GL2 gl;
    private GLU glu;

    public void setN(int n) {
        this.n = n;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setMode(MODES mode) {
        this.mode = mode;
    }

    public void display(GLAutoDrawable gLDrawable) {
        gl = gLDrawable.getGL().getGL2();
        glu = new GLU();
        GLUT glut = new GLUT();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();


        glu.gluLookAt(5, 5, 5, 0, 0, 0, 0, 1, 0);
        //Drahtwürfel zeichnen
        gl.glPushMatrix();
        gl.glRotatef(rotate, 1, 0, 0);

        if (this.mode == MODES.Pyramide)
            this.pyramid(2.0f, 3.0f, this.n, this.m);
        else
            this.stumpf(2.0f, 1.0f, 2.0f, this.n);

        this.drawAxis(1, 0, 0, 255, 0, 0);
        this.drawAxis(0, 1, 0, 0, 255, 0);
        this.drawAxis(0, 0, 1, 0, 0, 255);
        gl.glPopMatrix();
        gl.glFlush();
    }

    private void drawAxis(int x, int y, int z, float red, float green, float blue) {
        gl.glBegin(gl.GL_LINE_LOOP);
        gl.glColor3f(red, green, blue);
        gl.glVertex3f(0f, 0f, 0f);
        gl.glVertex3f(x * 10f, y * 10f, z * 10f);
        gl.glColor3f(255, 255, 255);
        gl.glEnd();
    }

    private void stumpf(float rad1, float rad2, float height, int n) {
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        gl.glBegin(GL2.GL_QUAD_STRIP);

        for (int k = 0; k <= n; ++k) {
            // exp(i*(k/n*2*pi) = cos(k/n*2*pi) + i*sin(k/n*2*pi)
            float xInS1 = (float) Math.cos(2 * Math.PI / n * k);
            float yInSi = (float) Math.sin(2 * Math.PI / n * k);
            gl.glVertex3f(xInS1 * rad1, 0, yInSi * rad1);
            gl.glVertex3f(xInS1 * rad2, height, yInSi * rad2);
        }

        gl.glEnd();
    }

    private void pyramid(float rad, float height, int n, int m) {
        float seg_height = height / m;
        float lastRad = rad;
        for (int k = 1; k <= m; ++k) {
            gl.glPushMatrix();
            gl.glTranslatef(0, (k - 1) * seg_height, 0);
            float newRad = rad / height * (height - seg_height * k);
            this.stumpf(lastRad, newRad, seg_height, n);
            lastRad = newRad;
            gl.glPopMatrix();
        }
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
        JPanel panel = new JPanel();
        panel.add(new JLabel("Number of corners: "));
        JSpinner corners = new JSpinner(new SpinnerNumberModel(4, 0, Integer.MAX_VALUE, 1));
        corners.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.setN((int) corners.getValue());
                canvas.display();
            }
        });
        panel.add(corners);
        panel.add(new JLabel("Number of Segments:"));
        JSpinner segments = new JSpinner(new SpinnerNumberModel(4, 0, Integer.MAX_VALUE, 1));
        segments.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.setM((int) segments.getValue());
                canvas.display();
            }
        });
        panel.add(segments);
        panel.add(new JLabel("Mode:"));
        String[] modes = new String [] {"Stumpf", "Pyramide"};
        JComboBox<String> mode = new JComboBox<String>(modes);
        mode.setSelectedItem("Pyramide");
        mode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mode.getSelectedItem().equals("Stumpf"))
                    canvas.setMode(MODES.Stumpf);
                else if (mode.getSelectedItem().equals("Pyramide"))
                    canvas.setMode(MODES.Pyramide);
                canvas.display();
            }
        });
        panel.add(mode);

        for (Component c: panel.getComponents())
            c.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.add(panel);
        frame.setSize(new Dimension(500, 150));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    public static void main(String[] args) {
        Frame frame = new Frame("n-Eck Pyramide");
        Exercise3 canvas = new Exercise3();
        frame.add(canvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(640, 640);
        frame.setVisible(true);

        JFrame settings = Exercise3.createSettingsFrame(canvas);
        settings.setLocation((int) frame.getLocation().getX() + frame.getWidth() + 30, 30);
        settings.setVisible(true);

        canvas.requestFocus();
    }
}
