package sheet_2;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Exercise3 extends GLCanvas implements GLEventListener {
    public Exercise3 () {
        this.addGLEventListener(this);
    }

    //nichts neues
    private GL2 gl;
    private GLU glu;

    public void display(GLAutoDrawable gLDrawable) {
        gl = gLDrawable.getGL().getGL2();
        glu = new GLU();
        GLUT glut = new GLUT();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        glu.gluLookAt(5, 5, 5, 0, 0, 0, 0, 1, 0);
        //Drahtwürfel zeichnen
        glut.glutWireCube(2.0f);
        this.drawAxis(1,0, 0, 255,0,0);
        this.drawAxis(0,1, 0, 0,255,0);
        this.drawAxis(0,0, 1, 0,0,255);
        gl.glFlush();
    }

    private void drawAxis(int x, int y, int z, float red, float green, float blue) {
        gl.glBegin(gl.GL_LINE_LOOP);
        gl.glColor3f(red,green,blue);
        gl.glVertex3f(0f,0f,0f);
        gl.glVertex3f(x*10f,y*10f,z*10f);
        gl.glColor3f(255,255,255);
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
        Frame frame = new Frame("ruhender Drahtwuerfel");
        Exercise3 canvas = new Exercise3();
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
