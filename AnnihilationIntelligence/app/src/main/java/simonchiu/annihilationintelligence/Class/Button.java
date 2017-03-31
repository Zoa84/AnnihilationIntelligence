package simonchiu.annihilationintelligence.Class;

import android.content.Context;
import android.graphics.Rect;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;
import com.threed.jpct.util.BitmapHelper;

import simonchiu.annihilationintelligence.Include.AGLFont;
import simonchiu.annihilationintelligence.Include.Rectangle;

/**
 * Created by Simon on 27/02/2017.
 */

//A Button class. This is created with a position and size, and can be drawn to the screen
//The update will take the x and y position of a finger to see if it has pressed the button
//The game can use the GetPressed function to return if the button has been pressed, and will
//reset this when drawn
//This class does not hold any functionality in terms of what the button does,
//but provides a method of letting the program know a button has been pressed

public class Button {
    private int iXPos;                  //X position of the button (the origin is the center)
    private int iYPos;                  //Y position of the button (the origin is the center)
    private int iXSize;                 //The size in X of the button (from the origin)
    private int iYSize;                 //The size in Y of the button (from the origin)
    private int iBorder = 20;           //An integer of the size of the white border
    private boolean bPressed = false;   //If the button has been pressed
    private Rect rButton;               //The rectangle shape of the button, used to draw the button
    private Texture tTexture;           //The texture of the button
    private Texture tTextureBorder;     //The texture of the button border (where applicable)
    private String sText;               //The text to draw on top of the button (if used)
    private boolean bText = false;      //If there is text to draw

    //Constructor with text to draw on the button
    public Button(String text, int xPos, int yPos, int xSize, int ySize, Context context) {
        iXPos = xPos;
        iYPos = yPos;
        iXSize = xSize;
        iYSize = ySize;
        sText = text;
        bText = true;

        //Set position of button using construction parameters
        rButton = new Rect(iXPos - iXSize, iYPos - iYSize, iXSize*2, iYSize*2);

        //Load Button texture
        int resID;
        resID = context.getResources().getIdentifier("img_button_pause", "drawable", context.getPackageName());
        tTexture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64), true);
        resID = context.getResources().getIdentifier("img_white", "drawable", context.getPackageName());
        tTextureBorder = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64), true);
    }

    //Constructor without text to draw, taking a button type
    public Button(int type, int xPos, int yPos, int size, Context context) {
        iXPos = xPos;
        iYPos = yPos;
        iXSize = size;
        iYSize = size;

        //Set position of button using construction parameters
        rButton = new Rect(iXPos - iXSize, iYPos - iYSize, iXSize*2, iYSize*2);

        //Load Button texture
        int resID;
        resID = context.getResources().getIdentifier("img_button_" + Integer.toString(type), "drawable", context.getPackageName());
        tTexture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), iXSize*2, iYSize*2), true);
    }

    //Constructor without text to draw, for number pad
    public Button(String number, int xPos, int yPos, int size, Context context) {
        iXPos = xPos;
        iYPos = yPos;
        iXSize = size;
        iYSize = size;

        //Set position of button using construction parameters
        rButton = new Rect(iXPos - iXSize, iYPos - iYSize, iXSize*2, iYSize*2);

        //Load Button
        int resID;
        resID = context.getResources().getIdentifier("img_button_num" + number, "drawable", context.getPackageName());
        tTexture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), iXSize*2, iYSize*2), true);
    }

    //Update to check if the button has been pressed
    public void Update(float xPos, float yPos) {
        if (iXPos - iXSize < xPos && iYPos - iYSize < yPos && iXPos + iXSize > xPos && iYPos + iYSize > yPos) {
            bPressed = true;
        }
    }

    //Draw function without text
    public void Draw(FrameBuffer fb) {
        fb.blit(tTexture, 0, 0, rButton.left, rButton.top, rButton.right, rButton.bottom, FrameBuffer.TRANSPARENT_BLITTING);
    }

    //Draw function with text (taking a font to draw with)
    public void Draw(FrameBuffer fb, AGLFont font) {
        fb.blit(tTexture, 0, 0, rButton.left, rButton.top, rButton.right, rButton.bottom, FrameBuffer.TRANSPARENT_BLITTING);
        if (bText) {
            Rectangle rect = new Rectangle();
            font.getStringBounds(sText, rect);
            font.blitString(fb, sText, iXPos - (rect.width / 2), iYPos + 25, 100, RGBColor.WHITE);

            fb.blit(tTextureBorder, 0, 0, rButton.left, rButton.top, iBorder, rButton.bottom, FrameBuffer.TRANSPARENT_BLITTING);
            fb.blit(tTextureBorder, 0, 0, rButton.left, rButton.top, rButton.right, iBorder, FrameBuffer.TRANSPARENT_BLITTING);
            fb.blit(tTextureBorder, 0, 0, rButton.left + rButton.right, rButton.top + rButton.bottom, -iBorder, -rButton.bottom, FrameBuffer.TRANSPARENT_BLITTING);
            fb.blit(tTextureBorder, 0, 0, rButton.left + rButton.right, rButton.top + rButton.bottom, -rButton.right, -iBorder, FrameBuffer.TRANSPARENT_BLITTING);
        }
    }

    //Returns if the button has been pressed
    public boolean GetPressed() {
        return bPressed;
    }

    //Reset the button press
    public void Reset() {
        bPressed = false;
    }

}
