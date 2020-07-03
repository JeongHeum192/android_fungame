package com.best.cy.fungame;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class MyButton {

    public Bitmap img;
    public int x, y;
    int w, h;

    private int myId = -1;
    private RectF rect;

    public boolean isTouch;
    public MyButton(Bitmap bitmap, int x, int y) {
        //버튼의 크기 , 비트맵
        this.x = x;
        this.y = y;
        bitmap = Bitmap.createScaledBitmap(bitmap, RunningBoyView.Width / 9, RunningBoyView.Width / 9 ,true);

        w = bitmap.getWidth();
        h = bitmap.getHeight();
        img = bitmap;

        rect = new RectF(x, y, x + w, y + h);

        this.x = x;
        this.y = y;
    }

    public void action(float x, float y, int id, boolean isDown) {
        if (isDown && rect.contains(x, y)) {
            isTouch = true;
            myId = id;
        }

        if(!isDown && id == myId) {
            isTouch = false;
        }
    }
}
