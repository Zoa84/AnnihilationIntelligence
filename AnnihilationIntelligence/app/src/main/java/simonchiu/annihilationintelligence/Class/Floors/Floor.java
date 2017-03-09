package simonchiu.annihilationintelligence.Class.Floors;

import android.content.Context;

import com.threed.jpct.Camera;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import java.io.IOException;
import java.io.InputStream;

import simonchiu.annihilationintelligence.Activity.GameActivity;

import static simonchiu.annihilationintelligence.Class.Defines.DEG_TO_RAD;

/**
 * Created by Simon on 07/03/2017.
 */

public class Floor {

    public void SetPosition(int i, World world) {
        Camera cam = world.getCamera();
        //Starting position
        if (i == 0) {
            cam.setPosition(-15f, 0, -30f);
        }
        //Elevator
        else if (i == 1) {
            cam.setPosition(-15f, 0, 26f);
        }
        //Door 2 (Left)
        else if (i == 2) {
            cam.setPosition(8f, 0, 26f);
        }
        //Door 1 (Right)
        else if (i == 3) {
            cam.setPosition(24f, 0, 26f);
        }
    }

    public void Destroy(World world) {
        world.dispose();
        TextureManager.getInstance().flush();
    }

    public void ObjectLoader(Context context, int i, String name, Object3D[] aObjects, Object3D[] object, World world) {
        InputStream is;

        try {
            is = context.getResources().getAssets().open("objects/" + name + ".obj");
            aObjects = Loader.loadOBJ(is, null, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        object[i] = aObjects[0];
        object[i].setTexture(name);
        object[i].build();
        world.addObject(object[i]);

        //Due to jPCT world axis, rotate around X-axis to draw right-side up
        object[i].setRotationPivot(SimpleVector.ORIGIN);
        object[i].rotateX(180 * DEG_TO_RAD);
        object[i].rotateMesh();
        object[i].clearRotation();
    }

}
