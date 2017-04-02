package simonchiu.annihilationintelligence.Class.Floors;

import android.content.Context;

import com.threed.jpct.Light;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

import simonchiu.annihilationintelligence.Activity.GameActivity;
import simonchiu.annihilationintelligence.Class.CollisionMap;

import static simonchiu.annihilationintelligence.Class.Defines.DEG_TO_RAD;
import static simonchiu.annihilationintelligence.Class.TransformFix.fixRotY;
import static simonchiu.annihilationintelligence.Class.TransformFix.fixTrans;

/**
 * Created by Simon on 11/03/2017.
 */

//Ground floor class

public class FloorGround extends Floor{
    //List of object and texture names to load
    private String[] textures = {"room", "floor", "chair", "comp", "elev", "door1", "reception", "frontdoor", "rug", "sign0"};
    private Object3D[] object = new Object3D[12];               //Array of all objects

    private World world = null;                                 //The world, used to draw the 3D game
    private static GameActivity master = null;
    private CollisionMap[] Collisions = new CollisionMap[2];    //An array of collision maps, for the tables and elevator

    //The limits of the room
    private int xWallLeft = 33;
    private int xWallRight = -33;
    private int yWallFront = -32;
    private int yWallBack = 30;
    private Object3D[] aInteObjects = new Object3D[3];          //Array of interactable objects

    public FloorGround(Context context) {
        if (master == null) {
            //The world to draw and place objects in
            world = new World();
            world.setAmbientLight(100, 100, 100);

            //jPCT Lighting object
            Light sun = new Light(world);
            sun.setIntensity(100, 100, 100);

            Texture texture;
            //For loop loading textures
            int resID;
            for (int i = 0; i < textures.length; i++) {
                resID = context.getResources().getIdentifier(textures[i], "drawable", context.getPackageName());
                texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 128, 128));
                if (!TextureManager.getInstance().containsTexture(textures[i])) { TextureManager.getInstance().addTexture(textures[i], texture); }
            }

            //Load all the objects, using the ObjectLoader function from the base class
            ObjectLoader(context, 0, "room");
            ObjectLoader(context, 1, "floor");
            ObjectLoader(context, 2, "elev");
            ObjectLoader(context, 3, "door1");
            ObjectLoader(context, 4, "frontdoor");
            ObjectLoader(context, 5, "reception");
            ObjectLoader(context, 6, "chair");
            ObjectLoader(context, 7, "chair");
            ObjectLoader(context, 8, "comp");
            ObjectLoader(context, 9, "comp");
            ObjectLoader(context, 10, "rug");
            ObjectLoader(context, 11, "sign0");

            //Add collisions to the collision map
            Collisions[0] = new CollisionMap(-15, 36, 14, 3);   //Elevator
            Collisions[1] = new CollisionMap(-24, -19, 13, 19); //Reception

            //Set the starting position of the player
            SetPosition(3);

            //Translate and rotate all objects
            object[6].translate(fixTrans(-24f, -5.5f, 30f));
            object[6].rotateY(fixRotY(-90f) * DEG_TO_RAD);
            object[7].translate(fixTrans(-24f, -5.5f, 17.5f));
            object[7].rotateY(fixRotY(-105f) * DEG_TO_RAD);
            object[8].translate(fixTrans(-16f, -1.2f, 18.5f));
            object[8].rotateY(fixRotY(-90f) * DEG_TO_RAD);
            object[9].translate(fixTrans(-16f, -1.2f, 30f));
            object[9].rotateY(fixRotY(-90f) * DEG_TO_RAD);

            //Add interactable objects to an array
            aInteObjects[0] = object[2];    //elevator
            aInteObjects[1] = object[3];    //door 1 - right door
            aInteObjects[2] = object[4];    //Front door

            //Set the lighting position
            SimpleVector sv = new SimpleVector();
            sv.set(object[0].getTransformedCenter());
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

    //Returns the world to the GSV
    public World GetWorld() {
        return world;
    }

    //Set the position of the character in the world, using the base class
    public void SetPosition(int i) {
        SetPosition(i, world);
    }

    //Destroys this world, using the base class
    public void Destroy() {
        Destroy(world);
        master = null;
    }

    //Loads the selected object into an array, using the base class
    private void ObjectLoader(Context context, int i, String name) {
        ObjectLoader(context, i, name, object, world);
    }

    //Returns if we are colliding with an object to the GSV
    public boolean Collisions(float xPos, float yPos) {
        for (int i = 0; i < Collisions.length; i++) {
            if (Collisions[i].Check(xPos, yPos)) {
                return true;
            }
        }
        return false;
    }

    //Returns if we are colliding with the wall in X
    public boolean CollisionsWallX(float xPos) {
        return (xPos > xWallLeft || xPos < xWallRight);
    }

    //Returns if we are colliding with the wall in Y
    public boolean CollisionsWallY(float yPos) {
        return (yPos > yWallBack || yPos < yWallFront);
    }

    //Return the interactable objects to the GSV
    public Object3D[] GetInteObjects() {
        return aInteObjects;
    }

    //Return a string relating to the interacted object
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
