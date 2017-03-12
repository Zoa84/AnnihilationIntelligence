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
 * Created by Simon on 11/03/2017.
 */

public class FloorGround extends Floor{
    private String[] textures = {"room", "floor", "chair", "comp", "elev", "door1", "reception", "frontdoor"};
    private Object3D[] object = new Object3D[8];
    private Object3D[] aObjects = null;

    private Light sun = null;
    private static GameActivity master = null;
    private World world = null;

    private CollisionMap[] Collisions = new CollisionMap[2];
    private int xWallLeft = 33;
    private int xWallRight = -33;
    private int yWallFront = -32;
    private int yWallBack = 28;
    private Object3D[] aInteObjects = new Object3D[3];

    public FloorGround(Context context) {
        if (master == null) {

            world = new World();
            world.setAmbientLight(100, 100, 100);

            sun = new Light(world);
            sun.setIntensity(100, 100, 100);

            Texture texture;

            //For loop loading textures
            int resID;
            for (int i = 0; i < textures.length; i++) {
                resID = context.getResources().getIdentifier(textures[i], "drawable", context.getPackageName());
                texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 512, 512));
                if (!TextureManager.getInstance().containsTexture(textures[i])) { TextureManager.getInstance().addTexture(textures[i], texture); }
            }

            //Using an input stream, we get the obj by the objects name
            //and load it to tObjects, which is an array, as the loadobj function returns multiple objects
            //however in our case, it happens to only be one object each time.
            //This is then but into an array called object which contains all the loaded objects together
            //These can be set textures and built

            ObjectLoader(context, 0, "room");

            ObjectLoader(context, 1, "floor");

            ObjectLoader(context, 2, "elev");

            ObjectLoader(context, 3, "door1");

            ObjectLoader(context, 4, "frontdoor");

            ObjectLoader(context, 5, "reception");

            ObjectLoader(context, 6, "chair");
            ObjectLoader(context, 7, "chair");

            Collisions[0] = new CollisionMap(-15, 36, 14, 7);   //Elevator
            Collisions[1] = new CollisionMap(-24, -19, 13, 19); //Reception

            SetPosition(3);

            SimpleVector sv = new SimpleVector();
            sv.set(object[0].getTransformedCenter());

            object[6].translate(fixTrans(-24f, -5.5f, 30f));
            object[6].rotateY(90f * DEG_TO_RAD);
            object[7].translate(fixTrans(-24f, -5.5f, 17.5f));
            object[7].rotateY(105f * DEG_TO_RAD);

            aInteObjects[0] = object[2];    //elevator
            aInteObjects[1] = object[3];    //door 1 - right door
            aInteObjects[2] = object[4];    //Front door

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
            //door1 - Check if we have the right item
            return "The door is locked. It needs a key card";
        }
        else if (i == 2) {
            //front door
            return "Escaped!";
        }
        return null;
    }
}
