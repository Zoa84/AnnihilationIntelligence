package simonchiu.annihilationintelligence.View;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;

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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import simonchiu.annihilationintelligence.Activity.GameActivity;
import simonchiu.annihilationintelligence.Class.Button;
import simonchiu.annihilationintelligence.Class.Joystick;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import static simonchiu.annihilationintelligence.Class.Defines.*;
import static simonchiu.annihilationintelligence.Class.TransformFix.*;

/**
 * Created by Simon on 16/02/2017.
 */

//The game surface view, ran as part of game activity to draw and render in 3d, and to implement the game

public class GameSurfaceView implements GLSurfaceView.Renderer {

    //Variables for game surface view
    private long time = System.currentTimeMillis();
    private FrameBuffer fb = null;
    private World world = null;
    private RGBColor bg = new RGBColor(50, 50, 100);
    private int fps = 0;
    private Light sun = null;
    private static GameActivity master = null;
    private Context context;

    private float xrot = 0.f;
    private float yrot = 0.f;

    private SimpleVector testery = new SimpleVector(0.f, 0.f, 0.f);

    //String of texture names, used to load textures and objects
    private String[] textures = {"screwdriver", "pointytree", "floor", "skybox", "table", "chair", "lamp"};
    private Texture[] aTextures = new Texture[textures.length];
    private Object3D[] object = new Object3D[textures.length];
    private Object3D[] aObjects = null;

    //Array of strings of UI texture names, and array of textures themselves
    private String[] uint = {"inv", "inte", "interact"};
    private Texture[] uintT = new Texture[uint.length];

    //Array of Joysticks
    private Joystick[] aMove = new Joystick[2];

    //Array of Buttons
    private Button[] aButtons = new Button[2];

    //Screen Size;
    private Point pPoint;

    private boolean[] bOptionData = new boolean[5]; //Array of booleans for the checkboxes and radio groups under Defines (using class Defines)
    private int[] iVolume = new int[2];             //Array of the volume for music (0) and sound (1)

    public void setOptions(boolean[] optionData, int[] volume) {
        bOptionData = optionData;
        iVolume = volume;
    }

    //Constructor
    public GameSurfaceView(Context context, Point point) {
        //Get the context, which allows us to load assets
        this.context = context;
        pPoint = point;

        //
        if (master == null) {

            world = new World();
            world.setAmbientLight(20, 20, 20);

            sun = new Light(world);
            sun.setIntensity(250, 250, 250);

            Texture texture;

            //For loop loading textures
            int resID;
            for (int i = 0; i < textures.length; i++) {
                resID = context.getResources().getIdentifier(textures[i], "drawable", context.getPackageName());
                texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 512, 512));
                TextureManager.getInstance().addTexture(textures[i], texture);
                aTextures[i] = texture;
            }

