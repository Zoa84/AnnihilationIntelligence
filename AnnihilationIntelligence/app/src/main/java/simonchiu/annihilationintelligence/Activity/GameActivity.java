package simonchiu.annihilationintelligence.Activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.threed.jpct.Logger;

import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import simonchiu.annihilationintelligence.Class.Media;
import simonchiu.annihilationintelligence.View.GameSurfaceView;

import static simonchiu.annihilationintelligence.Class.Defines.*;


public class GameActivity extends Activity {

    // Used to handle pause and resume...
    //TODO necessary?
    private static GameActivity master = null;

    private GLSurfaceView mGLView;
    private GameSurfaceView renderer = null;

    private Toast toast;
    boolean back = false;

    private boolean[] bOptionData = new boolean[5]; //Array of booleans for the checkboxes and radio groups under Defines (using class Defines)
    private int[] iVolume = new int[2];             //Array of the volume for music (0) and sound (1)

    protected void onCreate(Bundle savedInstanceState) {

        Logger.log("onCreate");

        //Set the game activity to use fullscreen, removing the action bars, etc
        int mUIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);

        //Get options and volume data from splash activity
        this.bOptionData = getIntent().getBooleanArrayExtra("optionData");
        this.iVolume = getIntent().getIntArrayExtra("volumeData");

        if (bOptionData[ORIENTATION]) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        //Play music (passing the element to play)
        if (!Media.getInstance().playMusic(MUSIC_BATTLE, iVolume[MUSIC], bOptionData[MUSIC])) Toast.makeText(this, "Couldn't play music", Toast.LENGTH_SHORT).show();

        //TODO necessary?
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

        renderer = new GameSurfaceView(this);

        renderer.setOptions(bOptionData[INVERTX], bOptionData[INVERTY]);

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

    //Return to menu todo change to show a pause menu, DOES NOT end the game in any way
    @Override
    public void onBackPressed(){
        if (!back) {
            toast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT);
            toast.show();
            back = true;
            new CountDownTimer(2000, 1000){
                public void onTick(long millisUntilFinished) {

                }
                public void onFinish() {
                    back = false;
                    toast.cancel();
                }
            }.start();
        }
        else{
            toast.cancel();
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //TODO necessary?
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
        renderer.touchEvent(me);

        try {
            Thread.sleep(15);
        } catch (Exception e) {
            // No need for this...
        }

        return super.onTouchEvent(me);
    }

}