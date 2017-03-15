package simonchiu.annihilationintelligence.Class;

import android.os.CountDownTimer;

/**
 * Created by Simon on 06/03/2017.
 */

public class TextBuffer {
    private String[] sText = new String[4];                         //An array of the strings to draw, not necessarily in order
    private boolean[] bTextDestroy = {false, false, false, false};  //Whether a text should be destroyed. If new text is overridden, the timer still continues, so this is a fix
    private int[] iOrder = {-1, -1, -1, -1};                        //The order we draw the text. -1 means there isn't text to draw
    private boolean bDelAbso = false;                               //A boolean for if we can destroy the text, used as a fix for the DeleteAll function

    //Add text to the text buffer. If there is no space, it won't add text
    public void AddText(String text, int time) {
        for (int i = 0; i < sText.length; i++) {
            if (sText[i] == null) {
                sText[i] = text;
                AddOrder(i);
                final int j = i;
                new CountDownTimer(time, 1000) {
                    public void onTick(long millisUntilFinished) {
                        bTextDestroy[j] = true;
                    }

                    public void onFinish() {
                        if (bTextDestroy[j] && !bDelAbso) {
                            bTextDestroy[j] = false;
                            DeleteText(j);
                        }
                    }
                }.start();
                break;
            }
        }
    }

    //Returns all the text to the surface view
    public String[] GetText() {
        return sText;
    }

    //Delete the text in a specific index
    private void DeleteText(int index) {
        sText[index] = null;
        DelOrder(index);
    }

    //Delete all the text saved
    public void DeleteAll() {
        sText = new String[4];
        bDelAbso = true;
        for (int i = 0; i < sText.length; i++) {
            bTextDestroy[i] = false;
            iOrder[i] = -1;
        }

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                bDelAbso = false;
            }
        }.start();
    }

    //Deletes all the text saved, and passes a time until we can delete text again
    public void DeleteAll(int time) {
        sText = new String[4];
        bDelAbso = true;
        for (int i = 0; i < sText.length; i++) {
            bTextDestroy[i] = false;
            iOrder[i] = -1;
        }

        new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                bDelAbso = false;
            }
        }.start();
    }

    //Adds a new text to the order we draw the texts
    private void AddOrder(int index) {
        for (int i = 0; i < iOrder.length; i++) {
            if (iOrder[i] == -1) {
                iOrder[i] = index;
                break;
            }
        }
    }

    //Delete a text from the order of texts to draw
    private void DelOrder(int index) {
        for (int i = 0; i < iOrder.length; i++) {
            if (iOrder[i] == index) {
                iOrder[i] = -1;
                break;
            }
        }
    }

    //Return the order to draw the texts
    public int[] GetOrder() {
        return iOrder;
    }

}
