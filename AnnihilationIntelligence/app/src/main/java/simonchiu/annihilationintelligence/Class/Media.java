package simonchiu.annihilationintelligence.Class;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import java.io.IOException;

/**
 * Created by Simon on 13/02/2017.
 */

//Media Class, a Singleton class which holds loaded music and sound files, and also plays them
//Data is loaded in the Splash Activity

public class Media {
    private AssetFileDescriptor[] descriptor = new AssetFileDescriptor[3];          //Array of music files
    private MediaPlayer mediaPlayer = new MediaPlayer();                            //The Media player, which plays the music files
    private int[] iSounds = new int[10];                                            //Array of sound files
    private SoundPool soundpool = new SoundPool(4, AudioManager.STREAM_MUSIC,0);    //Loads and plays the sound files

    //Sets the loaded music file to the array of music
    public void setupMusic(AssetFileDescriptor desc, int element) {
        descriptor[element] = desc;
    }

    //Sets the loaded sound file to the soundpool
    public void setupSound(AssetFileDescriptor desc, int element) {
        iSounds[element] = soundpool.load(desc, 1);
    }

    //Play the selected music file. Returns true if successful
    public boolean playMusic(int element, int volume, boolean play) {
        if (play)
        {
            try{
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(descriptor[element].getFileDescriptor(), descriptor[element].getStartOffset(), descriptor[element].getLength());
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
                changeMusicVolume(volume);
                mediaPlayer.start();
                return true;
            }
            catch (IOException e){
                return false;
            }
        }
        stopMusic();
        return true;
    }

    //Stop the current music
    public void stopMusic() {
        mediaPlayer.stop();
    }

    //Play the selected sound effect
    public void playSound(int element, int volume, boolean play) {
        if (play) soundpool.play(iSounds[element], (float) volume/100, (float) volume/100, 0, 0, 1);
    }

    //Change the volume of the music
    public void changeMusicVolume(int volume)
    {
        float fVolume = (float) (Math.log(100 - volume)/Math.log(100));
        mediaPlayer.setVolume(1 - fVolume, 1 - fVolume);
    }

    //Creates an instance of this class, which is passed to any activity which calls this
    private static final Media holder = new Media();
    public static Media getInstance() {return holder;}
}
