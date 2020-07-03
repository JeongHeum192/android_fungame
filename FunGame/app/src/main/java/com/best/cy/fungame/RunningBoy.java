package com.best.cy.fungame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RunningBoy {

    int imageCount = 0;
    int imageCount2 = 0;
    static int direction = 1;  //1 오른쪽으로, 2 왼쪽 방향으로 주인공 움직임

    private float mSpeed;  //스피드
    public int x, y;

    Bitmap imgBoy[] = new Bitmap[16];

    // 현재 이미지
    public Bitmap image;
    public int w, h;
    int imageNum;
    int imageNum2 = 8;

    //-----------------------------
    // 생성자
    //-----------------------------
    public RunningBoy(Context context, int height) {

        this.y = height / 2;
        for (int i = 0; i < 16; i++) {

            imgBoy[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy00 + i);
            int xWidth = RunningBoyView.Width / 9;
            int yWidth = RunningBoyView.Width / 7;

            imgBoy[i] = Bitmap.createScaledBitmap(imgBoy[i], xWidth, yWidth, true);

        }

        w = imgBoy[0].getWidth() / 2;
        h = imgBoy[0].getHeight() / 2;

        image = imgBoy[0];
        init();
    }

    private void init() {
        direction = 1;  //오른쪽 으로 이동
    }

    public void moveBoy() {

        if (direction == 1) x += mSpeed;
        if (direction == 2) x -= mSpeed;
    }


    public void moveBoy(boolean btnLeft, boolean btnRight) {

        if (direction == 1) {
            imageCount++;
            if (imageCount > RunningBoyView.Width / 200) {
                imageNum++;
                imageCount = 0;
            }
            if (imageNum >= 7) imageNum = 0;

        }

        if (direction == 2) {  //왼쪽방향이미지
            imageCount2++;
            if (imageCount2 > RunningBoyView.Width / 200) {
                imageNum2++;
                imageCount2 = 0;
            }
            if (imageNum2 > 15) imageNum2 = 8;

        }

        if (direction == 2)
            image = imgBoy[imageNum2];
        else if (direction == 1) image = imgBoy[imageNum];

        if (!btnLeft && !btnRight ) {
            if (direction == 1) {
                image = imgBoy[2];

            } else image = imgBoy[9];
        }

        if (btnLeft) {
            direction = 2;

            x -= RunningBoyView.Width / 300;
        }

        if (btnRight) {
            direction = 1;
            x += RunningBoyView.Width / 300;

        }
    }

}
