package simonchiu.annihilationintelligence.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import simonchiu.annihilationintelligence.R;

import static simonchiu.annihilationintelligence.Class.Defines.INVERTX;
import static simonchiu.annihilationintelligence.Class.Defines.INVERTY;
import static simonchiu.annihilationintelligence.Class.Defines.MUSIC;
import static simonchiu.annihilationintelligence.Class.Defines.ORIENTATION;
import static simonchiu.annihilationintelligence.Class.Defines.SOUND;

/*The Splash Screen Activity, and the starting point of the app. From here it creates a loading screen
for the player, then moves on to the Menu Activity.
 */

public class SplashActivity extends AppCompatActivity {

    boolean[] bOptionData = new boolean[5]; //Array of booleans for the checkboxes and radio groups under Defines (using class Defines)
    int[] iVolume = new int[2];             //Array of the volume for music (0) and sound (1)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    //After loading all necessary data, send it to the Menu Activity
                    Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                    intent.putExtra("optionData", bOptionData);
                    intent.putExtra("volumeData", iVolume);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();

        //Loading data from text file.
        File root = new File(Environment.getExternalStorageDirectory(), "Annihilation Intelligence");
        //If the directory does not exist, make one
        if (!root.exists()) {
            root.mkdirs();
        }
        try {
            //Load from text file
            File file = new File(root, "options.txt");
            //If the file does not exist, create one with default settings
            //Will be used during first time use of the app, or if the text file is somehow removed
            if (!file.exists()) {
                Toast.makeText(this, "Creating Text File", Toast.LENGTH_SHORT).show();
                FileWriter writer = new FileWriter(file);
                writer.append("true\ntrue\nfalse\nfalse\ntrue\n99\n99");
                writer.flush();
                writer.close();
            }
            //Create a reader and read through each line of the text file, as well as set them
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            //If any fails, will set default data for them
            //Music Checkbox
            if ((line = br.readLine()) != null) bOptionData[MUSIC] = Boolean.parseBoolean(line);
            else bOptionData[MUSIC] = true;
            //Sound Checkbox
            if ((line = br.readLine()) != null) bOptionData[SOUND] = Boolean.parseBoolean(line);
            else bOptionData[SOUND] = true;
            //Invert X axis Checkbox
            if ((line = br.readLine()) != null) bOptionData[INVERTX] = Boolean.parseBoolean(line);
            else bOptionData[INVERTX] = false;
            //Invert Y axis Checkbox
            if ((line = br.readLine()) != null) bOptionData[INVERTY] = Boolean.parseBoolean(line);
            else bOptionData[INVERTY] = false;
            //Orientation
            if ((line = br.readLine()) != null) bOptionData[ORIENTATION] = Boolean.parseBoolean(line);
            else bOptionData[ORIENTATION] = true;
            //Music Volume
            if ((line = br.readLine()) != null) iVolume[MUSIC] = Integer.parseInt(line);
            else iVolume[MUSIC] = 99;
            //Sound Volume
            if ((line = br.readLine()) != null) iVolume[SOUND] = Integer.parseInt(line);
            else iVolume[SOUND] = 99;
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //After loading from text file, set the current splash screen to correct orientation
        //OR do this just as it is loading during parsing
        if (bOptionData[ORIENTATION]) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        Toast.makeText(this, "Data Loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
