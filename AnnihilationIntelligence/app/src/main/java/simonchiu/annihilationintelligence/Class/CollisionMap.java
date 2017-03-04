package simonchiu.annihilationintelligence.Class;

/**
 * Created by Simon on 01/03/2017.
 */

//The collision map, used to detect collisions. Instead of 3D collisions with meshes,
//since the player cannot jump, we only need to consider collisions in X and Z, changed
//to X and Y to simplify it. These become Axis-Aligned Bounding Box collisions, checking if
//we have collided in one axis, then the other

public class CollisionMap {
    private float iXPos;    //X position of the object (origin is center)
    private float iYPos;    //Y position of the object (origin is center)
    private float iXSize;   //The size in X of the boundary
    private float iYSize;   //The size in X of the boundary

    public CollisionMap(float xPos, float yPos, int xSize, int ySize) {
        iXPos = xPos;
        iYPos = yPos;
        iXSize = xSize;
        iYSize = ySize;
    }

    //Check if there is a collision in the x axis
    public boolean CheckX(float xPos) {
        if (xPos > iXPos - iXSize) {
            if (xPos < iXPos + iXSize) {
                return true;
            }
        }
        return false;
    }

    //Check if there is a collision in the y (z) axis
    public boolean CheckY(float yPos) {
        if (yPos > iYPos - iYSize) {
            if (yPos < iYPos + iYSize) {
                return true;
            }
        }
        return false;
    }

    //Check if there is a collision
    public boolean Check(float xPos, float yPos) {
        if (CheckX(xPos)) {
            if (CheckY(yPos)) {
                return true;
            }
        }
        return false;
    }

}
