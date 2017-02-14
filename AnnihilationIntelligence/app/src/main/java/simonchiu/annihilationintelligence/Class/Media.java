package simonchiu.annihilationintelligence.Class;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Simon on 13/02/2017.
 */

/*Media Class, a Singleton class which holds loaded music and sound files, and also plays them
 */

public class Media {
    private AssetFileDescriptor[] descriptor = new AssetFileDescriptor[4];  //Array of music files
    private MediaPlayer mediaPlayer = new MediaPlayer();                    //The Media player, which plays the music files
    private int[] iSounds = new int[5];                                     //Array of sound files
    private SoundPool soundpool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);    //Loads and plays the sound files

    private int[] iVolume = {99, 99};

    //Sets the loaded music file to the array of music
    public void setupMusic(AssetFileDescriptor desc, int element) {
        descriptor[element] = desc;
    }

    //Sets the loaded sound file to the soundpool
    public void setupSound(AssetFileDescriptor desc, int element) {
        iSounds[element] = soundpool.load(desc, 1);
    }

    //Play the selected music file. Returns true if successful
    public boolean playMusic(int element) {
        try{
            mediaPlayer.setDataSource(descriptor[element].getFileDescriptor(), descriptor[element].getStartOffset(), descriptor[element].getLength());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }

    //Stop the current music
    public void stopMusic() {
        mediaPlayer.stop();
    }

    //Play the selected sound effect
    public void playSound(int element) {
        soundpool.play(iSounds[element], (float) iVolume[1], (float) iVolume[1], 0, 0, 1);
    }

    //Creates a instance of this class, which is passed to any activity which calls this
    private static final Media holder = new Media();
    public static Media getInstance() {return holder;}
}
