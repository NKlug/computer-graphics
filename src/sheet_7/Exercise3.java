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

import static com.jogamp.opengl.math.VectorUtil.*;

import java.awt.Frame;
import java.awt.event.*;
import java.io.InputStream;


public class Exercise3 {

    private static FPSAnimator animator;

    static class WinRenderer extends GLCanvas implements GLEventListener {

        private GL2 gl;
        private GLU glu;
        private Texture earthTexture;
        private Texture moonTexture;
        private Texture sunTexture;

        private float MOVE_STEP_SIZE = 0.5f;
        private float ANGLE_STEP_SIZE = 0.05f;

        private float[] eyePoint = new float[]{6, 0, 0};
        private float[] centerPoint = subVec3(fv(), eyePoint, new float[]{1, 0, 0});
        private float[] upVector;

        private float theta = 0;
        private float phi = (float) -Math.PI;

        float time = 0.0f;
        private float speed = 1;
        private boolean showCoordinates = false;
        private Texture starsTexture;

        public WinRenderer(GLCapabilities cap) {
            super(cap);

            moveView();

            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (e.getKeyChar() == '+') {
                        speed *= 2;
                    } else if (e.getKeyChar() == '-') {
                        speed *= 0.5;
                    } else if (e.getKeyChar() == 'a') {
                        relativeMoveY(-MOVE_STEP_SIZE);
                    } else if (e.getKeyChar() == 'd') {
                        relativeMoveY(MOVE_STEP_SIZE);
                    } else if (e.getKeyChar() == 'w') {
                        relativeMoveX(MOVE_STEP_SIZE);
                    } else if (e.getKeyChar() == 's') {
                        relativeMoveX(-MOVE_STEP_SIZE);
                    } else if (e.getKeyChar() == ' ') {
                        relativeMoveZ(MOVE_STEP_SIZE);
                    } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                        relativeMoveZ(-MOVE_STEP_SIZE);
                    } else if (e.getKeyChar() == 'f') {
                        showCoordinates = !showCoordinates;
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        phi -= ANGLE_STEP_SIZE;
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        phi += ANGLE_STEP_SIZE;
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        theta += ANGLE_STEP_SIZE;
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        theta -= ANGLE_STEP_SIZE;
                    }
                }
            });

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });
        }

        private float[] fv() {
            return new float[3];
        }

        private String arrayToString(float[] array) {
            StringBuilder result = new StringBuilder("[");
            for (int i = 0; i < array.length - 1; i++) {
                result.append(array[i]);
                result.append(", ");
            }
            if (array.length > 0)
                result.append(array[array.length - 1]);
            result.append("]");

            return result.toString();
        }

        private void printVectors() {
            System.out.println("Eye" + arrayToString(eyePoint));
            System.out.println("Center " + arrayToString(centerPoint));
            System.out.println("Up " + arrayToString(upVector));
            System.out.println();
        }

        private void moveView() {
            float[] centerPointOffset = new float[]{
                    (float) (Math.cos(theta) * Math.cos(phi)),
                    (float) (Math.cos(theta) * Math.sin(phi)),
                    (float) Math.sin(theta)};
            this.centerPoint = addVec3(fv(), eyePoint, centerPointOffset);

            this.upVector = new float[]{
                    (float) (Math.cos(theta + Math.PI / 2) * Math.cos(phi)),
                    (float) (Math.cos(theta + Math.PI / 2) * Math.sin(phi)),
                    (float) Math.sin(theta + Math.PI / 2)};

            printVectors();
        }

        private float[] getRelativeXVector() {
            return subVec3(fv(), centerPoint, eyePoint);
        }

        private float[] getRelativeYVector() {
            return crossVec3(fv(), getRelativeXVector(), upVector);
        }

        private float[] getRelativeZVector() {
            return crossVec3(fv(), getRelativeYVector(), getRelativeXVector());
        }

        private void relativeMoveX(float stepSize) {
            float[] toMove = scaleVec3(fv(), getRelativeXVector(), stepSize);
            this.eyePoint = addVec3(fv(), this.eyePoint, toMove);
            this.centerPoint = addVec3(fv(), this.centerPoint, toMove);
        }

        private void relativeMoveY(float stepSize) {
            float[] toMove = scaleVec3(fv(), getRelativeYVector(), stepSize);
            this.eyePoint = addVec3(fv(), this.eyePoint, toMove);
            this.centerPoint = addVec3(fv(), this.centerPoint, toMove);
        }

        private void relativeMoveZ(float stepSize) {
            float[] toMove = scaleVec3(fv(), getRelativeZVector(), stepSize);
            this.eyePoint = addVec3(fv(), this.eyePoint, toMove);
            this.centerPoint = addVec3(fv(), this.centerPoint, toMove);
        }


        private void forwardTime() {
            time += speed * (0.05f % 24 * 365);
        }

        public void display(GLAutoDrawable gLDrawable) {
            forwardTime();
            gl = gLDrawable.getGL().getGL2();
            glu = new GLU();
            final GLUT glut = new GLUT();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();

            gl.glColor3f(1f, 1f, 1f);

            glu.gluLookAt(eyePoint[0], eyePoint[1], eyePoint[2], centerPoint[0],
                    centerPoint[1], centerPoint[2], upVector[0],
                    upVector[1], upVector[2]);

            this.drawSun();
            this.drawStars();

            gl.glPushMatrix();
            gl.glRotatef(time / 24 / 365 * 360, 0, 0, 1);
            gl.glTranslatef(3, 0, 0);

            gl.glRotatef(23.44f, 1, 0, 0);
            this.drawEarth();

            gl.glPushMatrix();
            gl.glRotatef(-23.44f + 6f, 1, 0, 0);
            drawMoon();
            gl.glPopMatrix();
            gl.glPopMatrix();
            gl.glFlush();
        }

        private void drawStars() {
            gl.glPushMatrix();
            GLUquadric sphere_quadric = glu.gluNewQuadric();
            gl.glEnable(GL.GL_TEXTURE_2D);
            starsTexture.bind(gl);
            glu.gluQuadricTexture(sphere_quadric, true);
            glu.gluQuadricDrawStyle(sphere_quadric, GLU.GLU_FILL);
            glu.gluQuadricNormals(sphere_quadric, GLU.GLU_SMOOTH);
            glu.gluSphere(sphere_quadric, 50f, 50, 50);
            gl.glPopMatrix();
        }


        private void drawAxis(int x, int y, int z, float red, float green, float blue) {
            if (!showCoordinates) {
                return;
            }
            gl.glBegin(gl.GL_LINE_LOOP);
            gl.glColor3d(red, green, blue);
            gl.glVertex3f(0f, 0f, 0f);
            gl.glVertex3f(x * 10f, y * 10f, z * 10f);
            gl.glColor3d(255, 255, 255);
            gl.glEnd();
        }

        private void drawEarth() {
            gl.glPushMatrix();
            gl.glRotatef(time / 24f * 360, 0, 0, 1);

            this.drawAxis(1, 0, 0, 1, 0, 0);
            this.drawAxis(0, 0, 1, 0, 1, 0);

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
            gl.glRotatef(time / 30 / 24 * 360, 0, 0, 1);
            gl.glTranslatef(1f, 0, 0);

            this.drawAxis(1, 0, 0, 1, 0, 0);
            this.drawAxis(0, 0, 1, 0, 1, 0);

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

            this.drawAxis(1, 0, 0, 1, 0, 0);
            this.drawAxis(0, 0, 1, 0, 1, 0);

            GLUquadric sphere_quadric = glu.gluNewQuadric();
            gl.glEnable(GL.GL_TEXTURE_2D);
            sunTexture.bind(gl);
            glu.gluQuadricTexture(sphere_quadric, true);
            glu.gluQuadricDrawStyle(sphere_quadric, GLU.GLU_FILL);
            glu.gluQuadricNormals(sphere_quadric, GLU.GLU_SMOOTH);
            glu.gluSphere(sphere_quadric, 1f, 50, 50);
            gl.glPopMatrix();
        }

        public void init(GLAutoDrawable gLDrawable) {
            gl = gLDrawable.getGL().getGL2();
            gl.glEnable(GL2.GL_DEPTH_TEST);
            try {
                InputStream in = getClass().getClassLoader().getResourceAsStream("sheet_7/map.jpg");
                earthTexture = TextureIO.newTexture(in, true, "jpg");
                in = getClass().getClassLoader().getResourceAsStream("sheet_7/moon-map.jpg");
                moonTexture = TextureIO.newTexture(in, true, "jpg");
                in = getClass().getClassLoader().getResourceAsStream("sheet_7/sun-map.jpg");
                sunTexture = TextureIO.newTexture(in, true, "jpg");
                in = getClass().getClassLoader().getResourceAsStream("sheet_7/stars.png");
                starsTexture = TextureIO.newTexture(in, true, "png");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void dispose(GLAutoDrawable drawable) {

        }

        public void reshape(GLAutoDrawable gLDrawable, int x, int y,
                            int width, int height) {
            gl = gLDrawable.getGL().getGL2();
            glu = new GLU();
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            glu.gluPerspective(80, (float) width / (float) height, 1, 100);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
        }
    }

    public static void main(String[] args) {
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
            public void windowClosing(WindowEvent e) {
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
