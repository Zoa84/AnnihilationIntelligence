package simonchiu.annihilationintelligence.Class.Floors;

import android.content.Context;

import com.threed.jpct.Camera;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

import java.io.IOException;
import java.io.InputStream;

import simonchiu.annihilationintelligence.Activity.GameActivity;
import simonchiu.annihilationintelligence.Class.CollisionMap;

import static simonchiu.annihilationintelligence.Class.Defines.DEG_TO_RAD;
import static simonchiu.annihilationintelligence.Class.TransformFix.fixTrans;

/**
 * Created by Simon on 02/03/2017.
 */

public class FloorFourth extends Floor {
    private String[] textures = {"room", "floor", "table", "chair", "comp", "elev", "door2", "note", "elev_open"};
    private Object3D[] object = new Object3D[33];
    private Object3D[] aObjects = null;

    private Light sun = null;
    private static GameActivity master = null;
    private World world = null;

    private CollisionMap[] Collisions = new CollisionMap[10];
    private int xWallLeft = 33;
    private int xWallRight = -33;
    private int yWallFront = -34;
    private int yWallBack = 28;
    private Object3D[] aInteObjects = new Object3D[3];
    private boolean bElevDeath;

    Context context;

    public FloorFourth(Context context) {
        if (master == null) {

            this.context = context;

            bElevDeath = true;

            world = new World();
            world.setAmbientLight(100, 100, 100);

            sun = new Light(world);
            sun.setIntensity(100, 100, 100);

            Texture texture;

            //For loop loading textures
            int resID;
            for (int i = 0; i < textures.length; i++) {
                resID = context.getResources().getIdentifier(textures[i], "drawable", context.getPackageName());
                texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 128, 128));
                if (!TextureManager.getInstance().containsTexture(textures[i])) { TextureManager.getInstance().addTexture(textures[i], texture); }
            }

            //Using an input stream, we get the obj by the objects name
            //and load it to tObjects, which is an array, as the loadobj function returns multiple objects
            //however in our case, it happens to only be one object each time.
            //This is then but into an array called object which contains all the loaded objects together
            //These can be set textures and built

            ObjectLoader(context, 0, "room");

            ObjectLoader(context, 1, "floor");

            ObjectLoader(context, 2, "table");
            ObjectLoader(context, 3, "table");
            ObjectLoader(context, 4, "table");
            ObjectLoader(context, 5, "table");
            ObjectLoader(context, 6, "table");
            ObjectLoader(context, 7, "table");
            ObjectLoader(context, 8, "table");
            ObjectLoader(context, 9, "table");
            ObjectLoader(context, 10, "table");

            ObjectLoader(context, 11, "chair");
            ObjectLoader(context, 12, "chair");
            ObjectLoader(context, 13, "chair");
            ObjectLoader(context, 14, "chair");
            ObjectLoader(context, 15, "chair");
            ObjectLoader(context, 16, "chair");
            ObjectLoader(context, 17, "chair");
            ObjectLoader(context, 18, "chair");
            ObjectLoader(context, 19, "chair");

            ObjectLoader(context, 20, "comp");
            ObjectLoader(context, 21, "comp");
            ObjectLoader(context, 22, "comp");
            ObjectLoader(context, 23, "comp");
            ObjectLoader(context, 24, "comp");
            ObjectLoader(context, 25, "comp");
            ObjectLoader(context, 26, "comp");
            ObjectLoader(context, 27, "comp");
            ObjectLoader(context, 28, "comp");

            ObjectLoader(context, 29, "elev_open");

            ObjectLoader(context, 30, "door2");

            ObjectLoader(context, 31, "note");

            Collisions[0] = new CollisionMap(-20, 20, 5, 3);
            Collisions[1] = new CollisionMap(-20, 0, 5, 3);
            Collisions[2] = new CollisionMap(-20, -20, 5, 3);
            Collisions[3] = new CollisionMap(0, 20, 5, 3);
            Collisions[4] = new CollisionMap(0, 0, 5, 3);
            Collisions[5] = new CollisionMap(0, -20, 5, 3);
            Collisions[6] = new CollisionMap(20, 20, 5, 3);
            Collisions[7] = new CollisionMap(20, 0, 5, 3);
            Collisions[8] = new CollisionMap(20, -20, 5, 3);

