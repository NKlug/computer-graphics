package altklausur;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Exercise1 extends GLCanvas implements GLEventListener {

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

    public enum MODES {Stumpf, Pyramide}

    ;
    private MODES mode = MODES.Pyramide;


    public Exercise1() {
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


        glu.gluLookAt(0, 0, 10, 0, 0, 0, 0, 1, 0);
        gl.glPushMatrix();
        gl.glRotatef(rotate, 1, 0, 0);

        torus(3, 1, 10, 4);

        this.drawAxis(1, 0, 0, 255, 0, 0);
        this.drawAxis(0, 1, 0, 0, 255, 0);
        this.drawAxis(0, 0, 1, 0, 0, 255);
        gl.glPopMatrix();
        gl.glFlush();
    }

    private void torus(int R, int r, int slices, int edges) {
        gl.glPushMatrix();
        gl.glTranslatef(R, 0, 0);

        for (int i = 0; i < slices; i++) {
            double slice = i * Math.PI * 2 / slices;
            double nextSlice = (i + 1) * Math.PI * 2 / slices;

            for (int j = 0; j < edges; j++) {
                gl.glBegin(gl.GL_LINE_LOOP);
                double edge = j * Math.PI * 2 / edges;
                double nextEdge = (j + 1) * Math.PI * 2 / edges;

                float x = (float) ((R + r * Math.cos(edge)) * Math.cos(slice));
                float y = (float) ((R + r * Math.cos(edge)) * Math.sin(slice));
                float z = (float) (r * Math.sin(edge));
                gl.glVertex3f(x, y, z);

                x = (float) ((R + r * Math.cos(nextEdge)) * Math.cos(slice));
                y = (float) ((R + r * Math.cos(nextEdge)) * Math.sin(slice));
                z = (float) (r * Math.sin(nextEdge));
                gl.glVertex3f(x, y, z);

                x = (float) ((R + r * Math.cos(nextEdge)) * Math.cos(nextSlice));
                y = (float) ((R + r * Math.cos(nextEdge)) * Math.sin(nextSlice));
                z = (float) (r * Math.sin(nextEdge));

                gl.glVertex3f(x, y, z);

                x = (float) ((R + r * Math.cos(edge)) * Math.cos(nextSlice));
                y = (float) ((R + r * Math.cos(edge)) * Math.sin(nextSlice));
                z = (float) (r * Math.sin(edge));

                gl.glVertex3f(x, y, z);

                gl.glEnd();
            }
        }

        gl.glPopMatrix();
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

    public static void main(String[] args) {
        Frame frame = new Frame("n-Eck Pyramide");
        Exercise1 canvas = new Exercise1();
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
}
