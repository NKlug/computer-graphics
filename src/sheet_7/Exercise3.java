package sheet_7;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;


public class Exercise3 {

  private static FPSAnimator animator;

  static class WinRenderer extends GLCanvas implements GLEventListener {
    private GL2 gl;
    private GLU glu;
    private Texture earthTexture;
    private Texture moonTexture;
    private Texture sunTexture;

    private float speed = 1;
    private boolean toggleCoordinates = false;

    public WinRenderer(GLCapabilities cap)
    {
      super(cap);
      this.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          super.keyPressed(e);
          if (e.getKeyChar() == '+') {
            speed *= 2;
          } else if (e.getKeyChar() == '-') {
            speed *= 0.5;
          } else if (e.getKeyChar() == ' ') {
            toggleCoordinates = !toggleCoordinates;
          }
        }
      });
    }

    float time = 0.0f;

    void forwardTime()
    {
      time += speed * (0.05f % 24 * 365);
    }

    public void display(GLAutoDrawable gLDrawable)
    {
      forwardTime();
      gl = gLDrawable.getGL().getGL2();
      glu = new GLU();
      final GLUT glut = new GLUT();
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
      gl.glLoadIdentity();
      glu.gluLookAt(6, 0, 0, 0, 0, 0, 0, 0, 1);

      this.drawSun();

      gl.glPushMatrix();
      gl.glRotatef(time / 24 / 365 * 360, 0,0,1);
      gl.glTranslatef(3,0,0);


      gl.glRotatef(-23.44f, 1, 0,0);
      this.drawEarth();

      gl.glPushMatrix();
      gl.glRotatef(23.44f -6f, 1, 0,0);
      drawMoon();
      gl.glPopMatrix();
      gl.glPopMatrix();
      gl.glFlush();
    }


    private void drawAxis(int x, int y, int z, float red, float green, float blue) {
      if (!toggleCoordinates) return;
      gl.glBegin(gl.GL_LINE_LOOP);
      gl.glColor3d(red, green, blue);
      gl.glVertex3f(0f, 0f, 0f);
      gl.glVertex3f(x * 10f, y * 10f, z * 10f);
      gl.glColor3d(255, 255, 255);
      gl.glEnd();
    }

    private void drawEarth () {
      gl.glPushMatrix();
      gl.glRotatef(time / 24f * 360, 0,0,1);

      this.drawAxis(1,0,0,1,0,0);
      this.drawAxis(0,0,1,0,1,0);

      GLUquadric sphere_quadric = glu.gluNewQuadric();
      gl.glEnable(GL.GL_TEXTURE_2D);
      earthTexture.bind(gl);
      glu.gluQuadricTexture(sphere_quadric, true);
      glu.gluQuadricDrawStyle(sphere_quadric, GLU.GLU_FILL);
      glu.gluQuadricNormals(sphere_quadric, GLU.GLU_SMOOTH);
      glu.gluSphere(sphere_quadric, 0.6f, 50, 50);
      gl.glPopMatrix();
    }

    private void drawMoon() {
      gl.glPushMatrix();
      gl.glRotatef(2.5f * time, 0,0,1);
      gl.glTranslatef(1f,0,0);

      this.drawAxis(1,0,0,1,0,0);
      this.drawAxis(0,0,1,0,1,0);

      GLUquadric sphere_quadric = glu.gluNewQuadric();
      gl.glEnable(GL.GL_TEXTURE_2D);
      moonTexture.bind(gl);
      glu.gluQuadricTexture(sphere_quadric, true);
      glu.gluQuadricDrawStyle(sphere_quadric, GLU.GLU_FILL);
      glu.gluQuadricNormals(sphere_quadric, GLU.GLU_SMOOTH);
      glu.gluSphere(sphere_quadric, 0.3f, 50, 50);
      gl.glPopMatrix();
    }

    private void drawSun() {
      gl.glPushMatrix();

      this.drawAxis(1,0,0,1,0,0);
      this.drawAxis(0,0,1,0,1,0);

      GLUquadric sphere_quadric = glu.gluNewQuadric();
      gl.glEnable(GL.GL_TEXTURE_2D);
      sunTexture.bind(gl);
      glu.gluQuadricTexture(sphere_quadric, true);
      glu.gluQuadricDrawStyle(sphere_quadric, GLU.GLU_FILL);
      glu.gluQuadricNormals(sphere_quadric, GLU.GLU_SMOOTH);
      glu.gluSphere(sphere_quadric, 1f, 50, 50);
      gl.glPopMatrix();
    }

    public void init(GLAutoDrawable gLDrawable)
    {
      gl = gLDrawable.getGL().getGL2();
      gl.glEnable(GL2.GL_DEPTH_TEST);
      try
      {
        InputStream in = getClass().getClassLoader().getResourceAsStream("sheet_7/map.jpg");
        earthTexture = TextureIO.newTexture(in, true, "jpg");
        in = getClass().getClassLoader().getResourceAsStream("sheet_7/moon-map.jpg");
        moonTexture = TextureIO.newTexture(in, true, "jpg");
        in = getClass().getClassLoader().getResourceAsStream("sheet_7/sun-map.jpg");
        sunTexture = TextureIO.newTexture(in, true, "jpg");
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    @Override
    public void dispose(GLAutoDrawable drawable)
    {

    }

    public void reshape(GLAutoDrawable gLDrawable, int x, int y,
                        int width, int height) {
      gl = gLDrawable.getGL().getGL2();
      glu = new GLU();
      gl.glViewport(0, 0, width, height);
      gl.glMatrixMode(GL2.GL_PROJECTION);
      gl.glLoadIdentity();
      glu.gluPerspective(120, (float) width / (float) height, 1, 100);
      gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
  }

  public static void main(String[] args)
  {
    Frame frame = new Frame("texturierte Kugel");
    GLCapabilities cap = new GLCapabilities(null);
    cap.setDoubleBuffered(true);
    WinRenderer canvas = new WinRenderer(cap);
    canvas.addGLEventListener(canvas);
    //zwanzig Bilder pro Sekunde einstellen
    int fps = 30;
    animator = new FPSAnimator(canvas, fps);
    animator.setUpdateFPSFrames(fps * 2, System.out);
    frame.add(canvas);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e)
      {
        animator.stop();
        System.exit(0);
      }
    });
    frame.setSize(640, 480);
    frame.setVisible(true);
    animator.start();
    canvas.requestFocus();
  }
}