            //Load UI textures
            for (int i = 0; i < uint.length; i++) {
                resID = context.getResources().getIdentifier(uint[i], "drawable", context.getPackageName());
                texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 256, 256));
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
                    is = context.getResources().getAssets().open("objects/" + textures[i] + ".obj");
                    aObjects = Loader.loadOBJ(is, null, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                object[i] = aObjects[0];
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
            object[1].translate(fixTrans(25, 0, 0));
            object[2].translate(fixTrans(0f, -8.5f, 0f));
            object[3].scale(50);
            object[3].translate(fixTrans(0, 0, 100));
            object[4].translate(fixTrans(-10f, -6f, 0f));
            object[5].translate(fixTrans(-11f, -5.5f, 4f));
            object[5].rotateY(20f * DEG_TO_RAD);
            object[6].translate(fixTrans(-7f, -3.5f, 0f));
            object[6].rotateY(30 * DEG_TO_RAD);

            //Like shaders, ROTATE then TRANSLATE
            //Must also rotate and translate mesh for collisions
            object[0].rotateX(25 * DEG_TO_RAD);
            object[0].rotateZ(90 * DEG_TO_RAD);
            object[0].rotateMesh();
            object[0].clearRotation();

            object[0].translate(fixTrans(-12f, -3.4f, 0f));
            object[0].translateMesh();
            object[0].clearTranslation();

            sv.y -= 100;
            sv.z -= 100;
            sun.setPosition(sv);
            MemoryHelper.compact();

            //Initialise Joystick array
            //Set as 300 pixels away from both corners, with a radius of 128
            aMove[0] = new Joystick(200, pPoint.y - 200, 128, context);
            aMove[1] = new Joystick(pPoint.x - 200, pPoint.y - 200, 128, context);

            //Initialise Button array
            //Set options button and interact button
            aButtons[0] = new Button(0, pPoint.x - 250, pPoint.y - 556, 128, context);
            aButtons[1] = new Button(1, 128, 128, 128, context);

            if (master == null) {
                Logger.log("Saving master Activity!");
                master = ((GameActivity) context);
            }
        }
    }

    public void touchEvent(MotionEvent me) {
        Camera cam = world.getCamera();

        //The current pointer we are checking the onTouchEvent for (0 = first finger)
        int pointerIndex = me.getActionIndex();

        aButtons[0].Update(me.getX(pointerIndex), me.getY(pointerIndex));
        aButtons[1].Update(me.getX(pointerIndex), me.getY(pointerIndex));

        //Action is down for first finger
        if (me.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            if (me.getX(pointerIndex) < pPoint.x/2)
            {
                aMove[0].SetState(pointerIndex+1);
            }
            else
            {
                aMove[1].SetState(pointerIndex+1);
            }

            //if Interact button is pressed
            if (aButtons[0].GetPressed())
            {
                SimpleVector tester = cam.getPosition();
                if (object[0].rayIntersectsAABB(tester, testery) < 30.f)
                {
                    //Logger.log("HIT!");
                    object[0].setVisibility(false);
                }
            }

            //if Options button is pressed
            if (aButtons[1].GetPressed())
            {

            }
        }
        //Action is down for any finger other than first
        if (me.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN)
        {
            if (me.getX(pointerIndex) < pPoint.x/2)
            {
                aMove[0].SetState(pointerIndex+1);
            }
            else
            {
                aMove[1].SetState(pointerIndex+1);
            }

            //if Interact button is pressed
            if (aButtons[0].GetPressed())
            {
                SimpleVector tester = cam.getPosition();
                if (object[0].rayIntersectsAABB(tester, testery) < 30.f)
                {
                    //Logger.log("HIT!");
                    object[0].setVisibility(false);
                }
            }

            //if Options button is pressed
            if (aButtons[1].GetPressed())
            {

            }
        }

        //Action is up for last finger, so reset all
        if (me.getActionMasked() == MotionEvent.ACTION_UP)
        {
            aMove[0].Reset();
            aMove[1].Reset();
        }
        //Action is up for a finger other than last finger
        if (me.getActionMasked() == MotionEvent.ACTION_POINTER_UP)
        {
            if (me.getX(pointerIndex) < pPoint.x/2 && aMove[0].GetState() == pointerIndex + 1)
            {
                aMove[0].Reset();
            }
            else if (aMove[1].GetState() == pointerIndex + 1)
            {
                aMove[1].Reset();
            }
        }

        //Update joysticks with touch data
        if (aMove[0].GetState() != 0)
        {
            aMove[0].Update(me.getX(pointerIndex), me.getY(pointerIndex));
        }
        if (aMove[1].GetState() != 0)
        {
            aMove[1].Update(me.getX(pointerIndex), me.getY(pointerIndex));
        }

        //If the action is moving, then check through all pointers and set them accordingly
        //This fixes the problems of multiple finger presses to the screen
        if (me.getAction() == MotionEvent.ACTION_MOVE)
        {
            int pointerCount = me.getPointerCount();

            for (int i = 0; i < pointerCount; i++)
            {
                pointerIndex = i;
                int pointerId = me.getPointerId(pointerIndex);

                if (pointerId == 0)
                {
                    if (aMove[0].GetState() == 1)
                    {
                        aMove[0].Update(me.getX(pointerIndex), me.getY(pointerIndex));
                    }
                    else if (aMove[1].GetState() == 1)
                    {
                        aMove[1].Update(me.getX(pointerIndex), me.getY(pointerIndex));
                    }
                }
                if (pointerId == 1)
                {
                    if (aMove[0].GetState() == 2)
                    {
                        aMove[0].Update(me.getX(pointerIndex), me.getY(pointerIndex));
                    }
                    else if (aMove[1].GetState() == 2)
                    {
                        aMove[1].Update(me.getX(pointerIndex), me.getY(pointerIndex));
                    }
                }
            }
        }
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        if (fb != null) {
            fb.dispose();
        }
        fb = new FrameBuffer(gl, w, h);
    }

    public void onDrawFrame(GL10 gl) {
        Camera cam = world.getCamera();

        //Set the movement and rotation data
        float touchMove = -aMove[0].GetHor();
        float touchMoveUp = aMove[0].GetVer();
        float touchTurn = aMove[1].GetHor();
        float touchTurnUp = aMove[1].GetVer();

        //Set rotation data. Rotating in X is Pitch, Y is Yaw
        //However, in games, inverting x axis affects the yaw of the camera, and inverting the y axis affects the pitch
        //Therefore, the rotation in pitch (or x) uses the invert y from option data, and the rotation in yaw (or y)
        //uses the invert x from option data

        //Get amount to rotate in pitch
        if (touchTurnUp != 0) {
            if (bOptionData[INVERTY]) xrot -= touchTurnUp;
            else xrot += touchTurnUp;
            if (xrot > 90 * DEG_TO_RAD)
            {
                xrot = 90 * DEG_TO_RAD;
            }
            else if (xrot < -90 * DEG_TO_RAD)
            {
                xrot = -90 * DEG_TO_RAD;
            }
        }

        //Get amount to rotate in yaw
        if (touchTurn != 0) {
            if (bOptionData[INVERTX]) yrot -= touchTurn;
            else yrot += touchTurn;
        }

        //Move Camera
        if (touchMove != 0 || touchMoveUp != 0) {
            SimpleVector moveVector = new SimpleVector((touchMove * cos(yrot)) - (touchMoveUp * sin(yrot)), 0.f, (touchMoveUp * cos(yrot)) + (touchMove * sin(yrot)));
            cam.moveCamera(moveVector, 10f);
        }

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
        fb.clear(bg);
        world.renderScene(fb);
        world.draw(fb);
        fb.display();

        //Array of x y positions and sizes
        Rect buttons[] = {new Rect(0, 256, 256, 256), new Rect(1664, 256, 256, 256), new Rect(960, 512, 256, 256)};

        //Draw and display UI
        //get textures starting x and y, x and y start position on screen, and size to draw
        //BLACK is transparent, or use .png with transparency
        for (int i = 0; i < 2; i++) {
            fb.blit(uintT[i], 0, 0, buttons[i].left, buttons[i].top, buttons[i].right, buttons[i].bottom, FrameBuffer.TRANSPARENT_BLITTING);
        }
        //Draw interact text if can interact
        if (object[0].rayIntersectsAABB(cameraVector, cameraView) < 30.f && object[0].getVisibility())
        {
            //Looking at pickable object
            fb.blit(uintT[2], 0, 0, buttons[2].left, buttons[2].top, buttons[2].right, buttons[2].bottom, FrameBuffer.TRANSPARENT_BLITTING);
        }

        //Draw Joysticks
        for (int i = 0; i < aMove.length; i++) {
            aMove[i].Draw(fb);
        }

        //Draw Buttons
        for (int i = 0; i < aButtons.length; i++) {
            aButtons[i].Draw(fb);
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
