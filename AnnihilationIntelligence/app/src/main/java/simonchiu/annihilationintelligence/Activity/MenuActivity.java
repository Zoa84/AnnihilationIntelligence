package simonchiu.annihilationintelligence.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import simonchiu.annihilationintelligence.R;

import static simonchiu.annihilationintelligence.Class.Defines.INVERTX;
import static simonchiu.annihilationintelligence.Class.Defines.INVERTY;
import static simonchiu.annihilationintelligence.Class.Defines.MUSIC;
import static simonchiu.annihilationintelligence.Class.Defines.ORIENTATION;
import static simonchiu.annihilationintelligence.Class.Defines.SOUND;
import static simonchiu.annihilationintelligence.Class.Defines.OPTIONS;
import static simonchiu.annihilationintelligence.Class.Defines.GAME;

/*The Menu Activity. This is the central hub of the app, and is accessed after the Splash Screen
From here the player can choose to start the game, change options or exit. If exiting using the
Android buttons, a toast message will appear to ask the player to press again to exit.
 */

public class MenuActivity extends AppCompatActivity {

    boolean back = false;   //If the back button has been pressed
    Toast toast;            //Variable to hold a toast message

    boolean[] bOptionData = new boolean[5]; //Array of booleans for the checkboxes and radio groups under Defines (using class Defines)
    int[] iVolume = new int[2];             //Array of the volume for music (0) and sound (1)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Get options and volume data from splash activity
        this.bOptionData = getIntent().getBooleanArrayExtra("optionData");
        this.iVolume = getIntent().getIntArrayExtra("volumeData");

        setOrientation();
    }

    public void start (View view){ // Button press start game
        Intent intent = new Intent(MenuActivity.this,GameActivity.class);
        intent.putExtra("optionData", bOptionData);
        intent.putExtra("volumeData", iVolume);
        startActivityForResult(intent, GAME);
    }

    public void options (View view){ // Button press options
        Intent intent = new Intent(MenuActivity.this,OptionsActivity.class);
        intent.putExtra("optionData", bOptionData);
        intent.putExtra("volumeData", iVolume);
        startActivityForResult(intent, OPTIONS);
    }

    public void exit (View view){ // Button press exit game
        finish();
    }

    //End the game if trying to leave from menu
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
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //If returning from the Options Activity
        if (resultCode == OPTIONS) {
            bOptionData = data.getBooleanArrayExtra("optionData");
            iVolume = data.getIntArrayExtra("volumeData");
            setOrientation();
            saveData();
        }
        //If returning from the Game Activity
        else if (resultCode == GAME) {

        }
    }

    //Set the orientation, checking what the orientation has been set to
    private void setOrientation()
    {
        if (bOptionData[ORIENTATION]) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    //Save the options data to the text file
    private void saveData()
    {
        File root = new File(Environment.getExternalStorageDirectory(), "Annihilation Intelligence");
        if (!root.exists()) {
            root.mkdirs();
        }
        try {
            File file = new File(root, "options.txt");
            FileWriter writer = new FileWriter(file);
            writer.append(
                    bOptionData[MUSIC] + "\n" + bOptionData[SOUND] + "\n" + bOptionData[INVERTX] + "\n" +
                    bOptionData[INVERTY] + "\n" + bOptionData[ORIENTATION] + "\n" + iVolume[MUSIC] + "\n" + iVolume[SOUND]
            );
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        toast = Toast.makeText(this, "Data Saved" + iVolume[MUSIC], Toast.LENGTH_SHORT);
        toast.show();
    }

}
