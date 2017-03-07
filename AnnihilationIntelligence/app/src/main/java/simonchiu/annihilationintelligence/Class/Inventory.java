package simonchiu.annihilationintelligence.Class;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Logger;
import com.threed.jpct.Texture;
import com.threed.jpct.util.BitmapHelper;

/**
 * Created by Simon on 04/03/2017.
 */

//Class for drawing and managing the inventory. They are drawn to the top right corner
//Used like a singleton, there is only one instance of Inventory in the GameSurfaceView, and is
//used for any of the floors, thus it can keep data over between floors
//0 = key1, 1 = key2, 2 = screwdriver

public class Inventory {
    private boolean bInventory[] = {false, false, false};       //Array of whether we have picked up the item
    private boolean bInventoryUse[] = {false, false, false};    //Array of whether an item is selected. This could be changed to a single integer, but could be used for selecting multiple items
    private int iXPos[] = new int[3];                           //Array of the X positions of the icons for the inventory
    private int iYPos[] = new int[3];                           //Array of the Y positions of the icons for the inventory
    private int iSize = 128;                                    //The size of the icon (extents)
    private Rect[] rInventory = new Rect[3];                    //Array of rectangles for drawing the icons
    private Texture[] tTexture = new Texture[6];                //Array of textures for the icons. Includes a second set for when an item is selected

    public Inventory(Point pPoint, Context context) {
        iXPos[0] = pPoint.x-iSize;
        iYPos[0] = iSize;
        iXPos[1] = iXPos[0]-(iSize*2);
        iYPos[1] = iSize;
        iXPos[2] = iXPos[1]-(iSize*2);
        iYPos[2] = iSize;

        rInventory[0] = new Rect(iXPos[0] - iSize, iYPos[0] - iSize, iSize * 2, iYPos[0] + iSize);
        rInventory[1] = new Rect(iXPos[1] - iSize, iYPos[1] - iSize, iSize * 2, iYPos[1] + iSize);
        rInventory[2] = new Rect(iXPos[2] - iSize, iYPos[2] - iSize, iSize * 2, iYPos[2] + iSize);

        //Load item icon textures
        int resID;
        resID = context.getResources().getIdentifier("img_inv_key1_off", "drawable", context.getPackageName());
        tTexture[0] = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 256, 256));
        resID = context.getResources().getIdentifier("img_inv_key2_off", "drawable", context.getPackageName());
        tTexture[1] = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 256, 256));
        resID = context.getResources().getIdentifier("img_inv_screwdriver_off", "drawable", context.getPackageName());
        tTexture[2] = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 256, 256));

        //Load selected item icon textures
        resID = context.getResources().getIdentifier("img_inv_key1_on", "drawable", context.getPackageName());
        tTexture[3] = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 256, 256));
        resID = context.getResources().getIdentifier("img_inv_key2_on", "drawable", context.getPackageName());
        tTexture[4] = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 256, 256));
        resID = context.getResources().getIdentifier("img_inv_screwdriver_on", "drawable", context.getPackageName());
        tTexture[5] = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 256, 256));
    }

    //Set which item we have picked up
    public void SetInventory(int i) {
        if (i >= 0 && i < 3) {
            bInventory[i] = true;
        }
    }

    //Return if an item has been collected
    public boolean GetInventory(int i) {
        return bInventory[i];
    }

    //Pass a finger press, and check if selecting an item
    public boolean Update(float xPos, float yPos) {
        for (int i = 0; i < bInventory.length; i++) {
            if (bInventory[i] && iXPos[i] - iSize < xPos && iYPos[i] - iSize < yPos && iXPos[i] + iSize > xPos && iYPos[i] + iSize > yPos) {
                //If item is selected, then unselect it
                if (bInventoryUse[i]) {
                    bInventoryUse[i] = false;
                }
                //If not selected, select it, but unselect anything else
                else {
                    bInventoryUse[i] = true;
                    for (int j = 0; j < bInventory.length; j++) {
                        if (j != i) {bInventoryUse[j] = false;}
                    }
                }
                return true;
            }
        }
        return false;
    }

    //Draw the inventory (if collected), and draw if being used
    public void Draw(FrameBuffer fb) {
        for (int i = 0; i < bInventory.length; i++) {
            if (bInventory[i]) {
                if (bInventoryUse[i]) {
                    fb.blit(tTexture[i+3], 0, 0, rInventory[i].left, rInventory[i].top, rInventory[i].right, rInventory[i].bottom, FrameBuffer.TRANSPARENT_BLITTING);
                }
                else {
                    fb.blit(tTexture[i], 0, 0, rInventory[i].left, rInventory[i].top, rInventory[i].right, rInventory[i].bottom, FrameBuffer.TRANSPARENT_BLITTING);
                }
            }
        }
    }

    //Reset inventory
    public void Reset() {
        bInventory[0] = false;
        bInventory[1] = false;
        bInventory[2] = false;
    }
}
