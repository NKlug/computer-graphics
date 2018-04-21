package sheet_2;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class Exercise4 extends GLCanvas implements GLEventListener {

    /**
     * KEYMAP:
     *
     * toggleCoordinateSystem       Space
     * increase n                   +
     * decrease n                   -
     * increase X angle             a
     * decrease X angle             q
     * increase Y angle             s
     * decrease Y angle             w
     * increase Z angle             d
     * decrease Z angle             e
     */


    private int n = 2;
    private float cubeSize = 2.0f;
    private boolean coordinateSystemEnabled = true;
    private int angleX = 0;
    private int angleY = 0;
    private int angleZ = 0;

    private GL2 gl;
    private GLU glu;


    public Exercise4() {
        this.addGLEventListener(this);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '+') {
                    n += 1;
                } else if (e.getKeyChar() == '-') {
                    if (n > 0)
                        n -= 1;
                } else if (e.getKeyChar() == 'a') {
                    angleX += 2;
                } else if (e.getKeyChar() == 'q') {
                    angleX -=2;
                } else if (e.getKeyChar() == 'w') {
                    angleY -= 2;
                } else if (e.getKeyChar() == 's') {
                    angleY += 2;
                } else if (e.getKeyChar() == 'e') {
                    angleZ -= 2;
                } else if (e.getKeyChar() == 'd') {
                    angleZ += 2;
                } else if (e.getKeyChar() == ' ') {
                    toggleCoordinateSystem();
                }
                display();
            }
        });
    }

    public void display(GLAutoDrawable gLDrawable) {
        gl = gLDrawable.getGL().getGL2();
        glu = new GLU();
        GLUT glut = new GLUT();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glLoadIdentity();

        gl.glColor3f(1f, 1f, 1f);
        glu.gluLookAt(3, 4, 5, 0, 0, 0, 0, 0, 1);

        this.drawRotatedCubes(glut);

        this.drawCoordinateSystem();

        gl.glFlush();
    }

    private void drawRotatedCubes(GLUT glut) {
        gl.glPushMatrix();
        gl.glRotatef(angleX, 1, 0, 0);
        gl.glRotatef(angleY, 0, 1, 0);
        gl.glRotatef(angleZ, 0, 0, 1);
        glut.glutWireCube(this.cubeSize);
        this.drawOtherCubes(glut);

        gl.glFlush();
        gl.glPopMatrix();
        }

    private void drawCoordinateSystem() {
        if (this.coordinateSystemEnabled) {
            this.drawAxis(1, 0, 0, 255, 0, 0);
            this.drawAxis(0, 1, 0, 0, 255, 0);
            this.drawAxis(0, 0, 1, 0, 0, 255);
        }
    }

    private void toggleCoordinateSystem() {
        this.coordinateSystemEnabled = !this.coordinateSystemEnabled;
    }



    private void setColor(int x, int y, int z) {
        float r = ((x + 1) * 100) % 256;
        float g = ((y + 1) * 100) % 256;
        float b = ((z + 1) * 100) % 256;
        gl.glColor3f(r / 256, g / 256, b / 256);
    }

    private void drawOtherCubes(GLUT glut) {
        float newCubeSize = cubeSize / n;
        for (int x = 0; x < n; ++x) {
            for (int y = 0; y < n; ++y) {
                for (int z = 0; z < n; ++z) {
                    this.setColor(x, y, z);
                    gl.glTranslatef(
                            -cubeSize / 2 + newCubeSize / 2 + x * newCubeSize,
                            -cubeSize / 2 + newCubeSize / 2 + y * newCubeSize,
                            -cubeSize / 2 + newCubeSize / 2 + z * newCubeSize);
                    glut.glutSolidCube(newCubeSize);
                    gl.glTranslatef(
                            cubeSize / 2 - newCubeSize / 2 - x * newCubeSize,
                            cubeSize / 2 - newCubeSize / 2 - y * newCubeSize,
                            cubeSize / 2 - newCubeSize / 2 - z * newCubeSize);
                }
            }

        }

    }

    private void drawAxis(int x, int y, int z, float red, float green, float blue) {
        gl.glBegin(gl.GL_LINE_LOOP);
        gl.glColor3d(red, green, blue);
        gl.glVertex3f(0f, 0f, 0f);
        gl.glVertex3f(x * 10f, y * 10f, z * 10f);
        gl.glColor3d(255, 255, 255);
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
        Frame frame = new Frame("Drahtwürfel mit kleineren Würfeln");
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
}
