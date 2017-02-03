package simonchiu.annihilationintelligence.Activity;

import android.app.Activity;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * A simple demo. This shows more how to use jPCT-AE than it shows how to write
 * a proper application for Android. It includes basic activity management to
 * handle pause and resume...
 *
 * @author EgonOlsen
 *
 */
public class GameActivity extends Activity {

    // Used to handle pause and resume...
    private static GameActivity master = null;

    private GLSurfaceView mGLView;
    private MyRenderer renderer = null;
    private FrameBuffer fb = null;
    private World world = null;
    private RGBColor back = new RGBColor(50, 50, 100);

    private float touchTurn = 0;
    private float touchTurnUp = 0;

    private float touchMove = 0;
    private float touchMoveUp = 0;

    private float xpos = -1;
    private float ypos = -1;

    private float xrot = 0.f;
    private float yrot = 0.f;

    private float DEG_TO_RAD = 3.141f / 180f;
    private int invCam = 1;

    private SimpleVector testery = new SimpleVector(0.f, 0.f, 0.f);

    //String of texture names, used to load textures and objects
    private String[] textures = {"screwdriver", "pointytree", "floor", "skybox", "table", "chair", "lamp"};
    private Texture[] tTextures = new Texture[textures.length];
    private Object3D[] object = new Object3D[textures.length];
    private Object3D[] tObjects = null;

    //Array of strings of UI texture names, and array of textures themselves
    private String[] uint = {"inv", "inte", "interact"};
    private Texture[] uintT = new Texture[uint.length];

    private int fps = 0;

    private Light sun = null;

