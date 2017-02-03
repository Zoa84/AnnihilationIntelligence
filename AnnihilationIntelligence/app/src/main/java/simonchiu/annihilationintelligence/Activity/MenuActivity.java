package simonchiu.annihilationintelligence.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import simonchiu.annihilationintelligence.R;

/*The Menu Activity. This is the central hub of the app, and is accessed after the Splash Screen
From here the player can choose to start the game, change options or exit. If exiting using the
Android buttons, a toast message will appear to ask the player to press again to exit.
 */

public class MenuActivity extends AppCompatActivity {

    boolean back = false;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void start (View view){ // Button press start game
        Intent intent = new Intent(MenuActivity.this,GameActivity.class);
        startActivity(intent);
    }

    public void options (View view){ // Button press options
        //Intent intent = new Intent(MenuActivity.this,OptionsActivity.class);
        //startActivity(intent);
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

}
