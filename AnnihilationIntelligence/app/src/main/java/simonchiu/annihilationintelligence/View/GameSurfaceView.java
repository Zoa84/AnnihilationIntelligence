package simonchiu.annihilationintelligence.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.CountDownTimer;
import android.view.MotionEvent;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.util.BitmapHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import simonchiu.annihilationintelligence.Activity.GameActivity;
import simonchiu.annihilationintelligence.Class.Button;
import simonchiu.annihilationintelligence.Class.ElevMenu;
import simonchiu.annihilationintelligence.Class.Floors.FloorGround;
import simonchiu.annihilationintelligence.Class.Floors.FloorSecond;
import simonchiu.annihilationintelligence.Class.Floors.FloorThird;
import simonchiu.annihilationintelligence.Class.Floors.FloorFourth;
import simonchiu.annihilationintelligence.Class.Floors.FloorFirst;
import simonchiu.annihilationintelligence.Class.Inventory;
import simonchiu.annihilationintelligence.Class.Joystick;
import simonchiu.annihilationintelligence.Class.NumPad;
import simonchiu.annihilationintelligence.Class.PauseMenu;
import simonchiu.annihilationintelligence.Class.TextBuffer;
import simonchiu.annihilationintelligence.Include.AGLFont;
import simonchiu.annihilationintelligence.Include.Rectangle;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import static simonchiu.annihilationintelligence.Class.Defines.*;

/**
 * Created by Simon on 16/02/2017.
 */

//The game surface view, ran as part of game activity to draw and render in 3d, and to implement the game

public class GameSurfaceView implements GLSurfaceView.Renderer {

    //Variables for game surface view
    private long time = System.currentTimeMillis();
    private FrameBuffer fb = null;
    private RGBColor bg = new RGBColor(50, 50, 100);
    private int fps = 0;
    private static GameActivity master = null;
    private Context context;

    //X and Y rotations for the camera
    private float xRot = 0.f;
    private float yRot = 0.f;

    //Array of Joysticks
    private Joystick[] aMove = new Joystick[2];

    //Array of Buttons
    private Button[] aButtons = new Button[2];

    private PauseMenu pauseMenu;
    private Button[] aPauseButtons = new Button[3];

    private ElevMenu ElevMenu;
    private Button[] aElevButtons = new Button[5];

    private NumPad NumPad;
    private int[] iNumbers = {10, 10, 10};

    //Screen Size;
    private Point pPoint;

    //Distance to interact
    private float iDistance = 20f;

    private int iFloor = 3;
    private FloorThird FloorThird;
    private FloorFourth FloorFourth;
    private FloorSecond FloorSecond;
    private FloorFirst FloorFirst;
    private FloorGround FloorGround;

    //if game is paused
    private boolean bPaused = false;

    private AGLFont[] AGLFont = new AGLFont[2];

    private boolean bDestroy = false, bElev = false, bNumPad = false;

    private Inventory Inventory;                    //The inventory, manages collection, and selecting items

    private TextBuffer TextBuffer = new TextBuffer();

    private Texture tInteract = null, tDot = null, tLoading = null, tGameOver = null, tBlack = null, tWin = null;

    private boolean[] bOptionData = new boolean[5]; //Array of booleans for the checkboxes and radio groups under Defines (using class Defines)

    public void setOptions(boolean[] optionData) {
        bOptionData = optionData;
    }