    protected void onCreate(Bundle savedInstanceState) {

        Logger.log("onCreate");

        if (master != null) {
            copy(master);
        }

        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(getApplication());

        mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                // Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
                // back to Pixelflinger on some device (read: Samsung I7500)
                int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
                EGLConfig[] configs = new EGLConfig[1];
                int[] result = new int[1];
                egl.eglChooseConfig(display, attributes, configs, 1, result);
                return configs[0];
            }
        });

        renderer = new MyRenderer();
        mGLView.setRenderer(renderer);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void copy(Object src) {
        try {
            Logger.log("Copying data from master Activity!");
            Field[] fs = src.getClass().getDeclaredFields();
            for (Field f : fs) {
                f.setAccessible(true);
                f.set(this, f.get(src));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean onTouchEvent(MotionEvent me) {
        Camera cam = world.getCamera();
        if (me.getY() > 512) {
            if (me.getX() > 960.f) {
                //stop moving while turning
                //REMOVE FOR BOTH LATER
                touchMove = 0;
                touchMoveUp = 0;
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    xpos = me.getX();
                    ypos = me.getY();
                }

                if (me.getAction() == MotionEvent.ACTION_MOVE) {
                    float xd = me.getX() - xpos;
                    float yd = me.getY() - ypos;

                    if (xpos != -1) {
                        touchTurn = xd / -3000f;
                        touchTurnUp = yd / -3000f;
                    } else {
                        touchTurn = 0;
                        touchTurnUp = 0;
                    }
                }
            } else {
                if (me.getAction() == MotionEvent.ACTION_MOVE) {
                    touchTurn = 0;
                    touchTurnUp = 0;
                }
            }

            //Movement
            if (me.getX() < 960.f) {
                //stop turning while moving
                //REMOVE FOR BOTH LATER
                touchTurn = 0;
                touchTurnUp = 0;
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    xpos = me.getX();
                    ypos = me.getY();
                }

                if (me.getAction() == MotionEvent.ACTION_MOVE) {
                    float xd = me.getX() - xpos;
                    float yd = me.getY() - ypos;
                    if (xpos != -1) {
                        touchMove = xd / 3000f;
                        touchMoveUp = yd / -3000f;
                    }
                }
            } else {
                if (me.getAction() == MotionEvent.ACTION_MOVE) {
                    touchMove = 0;
                    touchMoveUp = 0;
                }
            }
        }
        else //if touching top half of screen
        {
            //Within Y of buttons
            if (me.getY() > 256 && me.getY() < 512)
            {
                //Normally should check if larger then left side, but against wall
                //Within X if Invert camera axis
                if (me.getX() < 256)
                {
                    if (me.getAction() == MotionEvent.ACTION_UP) {
                        invCam *= -1;
                    }
                }
                //Within X if Interact button
                else if (me.getX() > 1664 && me.getX() < 1920)
                {
                    if (me.getAction() == MotionEvent.ACTION_UP) {
                        SimpleVector tester = cam.getPosition();
                        if (object[0].rayIntersectsAABB(tester, testery) < 30.f)
                        {
                            //Logger.log("HIT!");
                            object[0].setVisibility(false);
                        }
                    }
                }
            }
        }

        if (me.getAction() == MotionEvent.ACTION_UP) {
            xpos = -1;
            ypos = -1;
            touchTurn = 0;
            touchTurnUp = 0;
            touchMove = 0;
            touchMoveUp = 0;
        }

        try {
            Thread.sleep(15);
        } catch (Exception e) {
            // No need for this...
        }

        return super.onTouchEvent(me);
    }

    protected boolean isFullscreenOpaque() {
        return true;
    }

    class MyRenderer implements GLSurfaceView.Renderer {

        private long time = System.currentTimeMillis();

        public MyRenderer() {
        }

        public void onSurfaceChanged(GL10 gl, int w, int h) {
            if (fb != null) {
                fb.dispose();
            }
            fb = new FrameBuffer(gl, w, h);

            if (master == null) {

                world = new World();
                world.setAmbientLight(20, 20, 20);

                sun = new Light(world);
                sun.setIntensity(250, 250, 250);

                Texture texture;

                //For loop loading textures
                int resID;
                for (int i = 0; i < textures.length; i++) {
                    resID = getResources().getIdentifier(textures[i], "drawable", getPackageName());
                    texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(resID)), 512, 512));
                    TextureManager.getInstance().addTexture(textures[i], texture);
                    tTextures[i] = texture;
                }

                //Load UI textures
                for (int i = 0; i < uint.length; i++) {
                    resID = getResources().getIdentifier(uint[i], "drawable", getPackageName());
                    texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(resID)), 256, 256));
                    TextureManager.getInstance().addTexture(uint[i], texture);
                    uintT[i] = texture;
                }

                //Using an input stream, we get the obj by the objects name
                //and load it to tObjects, which is an array, as the loadobj function returns multiple objects
                //however in our case, it happens to only be one object each time.
                //This is then but into an array called object which contains all the loaded objects together
                //These can be set textures and built
                InputStream is;

                for (int i = 0; i < textures.length; i++) {
                    try {
                        is = getResources().getAssets().open(textures[i] + ".obj");
                        tObjects = Loader.loadOBJ(is, null, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    object[i] = tObjects[0];
                    object[i].setTexture(textures[i]);
                    object[i].build();
                    world.addObject(object[i]);

                    //Due to jPCT world axis, rotate around X-axis to draw right-side up
                    object[i].setRotationPivot(SimpleVector.ORIGIN);
                    object[i].rotateX(180 * DEG_TO_RAD);
                    object[i].rotateMesh();
                    object[i].clearRotation();
                }

                Camera cam = world.getCamera();
                cam.moveCamera(Camera.CAMERA_MOVEOUT, 100);

                SimpleVector sv = new SimpleVector();
                sv.set(object[0].getTransformedCenter());

                //Use Maya scene to draw whole scene
                //Create a load object from scene function, reversing all necessary values
                /*
                Need opposite Y translates
                Need opposite Z translates
                Need opposite X Rotates
                Need opposite Y Rotates
                */
                object[1].translate(25, 0, 0);
                object[2].translate(0f, 8.5f, 0f);
                object[3].scale(50);
                object[3].translate(0, 0, -100);
                object[4].translate(-10f, 6f, 0f);
                object[5].translate(-11f, 5.5f, -4f);
                object[5].rotateY(20f * DEG_TO_RAD);
                object[6].translate(-7f, 3.5f, 0f);
                object[6].rotateY(30 * DEG_TO_RAD);

                //Like shaders, ROTATE then TRANSLATE
                //Must also rotate and translate mesh for collisions
                object[0].rotateX(25 * DEG_TO_RAD);
                object[0].rotateZ(90 * DEG_TO_RAD);
                object[0].rotateMesh();
                object[0].clearRotation();

                object[0].translate(-12f, 3.4f, 0f);
                object[0].translateMesh();
                object[0].clearTranslation();

                sv.y -= 100;
                sv.z -= 100;
                sun.setPosition(sv);
                MemoryHelper.compact();

                if (master == null) {
                    Logger.log("Saving master Activity!");
                    master = GameActivity.this;
                }
            }
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        public void onDrawFrame(GL10 gl) {
            Camera cam = world.getCamera();

            //Get amount to rotate in x
            if (touchTurnUp != 0) {
                xrot += invCam * touchTurnUp;
                if (xrot > 90 * DEG_TO_RAD)
                {
                    xrot = 90 * DEG_TO_RAD;
                }
                else if (xrot < -90 * DEG_TO_RAD)
                {
                    xrot = -90 * DEG_TO_RAD;
                }
            }

            //Get amount to rotate in y
            if (touchTurn != 0) {
                yrot += invCam * touchTurn;
            }

            //Move Camera
            SimpleVector moveVector = new SimpleVector((touchMove * cos(yrot)) - (touchMoveUp * sin(yrot)), 0.f, (touchMoveUp * cos(yrot)) + (touchMove * sin(yrot)));
            cam.moveCamera(moveVector, 10f);

            //Rotate Camera
            SimpleVector cameraVector = cam.getPosition();
            cameraVector.z += 1f;
            cam.lookAt(cameraVector);
            cam.rotateY(yrot);
            cam.rotateX(xrot);

            //instead of outright vector, find in yrot,
            //then do a pythagoras check for angle?
            SimpleVector cameraView = new SimpleVector(-sin(yrot), -xrot, cos(yrot));
            /*
            cameraVector.z -= 1f;
            if (object[0].rayIntersectsAABB(cameraVector, cameraView) < 30.f)
            {
                //Logger.log("HIT!");
                object[0].setTexture(textures[1]);
            }
            else
            {
                object[0].setTexture(textures[0]);
            }*/

            //Draw and display 3D objects
            fb.clear(back);
            world.renderScene(fb);
            world.draw(fb);
            fb.display();

            //Array of x y positions and sizes
            Rect buttons[] = {new Rect(0, 256, 256, 256), new Rect(1664, 256, 256, 256), new Rect(960, 512, 256, 256)};

            //Draw and display UI
            //get textures starting x and y, x and y start position on screen, and size to draw
            //BLACK is transparent
            for (int i = 0; i < 2; i++) {
                fb.blit(uintT[i], 0, 0, buttons[i].left, buttons[i].top, buttons[i].right, buttons[i].bottom, FrameBuffer.TRANSPARENT_BLITTING);
            }
            //Draw interact text if can interact
            if (object[0].rayIntersectsAABB(cameraVector, cameraView) < 30.f && object[0].getVisibility())
            {
                //Looking at pickable object
                fb.blit(uintT[2], 0, 0, buttons[2].left, buttons[2].top, buttons[2].right, buttons[2].bottom, FrameBuffer.TRANSPARENT_BLITTING);
            }

            fb.setRenderTarget(uintT[0]);
            world.renderScene(fb);
            fb.removeRenderTarget();

            if (System.currentTimeMillis() - time >= 1000) {
                //Logger.log(fps + "fps");
                fps = 0;
                time = System.currentTimeMillis();
            }
            fps++;
        }
    }
}