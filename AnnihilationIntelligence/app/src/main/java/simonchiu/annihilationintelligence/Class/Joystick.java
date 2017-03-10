package simonchiu.annihilationintelligence.Class;

import android.content.Context;
import android.graphics.Rect;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.util.BitmapHelper;

/**
 * Created by Simon on 21/02/2017.
 */

//Joystick class - Creates joysticks for the HUD, and allows usage of the Joysticks to simulate
//real joysticks. Includes functionality for getting the amount to move in X or Y, as well
//as draw the joystick and the position

public class Joystick {
    private int iXPos;                              //X position of joystick (origin is middle)
    private int iYPos;                              //Y position of joystick
    private int iSize;                              //X/Y extents
    private int iLimit;                             //Limits for joystick recognition
    private int iState;                             //State for pointer to joystick
    private float fScale;                           //Scale for joystick to movement
    private Rect rBackground, rButton;              //Saves data to a format to use in the draw function
    private Texture[] aTextures = new Texture[2];   //Array of the joystick textures, the background and the button itself

    //Gets the x and y pos from the constructor, as well as size, and will setup the data, as well as load the textures
    public Joystick(int xPos, int yPos, int size, Context context) {
        iXPos = xPos;
        iYPos = yPos;
        iSize = size;
        iLimit = iSize + (iSize/2);
        iState = 0;
        fScale = -5000f;

        //Set position of joystick background and button, using constructor parameters
        //Drawn as a rectangle, so need the four corners, not the middle with extents
        rBackground = new Rect(iXPos - iSize, iYPos - iSize, iSize*2, iSize*2);
        rButton = new Rect(iXPos - (iSize/2), iYPos - (iSize/2), iSize, iSize);

        int resID;
        Texture texture;
        //Load Joystick background
        resID = context.getResources().getIdentifier("img_joystick_bg", "drawable", context.getPackageName());
        texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), iSize*2, iSize*2), true);
        aTextures[0] = texture;
        //Load Joystick button
        resID = context.getResources().getIdentifier("img_joystick_button", "drawable", context.getPackageName());
        texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), iSize, iSize));
        aTextures[1] = texture;
    }

    //Update function which is run in the update of the game
    //Gets the X and Y position of a finger, and checks if within the limit to use
    public void Update(float xPos, float yPos) {
        float fLength = ((iXPos - xPos) * (iXPos - xPos)) + ((iYPos - yPos) * (iYPos - yPos));
        if (fLength < iLimit*iLimit) {
            rButton.left = (int) xPos - (iSize / 2);
            rButton.top = (int) yPos - (iSize / 2);
        }
    }

    //Draw function which is passed the necessary place to load texture to (the frame buffer)
    //then draws it
    public void Draw(FrameBuffer fb) {
        //Draw Joystick background
        fb.blit(aTextures[0], 0, 0, rBackground.left, rBackground.top, rBackground.right, rBackground.bottom, FrameBuffer.TRANSPARENT_BLITTING);

        //Draw Joystick button
        fb.blit(aTextures[1], 0, 0, rButton.left, rButton.top, rButton.right, rButton.bottom, FrameBuffer.TRANSPARENT_BLITTING);
    }

    //Reset the joystick buttons position and state
    public void Reset() {
        rButton.left = iXPos - (iSize/2);
        rButton.top = iYPos - (iSize/2);
        iState = 0;
    }

    //Return the horizontal difference of the joystick
    public float GetHor() {
        return ((rButton.left + (iSize / 2)) - iXPos) / fScale;
    }

    //Return the vertical difference of the joystick
    public float GetVer() {
        return ((rButton.top + (iSize / 2)) - iYPos) / fScale;
    }

    //Set the state, which type of finger has been used
    public void SetState(int state) {
        iState = state;
    }

    //Return the current state
    public int GetState() {
        return iState;
    }
}
