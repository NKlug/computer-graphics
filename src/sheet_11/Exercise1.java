package sheet_11;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.FloatBuffer;

public class Exercise1 extends GLCanvas implements GLEventListener {

    private GLU glu = new GLU();
    private GL2 gl;
    private Texture tex1;
    float[] control_points;
    float time = 0;
    FloatBuffer buf;
    private float phi = 0;
    private float theta = 0;
    int steps = 4;
    static final long serialVersionUID = 0;
    float rotX = 0.0f, rotY = 0.0f, rotZ = 0.0f, eyeZ = 5.0f;
    float bezier_z = 0.0f;
    float bezier_x = 0.5f;
    float bezier_y = 0.5f;

    public Exercise1() {
        super();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '+') {
                    steps += 1;
                } else if (e.getKeyChar() == '-') {
                    if (steps > 0)
                        steps -= 1;
                } else if (e.getKeyChar() == 'a') {
                    phi -= 0.1;
                } else if (e.getKeyChar() == 'd') {
                    phi += 0.1;
                } else if (e.getKeyChar() == 'w') {
                    theta += 0.1;
                } else if (e.getKeyChar() == 's') {
                    theta -= 0.1;
                } else if (e.getKeyChar() == 'q') {
                    phi -= 0.1;
                    theta += 0.1;
                } else if (e.getKeyChar() == 'e') {
                    phi += 0.1;
                    theta += 0.1;
                } else if (e.getKeyChar() == 'y') {
                    phi -= 0.1;
                    theta -= 0.1;
                } else if (e.getKeyChar() == 'x') {
                    phi += 0.1;
                    theta -= 0.1;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    time += 1;
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    time -= 1;
                } else if (e.getKeyChar() == ' ') {
//                    toggleCoordinateSystem();
                }
                display();
            }
        });
    }

    private Texture loadTexture(GL2 gl) {
        Texture tex = null;
        try {
            tex = TextureIO.newTexture(new File("src/sheet_11/south_africa_flag.jpg"), false);
            tex.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
            tex.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        } catch (Exception e) {
            System.out.println("Error loading texture ");
            e.printStackTrace();
        }
        return tex;
    }

    private void calculateControlPoints() {
        control_points[3] = (float) (0.5 * Math.sin(0.5 + time * 0.5));
        control_points[6] = (float) (0.5 *  Math.sin(time * 0.5));
//        for (int i = 1; i < control_points.length / 3; i++) {
//            control_points[3 * i] = (float) Math.sin(time * 0.5);
//        }
    }


    private void drawAxis(int x, int y, int z, float red, float green, float blue) {
        gl.glBegin(gl.GL_LINE_LOOP);
        gl.glColor3f(red, green, blue);
        gl.glVertex3f(0f, 0f, 0f);
        gl.glVertex3f(x * 10f, y * 10f, z * 10f);
        gl.glColor3f(255, 255, 255);
        gl.glEnd();
    }

    public void display(GLAutoDrawable gLDrawable) {

        gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        float r = 10f;
        float x = (float) (r*Math.sin(theta) *Math.cos(phi));
        float y = (float) (r*Math.sin(theta)*Math.sin(phi));
        float z = (float) (r*Math.cos(theta));

        float upX = (float) (-Math.cos(theta)*Math.cos(phi));
        float upY = (float) (-Math.cos(theta)*Math.sin(phi));
        float upZ = (float) Math.sin(theta);

        glu.gluLookAt(x, y, z, 0, 0, 0, upX, upY, upZ);        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
                GL2.GL_DECAL);
        tex1.bind(gl);

        this.drawAxis(1, 0, 0, 1, 0, 0);
        this.drawAxis(0, 1, 0, 0, 1, 0);
        this.drawAxis(0, 0, 1, 0, 0, 1);

        // gl.glDisable(GL.GL_TEXTURE_2D);
        float width = 1f / steps;

        this.calculateControlPoints();
        gl.glBegin(GL2.GL_QUAD_STRIP);

        for (int i = 0; i < steps; i++) {
            float t = i * width;

            float[] nextPoint = Bezier_eval.eval(control_points, t);


            gl.glTexCoord2f(t, 0);
            gl.glVertex3f(nextPoint[0], nextPoint[1], 0);


            gl.glTexCoord2f(t, 1);
            gl.glVertex3f(nextPoint[0], nextPoint[1], 1);
        }
        gl.glEnd();
        gl.glFlush();
        }

    public void displayChanged(GLAutoDrawable gLDrawable,
                               boolean modeChanged, boolean deviceChanged) {
    }

    public void init(GLAutoDrawable gLDrawable) {

        GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClearColor(0, 0, 0, 0);
        gl.glShadeModel(GL2.GL_FLAT);
        gl.glEnable(GL.GL_DEPTH_TEST);
        try {
            tex1 = loadTexture(gl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        control_points = new float[9];
        control_points[0] = 0;
        control_points[1] = 0;
        control_points[2] = 1;

        control_points[3] = 0;
        control_points[4] = 0.5f;
        control_points[5] = 1;

        control_points[6] = 0;
        control_points[7] = 1;
        control_points[8] = 1;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width,
                        int height) {
        GL2 gl = gLDrawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(60.0, 1.0, 0.1, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public static void main(String[] args) {
        Frame frame = new Frame("Raeumliche Beziertexturierung");
        Exercise1 canvas = new Exercise1();
        canvas.addGLEventListener(canvas);
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


