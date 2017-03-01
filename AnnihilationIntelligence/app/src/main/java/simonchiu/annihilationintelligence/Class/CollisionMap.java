package simonchiu.annihilationintelligence.Class;

/**
 * Created by Simon on 01/03/2017.
 */

public class CollisionMap {
    float iXPos;
    float iYPos;
    float iXSize;
    float iYSize;

    public CollisionMap(float xPos, float yPos, int xSize, int ySize) {
        iXPos = xPos;
        iYPos = yPos;
        iXSize = xSize;
        iYSize = ySize;
    }

    public boolean CheckX(float xPos) {
        if (xPos > iXPos - iXSize) {
            if (xPos < iXPos + iXSize) {
                return true;
            }
        }
        return false;
    }

    public boolean CheckY(float yPos) {
        if (yPos > iYPos - iYSize) {
            if (yPos < iYPos + iYSize) {
                return true;
            }
        }
        return false;
    }

    public boolean Check(float xPos, float yPos) {
        if (xPos > iXPos - iXSize) {
            if (xPos < iXPos + iXSize) {
                if (yPos > iYPos - iYSize) {
                    if (yPos < iYPos + iYSize) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
