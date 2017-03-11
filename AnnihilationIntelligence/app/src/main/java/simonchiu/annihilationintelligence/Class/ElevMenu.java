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

//ElevMenu class.

public class ElevMenu {
    private Texture tTexture;
    private Rect rMenu;

    public ElevMenu(Point pPoint, Context context) {
        //Set position of the menu using construction parameters
        rMenu = new Rect((pPoint.x/2) - (pPoint.x/4 - 50), 100, (pPoint.x/4 - 50)*2, pPoint.y-200);

        //Load Menu background
        int resID;
        resID = context.getResources().getIdentifier("img_grey", "drawable", context.getPackageName());
        tTexture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));
    }

    public void Draw(FrameBuffer fb) {
        fb.blit(tTexture, 0, 0, rMenu.left, rMenu.top, rMenu.right, rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);
    }
}
