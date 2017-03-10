package simonchiu.annihilationintelligence.Class;

import android.os.CountDownTimer;

/**
 * Created by Simon on 06/03/2017.
 */

public class TextBuffer {
    private int iTextNums = 0;
    private String[] sText = new String[4];

    public void AddText(String text, int time) {
        if (iTextNums <4) {
            sText[iTextNums] = text;
            iTextNums++;

            new CountDownTimer(time, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    DeleteText();
                }
            }.start();
        }
    }

    public String[] GetText() {
        if (iTextNums > 0) {
            String[] returnText = new String[iTextNums];
            for (int i = 0; i < iTextNums; i++) {
                returnText[i] = sText[i];
            }
            return returnText;
        }
        return null;
    }

    private void DeleteText() {
        iTextNums--;
        for (int i = 0; i < iTextNums; i++) {
            sText[i] = sText[i+1];
        }
    }

    public void DeleteAll() {
        iTextNums = 0;
        sText = new String[4];
    }

}
