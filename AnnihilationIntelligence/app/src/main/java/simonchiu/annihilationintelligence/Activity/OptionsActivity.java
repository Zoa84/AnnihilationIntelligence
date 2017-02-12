package simonchiu.annihilationintelligence.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import simonchiu.annihilationintelligence.R;

import static simonchiu.annihilationintelligence.Class.Defines.INVERTX;
import static simonchiu.annihilationintelligence.Class.Defines.INVERTY;
import static simonchiu.annihilationintelligence.Class.Defines.MUSIC;
import static simonchiu.annihilationintelligence.Class.Defines.OPTIONS;
import static simonchiu.annihilationintelligence.Class.Defines.ORIENTATION;
import static simonchiu.annihilationintelligence.Class.Defines.SOUND;

public class OptionsActivity extends AppCompatActivity {

    boolean[] bOptionData = new boolean[5]; //Array of booleans for the checkboxes and radio groups under Defines (using class Defines)
    int[] iVolume = new int[2];             //Array of the volume for music (0) and sound (1)
    CheckBox[] cCheckBox = new CheckBox[4]; //Array of all checkboxes (0 = Music, 1 = Sound, 2 = Invert X, 3 = Invert Y)
    RadioGroup rgOrien;                     //The radio group for the orientation
    SeekBar[] sVolume = new SeekBar[2];     //Array of the seek bars for the music and sound volume

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Get options and volume data from Menu activity
        this.bOptionData = getIntent().getBooleanArrayExtra("optionData");
        this.iVolume = getIntent().getIntArrayExtra("volumeData");
        //Set orientation
        if (bOptionData[ORIENTATION]) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);



        //Settings for the Music
        cCheckBox[MUSIC] = (CheckBox) findViewById(R.id.cMusic);
        cCheckBox[MUSIC].setChecked(bOptionData[MUSIC]);

        //Settings for the Sound
        cCheckBox[SOUND] = (CheckBox) findViewById(R.id.cSound);
        cCheckBox[SOUND].setChecked(bOptionData[SOUND]);

        //Settings for Inverting camera controls
        cCheckBox[INVERTX] = (CheckBox) findViewById(R.id.cInvX);
        cCheckBox[INVERTY] = (CheckBox) findViewById(R.id.cInvY);
        //Set the check buttons to current settings
        cCheckBox[INVERTX].setChecked(bOptionData[INVERTX]);
        cCheckBox[INVERTY].setChecked(bOptionData[INVERTY]);
        //Listeners to check for changes to the check boxes
        cCheckBox[INVERTX].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bOptionData[INVERTX] = cCheckBox[INVERTX].isChecked();
            }
        });
        cCheckBox[INVERTY].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bOptionData[INVERTY] = cCheckBox[INVERTY].isChecked();
            }
        });

        //Settings for the Orientation
        rgOrien = (RadioGroup) findViewById(R.id.rgOrien);
        rgOrien.clearCheck();
        //Set the radio buttons to current settings
        if (bOptionData[ORIENTATION]) {
            rgOrien.check(R.id.rLeft);
        }
        else
        {
            rgOrien.check(R.id.rRight);
        }
        //Listener to check for changes to the radio group
        rgOrien.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //soundpool.play(sounds[3],(float) soundVolume/100,(float) soundVolume/100,0,0,1);
                bOptionData[ORIENTATION] = rgOrien.findViewById(rgOrien.getCheckedRadioButtonId()) == findViewById(R.id.rLeft);
                if (bOptionData[ORIENTATION]) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
        });

        //Seek bar for Music and Sound
        //Set the seek bars to current settings
        sVolume[MUSIC] = (SeekBar) findViewById(R.id.sMusic);
        sVolume[MUSIC].setProgress(iVolume[MUSIC]);
        sVolume[SOUND] = (SeekBar) findViewById(R.id.sSound);
        sVolume[SOUND].setProgress(iVolume[SOUND]);

        //Listener for seek bars
        sVolume[MUSIC].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iVolume[MUSIC] = progress;
                //musicLog = (float) (Math.log(100 - musicVolume)/Math.log(100));
                //mediaPlayer.setVolume(1-musicLog,1-musicLog);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //TODO Sound effect
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO Sound effect
            }
        });
        sVolume[SOUND].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iVolume[SOUND] = progress;
                //TODO Sound effect
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //TODO Sound effect
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO Sound effect
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Return to menu activity, without saving any changes
        super.onBackPressed();

    }

    public void cancel (View view){ // Button press cancel
        //Return to menu activity, without saving any changes
        Intent data = new Intent();
        data.putExtra("optionData", getIntent().getBooleanArrayExtra("optionData"));
        data.putExtra("volumeData", getIntent().getIntArrayExtra("volumeData"));
        setResult(OPTIONS, data);
        finish();
    }

    public void restore (View view){ // Button press restore settings
        //Return to menu activity, setting the default settings
        bOptionData[MUSIC] = true;
        bOptionData[SOUND] = true;
        bOptionData[INVERTX] = false;
        bOptionData[INVERTY] = false;
        bOptionData[ORIENTATION] = true;
        iVolume[MUSIC] = 99;
        iVolume[SOUND] = 99;

        //Reset default settings, and run Save function
        save(view);
    }

    public void save (View view){ // Button press save
        //Return to menu activity, saving the settings
        Intent data = new Intent();
        data.putExtra("optionData", bOptionData);
        data.putExtra("volumeData", iVolume);
        setResult(OPTIONS, data);
        finish();
    }

}
