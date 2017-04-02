package simonchiu.annihilationintelligence.Class;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

import static simonchiu.annihilationintelligence.Class.Defines.DEG_TO_RAD;

/**
 * Created by Simon on 17/02/2017.
 */

//A class to fix the way jPCT translates and rotates, as it uses a different coordinate
//system. Any translation or rotation can be passed here first before being passed to the objects
//transformations

public class TransformFix {
    //Fixes translations from Maya to jPCT axis, taking a SimpleVector
    public static SimpleVector fixTrans(SimpleVector vector)
    {
        SimpleVector translate = new SimpleVector();
        translate.x = vector.x;
        translate.y = -vector.y;
        translate.z = -vector.z;

        return translate;
    }

    //Fixes translations from Maya to jPCT axis, taking three floats
    public static SimpleVector fixTrans(float x, float y, float z)
    {
        SimpleVector translate = new SimpleVector();
        translate.x = x;
        translate.y = -y;
        translate.z = -z;

        return translate;
    }

    //Fixes rotations from Maya to jPCT axis, taking a SimpleVector
    public static SimpleVector fixRot(SimpleVector vector)
    {
        SimpleVector rotation = new SimpleVector();
        rotation.x = -vector.x;
        rotation.y = -vector.y;
        rotation.z = vector.z;

        return rotation;
    }

    //Fixes rotations from Maya to jPCT axis, taking three floats
    public static SimpleVector fixRot(float x, float y, float z)
    {
        SimpleVector rotation = new SimpleVector();
        rotation.x = -x;
        rotation.y = -y;
        rotation.z = z;

        return rotation;
    }

    //Fixes single rotations in Y
    public static float fixRotY(float y)
    {
        float yRotation;
        yRotation = -y;
        return yRotation;
    }

    //Fixes single rotations in Z
    public static float fixRotZ(float z)
    {
        float zRotation;
        zRotation = -z;
        return zRotation;
    }

    //Fixes the rotation objects are loaded in
    public static Object3D ObjectLoadFix(Object3D object) {
        //Due to jPCT world axis, rotate around X-axis to draw right-side up
        object.setRotationPivot(SimpleVector.ORIGIN);
        object.rotateX(180 * DEG_TO_RAD);
        object.rotateMesh();
        object.clearRotation();
        return object;
    }

}
