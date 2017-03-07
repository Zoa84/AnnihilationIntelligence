package simonchiu.annihilationintelligence.Class;

import android.os.CountDownTimer;

/**
 * Created by Simon on 06/03/2017.
 */

public class TextBuffer {
    int iTextNums = 0;
    String[] sText = new String[4];

    public TextBuffer() {

    }

    public void AddText(String text, int time) {
        if (iTextNums <4) {
            sText[iTextNums] = text;
            iTextNums++;

            new CountDownTimer(time, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    iTextNums--;
                    for (int i = 0; i < iTextNums; i++) {
                        sText[i] = sText[i+1];
                    }
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

}
