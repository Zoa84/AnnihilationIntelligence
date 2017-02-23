package simonchiu.annihilationintelligence.Class;

import android.content.Context;
import android.graphics.Rect;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.util.BitmapHelper;

/**
 * Created by Simon on 21/02/2017.
 */

//Pass the x and y pos from the constructor, as well as size, and will setup the data,
//as well as draw to screen

public class Joystick {
    private int iXPos;                              //X position of joystick (origin is middle)
    private int iYPos;                              //Y position of joystick
    private int iSize;                              //x/y extents
    private int iLimit;                             //Limits for joystick recognition
    private int iState;                             //State for pointer to joystick
    private float fScale;                           //Scale for joystick to movement
    private Rect rBackground, rButton;              //Saves data to a format to use in the draw function
    private boolean bPressed;
    private Texture[] tTextures = new Texture[2];   //Array of the joystick textures, the background and the button itself


    public Joystick(int xPos, int yPos, int size, Context context) {
        iXPos = xPos;
        iYPos = yPos;
        iSize = size;
        iLimit = iSize + (iSize/2);
        iState = 0;
        fScale = -3000f;

        rBackground = new Rect(iXPos - iSize, iYPos - iSize, iSize*2, iSize*2);
        rButton = new Rect(iXPos - (iSize/2), iYPos - (iSize/2), iSize, iSize);

        int resID;
        Texture texture;
        //Load Joystick background
        resID = context.getResources().getIdentifier("img_joystick_bg", "drawable", context.getPackageName());
        texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), iSize*2, iSize*2));
        tTextures[0] = texture;
        //Load Joystick button
        resID = context.getResources().getIdentifier("img_joystick_button", "drawable", context.getPackageName());
        texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), iSize, iSize));
        tTextures[1] = texture;


    }

    //Have an update function run in the update, can either be used if finger is in boundary, or set with parameters of
    //the fingers x and y and use an if statement to decide if within bounds
    public void Update(float xPos, float yPos) {
        float fLength = ((iXPos - xPos) * (iXPos - xPos)) + ((iYPos - yPos) * (iYPos - yPos));
        if (fLength < iLimit*iLimit) {
            rButton.left = (int) xPos - (iSize / 2);
            rButton.top = (int) yPos - (iSize / 2);
            bPressed = true;
        }
    }

    //Draw function which is passed the necessary place to load texture to (the frame buffer)
    //then draws it
    public void Draw(FrameBuffer fb) {
        //Draw Joystick background
        fb.blit(tTextures[0], 0, 0, rBackground.left, rBackground.top, rBackground.right, rBackground.bottom, FrameBuffer.TRANSPARENT_BLITTING);

        //TODO calculations to move button (if necessary)
        //Draw Joystick button
        fb.blit(tTextures[1], 0, 0, rButton.left, rButton.top, rButton.right, rButton.bottom, FrameBuffer.TRANSPARENT_BLITTING);
    }

    public void Reset() {
        rButton.left = iXPos - (iSize/2);
        rButton.top = iYPos - (iSize/2);
        iState = 0;
        bPressed = false;
    }

    public float GetHor() {
        return ((rButton.left + (iSize / 2)) - iXPos) / fScale;
    }

    public float GetVer() {
        return ((rButton.top + (iSize / 2)) - iYPos)/fScale;
    }

    public void SetState(int state) {
        iState = state;
    }

    public int GetState() {
        return iState;
    }
}
