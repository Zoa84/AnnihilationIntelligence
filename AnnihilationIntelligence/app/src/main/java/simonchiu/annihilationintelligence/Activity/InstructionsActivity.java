package simonchiu.annihilationintelligence.Activity;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import simonchiu.annihilationintelligence.Class.Media;
import simonchiu.annihilationintelligence.R;

import static simonchiu.annihilationintelligence.Class.Defines.*;

//Instructions activity, allowing the user to see how to play

public class InstructionsActivity extends AppCompatActivity {

    boolean[] bOptionData = new boolean[5]; //Array of booleans for the checkboxes and radio groups under Defines (using class Defines)
    int[] iVolume = new int[2];             //Array of the volume for music (0) and sound (1)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        //Get options and volume data from PauseMenu activity
        this.bOptionData = getIntent().getBooleanArrayExtra("optionData");
        this.iVolume = getIntent().getIntArrayExtra("volumeData");

        //Set orientation
        if (bOptionData[ORIENTATION]) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

    }

    @Override
    public void onBackPressed() {
        //Return to menu activity
        Media.getInstance().playSound(SOUND_SELECT, iVolume[SOUND], bOptionData[SOUND]);
        finish();
    }

    public void menu (View view) {
        onBackPressed();
    }

}
