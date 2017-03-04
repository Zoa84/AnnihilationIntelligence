package simonchiu.annihilationintelligence;

/**
 * Created by Simon on 02/03/2017.
 */

//An unused Unused class, used simply to hold old data which might be useful later
//Will likely be removed when finished

public class Unused {

    //GameSurfaceView - Old Texture and object loader
    /*Texture texture;

            //For loop loading textures
            int resID;
            for (int i = 0; i < textures.length; i++) {
                resID = context.getResources().getIdentifier(textures[i], "drawable", context.getPackageName());
                texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 512, 512));
                TextureManager.getInstance().addTexture(textures[i], texture);
                aTextures[i] = texture;
            }

            //Load UI textures
            for (int i = 0; i < uint.length; i++) {
                resID = context.getResources().getIdentifier(uint[i], "drawable", context.getPackageName());
                texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(context.getResources().getDrawable(resID)), 256, 256));
                TextureManager.getInstance().addTexture(uint[i], texture);
                uintT[i] = texture;
            }

            //Using an input stream, we get the obj by the objects name
            //and load it to tObjects, which is an array, as the loadobj function returns multiple objects
            //however in our case, it happens to only be one object each time.
            //This is then but into an array called object which contains all the loaded objects together
            //These can be set textures and built
            InputStream is;

            for (int i = 0; i < textures.length; i++) {
                try {
                    is = context.getResources().getAssets().open("objects/" + textures[i] + ".obj");
                    aObjects = Loader.loadOBJ(is, null, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                object[i] = aObjects[0];
                object[i].setTexture(textures[i]);
                object[i].build();
                world.addObject(object[i]);

                //Due to jPCT world axis, rotate around X-axis to draw right-side up
                object[i].setRotationPivot(SimpleVector.ORIGIN);
                object[i].rotateX(180 * DEG_TO_RAD);
                object[i].rotateMesh();
                object[i].clearRotation();
            }
            */
}
