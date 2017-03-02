package simonchiu.annihilationintelligence.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import simonchiu.annihilationintelligence.Activity.GameActivity;
import simonchiu.annihilationintelligence.Class.Button;
import simonchiu.annihilationintelligence.Class.Floors.FloorThird;
import simonchiu.annihilationintelligence.Class.Floors.FloorFourth;
import simonchiu.annihilationintelligence.Class.Joystick;
import simonchiu.annihilationintelligence.Class.PauseMenu;
import simonchiu.annihilationintelligence.Include.AGLFont;
import simonchiu.annihilationintelligence.Include.Rectangle;
import simonchiu.annihilationintelligence.R;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import static java.security.AccessController.getContext;
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
    private static GameActivity master = null;
    private Context context;

    //X and Y rotations for the camera
    private float xRot = 0.f;
    private float yRot = 0.f;

    private SimpleVector cameraRay = new SimpleVector(0.f, 0.f, 0.f);

    //Array of Joysticks
    private Joystick[] aMove = new Joystick[2];

    //Array of Buttons
    private Button[] aButtons = new Button[2];

    private PauseMenu pauseMenu;

    //Screen Size;
    private Point pPoint;

    private int iFloor = 3;
    private FloorThird FloorThird;
    private FloorFourth FloorFourth;

    //if game is paused
    private boolean bPaused = false;

    private AGLFont[] AGLFont = new AGLFont[2];
    private Paint Paint = new Paint();
    private Button[] aPauseButtons = new Button[3];
    private boolean bDestroy = false;

    private boolean[] bOptionData = new boolean[5]; //Array of booleans for the checkboxes and radio groups under Defines (using class Defines)
    private int[] iVolume = new int[2];             //Array of the volume for music (0) and sound (1)

    public void setOptions(boolean[] optionData, int[] volume) {
        bOptionData = optionData;
        iVolume = volume;
    }

    //Constructor
    public GameSurfaceView(Context context, Point point) {
        //Set up two fonts, one large, one small
        Paint = new Paint();
        Paint.setAntiAlias(true);
        Paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
        Paint.setTextSize(100);
        AGLFont[0] = new AGLFont(Paint);
        Paint.setTextSize(50);
        AGLFont[1] = new AGLFont(Paint);

        //Get the context, which allows us to load assets
        this.context = context;
        //The Width and Height of the screen
        pPoint = point;

        if (master == null) {
            //Load the floors
            FloorThird = new FloorThird(context);
            //FloorFourth = new FloorFourth(context);

            //Initialise Joystick array
            //Set as 300 pixels away from both corners, with a radius of 128
            aMove[0] = new Joystick(200, pPoint.y - 200, 128, context);
            aMove[1] = new Joystick(pPoint.x - 200, pPoint.y - 200, 128, context);

            //Initialise Button array
            //Set options button and interact button
            aButtons[0] = new Button(0, pPoint.x - 250, pPoint.y - 556, 128, context);
            aButtons[1] = new Button(1, 128, 128, 128, context);

            pauseMenu = new PauseMenu(pPoint.x/2, pPoint.y/2, pPoint.x/2 - 50, pPoint.y/2 - 50, context);

            aPauseButtons[0] = new Button("Pause Menu", pPoint.x/2, pPoint.y/10*2, 320, 100, context);
            aPauseButtons[1] = new Button("Resume", pPoint.x/2, pPoint.y/2, 250, 100, context);
            aPauseButtons[2] = new Button("Return to Menu", pPoint.x/2, pPoint.y/10*8, 380, 100, context);

            if (master == null) {
                Logger.log("Saving master Activity!");
                master = ((GameActivity) context);
            }
        }
    }

    public void touchEvent(MotionEvent me) {
        Camera cam = new Camera();

        //Get Camera of current floor
        if (iFloor == 3) {
            cam = FloorThird.GetWorld().getCamera();
        }

        //The current pointer we are checking the onTouchEvent for (0 = first finger)
        int pointerIndex = me.getActionIndex();

        aButtons[0].Update(me.getX(pointerIndex), me.getY(pointerIndex));
        aButtons[1].Update(me.getX(pointerIndex), me.getY(pointerIndex));

        //if Options button is pressed
        if (me.getActionMasked() == MotionEvent.ACTION_DOWN || me.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            if (aButtons[1].GetPressed()) {
                Logger.log("Options");
                if (!bPaused) {
                    ((GameActivity) context).PlaySound(SOUND_SELECT);
                    bPaused = true;
                }
            }

            aPauseButtons[1].Update(me.getX(pointerIndex), me.getY(pointerIndex));
            aPauseButtons[2].Update(me.getX(pointerIndex), me.getY(pointerIndex));

            if (bPaused) {
                if (aPauseButtons[1].GetPressed()) {
                    ((GameActivity) context).PlaySound(SOUND_SELECT);
                    bPaused = false;
                }
                else if (aPauseButtons[2].GetPressed()) {
                    ((GameActivity) context).PlaySound(SOUND_SELECT);
                    bDestroy = true;
                }
            }
        }

        //if game is not paused, can update inputs
        if (!bPaused) {
            //Action is down for first finger
            if (me.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if (me.getX(pointerIndex) < pPoint.x / 2) {
                    aMove[0].SetState(pointerIndex + 1);
                } else {
                    aMove[1].SetState(pointerIndex + 1);
                }

                //if Interact button is pressed
                if (aButtons[0].GetPressed()) {
                    SimpleVector tester = cam.getPosition();
                    tester.z += 1;
                    ((GameActivity) context).PlaySound(SOUND_SELECT);
                    /*
                    if (object[0].rayIntersectsAABB(tester, cameraRay) < 30.f) {
                        //Logger.log("HIT!");
                        object[0].setVisibility(false);
                    }*/
                }
            }
            //Action is down for any finger other than first
            if (me.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
                if (me.getX(pointerIndex) < pPoint.x / 2) {
                    aMove[0].SetState(pointerIndex + 1);
                } else {
                    aMove[1].SetState(pointerIndex + 1);
                }

                //if Interact button is pressed
                if (aButtons[0].GetPressed()) {
                    SimpleVector tester = cam.getPosition();
                    tester.z += 1;
                    ((GameActivity) context).PlaySound(SOUND_SELECT);
                    /*
                    if (object[0].rayIntersectsAABB(tester, cameraRay) < 30.f) {
                        //Logger.log("HIT!");
                        object[0].setVisibility(false);
                    }*/
                }
            }

            //Action is up for last finger, so reset all
            if (me.getActionMasked() == MotionEvent.ACTION_UP) {
                aMove[0].Reset();
                aMove[1].Reset();
            }
            //Action is up for a finger other than last finger
            if (me.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
                if (me.getX(pointerIndex) < pPoint.x / 2 && aMove[0].GetState() == pointerIndex + 1) {
                    aMove[0].Reset();
                } else if (aMove[1].GetState() == pointerIndex + 1) {
                    aMove[1].Reset();
                }
            }

            //Update joysticks with touch data
            if (aMove[0].GetState() != 0) {
                aMove[0].Update(me.getX(pointerIndex), me.getY(pointerIndex));
            }
            if (aMove[1].GetState() != 0) {
                aMove[1].Update(me.getX(pointerIndex), me.getY(pointerIndex));
            }

            //If the action is moving, then check through all pointers and set them accordingly
            //This fixes the problems of multiple finger presses to the screen
            if (me.getAction() == MotionEvent.ACTION_MOVE) {
                int pointerCount = me.getPointerCount();

                for (int i = 0; i < pointerCount; i++) {
                    pointerIndex = i;
                    int pointerId = me.getPointerId(pointerIndex);

                    if (pointerId == 0) {
                        if (aMove[0].GetState() == 1) {
                            aMove[0].Update(me.getX(pointerIndex), me.getY(pointerIndex));
                        } else if (aMove[1].GetState() == 1) {
                            aMove[1].Update(me.getX(pointerIndex), me.getY(pointerIndex));
                        }
                    }
                    if (pointerId == 1) {
                        if (aMove[0].GetState() == 2) {
                            aMove[0].Update(me.getX(pointerIndex), me.getY(pointerIndex));
                        } else if (aMove[1].GetState() == 2) {
                            aMove[1].Update(me.getX(pointerIndex), me.getY(pointerIndex));
                        }
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
        Camera cam = new Camera();

        //Get Camera of current floor
        if (iFloor == 3) {
            cam = FloorThird.GetWorld().getCamera();
        }

        //if game is not paused
        if (!bPaused) {
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
                if (bOptionData[INVERTY]) xRot -= touchTurnUp;
                else xRot += touchTurnUp;
                if (xRot > 90 * DEG_TO_RAD) {
                    xRot = 90 * DEG_TO_RAD;
                } else if (xRot < -90 * DEG_TO_RAD) {
                    xRot = -90 * DEG_TO_RAD;
                }
            }

            //Get amount to rotate in yaw
            if (touchTurn != 0) {
                if (bOptionData[INVERTX]) yRot -= touchTurn;
                else yRot += touchTurn;
            }

            //Move Camera
            if (touchMove != 0 || touchMoveUp != 0) {
                SimpleVector moveVector = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                cam.moveCamera(moveVector, 10f);
                float xCamPos = cam.getPosition().x;
                float yCamPos = cam.getPosition().z;

                //Check collisions on current floor
                //If colliding with anything, move back to original position
                if (iFloor == 3) {
                    int check = FloorThird.Collisions(xCamPos, yCamPos);
                    if (check == 1) {
                        SimpleVector testVector = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                        cam.moveCamera(testVector, -10f);
                    }
                    else {
                        //Wall Collisions
                        if (FloorThird.CollisionsWallX(xCamPos)) {
                            SimpleVector testVector = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, 0.f);
                            cam.moveCamera(testVector, -10f);
                        }
                        if (FloorThird.CollisionsWallY(yCamPos)) {
                            SimpleVector testVector = new SimpleVector(0.f, 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                            cam.moveCamera(testVector, -10f);
                        }
                    }
                }
            }
        }

        //Rotate Camera
        SimpleVector cameraVector = cam.getPosition();
        cameraVector.z += 1f;

        if (!bPaused) {
            cam.lookAt(cameraVector);
            cam.rotateY(yRot);
            cam.rotateX(xRot);
        }

        if (iFloor == 3 && !bDestroy) {
            fb.clear(bg);
            FloorThird.GetWorld().renderScene(fb);
            FloorThird.GetWorld().draw(fb);
            fb.display();
        }

        cameraRay = new SimpleVector(-sin(yRot), -xRot, cos(yRot));

        //Draw and display UI
        if (!bDestroy) {
            //Draw Joysticks
            for (int i = 0; i < aMove.length; i++) {aMove[i].Draw(fb);}

            //Draw Buttons
            for (int i = 0; i < aButtons.length; i++) {aButtons[i].Draw(fb);}

            //Draw pause menu
            if (bPaused) {
                pauseMenu.Draw(fb);

                aPauseButtons[0].Draw(fb, AGLFont[0]);
                aPauseButtons[1].Draw(fb, AGLFont[0]);
                aPauseButtons[2].Draw(fb, AGLFont[0]);
            }

            if (iFloor == 3) {
                FloorThird.GetWorld().renderScene(fb);
            }
            fb.removeRenderTarget();

            //Reset
            for (int i = 0; i < aButtons.length; i++) {aButtons[i].Reset();}
            aPauseButtons[1].Reset();
            aPauseButtons[2].Reset();
        }

        if (System.currentTimeMillis() - time >= 1000) {
            //Logger.log(fps + "fps");
            fps = 0;
            time = System.currentTimeMillis();
        }
        fps++;

        if (bDestroy) {
            master = null;
            fb.dispose();
            if (iFloor == 3) {
                FloorThird.Destroy();
            }
            Intent intent = new Intent();
            intent.putExtra("completed", false);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ((GameActivity) context).setResult(GAME, intent);
            ((GameActivity) context).finish();
        }
    }

}
