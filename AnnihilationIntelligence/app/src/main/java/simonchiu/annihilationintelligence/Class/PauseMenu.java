package simonchiu.annihilationintelligence.Class;

import android.content.Context;
import android.graphics.Rect;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.util.BitmapHelper;

/**
 * Created by Simon on 28/02/2017.
 */

public class PauseMenu {
    private int iXPos;
    private int iYPos;
    private int iXSize;
    private int iYSize;
    private Texture texture;
    private Rect rMenu;

    private Rect rResume;
    private Rect rReturn;

    public PauseMenu(int xPos, int yPos, int xSize, int ySize, Context context) {
        iXPos = xPos;
        iYPos = yPos;
        iXSize = xSize;
        iYSize = ySize;

        //Set position of button using construction parameters
        rMenu = new Rect(iXPos - iXSize, iYPos - iYSize, iXSize*2, iYSize*2);

        int resID;
        //Load Button
        resID = context.getResources().getIdentifier("img_pause_menu", "drawable", context.getPackageName());
        texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 64, 64));
    }

    public void Update() {

    }

    public void Draw(FrameBuffer fb) {
        fb.blit(texture, 0, 0, rMenu.left, rMenu.top, rMenu.right, rMenu.bottom, FrameBuffer.TRANSPARENT_BLITTING);
    }
}
