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
//The game can use the GetPressed function to return if the button has bee pressed, and will
//reset this when drawn
//This class does not hold any functionality in terms of what the button does,
//but provides a method of letting the program know what functionality we want

public class Button {
    private int iXPos;
    private int iYPos;
    private int iXSize;
    private int iYSize;
    private boolean bPressed = false;
    private Rect rButton;
    private Texture texture;
    private String sText;
    private boolean bText = false;

    public Button(String text, int xPos, int yPos, int xSize, int ySize, Context context) {
        iXPos = xPos;
        iYPos = yPos;
        iXSize = xSize;
        iYSize = ySize;
        sText = text;
        bText = true;

        //Set position of button using construction parameters
        rButton = new Rect(iXPos - iXSize, iYPos - iYSize, iXSize*2, iYSize*2);

        int resID;
        //Load Button
        resID = context.getResources().getIdentifier("img_button_pause", "drawable", context.getPackageName());
        texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));
    }

    public Button(int type, int xPos, int yPos, int size, Context context) {
        iXPos = xPos;
        iYPos = yPos;
        iXSize = size;
        iYSize = size;

        //Set position of button using construction parameters
        rButton = new Rect(iXPos - iXSize, iYPos - iYSize, iXSize*2, iYSize*2);

        int resID;
        //Load Button
        resID = context.getResources().getIdentifier("img_button_" + Integer.toString(type), "drawable", context.getPackageName());
        texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), iXSize*2, iYSize*2));
    }

    public void Update(float xPos, float yPos) {
        if (iXPos - iXSize < xPos && iYPos - iYSize < yPos && iXPos + iXSize > xPos && iYPos + iYSize > yPos) {
            bPressed = true;
        }
    }

    public void Draw(FrameBuffer fb) {
        fb.blit(texture, 0, 0, rButton.left, rButton.top, rButton.right, rButton.bottom, FrameBuffer.TRANSPARENT_BLITTING);
    }

    public void Draw(FrameBuffer fb, AGLFont font) {
        fb.blit(texture, 0, 0, rButton.left, rButton.top, rButton.right, rButton.bottom, FrameBuffer.TRANSPARENT_BLITTING);
        Rectangle rect = new Rectangle();
        font.getStringBounds(sText, rect);
        font.blitString(fb, sText, iXPos - (rect.width/2), iYPos + 25, 100, RGBColor.WHITE);
    }

    public boolean GetPressed() {
        return bPressed;
    }

    public void Reset() {
        bPressed = false;
    }

}
