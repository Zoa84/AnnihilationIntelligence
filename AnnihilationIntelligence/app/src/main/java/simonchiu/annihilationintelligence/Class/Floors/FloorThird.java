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
import simonchiu.annihilationintelligence.Class.Button;
import simonchiu.annihilationintelligence.Class.CollisionMap;
import simonchiu.annihilationintelligence.Class.Joystick;
import simonchiu.annihilationintelligence.Class.PauseMenu;

import static simonchiu.annihilationintelligence.Class.Defines.DEG_TO_RAD;
import static simonchiu.annihilationintelligence.Class.TransformFix.fixTrans;

/**
 * Created by Simon on 01/03/2017.
 */

public class FloorThird {
    private String[] textures = {"floor", "skybox", "table", "chair", "comp"};
    private Texture[] aTextures = new Texture[textures.length];
    private Object3D[] object = new Object3D[28];
    private Object3D[] aObjects = null;

    private Light sun = null;
    private static GameActivity master = null;
    private World world = null;

    private CollisionMap[] Collisions = new CollisionMap[9];
    private int xWall = 35;
    private int yWall = 35;

    public FloorThird(Context context) {
        if (master == null) {

            world = new World();
            world.setAmbientLight(20, 20, 20);

            sun = new Light(world);
            sun.setIntensity(250, 250, 250);

            Texture texture;

            //For loop loading textures
            int resID;
            for (int i = 0; i < textures.length; i++) {
                resID = context.getResources().getIdentifier(textures[i], "drawable", context.getPackageName());
                texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 512, 512));
                TextureManager.getInstance().addTexture(textures[i], texture);
                aTextures[i] = texture;
            }

            //Using an input stream, we get the obj by the objects name
            //and load it to tObjects, which is an array, as the loadobj function returns multiple objects
            //however in our case, it happens to only be one object each time.
            //This is then but into an array called object which contains all the loaded objects together
            //These can be set textures and built
            ObjectLoader(context, 0, 0, "floor");

            ObjectLoader(context, 1, 2, "table");
            ObjectLoader(context, 2, 2, "table");
            ObjectLoader(context, 3, 2, "table");
            ObjectLoader(context, 4, 2, "table");
            ObjectLoader(context, 5, 2, "table");
            ObjectLoader(context, 6, 2, "table");
            ObjectLoader(context, 7, 2, "table");
            ObjectLoader(context, 8, 2, "table");
            ObjectLoader(context, 9, 2, "table");

            ObjectLoader(context, 10, 3, "chair");
            ObjectLoader(context, 11, 3, "chair");
            ObjectLoader(context, 12, 3, "chair");
            ObjectLoader(context, 13, 3, "chair");
            ObjectLoader(context, 14, 3, "chair");
            ObjectLoader(context, 15, 3, "chair");
            ObjectLoader(context, 16, 3, "chair");
            ObjectLoader(context, 17, 3, "chair");
            ObjectLoader(context, 18, 3, "chair");

            ObjectLoader(context, 19, 4, "comp");
            ObjectLoader(context, 20, 4, "comp");
            ObjectLoader(context, 21, 4, "comp");
            ObjectLoader(context, 22, 4, "comp");
            ObjectLoader(context, 23, 4, "comp");
            ObjectLoader(context, 24, 4, "comp");
            ObjectLoader(context, 25, 4, "comp");
            ObjectLoader(context, 26, 4, "comp");
            ObjectLoader(context, 27, 4, "comp");



            Collisions[0] = new CollisionMap(-20, 20, 5, 3);
            Collisions[1] = new CollisionMap(-20, 0, 5, 3);
            Collisions[2] = new CollisionMap(-20, -20, 5, 3);
            Collisions[3] = new CollisionMap(0, 20, 5, 3);
            Collisions[4] = new CollisionMap(0, 0, 5, 3);
            Collisions[5] = new CollisionMap(0, -20, 5, 3);
            Collisions[6] = new CollisionMap(20, 20, 5, 3);
            Collisions[7] = new CollisionMap(20, 0, 5, 3);
            Collisions[8] = new CollisionMap(20, -20, 5, 3);

            Camera cam = world.getCamera();
            cam.setPosition(-35f, 0, -35f);