            Collisions[9] = new CollisionMap(-15, 36, 14, 7);

            SetPosition(2);

            SimpleVector sv = new SimpleVector();
            sv.set(object[0].getTransformedCenter());

            object[2].translate(fixTrans(-20f, -6f, -20f));
            object[3].translate(fixTrans(-20f, -6f, 0f));
            object[4].translate(fixTrans(-20f, -6f, 20f));
            object[5].translate(fixTrans(0f, -6f, -20f));
            object[6].translate(fixTrans(0f, -6f, 0f));
            object[7].translate(fixTrans(0f, -6f, 20f));
            object[8].translate(fixTrans(20f, -6f, -20f));
            object[9].translate(fixTrans(20f, -6f, 0f));
            object[10].translate(fixTrans(20f, -6f, 20f));

            object[11].translate(fixTrans(-20f, -5.5f, -15.5f));
            object[12].translate(fixTrans(-20f, -5.5f, 4.5f));
            object[13].translate(fixTrans(-20f, -5.5f, 24.5f));
            object[14].translate(fixTrans(0f, -5.5f, -15.5f));
            object[15].translate(fixTrans(0f, -5.5f, 4.5f));
            object[16].translate(fixTrans(0f, -5.5f, 24.5f));
            object[17].translate(fixTrans(20f, -5.5f, -15.5f));
            object[18].translate(fixTrans(20f, -5.5f, 4.5f));
            object[19].translate(fixTrans(20f, -5.5f, 24.5f));

            object[20].translate(fixTrans(-20f, -1.5f, -20f));
            object[21].translate(fixTrans(-20f, -1.5f, 0f));
            object[22].translate(fixTrans(-20f, -1.5f, 20f));
            object[23].translate(fixTrans(0f, -1.5f, -20f));
            object[24].translate(fixTrans(0f, -1.5f, 0f));
            object[25].translate(fixTrans(0f, -1.5f, 20f));
            object[26].translate(fixTrans(20f, -1.5f, -20f));
            object[27].translate(fixTrans(20f, -1.5f, 0f));
            object[28].translate(fixTrans(20f, -1.5f, 20f));

            aInteObjects[0] = object[29];   //elevator
            aInteObjects[1] = object[30];   //door 2 - left door
            aInteObjects[2] = object[31];   //note

            sv.y -= 100;
            sv.z -= 100;
            sun.setPosition(sv);
            MemoryHelper.compact();

            if (master == null) {
                Logger.log("Saving master Activity!");
                master = ((GameActivity) context);
            }
        }
    }

    public void SetElevSafe() {
        bElevDeath = false;
        ObjectLoader(context, 29, "elev");
    }

    public boolean GetElevSafe() {
        return bElevDeath;
    }

    public World GetWorld() {
        return world;
    }

    public void SetPosition(int i) {
        SetPosition(i, world);
    }

    public void Destroy() {
        Destroy(world);
        master = null;
    }

    private void ObjectLoader(Context context, int i, String name) {
        ObjectLoader(context, i, name, aObjects, object, world);
    }

    public boolean Collisions(float xPos, float yPos) {
        for (int i = 0; i < Collisions.length; i++) {
            if (Collisions[i].Check(xPos, yPos)) {
                return true;
            }
        }
        return false;
    }

    public boolean CollisionsWallX(float xPos) {
        return (xPos > xWallLeft || xPos < xWallRight);
    }

    public boolean CollisionsWallY(float yPos) {
        return (yPos > yWallBack || yPos < yWallFront);
    }

    public Object3D[] GetInteObjects() {
        return aInteObjects;
    }

    public String Interact(int i) {
        if (i == 0) {
            //elev - Available after entering 4th floor
            return "Which floor?";
        }
        else if (i == 1) {
            //door2 - Check if we have the right item
            return "The door is locked. It needs a key card";
        }
        else if (i == 2) {
            //door2 - Check if we have the right item
            return "It reads: Warning: Take care when using the";
        }
        return null;
    }
}