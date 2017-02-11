package simonchiu.annihilationintelligence.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import simonchiu.annihilationintelligence.R;

import static simonchiu.annihilationintelligence.Activity.OptionsActivity.Options.INVERTX;
import static simonchiu.annihilationintelligence.Activity.OptionsActivity.Options.INVERTY;
import static simonchiu.annihilationintelligence.Activity.OptionsActivity.Options.ORIENTATION;

public class OptionsActivity extends AppCompatActivity {

    class Options {
        public static final int MUSIC = 0;
        public static final int SOUND = 1;
        public static final int INVERTX = 2;
        public static final int INVERTY = 3;
        public static final int ORIENTATION = 4;
    }

    boolean[] bOptionData = new boolean[5]; //Array of booleans for the checkboxes and radio groups under Options (using class Options)
    int[] iVolume = new int[2];             //Array of the volume for music (0) and sound (1)
    CheckBox[] cCheckBox = new CheckBox[4]; //Array of all checkboxes (0 = Music, 1 = Sound, 2 = Invert X, 3 = Invert Y)
    RadioGroup rgOrien;                     //The radio group for the orientation
    SeekBar[] sVolume = new SeekBar[2];     //Array of the seek bars for the music and sound volume

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Load current settings from options.txt
        //TODO change to load, currently using fake settings
        bOptionData[Options.MUSIC] = true;
        bOptionData[Options.SOUND] = true;
        bOptionData[Options.INVERTX] = false;
        bOptionData[Options.INVERTY] = false;
        bOptionData[Options.ORIENTATION] = true;
        iVolume[Options.MUSIC] = 99;
        iVolume[Options.SOUND] = 99;

        //Settings for the Music
        cCheckBox[Options.MUSIC] = (CheckBox) findViewById(R.id.cMusic);
        cCheckBox[Options.MUSIC].setChecked(bOptionData[Options.MUSIC]);

        //Settings for the Sound
        cCheckBox[Options.SOUND] = (CheckBox) findViewById(R.id.cSound);
        cCheckBox[Options.SOUND].setChecked(bOptionData[Options.SOUND]);

        //Settings for Inverting camera controls
        cCheckBox[Options.INVERTX] = (CheckBox) findViewById(R.id.cInvX);
        cCheckBox[Options.INVERTY] = (CheckBox) findViewById(R.id.cInvY);
        //Set the check buttons to current settings
        cCheckBox[Options.INVERTX].setChecked(bOptionData[Options.INVERTX]);
        cCheckBox[Options.INVERTY].setChecked(bOptionData[Options.INVERTY]);
        //Listeners to check for changes to the check boxes
        cCheckBox[Options.INVERTX].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bOptionData[Options.INVERTX] = cCheckBox[Options.INVERTX].isChecked();
            }
        });
        cCheckBox[Options.INVERTY].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bOptionData[Options.INVERTY] = cCheckBox[Options.INVERTY].isChecked();
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
            }
        });

        //Seek bar for Music and Sound
        //Set the seek bars to current settings
        sVolume[Options.MUSIC] = (SeekBar) findViewById(R.id.sMusic);
        sVolume[Options.MUSIC].setProgress(iVolume[Options.MUSIC]);
        sVolume[Options.SOUND] = (SeekBar) findViewById(R.id.sSound);
        sVolume[Options.SOUND].setProgress(iVolume[Options.SOUND]);

        //Listener for seek bars
        sVolume[Options.MUSIC].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iVolume[Options.MUSIC] = progress;
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
        sVolume[Options.SOUND].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iVolume[Options.SOUND] = progress;
                //TODO Sound effect
                //Set Volumes
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
        onBackPressed();
    }

    public void restore (View view){ // Button press restore settings
        //Return to menu activity, setting the default settings
        bOptionData[Options.MUSIC] = true;
        bOptionData[Options.SOUND] = true;
        bOptionData[Options.INVERTX] = false;
        bOptionData[Options.INVERTY] = false;
        bOptionData[Options.ORIENTATION] = true;
        iVolume[Options.MUSIC] = 99;
        iVolume[Options.SOUND] = 99;

        //Reset default settings, and run Save function
        save(view);
    }

    public void save (View view){ // Button press save
        //Return to menu activity, using the newly set settings
        onBackPressed();
    }

}