            SimpleVector sv = new SimpleVector();
            sv.set(object[0].getTransformedCenter());

            object[0].translate(fixTrans(0f, -8.5f, 0f));

            object[1].translate(fixTrans(-20f, -6f, -20f));
            object[2].translate(fixTrans(-20f, -6f, 0f));
            object[3].translate(fixTrans(-20f, -6f, 20f));
            object[4].translate(fixTrans(0f, -6f, -20f));
            object[5].translate(fixTrans(0f, -6f, 0f));
            object[6].translate(fixTrans(0f, -6f, 20f));
            object[7].translate(fixTrans(20f, -6f, -20f));
            object[8].translate(fixTrans(20f, -6f, 0f));
            object[9].translate(fixTrans(20f, -6f, 20f));

            object[10].translate(fixTrans(-20f, -5.5f, -15.5f));
            object[11].translate(fixTrans(-20f, -5.5f, 4.5f));
            object[12].translate(fixTrans(-20f, -5.5f, 24.5f));
            object[12].rotateY(30 * DEG_TO_RAD);
            object[13].translate(fixTrans(0f, -5.5f, -15.5f));
            object[14].translate(fixTrans(0f, -5.5f, 4.5f));
            object[15].translate(fixTrans(0f, -5.5f, 24.5f));
            object[16].translate(fixTrans(20f, -5.5f, -15.5f));
            object[17].translate(fixTrans(20f, -5.5f, 4.5f));
            object[18].translate(fixTrans(20f, -5.5f, 24.5f));

            object[19].translate(fixTrans(-20f, -1.5f, -20f));
            object[20].translate(fixTrans(-20f, -1.5f, 0f));
            object[21].translate(fixTrans(-20f, -1.5f, 20f));
            object[22].translate(fixTrans(0f, -1.5f, -20f));
            object[23].translate(fixTrans(0f, -1.5f, 0f));
            object[24].translate(fixTrans(0f, -1.5f, 20f));
            object[25].translate(fixTrans(20f, -1.5f, -20f));
            object[26].translate(fixTrans(20f, -1.5f, 0f));
            object[27].translate(fixTrans(20f, -1.5f, 20f));

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

    public Light GetSun() {
        return sun;
    }

    public SimpleVector test(){
        return object[0].getTransformedCenter();
    }

    public void Destroy() {
        master = null;
        world.dispose();
        TextureManager.getInstance().flush();
    }

    private void ObjectLoader(Context context, int i, int texID, String name) {
        InputStream is;

        try {
            is = context.getResources().getAssets().open("objects/" + name + ".obj");
            aObjects = Loader.loadOBJ(is, null, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        object[i] = aObjects[0];
        object[i].setTexture(TextureManager.getInstance().getNameByTexture(aTextures[texID]));
        object[i].build();
        world.addObject(object[i]);

        //Due to jPCT world axis, rotate around X-axis to draw right-side up
        object[i].setRotationPivot(SimpleVector.ORIGIN);
        object[i].rotateX(180 * DEG_TO_RAD);
        object[i].rotateMesh();
        object[i].clearRotation();
    }

    private int CollisionsX(float xPos, int i) {
        if (Collisions[i].CheckX(xPos))
        {
            return i;
        }
        return -1;
    }

    private int CollisionsY(float yPos, int i) {
        if (Collisions[i].CheckY(yPos))
        {
            return i;
        }
        return -1;
    }

    public int Collisions(float xPos, float yPos) {
        int output = 0;
        for (int i = 0; i < Collisions.length; i++) {
            int test = CollisionsX(xPos, i);
            if (test != -1) {
                test = CollisionsY(yPos, test);
                if (test != -1) {
                    output = 1;                     //Collision in X and Y
                    return output;
                }
            }
        }

        return output;
    }

    public boolean CollisionsWallX(float xPos) {
        if (xPos > xWall || xPos < -xWall) {
            return true;
        }
        return false;
    }

    public boolean CollisionsWallY(float yPos) {
        if (yPos > yWall || yPos < -yWall) {
            return true;
        }
        return false;
    }

    public boolean CollisionsWall(float xPos, float yPos) {
        if (xPos > xWall || xPos < -xWall || yPos > yWall || yPos < -yWall) {
            return true;
        }
        return false;
    }
}