    //Constructor
    public GameSurfaceView(Context context, Point point) {
        //Set up two fonts, one large, one small
        Paint Paint = new Paint();
        Paint.setAntiAlias(true);
        Paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
        Paint.setTextSize(100);
        AGLFont[0] = new AGLFont(Paint);
        Paint.setTextSize(50);
        AGLFont[1] = new AGLFont(Paint);

        Paint.reset();

        //Get the context, which allows us to load assets
        this.context = context;
        //The Width and Height of the screen
        pPoint = point;

        if (master == null) {
            //Load the floors
            FloorThird = new FloorThird(context);

            //Initialise Joystick array
            //Set as 300 pixels away from both corners, with a radius of 128
            aMove[0] = new Joystick(200, pPoint.y - 200, 128, context);
            aMove[1] = new Joystick(pPoint.x - 200, pPoint.y - 200, 128, context);

            //Initialise Button array
            //Set options button and interact button
            aButtons[0] = new Button(0, pPoint.x - 250, pPoint.y - 556, 128, context);
            aButtons[1] = new Button(1, 148, 148, 128, context);

            pauseMenu = new PauseMenu(pPoint.x/2, pPoint.y/2, pPoint.x/2 - 50, pPoint.y/2 - 50, context);

            ElevMenu = new ElevMenu(pPoint, context);

            NumPad = new NumPad(pPoint, context);

            aPauseButtons[0] = new Button("Pause Menu", pPoint.x/2, pPoint.y/10*2, 320, 100, context);
            aPauseButtons[1] = new Button("Resume", pPoint.x/2, pPoint.y/2, 250, 100, context);
            aPauseButtons[2] = new Button("Return to Menu", pPoint.x/2, pPoint.y/10*8, 380, 100, context);

            aElevButtons[0] = new Button(2, pPoint.x/5*2, 50 + ((pPoint.y-100)/6), 64, context);
            aElevButtons[1] = new Button(2, pPoint.x/5*2, 50 + 2*((pPoint.y-100)/6), 64, context);
            aElevButtons[2] = new Button(2, pPoint.x/5*2, 50 + 3*((pPoint.y-100)/6), 64, context);
            aElevButtons[3] = new Button(2, pPoint.x/5*2, 50 + 4*((pPoint.y-100)/6), 64, context);
            aElevButtons[4] = new Button(2, pPoint.x/5*2, 50 + 5*((pPoint.y-100)/6), 64, context);

            Inventory = new Inventory(pPoint, context);

            //Load interact text
            int resID;
            resID = context.getResources().getIdentifier("img_interact", "drawable", context.getPackageName());
            tInteract = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 256, 256));
            resID = context.getResources().getIdentifier("img_dot", "drawable", context.getPackageName());
            tDot = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));
            resID = context.getResources().getIdentifier("img_loading", "drawable", context.getPackageName());
            tLoading = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 512, 512));
            resID = context.getResources().getIdentifier("img_gameover", "drawable", context.getPackageName());
            tGameOver = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 512, 512));
            resID = context.getResources().getIdentifier("img_black", "drawable", context.getPackageName());
            tBlack = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));
            resID = context.getResources().getIdentifier("img_win", "drawable", context.getPackageName());
            tWin = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 512, 512));

            if (master == null) {
                Logger.log("Saving master Activity!");
                master = ((GameActivity) context);
            }

            //Starting text dialogue
            TextBuffer.AddText("Hmm? I must've fell asleep...", 6000);
            TextBuffer.AddText("Where is everyone? And what time is it?", 6000);
            TextBuffer.AddText("I'm on the 3rd floor... I think...", 6000);
            TextBuffer.AddText("Do I work here? I've gotta get out of here", 6000);
        }
    }

    public void touchEvent(MotionEvent me) {
        Camera cam = new Camera();

        //Get Camera of current floor
        if (iFloor == 3) {
            cam = FloorThird.GetWorld().getCamera();
        }
        else if (iFloor == 4) {
            cam = FloorFourth.GetWorld().getCamera();
        }
        else if (iFloor == 2) {
            cam = FloorSecond.GetWorld().getCamera();
        }
        else if (iFloor == 1) {
            cam = FloorFirst.GetWorld().getCamera();
        }
        else if (iFloor == 0) {
            cam = FloorGround.GetWorld().getCamera();
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
                    TogglePaused();
                }
            }

            aPauseButtons[1].Update(me.getX(pointerIndex), me.getY(pointerIndex));
            aPauseButtons[2].Update(me.getX(pointerIndex), me.getY(pointerIndex));

            if (bPaused) {
                //Resume game
                if (aPauseButtons[1].GetPressed()) {
                    TogglePaused();
                }
                //Return to Menu
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
                //if Interact button is pressed
                InteractUpdate(cam, me, pointerIndex);
            }
            //Action is down for any finger other than first
            if (me.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
                //if Interact button is pressed
                InteractUpdate(cam, me, pointerIndex);
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
                aMove[0].Update(me.getX(pointerIndex), me.getY(pointerIndex), pointerIndex);
            }
            if (aMove[1].GetState() != 0) {
                aMove[1].Update(me.getX(pointerIndex), me.getY(pointerIndex), pointerIndex);
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
                            aMove[0].Update(me.getX(pointerIndex), me.getY(pointerIndex), pointerIndex);
                        } else if (aMove[1].GetState() == 1) {
                            aMove[1].Update(me.getX(pointerIndex), me.getY(pointerIndex), pointerIndex);
                        }
                    }
                    if (pointerId == 1) {
                        if (aMove[0].GetState() == 2) {
                            aMove[0].Update(me.getX(pointerIndex), me.getY(pointerIndex), pointerIndex);
                        } else if (aMove[1].GetState() == 2) {
                            aMove[1].Update(me.getX(pointerIndex), me.getY(pointerIndex), pointerIndex);
                        }
                    }
                }
            }
        }
    }

    //Update for the interact buttons. This is repeated between ACTION_DOWN and ACTION_POINTER_DOWN, so
    //it has been created as a function
    private void InteractUpdate(Camera cam, MotionEvent me, int pointerIndex) {
        if (me.getX(pointerIndex) < pPoint.x / 2) {
            aMove[0].SetState(pointerIndex + 1);
        } else {
            aMove[1].SetState(pointerIndex + 1);
        }

        //If we're using the elevator, check for elevator button presses
        if (bElev) {
            //Do buttons
            for (int i = 0; i < aElevButtons.length; i++) {
                aElevButtons[i].Update(me.getX(pointerIndex), me.getY(pointerIndex));
                if (aElevButtons[i].GetPressed()) {
                    bElev = false;
                    //Goto floor 4
                    if (i == 0) {
                        if (iFloor != 4) {
                            ((GameActivity) context).PlaySound(SOUND_ELEV_DOORS);
                            iFloor = -1;
                            xRot = 0.f;
                            yRot = 180.f * DEG_TO_RAD;
                            FloorFourth.SetPosition(1);
                            new CountDownTimer(2000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    TextBuffer.DeleteAll();
                                    iFloor = 4;
                                    TextBuffer.AddText("4th Floor", 3000);
                                }
                            }.start();
                        }
                        //Already on this floor
                        else {
                            ((GameActivity) context).PlaySound(SOUND_FAIL);
                            TextBuffer.DeleteAll();
                            String text = "I'm already on this floor";
                            TextBuffer.AddText(text, 3000);
                        }
                    }
                    //Goto floor 3
                    else if (i == 1) {
                        if (iFloor != 3) {
                            ((GameActivity) context).PlaySound(SOUND_ELEV_DOORS);
                            iFloor = -1;
                            xRot = 0.f;
                            yRot = 180.f * DEG_TO_RAD;
                            FloorThird.SetPosition(1);
                            new CountDownTimer(2000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    TextBuffer.DeleteAll();
                                    iFloor = 3;
                                    TextBuffer.AddText("3rd Floor", 3000);
                                }
                            }.start();
                        }
                        //Already on this floor
                        else {
                            ((GameActivity) context).PlaySound(SOUND_FAIL);
                            String text = "I'm already on this floor";
                            TextBuffer.DeleteAll();
                            TextBuffer.AddText(text, 3000);
                        }
                    }
                    //Goto floor 2
                    else if (i == 2) {
                        if (iFloor != 2) {
                            ((GameActivity) context).PlaySound(SOUND_ELEV_DOORS);
                            iFloor = -1;
                            xRot = 0.f;
                            yRot = 180.f * DEG_TO_RAD;
                            if (FloorSecond == null) {
                                FloorSecond = new FloorSecond(context);
                            }
                            FloorSecond.SetPosition(1);
                            new CountDownTimer(2000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    TextBuffer.DeleteAll();
                                    iFloor = 2;
                                    TextBuffer.AddText("2nd Floor", 3000);
                                }
                            }.start();
                        }
                        //Already on this floor
                        else {
                            ((GameActivity) context).PlaySound(SOUND_FAIL);
                            String text = "I'm already on this floor";
                            TextBuffer.DeleteAll();
                            TextBuffer.AddText(text, 3000);
                        }
                    }
                    //Goto floor 1
                    else if (i == 3) {
                        ((GameActivity) context).PlaySound(SOUND_FAIL);
                        TextBuffer.DeleteAll();
                        String text = "It won't let me go to this floor";
                        TextBuffer.AddText(text, 3000);
                    }
                    //Goto ground floor
                    else if (i == 4) {
                        ((GameActivity) context).PlaySound(SOUND_FAIL);
                        TextBuffer.DeleteAll();
                        String text = "It won't let me go to this floor";
                        TextBuffer.AddText(text, 3000);
                    }
                    break;
                }
            }
        }

        //If the number pad on floor 1 is open, check for numpad inputs
        if (bNumPad) {
            int iButton = NumPad.Update(me.getX(pointerIndex), me.getY(pointerIndex));
            if (iButton == -1) {
                //Do nothing
            }
            else if (iButton == 10) {
                ((GameActivity) context).PlaySound(SOUND_SELECT);
                bNumPad = false;
                iNumbers[0] = 10;
                iNumbers[1] = 10;
                iNumbers[2] = 10;
            }
            else if (iButton == 11) {
                ((GameActivity) context).PlaySound(SOUND_SELECT);

                //Check if the code is correct
                if (iNumbers[0] == 3 && iNumbers[1] == 7 && iNumbers[2] == 1)
                {
                    Inventory.SetSelected(4);
                    ((GameActivity) context).PlaySound(SOUND_PICKUP);
                    TextBuffer.DeleteAll(4000);
                    TextBuffer.AddText("It seems to have worked.", 4000);
                    TextBuffer.AddText("Maybe the keycard reader's working now", 4000);
                }
                else {
                    //Game over from wrong code
                    iFloor = -3;
                    ((GameActivity) context).PlaySound(SOUND_DEATH);
                    ((GameActivity) context).StopMusic();
                    new CountDownTimer(3000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            bDestroy = true;
                        }
                    }.start();
                }

                bNumPad = false;
                iNumbers[0] = 10;
                iNumbers[1] = 10;
                iNumbers[2] = 10;
            }
            //Pressed a number
            else {
                ((GameActivity) context).PlaySound(SOUND_SELECT);
                if (iNumbers[0] == 10) {
                    iNumbers[0] = iButton;
                }
                else if (iNumbers[1] == 10) {
                    iNumbers[1] = iButton;
                }
                else if (iNumbers[2] == 10) {
                    iNumbers[2] = iButton;
                }
            }
        }

        //Check if interacting with interactable objects
        if (aButtons[0].GetPressed() && !bNumPad) {
            Object3D[] objects;
            if (iFloor == 3) {
                objects = FloorThird.GetInteObjects();
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].rayIntersectsAABB(cam.getPosition(), cam.getDirection()) < iDistance) {
                        //Elevator
                        if (i == 0) {
                            //If we haven't unlocked the elevator
                            if (!Inventory.GetSelected(3)) {
                                ((GameActivity) context).PlaySound(SOUND_FAIL);
                                String text = "The elevator isn't working";
                                TextBuffer.AddText(text, 3000);
                            }
                            //Call the elevator
                            else {
                                ((GameActivity) context).PlaySound(SOUND_ELEV_DING);
                                String text = FloorThird.Interact(i);
                                if (bElev) {
                                    bElev = false;
                                }
                                else {
                                    TextBuffer.AddText(text, 3000);
                                    bElev = true;
                                }
                            }
                        }
                        //Door 1 (Right)
                        else if (i == 1) {
                            if (Inventory.GetSelected(0) || Inventory.GetSelected(1)) {
                                //Goto 4th floor
                                ((GameActivity) context).PlaySound(SOUND_STAIR);
                                iFloor = -1;
                                xRot = 0.f;
                                yRot = 180.f * DEG_TO_RAD;
                                if (FloorFourth == null) {
                                    FloorFourth = new FloorFourth(context);
                                }
                                FloorFourth.SetPosition(2);
                                new CountDownTimer(2000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        TextBuffer.DeleteAll();
                                        iFloor = 4;
                                        TextBuffer.AddText("4th Floor", 3000);
                                        //First time, the AI prepares a trap
                                        if (!Inventory.GetSelected(3)) {
                                            TextBuffer.AddText("Was that noise from the elevator?", 4000);
                                            ((GameActivity) context).PlaySound(SOUND_ELEV_DING);
                                            Inventory.SetSelected(3);
                                        }
                                    }
                                }.start();
                            }
                            else {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = FloorThird.Interact(i);
                                TextBuffer.AddText(text, 3000);
                            }
                        }
                        //Door 2 (Left)
                        else if (i == 2){
                            if (Inventory.GetSelected(0)) {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = "The Keycard won't work on this door";
                                TextBuffer.AddText(text, 3000);
                            }
                            else if (Inventory.GetSelected(1)) {
                                //Goto 2nd floor
                                ((GameActivity) context).PlaySound(SOUND_STAIR);
                                iFloor = -1;
                                xRot = 0.f;
                                yRot = 180.f * DEG_TO_RAD;
                                FloorSecond.SetPosition(3);
                                new CountDownTimer(2000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        TextBuffer.DeleteAll();
                                        TextBuffer.AddText("2nd Floor", 3000);
                                        iFloor = 2;
                                    }
                                }.start();
                            }
                            else {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = FloorThird.Interact(i);
                                TextBuffer.AddText(text, 3000);
                            }
                        }
                        //Keycard 1
                        else if (i == 3) {
                            if (!Inventory.GetInventory(0)) {
                                ((GameActivity) context).PlaySound(SOUND_PICKUP);
                                String text = FloorThird.Interact(i);
                                Inventory.SetInventory(0);
                                TextBuffer.AddText(text, 3000);
                            }
                        }
                    }
                }
            }
            else if (iFloor == 4) {
                objects = FloorFourth.GetInteObjects();
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].rayIntersectsAABB(cam.getPosition(), cam.getDirection()) < iDistance) {
                        //"Elevator
                        if (i == 0) {
                            if (FloorFourth.GetElevSafe()) {
                                //Game over from using unsafe elevator
                                iFloor = -2;
                                ((GameActivity) context).PlaySound(SOUND_DEATH);
                                ((GameActivity) context).StopMusic();
                                new CountDownTimer(3000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        bDestroy = true;
                                    }
                                }.start();

                            }
                            else {
                                ((GameActivity) context).PlaySound(SOUND_ELEV_DING);
                                String text = FloorFourth.Interact(i);
                                if (bElev) {
                                    bElev = false;
                                }
                                else {
                                    TextBuffer.AddText(text, 3000);
                                    bElev = true;
                                }
                            }
                        }
                        //Door2 (Left)
                        else if (i == 1) {
                            if (Inventory.GetSelected(0) || Inventory.GetSelected(1)) {
                                //Goto 3rd floor
                                ((GameActivity) context).PlaySound(SOUND_STAIR);
                                iFloor = -1;
                                xRot = 0.f;
                                yRot = 180.f * DEG_TO_RAD;
                                FloorThird.SetPosition(3);
                                new CountDownTimer(2000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        TextBuffer.DeleteAll();
                                        TextBuffer.AddText("3rd Floor", 3000);
                                        iFloor = 3;
                                        FloorFourth.SetElevSafe();
                                    }
                                }.start();
                            }
                            else {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = FloorFourth.Interact(i);
                                TextBuffer.AddText(text, 3000);
                            }
                        }
                        //note 1
                        else if (i == 2) {
                            ((GameActivity) context).PlaySound(SOUND_SELECT);
                            String text = FloorFourth.Interact(i);
                            TextBuffer.DeleteAll(6000);
                            TextBuffer.AddText(text, 6000);
                            TextBuffer.AddText("elevator. It is controlled by the AI which controls", 6000);
                            TextBuffer.AddText("when the elevator moves or stops in an", 6000);
                            TextBuffer.AddText("emergency. Do not use unless called for.", 6000);
                        }
                    }
                }
            }
            else if (iFloor == 2) {
                objects = FloorSecond.GetInteObjects();
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].rayIntersectsAABB(cam.getPosition(), cam.getDirection()) < iDistance) {
                        //"Elevator
                        if (i == 0) {
                            ((GameActivity) context).PlaySound(SOUND_ELEV_DING);
                            String text = FloorSecond.Interact(i);
                            if (bElev) {
                                bElev = false;
                            }
                            else {
                                TextBuffer.AddText(text, 3000);
                                bElev = true;
                            }
                        }
                        //Door1 (Right)
                        else if (i == 1) {
                            if (Inventory.GetSelected(0)) {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = "The Keycard won't work on this door";
                                TextBuffer.AddText(text, 3000);
                            }
                            else if (Inventory.GetSelected(1)) {
                                //Goto 3rd floor
                                ((GameActivity) context).PlaySound(SOUND_STAIR);
                                iFloor = -1;
                                xRot = 0.f;
                                yRot = 180.f * DEG_TO_RAD;
                                FloorThird.SetPosition(2);
                                new CountDownTimer(2000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        TextBuffer.DeleteAll();
                                        TextBuffer.AddText("3rd Floor", 3000);
                                        iFloor = 3;
                                    }
                                }.start();
                            }
                            else {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = FloorSecond.Interact(i);
                                TextBuffer.AddText(text, 3000);
                            }
                        }
                        //Door2 (Left)
                        else if (i == 2) {
                            if (Inventory.GetSelected(0)) {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = "The Keycard won't work on this door";
                                TextBuffer.AddText(text, 3000);
                            }
                            else if (Inventory.GetSelected(1)) {
                                //Goto 1st floor
                                ((GameActivity) context).PlaySound(SOUND_STAIR);
                                iFloor = -1;
                                xRot = 0.f;
                                yRot = 180.f * DEG_TO_RAD;
                                if (FloorFirst == null) {
                                    FloorFirst = new FloorFirst(context);
                                }
                                FloorFirst.SetPosition(3);
                                new CountDownTimer(2000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        TextBuffer.DeleteAll();
                                        TextBuffer.AddText("1st Floor", 3000);
                                        iFloor = 1;
                                    }
                                }.start();
                            }
                            else {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = FloorSecond.Interact(i);
                                TextBuffer.AddText(text, 3000);
                            }
                        }
                        //Keycard 2
                        else if (i == 3) {
                            if (!Inventory.GetInventory(1)) {
                                ((GameActivity) context).PlaySound(SOUND_PICKUP);
                                String text = FloorSecond.Interact(i);
                                Inventory.SetInventory(1);
                                TextBuffer.AddText(text, 3000);
                            }
                        }
                        //Electrical box
                        else if (i == 4) {
                            if (!Inventory.GetSelected(2)) {
                                if (FloorSecond.GetElec()) {
                                    ((GameActivity) context).PlaySound(SOUND_SELECT);
                                    TextBuffer.AddText("It reads: 173. Is that a code?", 3000);
                                }
                                else {
                                    ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                    TextBuffer.AddText("An electrical box. It's locked by screws", 3000);
                                }
                            }
                            else {
                                if (!FloorSecond.GetElec()) {
                                    ((GameActivity) context).PlaySound(SOUND_CODE);
                                    FloorSecond.OpenElec();
                                    String text = FloorSecond.Interact(i);
                                    TextBuffer.AddText(text, 3000);
                                }
                                else {
                                    ((GameActivity) context).PlaySound(SOUND_SELECT);
                                    TextBuffer.AddText("It reads: 173. Is that a code?", 3000);
                                }
                            }
                        }
                    }
                }
            }
            else if (iFloor == 1) {
                objects = FloorFirst.GetInteObjects();
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].rayIntersectsAABB(cam.getPosition(), cam.getDirection()) < iDistance) {
                        //"Elevator
                        if (i == 0) {
                            ((GameActivity) context).PlaySound(SOUND_FAIL);
                            String text = "The elevator won't work properly";
                            TextBuffer.AddText(text, 3000);
                        }
                        //Door1 (Right)
                        else if (i == 1) {
                            if (Inventory.GetSelected(0)) {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = "The Keycard won't work on this door";
                                TextBuffer.AddText(text, 3000);
                            }
                            else if (Inventory.GetSelected(1)) {
                                //Goto 2nd floor
                                ((GameActivity) context).PlaySound(SOUND_STAIR);
                                iFloor = -1;
                                xRot = 0.f;
                                yRot = 180.f * DEG_TO_RAD;
                                FloorSecond.SetPosition(2);
                                new CountDownTimer(2000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        TextBuffer.DeleteAll();
                                        TextBuffer.AddText("2nd Floor", 3000);
                                        iFloor = 2;
                                    }
                                }.start();
                            }
                            else {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = FloorFirst.Interact(i);
                                TextBuffer.AddText(text, 3000);
                            }
                        }
                        //Door2 (Left)
                        else if (i == 2) {
                            if (Inventory.GetSelected(4)) {
                                if (Inventory.GetSelected(0)) {
                                    ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                    String text = "The Keycard won't work on this door";
                                    TextBuffer.AddText(text, 3000);
                                }
                                else if (Inventory.GetSelected(1)) {
                                    //Goto G floor
                                    ((GameActivity) context).PlaySound(SOUND_STAIR);
                                    iFloor = -1;
                                    xRot = 0.f;
                                    yRot = 180.f * DEG_TO_RAD;
                                    if (FloorGround == null) {
                                        FloorGround = new FloorGround(context);
                                    }
                                    FloorGround.SetPosition(3);
                                    new CountDownTimer(2000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                        }

                                        public void onFinish() {
                                            TextBuffer.DeleteAll();
                                            TextBuffer.AddText("Ground Floor", 3000);
                                            iFloor = 0;
                                        }
                                    }.start();
                                }
                                else {
                                    ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                    String text = FloorFirst.Interact(i);
                                    TextBuffer.AddText(text, 3000);
                                }
                            }
                            else {
                                ((GameActivity) context).PlaySound(SOUND_FAIL);
                                TextBuffer.DeleteAll(4000);
                                TextBuffer.AddText("The Keycard reader isn't working", 4000);
                                TextBuffer.AddText("Can I reset it somehow?", 4000);
                            }
                        }
                        //Screwdriver
                        else if (i == 3) {
                            if (!Inventory.GetInventory(2)) {
                                ((GameActivity) context).PlaySound(SOUND_PICKUP);
                                String text = FloorFirst.Interact(i);
                                Inventory.SetInventory(2);
                                TextBuffer.AddText(text, 3000);
                            }
                        }
                        //Electrical box
                        else if (i == 4) {
                            if (!Inventory.GetSelected(2)) {
                                if (FloorFirst.GetElec()) {
                                    if (Inventory.GetSelected(4)) {
                                        ((GameActivity) context).PlaySound(SOUND_FAIL);
                                        TextBuffer.AddText("I've already put in the right code", 3000);
                                    }
                                    else {
                                        ((GameActivity) context).PlaySound(SOUND_SELECT);
                                        bNumPad = true;
                                    }
                                }
                                else {
                                    ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                    TextBuffer.AddText("An electrical box. It's locked by screws", 3000);
                                }
                            }
                            else {
                                if (!FloorFirst.GetElec()) {
                                    ((GameActivity) context).PlaySound(SOUND_CODE);
                                    FloorFirst.OpenElec();
                                    String text = FloorFirst.Interact(i);
                                    TextBuffer.AddText(text, 3000);
                                }
                                else {
                                    if (Inventory.GetSelected(4)) {
                                        ((GameActivity) context).PlaySound(SOUND_FAIL);
                                        TextBuffer.AddText("I've already put in the right code", 3000);
                                    }
                                    else {
                                        ((GameActivity) context).PlaySound(SOUND_SELECT);
                                        bNumPad = true;
                                    }
                                }
                            }
                        }
                        //Note 2
                        else if (i == 5) {
                            ((GameActivity) context).PlaySound(SOUND_SELECT);
                            String text = FloorFirst.Interact(i);
                            TextBuffer.DeleteAll(6000);
                            TextBuffer.AddText(text, 6000);
                            TextBuffer.AddText("reset the power, the machines been acting", 6000);
                            TextBuffer.AddText("up a bit. The reset code from the 2nd", 6000);
                            TextBuffer.AddText("floor's wrong. Just flip the code it gives", 6000);
                        }
                    }
                }
            }
            //Ground Floor
            else if (iFloor == 0) {
                objects = FloorGround.GetInteObjects();
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].rayIntersectsAABB(cam.getPosition(), cam.getDirection()) < iDistance) {
                        //"Elevator
                        if (i == 0) {
                            ((GameActivity) context).PlaySound(SOUND_FAIL);
                            String text = "The elevator won't work properly";
                            TextBuffer.AddText(text, 3000);
                        }
                        //Door1 (Right)
                        else if (i == 1) {
                            if (Inventory.GetSelected(0)) {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = "The Keycard won't work on this door";
                                TextBuffer.AddText(text, 3000);
                            }
                            else if (Inventory.GetSelected(1)) {
                                //Goto 1st floor
                                ((GameActivity) context).PlaySound(SOUND_STAIR);
                                iFloor = -1;
                                xRot = 0.f;
                                yRot = 180.f * DEG_TO_RAD;
                                FloorFirst.SetPosition(2);
                                new CountDownTimer(2000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        TextBuffer.DeleteAll();
                                        TextBuffer.AddText("1st Floor", 3000);
                                        iFloor = 1;
                                    }
                                }.start();
                            }
                            else {
                                ((GameActivity) context).PlaySound(SOUND_LOCKED);
                                String text = FloorGround.Interact(i);
                                TextBuffer.AddText(text, 3000);
                            }
                        }
                        //EXIT
                        else if (i == 2) {
                            //Win
                            iFloor = 5;
                            ((GameActivity) context).PlaySound(SOUND_WIN);
                            ((GameActivity) context).StopMusic();
                            new CountDownTimer(3000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    bDestroy = true;
                                }
                            }.start();
                        }
                    }
                }
            }
            Inventory.ResetUse();
        }
        //Check if its pressing the items
        else {
            if (Inventory.Update(me.getX(pointerIndex), me.getY(pointerIndex))) {((GameActivity) context).PlaySound(SOUND_SELECT);}
        }
    }

    public void TogglePaused() {
        ((GameActivity) context).PlaySound(SOUND_SELECT);
        bPaused = !bPaused;
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
        else if (iFloor == 4) {
            cam = FloorFourth.GetWorld().getCamera();
        }
        else if (iFloor == 2) {
            cam = FloorSecond.GetWorld().getCamera();
        }
        else if (iFloor == 1) {
            cam = FloorFirst.GetWorld().getCamera();
        }
        else if (iFloor == 0) {
            cam = FloorGround.GetWorld().getCamera();
        }

        //if game is not paused
        if (!bPaused && !bElev && !bNumPad) {
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
            if (iFloor != -1 && touchMove != 0 || touchMoveUp != 0) {
                SimpleVector moveVector = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                cam.moveCamera(moveVector, 10f);
                float xCamPos = cam.getPosition().x;
                float yCamPos = cam.getPosition().z;

                //Check collisions on current floor
                //If colliding with anything, move back to original position
                if (iFloor == 3) {
                    boolean check = FloorThird.Collisions(xCamPos, yCamPos);
                    if (check) {
                        SimpleVector Move = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                        cam.moveCamera(Move, -10f);
                    }
                    else {
                        //Wall Collisions
                        if (FloorThird.CollisionsWallX(xCamPos)) {
                            SimpleVector Move = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, 0.f);
                            cam.moveCamera(Move, -10f);
                        }
                        if (FloorThird.CollisionsWallY(yCamPos)) {
                            SimpleVector Move = new SimpleVector(0.f, 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                            cam.moveCamera(Move, -10f);
                        }
                    }
                }
                else if (iFloor == 4) {
                    boolean check = FloorFourth.Collisions(xCamPos, yCamPos);
                    if (check) {
                        SimpleVector Move = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                        cam.moveCamera(Move, -10f);
                    }
                    else {
                        //Wall Collisions
                        if (FloorFourth.CollisionsWallX(xCamPos)) {
                            SimpleVector Move = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, 0.f);
                            cam.moveCamera(Move, -10f);
                        }
                        if (FloorFourth.CollisionsWallY(yCamPos)) {
                            SimpleVector Move = new SimpleVector(0.f, 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                            cam.moveCamera(Move, -10f);
                        }
                    }
                }
                else if (iFloor == 2) {
                    boolean check = FloorSecond.Collisions(xCamPos, yCamPos);
                    if (check) {
                        SimpleVector Move = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                        cam.moveCamera(Move, -10f);
                    }
                    else {
                        //Wall Collisions
                        if (FloorSecond.CollisionsWallX(xCamPos)) {
                            SimpleVector Move = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, 0.f);
                            cam.moveCamera(Move, -10f);
                        }
                        if (FloorSecond.CollisionsWallY(yCamPos)) {
                            SimpleVector Move = new SimpleVector(0.f, 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                            cam.moveCamera(Move, -10f);
                        }
                    }
                }
                else if (iFloor == 1) {
                    boolean check = FloorFirst.Collisions(xCamPos, yCamPos);
                    if (check) {
                        SimpleVector Move = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                        cam.moveCamera(Move, -10f);
                    }
                    else {
                        //Wall Collisions
                        if (FloorFirst.CollisionsWallX(xCamPos)) {
                            SimpleVector Move = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, 0.f);
                            cam.moveCamera(Move, -10f);
                        }
                        if (FloorFirst.CollisionsWallY(yCamPos)) {
                            SimpleVector Move = new SimpleVector(0.f, 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                            cam.moveCamera(Move, -10f);
                        }
                    }
                }
                else if (iFloor == 0) {
                    boolean check = FloorGround.Collisions(xCamPos, yCamPos);
                    if (check) {
                        SimpleVector Move = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                        cam.moveCamera(Move, -10f);
                    }
                    else {
                        //Wall Collisions
                        if (FloorGround.CollisionsWallX(xCamPos)) {
                            SimpleVector Move = new SimpleVector((touchMove * cos(yRot)) - (touchMoveUp * sin(yRot)), 0.f, 0.f);
                            cam.moveCamera(Move, -10f);
                        }
                        if (FloorGround.CollisionsWallY(yCamPos)) {
                            SimpleVector Move = new SimpleVector(0.f, 0.f, (touchMoveUp * cos(yRot)) + (touchMove * sin(yRot)));
                            cam.moveCamera(Move, -10f);
                        }
                    }
                }
            }
        }

        //Rotate Camera
        if (!bPaused && iFloor != -1 && !bElev && !bNumPad) {
            SimpleVector cameraVector = cam.getPosition();
            cameraVector.z += 1f;
            cam.lookAt(cameraVector);
            cam.rotateY(yRot);
            cam.rotateX(xRot);
        }

        if (!bDestroy) {
            fb.clear(bg);
        }

        //Draw current floor
        if (iFloor == -3) {
            fb.blit(tBlack, 0, 0, 0, 0, pPoint.x, pPoint.y, FrameBuffer.OPAQUE_BLITTING);
            fb.blit(tGameOver, 0, 0, (pPoint.x/2) - 256, (pPoint.y/2) - 256, 512, 512, FrameBuffer.TRANSPARENT_BLITTING);
            DrawText(fb, AGLFont[1], "The electrics malfunctioned and electrocuted you", pPoint.x/2, (pPoint.y/2) + 100, true);
            fb.display();
        }
        else if (iFloor == -2) {
            fb.blit(tBlack, 0, 0, 0, 0, pPoint.x, pPoint.y, FrameBuffer.OPAQUE_BLITTING);
            fb.blit(tGameOver, 0, 0, (pPoint.x/2) - 256, (pPoint.y/2) - 256, 512, 512, FrameBuffer.TRANSPARENT_BLITTING);
            DrawText(fb, AGLFont[1], "The elevator doors closed and crushed you", pPoint.x/2, (pPoint.y/2) + 100, true);
            fb.display();
        }
        else if (iFloor == -1) {
            fb.blit(tBlack, 0, 0, 0, 0, pPoint.x, pPoint.y, FrameBuffer.OPAQUE_BLITTING);
            fb.blit(tLoading, 0, 0, (pPoint.x/2) - 256, (pPoint.y/2) - 256, 512, 512, FrameBuffer.TRANSPARENT_BLITTING);
            fb.display();
        }
        else if (iFloor == 3 && !bDestroy) {
            FloorThird.GetWorld().renderScene(fb);
            FloorThird.GetWorld().draw(fb);
            fb.display();
        }
        else if (iFloor == 4 && !bDestroy) {
            FloorFourth.GetWorld().renderScene(fb);
            FloorFourth.GetWorld().draw(fb);
            fb.display();
        }
        else if (iFloor == 2 && !bDestroy) {
            FloorSecond.GetWorld().renderScene(fb);
            FloorSecond.GetWorld().draw(fb);
            fb.display();
        }
        else if (iFloor == 1 && !bDestroy) {
            FloorFirst.GetWorld().renderScene(fb);
            FloorFirst.GetWorld().draw(fb);
            fb.display();
        }
        else if (iFloor == 0 && !bDestroy) {
            FloorGround.GetWorld().renderScene(fb);
            FloorGround.GetWorld().draw(fb);
            fb.display();
        }
        else if (iFloor == 5) {
            fb.blit(tBlack, 0, 0, 0, 0, pPoint.x, pPoint.y, FrameBuffer.OPAQUE_BLITTING);
            fb.blit(tWin, 0, 0, (pPoint.x/2) - 256, (pPoint.y/2) - 256, 512, 512, FrameBuffer.TRANSPARENT_BLITTING);
            DrawText(fb, AGLFont[1], "You managed to escape the AI! Congratulations!", pPoint.x/2, (pPoint.y/2) + 100, true);
            fb.display();
        }

        //Draw and display UI
        if (!bDestroy && iFloor != -1 && iFloor != -2  && iFloor != -3 && iFloor != 5) {
            //Draw Joysticks
            for (int i = 0; i < aMove.length; i++) {aMove[i].Draw(fb);}

            //Draw Buttons
            for (int i = 0; i < aButtons.length; i++) {aButtons[i].Draw(fb);}

            //Draw the inventory (if collected)
            Inventory.Draw(fb);

            //Draw message if looking at a usable object
            if (iFloor == 3) {
                Object3D[] objects = FloorThird.GetInteObjects();
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].rayIntersectsAABB(cam.getPosition(), cam.getDirection()) < iDistance) {
                        if (i == 3) {
                            if (!Inventory.GetInventory(0)) {
                                fb.blit(tInteract, 0, 0, (pPoint.x / 2) - 128, (pPoint.y / 2) - 64, 256, 256, FrameBuffer.TRANSPARENT_BLITTING);
                            }
                        }
                        else {
                            fb.blit(tInteract, 0, 0, (pPoint.x / 2) - 128, (pPoint.y / 2) - 64, 256, 256, FrameBuffer.TRANSPARENT_BLITTING);
                        }
                    }
                }
            }
            else if (iFloor == 4) {
                Object3D[] objects = FloorFourth.GetInteObjects();
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].rayIntersectsAABB(cam.getPosition(), cam.getDirection()) < iDistance) {
                        if (i == 3) {
                            if (!Inventory.GetInventory(0)) {
                                fb.blit(tInteract, 0, 0, (pPoint.x / 2) - 128, (pPoint.y / 2) - 64, 256, 256, FrameBuffer.TRANSPARENT_BLITTING);
                            }
                        }
                        else {
                            fb.blit(tInteract, 0, 0, (pPoint.x / 2) - 128, (pPoint.y / 2) - 64, 256, 256, FrameBuffer.TRANSPARENT_BLITTING);
                        }
                    }
                }
            }
            else if (iFloor == 2) {
                Object3D[] objects = FloorSecond.GetInteObjects();
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].rayIntersectsAABB(cam.getPosition(), cam.getDirection()) < iDistance) {
                        if (i == 3) {
                            if (!Inventory.GetInventory(1)) {
                                fb.blit(tInteract, 0, 0, (pPoint.x / 2) - 128, (pPoint.y / 2) - 64, 256, 256, FrameBuffer.TRANSPARENT_BLITTING);
                            }
                        }
                        else {
                            fb.blit(tInteract, 0, 0, (pPoint.x / 2) - 128, (pPoint.y / 2) - 64, 256, 256, FrameBuffer.TRANSPARENT_BLITTING);
                        }
                    }
                }
            }
            else if (iFloor == 1) {
                Object3D[] objects = FloorFirst.GetInteObjects();
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].rayIntersectsAABB(cam.getPosition(), cam.getDirection()) < iDistance) {
                        if (i == 3) {
                            if (!Inventory.GetInventory(2)) {
                                fb.blit(tInteract, 0, 0, (pPoint.x / 2) - 128, (pPoint.y / 2) - 64, 256, 256, FrameBuffer.TRANSPARENT_BLITTING);
                            }
                        }
                        else {
                            fb.blit(tInteract, 0, 0, (pPoint.x / 2) - 128, (pPoint.y / 2) - 64, 256, 256, FrameBuffer.TRANSPARENT_BLITTING);
                        }
                    }
                }
            }
            else if (iFloor == 0) {
                Object3D[] objects = FloorGround.GetInteObjects();
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].rayIntersectsAABB(cam.getPosition(), cam.getDirection()) < iDistance) {
                        fb.blit(tInteract, 0, 0, (pPoint.x / 2) - 128, (pPoint.y / 2) - 64, 256, 256, FrameBuffer.TRANSPARENT_BLITTING);
                    }
                }
            }

            //Draw dot sight
            fb.blit(tDot, 0, 0, (pPoint.x / 2) - 32, (pPoint.y / 2) - 32, 64, 64, FrameBuffer.TRANSPARENT_BLITTING);

            //Draw Text/Subtitles
            String[] sText = TextBuffer.GetText();
            int[] iOrder = TextBuffer.GetOrder();
            int j = 0;
            for (int i = 0; i < sText.length; i++) {
                if (sText[i] != null && iOrder[i] != -1) {
                    DrawText(fb, AGLFont[1], sText[i], 25, 350 + j * 100, false);
                    j++;
                }
            }


            /*if (sText != null) {
                for (int i = 0; i < sText.length; i++) {
                    DrawText(fb, AGLFont[1], sText[i], 25, 350 + i*100, false);
                }
            }*/

            //Draw elevator buttons
            if (bElev) {
                ElevMenu.Draw(fb);
                for (int i = 0; i < aElevButtons.length; i++) {
                    aElevButtons[i].Draw(fb);
                    if (i != 4) {
                        DrawText(fb, AGLFont[0], Integer.toString(4 - i), pPoint.x / 5 * 3, 250 + i * ((pPoint.y - 100) / 6), true);
                    }
                    else {
                        DrawText(fb, AGLFont[0], "G", pPoint.x / 5 * 3, 250 + i * ((pPoint.y - 100) / 6), true);
                    }
                }
            }

            //Draw Numpad
            if (bNumPad) {
                NumPad.Draw(fb);
                if (iNumbers[0] != 10) {
                    if (iNumbers[1] != 10) {
                        if (iNumbers[2] != 10) {
                            DrawText(fb, AGLFont[0], Integer.toString(iNumbers[0]) + Integer.toString(iNumbers[1]) + Integer.toString(iNumbers[2]), pPoint.x/2, (pPoint.y/2) - 250, true);
                        }
                        else {
                            DrawText(fb, AGLFont[0], Integer.toString(iNumbers[0]) + Integer.toString(iNumbers[1]), pPoint.x/2, (pPoint.y/2) - 250, true);
                        }
                    }
                    else {
                        DrawText(fb, AGLFont[0], Integer.toString(iNumbers[0]), pPoint.x/2, (pPoint.y/2) - 250, true);
                    }
                }
            }

            //Draw pause menu
            if (bPaused) {
                pauseMenu.Draw(fb);
                aPauseButtons[0].Draw(fb, AGLFont[0]);
                aPauseButtons[1].Draw(fb, AGLFont[0]);
                aPauseButtons[2].Draw(fb, AGLFont[0]);
            }

            fb.display();
            fb.removeRenderTarget();

            //Reset
            for (int i = 0; i < aButtons.length; i++) {aButtons[i].Reset();}
            for (int i = 0; i < aElevButtons.length; i++) {aElevButtons[i].Reset();}
            aPauseButtons[1].Reset();
            aPauseButtons[2].Reset();
        }

        if (System.currentTimeMillis() - time >= 1000) {
            //Logger.log(fps + "fps");
            fps = 0;
            time = System.currentTimeMillis();
        }
        fps++;

        //If game is being destroyed (returning to menu or closing app) reset data
        //Java does nnot use destructors like C++, meaning some data must be reset manually
        if (bDestroy) {
            master = null;
            fb.dispose();
            FloorThird.Destroy();
            if (FloorFourth != null) {
                FloorFourth.Destroy();
            }
            if (FloorSecond != null) {
                FloorSecond.Destroy();
            }
            if (FloorFirst != null) {
                FloorFirst.Destroy();
            }
            if (FloorGround != null) {
                FloorGround.Destroy();
            }
            Inventory.Reset();
            Intent intent = new Intent();
            if (iFloor == 5) {
                intent.putExtra("completed", true);
            }
            else {
                intent.putExtra("completed", false);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ((GameActivity) context).setResult(GAME, intent);
            ((GameActivity) context).finish();
        }
    }

    private void DrawText(FrameBuffer fb, AGLFont font, String text, int xPos, int yPos, boolean center) {
        if (center) {
            Rectangle rect = new Rectangle();
            font.getStringBounds(text, rect);
            font.blitString(fb, text, xPos - (rect.width/2), yPos, 100, RGBColor.WHITE);
        }
        else {
            font.blitString(fb, text, xPos, yPos, 100, RGBColor.WHITE);
        }
    }

}
