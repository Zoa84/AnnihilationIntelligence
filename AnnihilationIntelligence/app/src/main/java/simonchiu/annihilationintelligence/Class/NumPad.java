package simonchiu.annihilationintelligence.Class;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.util.BitmapHelper;

/**
 * Created by Simon on 11/03/2017.
 */

//The Number Pad class, used to draw the number pad, as well as draw and position the
//number pad buttons

public class NumPad {
    private Texture tTexture0;                  //The texture for the number pad menu colour
    private Texture tTexture1;                  //The texture for the white border
    private Rect rMenu;                         //A Rectangle shape of the number pad menu, used for drawing
    private Button[] bNumbers = new Button[12]; //An array of all the number pad buttons (0-9 and Cancel and OK)
    private int iSpacing = 150;                 //An integer for the spacing between buttons
    private int iBorder = 30;                   //An integer for the size of the white border

    public NumPad(Point pPoint, Context context) {
        //Set position of the menu using construction parameters
        rMenu = new Rect((pPoint.x/2) - (pPoint.x/4), 100, (pPoint.x/4)*2, pPoint.y-200);

        //Load Menu background and white colour
        int resID;
        resID = context.getResources().getIdentifier("img_grey", "drawable", context.getPackageName());
        tTexture0 = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));
        resID = context.getResources().getIdentifier("img_white", "drawable", context.getPackageName());
        tTexture1 = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));

        //Set the position of all the muber pad buttons
        bNumbers[0] = new Button("0", pPoint.x/2, (pPoint.y/2) + (iSpacing*2), 64, context);
        bNumbers[1] = new Button("1", (pPoint.x/2)-iSpacing, (pPoint.y/2) - iSpacing, 64, context);
        bNumbers[2] = new Button("2", pPoint.x/2, (pPoint.y/2) - iSpacing, 64, context);
        bNumbers[3] = new Button("3", (pPoint.x/2)+iSpacing, (pPoint.y/2) - iSpacing, 64, context);
        bNumbers[4] = new Button("4", (pPoint.x/2)-iSpacing, (pPoint.y/2), 64, context);
        bNumbers[5] = new Button("5", pPoint.x/2, (pPoint.y/2), 64, context);
        bNumbers[6] = new Button("6", (pPoint.x/2)+iSpacing, (pPoint.y/2), 64, context);
        bNumbers[7] = new Button("7", (pPoint.x/2)-iSpacing, (pPoint.y/2) + iSpacing, 64, context);
        bNumbers[8] = new Button("8", pPoint.x/2, (pPoint.y/2) + iSpacing, 64, context);
        bNumbers[9] = new Button("9", (pPoint.x/2)+iSpacing, (pPoint.y/2) + iSpacing, 64, context);
        bNumbers[10] = new Button(3, (pPoint.x/2)-iSpacing, (pPoint.y/2) + (iSpacing*2), 64, context);
        bNumbers[11] = new Button(4, (pPoint.x/2)+iSpacing, (pPoint.y/2) + (iSpacing*2), 64, context);
    }

    public void Draw(FrameBuffer fb) {
        //Draw the number pad background, and the white border
        fb.blit(tTexture0, 0, 0, rMenu.left, rMenu.top, rMenu.right, rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);
        fb.blit(tTexture1, 0, 0, rMenu.left, rMenu.top, iBorder, rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);
        fb.blit(tTexture1, 0, 0, rMenu.left, rMenu.top, rMenu.right, iBorder, FrameBuffer.TRANSPARENT_BLITTING);
        fb.blit(tTexture1, 0, 0, rMenu.left + rMenu.right, rMenu.top + rMenu.bottom, -iBorder, -rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);
        fb.blit(tTexture1, 0, 0, rMenu.left + rMenu.right, rMenu.top + rMenu.bottom, -rMenu.right, -iBorder, FrameBuffer.TRANSPARENT_BLITTING);

        //For loop to draw all the number pad buttons
        for (int i = 0; i < bNumbers.length; i++) {
            bNumbers[i].Draw(fb);
        }
    }

    //Function to pass a finger press, and returns the index of the button pressed. Returns -1 if none are pressed
    public int Update(float xPos, float yPos) {
        for (int i = 0; i < bNumbers.length; i++) {
            bNumbers[i].Update(xPos, yPos);
            if (bNumbers[i].GetPressed()) {
                Reset();
                return i;
            }
        }
        Reset();
        return -1;
    }

    //Resets the button presses
    private void Reset() {
        for (int i = 0; i < bNumbers.length; i++) {
            bNumbers[i].Reset();
        }
    }


}
