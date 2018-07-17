package altklausur;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Exercise4 extends GLCanvas implements GLEventListener {

    /**
     * KEYMAP:
     * Drehen                       a, d
     * Anzahl Ecken erhöhen         +
     * Anzahl Ecken verringern      -
     * Anzahl Segmente erhöhen      Pfeil oben
     * Anzahl Segmente verringern   Pfeil unten
     */

    private float rotate = 0;
    private int n = 4;
    private int m = 4;
    private MODES mode = MODES.Pyramide;

    ;
    private GL2 gl;
    private GLU glu;

    public Exercise4() {
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

    public static void main(String[] args) {
        Frame frame = new Frame("n-Eck Pyramide");
        Exercise4 canvas = new Exercise4();
        frame.add(canvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(640, 640);
        frame.setVisible(true);
        canvas.requestFocus();
    }

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
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(gl.GL_PROJECTION);
        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glLoadIdentity();
        gl.glOrtho(-2, 2, -2, 2, -2, 2);
//        gl.glOrtho(-6,6,-6,6,-6,6);
        gl.glMatrixMode(gl.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(0, 1, 0, 0, 0, 0, 1, 0, 0);
        gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_FILL);

        this.drawAxis(1, 0, 0, 255, 0, 0);
        this.drawAxis(0, 1, 0, 0, 255, 0);
        this.drawAxis(0, 0, 1, 0, 0, 255);

        gl.glBegin(gl.GL_POLYGON);
        gl.glColor3f(1, 0, 0);
        gl.glVertex3f(1, 0, 1);
        gl.glVertex3f(0, 0, 1);
        gl.glColor3f(0, 1, 0);
        gl.glVertex3f(1, 0, 0);
        gl.glColor3f(0.2f, 0.2f, 0.2f);
        gl.glEnd();

        gl.glPushMatrix();

        GLUquadric quad = glu.gluNewQuadric();
        gl.glTranslatef(-1, 0, 0);
        gl.glRotatef(90, 0, 1, 0);
        glu.gluCylinder(quad, 1, 1, 1, 100, 100);
        gl.glPopMatrix();

        gl.glTranslatef(-3.5f, 0, 0);
        gl.glBegin(gl.GL_POLYGON);
        gl.glColor3f(0, 0, 1);
        gl.glVertex3f(1, 0, 0);
        gl.glVertex3f(1, 0, 1);
        gl.glVertex3f(0, 0, 1);
        gl.glVertex3f(0, 0, 0);
        gl.glEnd();


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

    public enum MODES {Stumpf, Pyramide}
}
