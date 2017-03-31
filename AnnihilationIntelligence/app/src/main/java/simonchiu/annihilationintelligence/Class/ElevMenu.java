package simonchiu.annihilationintelligence.Class;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.util.BitmapHelper;

/**
 * Created by Simon on 10/03/2017.
 */

//The elevator menu class, used to draw the menu when using the elevator, relative to the screen

public class ElevMenu {
    private Texture tTexture0;      //The texture for the elevator menu colour
    private Texture tTexture1;      //The texture for the white border
    private Rect rMenu;             //A Rectangle shape of the elevator menu, used for drawing
    private int iBorder = 30;       //An integer for the size of the white border

    public ElevMenu(Point pPoint, Context context) {
        //Set position of the menu using construction parameters
        rMenu = new Rect((pPoint.x/2) - (pPoint.x/4 - 50), 100, (pPoint.x/4 - 50)*2, pPoint.y-200);

        //Load Menu background and white colour
        int resID;
        resID = context.getResources().getIdentifier("img_grey", "drawable", context.getPackageName());
        tTexture0 = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));
        resID = context.getResources().getIdentifier("img_white", "drawable", context.getPackageName());
        tTexture1 = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));
    }

    //Draw the Elevator menu
    public void Draw(FrameBuffer fb) {
        fb.blit(tTexture0, 0, 0, rMenu.left, rMenu.top, rMenu.right, rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);

        fb.blit(tTexture1, 0, 0, rMenu.left, rMenu.top, iBorder, rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);
        fb.blit(tTexture1, 0, 0, rMenu.left, rMenu.top, rMenu.right, iBorder, FrameBuffer.TRANSPARENT_BLITTING);
        fb.blit(tTexture1, 0, 0, rMenu.left + rMenu.right, rMenu.top + rMenu.bottom, -iBorder, -rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);
        fb.blit(tTexture1, 0, 0, rMenu.left + rMenu.right, rMenu.top + rMenu.bottom, -rMenu.right, -iBorder, FrameBuffer.TRANSPARENT_BLITTING);
    }
}
