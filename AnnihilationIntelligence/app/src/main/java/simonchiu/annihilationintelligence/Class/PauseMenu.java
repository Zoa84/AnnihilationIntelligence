package simonchiu.annihilationintelligence.Class;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.util.BitmapHelper;

/**
 * Created by Simon on 28/02/2017.
 */

//PauseMenu class, which is used to draw the box to cover the background
//This is actually a very basic class, as the Resume and Return to Menu is done via the buttons and the GameSurfaceView

public class PauseMenu {
    private Texture tTexture0;
    private Texture tTexture1;
    private Rect rMenu;
    private int iBorder = 30;

    public PauseMenu(int xPos, int yPos, int xSize, int ySize, Context context) {
        //Set position of the menu using construction parameters
        rMenu = new Rect(xPos - xSize, yPos - ySize, xSize*2, ySize*2);

        //Load Menu background
        int resID;
        resID = context.getResources().getIdentifier("img_pause_menu", "drawable", context.getPackageName());
        tTexture0 = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));
        resID = context.getResources().getIdentifier("img_white", "drawable", context.getPackageName());
        tTexture1 = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));
    }

    public void Draw(FrameBuffer fb) {
        fb.blit(tTexture0, 0, 0, rMenu.left, rMenu.top, rMenu.right, rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);

        fb.blit(tTexture1, 0, 0, rMenu.left, rMenu.top, iBorder, rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);
        fb.blit(tTexture1, 0, 0, rMenu.left, rMenu.top, rMenu.right, iBorder, FrameBuffer.TRANSPARENT_BLITTING);
        fb.blit(tTexture1, 0, 0, rMenu.left + rMenu.right, rMenu.top + rMenu.bottom, -iBorder, -rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);
        fb.blit(tTexture1, 0, 0, rMenu.left + rMenu.right, rMenu.top + rMenu.bottom, -rMenu.right, -iBorder, FrameBuffer.TRANSPARENT_BLITTING);
    }
}
