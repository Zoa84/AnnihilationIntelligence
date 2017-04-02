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

import static simonchiu.annihilationintelligence.Class.TransformFix.fixTrans;

/**
 * Created by Simon on 11/03/2017.
 */

//Second floor class

public class FloorSecond extends Floor{
    //List of object and texture names to load
    private String[] textures = {"room", "floor", "table", "chair", "comp", "elev", "door1", "door2", "key2", "elecbox", "elecbox_open2", "sign2"};
    private Object3D[] object = new Object3D[36];               //Array of all objects

    private World world = null;                                 //The world, used to draw the 3D game
    private static GameActivity master = null;
    private CollisionMap[] Collisions = new CollisionMap[10];   //An array of collision maps, for the tables and elevator

    //The limits of the room
    private int xWallLeft = 33;
    private int xWallRight = -33;
    private int yWallFront = -32;
    private int yWallBack = 30;
    private Object3D[] aInteObjects = new Object3D[5];          //Array of interactable objects

    private boolean bElecOpen = false;          //Boolean for if the electrical box has been opened

    public FloorSecond(Context context) {
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
            ObjectLoader(context, 29, "elev");
            ObjectLoader(context, 30, "door1");
            ObjectLoader(context, 31, "door2");
            ObjectLoader(context, 32, "key2");
            ObjectLoader(context, 33, "elecbox");
            ObjectLoader(context, 34, "elecbox_open2");
            ObjectLoader(context, 35, "sign2");

            //Add collisions to the collision map
            Collisions[0] = new CollisionMap(-20, 20, 5, 3);
            Collisions[1] = new CollisionMap(-20, 0, 5, 3);
            Collisions[2] = new CollisionMap(-20, -20, 5, 3);
            Collisions[3] = new CollisionMap(0, 20, 5, 3);
            Collisions[4] = new CollisionMap(0, 0, 5, 3);
            Collisions[5] = new CollisionMap(0, -20, 5, 3);
            Collisions[6] = new CollisionMap(20, 20, 5, 3);
            Collisions[7] = new CollisionMap(20, 0, 5, 3);
            Collisions[8] = new CollisionMap(20, -20, 5, 3);
            Collisions[9] = new CollisionMap(-15, 36, 14, 3);

            //Set the starting position of the player
            SetPosition(3);

            //Translate and rotate all objects
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

            //Add interactable objects to an array
            aInteObjects[0] = object[29];   //elevator
            aInteObjects[1] = object[30];   //door 1 - right door
            aInteObjects[2] = object[31];   //door 2 - left door
            aInteObjects[3] = object[32];   //keycard 2
            aInteObjects[4] = object[34];   //electrical box

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
            //door2 - Check if we have the right item
            return "The door is locked. It needs a key card";
        }
        else if (i == 3) {
            //key2 - Pickup item
            object[32].setVisibility(false);
            return "Got Keycard Lv2";
        }
        else if (i == 4) {
            //electrical box
            object[33].setVisibility(false);
            return "You remove the screws and the panel";
        }
        return null;
    }

    //Sets the electrical box to open
    public void OpenElec() {
        if (!bElecOpen) {bElecOpen = true;}
    }

    //Return the state of the electrical box
    public boolean GetElec() {
        return bElecOpen;
    }
}
