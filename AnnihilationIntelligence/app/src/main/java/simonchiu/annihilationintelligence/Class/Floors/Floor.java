package simonchiu.annihilationintelligence.Class.Floors;

import android.content.Context;

import com.threed.jpct.Camera;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import java.io.IOException;
import java.io.InputStream;

import static simonchiu.annihilationintelligence.Class.TransformFix.*;

/**
 * Created by Simon on 07/03/2017.
 */

//A base class that each floor class inherits from
//Contains functions that each floor will use, however, does not hold any variables

class Floor {

    //Set the position of the character, such as coming out of an elevator or door
    void SetPosition(int i, World world) {
        Camera cam = world.getCamera();
        //Starting position
        if (i == 0) {
            cam.setPosition(fixTrans(-15f, 1.5f, 30f));
        }
        //Elevator
        else if (i == 1) {
            cam.setPosition(fixTrans(-15f, 1.5f, -26f));
        }
        //Door 2 (Left)
        else if (i == 2) {
            cam.setPosition(fixTrans(8f, 1.5f, -26f));
        }
        //Door 1 (Right)
        else if (i == 3) {
            cam.setPosition(fixTrans(24f, 1.5f, -26f));
        }
    }

    //Destroys the current world. Used as garbage collection when ending the game
    void Destroy(World world) {
        world.dispose();
        TextureManager.getInstance().flush();
    }

    //Loads objects in the correct
    void ObjectLoader(Context context, int i, String name, Object3D[] object, World world) {
        InputStream is;
        Object3D[] localObject = null;

        try {
            is = context.getResources().getAssets().open("objects/" + name + ".obj");
            localObject = Loader.loadOBJ(is, null, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        object[i] = localObject[0];
        object[i].setTexture(name);
        object[i].build();
        world.addObject(object[i]);

        //Fixes the rotation of loaded objects
        object[i] = ObjectLoadFix(object[i]);
    }

}
