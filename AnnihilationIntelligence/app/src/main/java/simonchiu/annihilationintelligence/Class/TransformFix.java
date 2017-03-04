package simonchiu.annihilationintelligence.Class;

import com.threed.jpct.SimpleVector;

/**
 * Created by Simon on 17/02/2017.
 */

//A class to fix the way j-PCT translates and rotates, as it uses a different coordinate
//system. Any translation or rotation can be passed here first before passed to the objects
//transformations

public class TransformFix {

    //Fixes translations from Maya to j-PCT axis
    public static SimpleVector fixTrans(SimpleVector vector)
    {
        SimpleVector translate = new SimpleVector();
        translate.x = vector.x;
        translate.y = -vector.y;
        translate.z = -vector.z;

        return translate;
    }

    //Fixes translations from Maya to j-PCT axis
    public static SimpleVector fixTrans(float x, float y, float z)
    {
        SimpleVector translate = new SimpleVector();
        translate.x = x;
        translate.y = -y;
        translate.z = -z;

        return translate;
    }

    //Fixes rotations from Maya to j-PCT axis
    public static SimpleVector fixRot(SimpleVector vector)
    {
        SimpleVector translate = new SimpleVector();
        translate.x = -vector.x;
        translate.y = -vector.y;
        translate.z = vector.z;

        return translate;
    }

    //Fixes rotations from Maya to j-PCT axis
    public static SimpleVector fixRot(float x, float y, float z)
    {
        SimpleVector translate = new SimpleVector();
        translate.x = -x;
        translate.y = -y;
        translate.z = z;

        return translate;
    }

}
