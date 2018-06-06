package sheet_8;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


@SuppressWarnings("Duplicates")
public class Exercise1 extends GLCanvas implements GLEventListener {

  public Exercise1() {
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
    glu.gluLookAt(0, 0, -10, 0, 0, 0, 0, 1, 0);
    //Drahtwürfel zeichnen
    this.drawStrip();
    gl.glFlush();
  }

  private void drawStrip() {
    gl.glColor3f(0,0,0);
    gl.glBegin(gl.GL_QUADS);
    gl.glVertex3f(-5,-5,0);
    gl.glVertex3f(-5,5,0);
    gl.glVertex3f(-1,5,0);
    gl.glVertex3f(-1,-5,0);
    gl.glEnd();

    gl.glBegin(gl.GL_QUADS);
    gl.glVertex3f(-1,-5,0);
    gl.glVertex3f(-1,5,0);
    gl.glColor3f(1,1,1);
    gl.glVertex3f(1,5,0);
    gl.glVertex3f(1,-5,0);
    gl.glEnd();

    gl.glBegin(gl.GL_QUADS);

    gl.glVertex3f(1,-5,0);
    gl.glVertex3f(1,5,0);
    gl.glVertex3f(5,5,0);
    gl.glVertex3f(5,-5,0);
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
