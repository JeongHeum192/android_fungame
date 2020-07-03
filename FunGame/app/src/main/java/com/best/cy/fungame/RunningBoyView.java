package com.best.cy.fungame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.core.view.MotionEventCompat;

import java.util.Random;

//-----------------------------
// GameView
//-----------------------------
public class RunningBoyView extends View {

    Bitmap backGround;
    Bitmap mountain;

    static Context context;
    private MyThread mThread;
    static int Width, Height;

    Bitmap ball;
    int ball_x, ball_y;
    int bWidth, bHeight;


    static public RunningBoy runningBoy;

    Bitmap schoolBus;
    int schoolBus_x, schoolBus_y;

    // 버튼
    MyButton btnLeft;
    MyButton btnRight;

    Bitmap btnStart;

    int btnStart_x;
    int btnStart_y;
    int btnStart_width;
    int btnStart_height;

    Bitmap timeImage;
    int timeImage_w;

    Bitmap exit;  //게임 안에서 나가기
    Bitmap help;
    int help_x, help_y;
    int button_width;

    int exit_x, exit_y;
    int exit_width;
    int exit_height;

    int gameTime;

    //0 이면 게임 시작화면, 1 이면 게임 시작전 화면,  2 이면 게임 결과 화면
    int whichScreen = 1;
    int basicUnit;
    int basicUnit2;  //기기 세로 크기 Height 을 기준으로 단위크기 만들기

    int scoreBasic = 0;

    int player1Score;

    Paint paint = new Paint();
    Paint paintQuestion = new Paint();
    Paint paintB = new Paint();
    Paint paintBS = new Paint();

    Paint paintTitle = new Paint();

    Paint p1;
    Paint pAlpha;
    Paint paintRed = new Paint(); //시간표시에 사용

    public RunningBoyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Display display = ((WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        Point p11 = new Point();

        display.getSize(p11);
        Height = p11.y;
        Width = p11.x;

        gameTime = Width;
        scoreBasic = Width / 14;   //점수 나오는 시간 조정

        Bitmap imgLeft = BitmapFactory.decodeResource(getResources(), R.drawable.btnleft);
        imgLeft = Bitmap.createScaledBitmap(imgLeft, Width / 11, Width / 11, true);

        Bitmap imgRight = BitmapFactory.decodeResource(getResources(), R.drawable.btnright);

        //공
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        ball = Bitmap.createScaledBitmap(ball, Width / 9, Width / 9, true);

        bWidth = ball.getWidth() / 2;
        bHeight = bWidth;
        ball_x = 100;
        ball_y = -200;

        //배경 - 버스
        schoolBus = BitmapFactory.decodeResource(getResources(), R.drawable.minibus);
        schoolBus = Bitmap.createScaledBitmap(schoolBus, Width / 4, Width / 9, true);
        schoolBus_x = Width * 2 / 3;
        schoolBus_y = Height / 2;

        //배경 그림
        backGround = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        backGround = Bitmap.createScaledBitmap(backGround, Width, Height, true);

        //화면배치등에 사용된 버튼크기의 기본값 설정
        basicUnit = imgLeft.getWidth();
        basicUnit2 = Height / 10;

        btnLeft = new MyButton(imgLeft, 0, Height - basicUnit - basicUnit / 2);
        btnRight = new MyButton(imgRight, Width - btnLeft.w, Height - basicUnit - basicUnit / 2);

        //게임 중 나가기 버튼
        exit = BitmapFactory.decodeResource(context.getResources(), R.drawable.exit);
        exit = Bitmap.createScaledBitmap(exit, Width / 7, Height / 9, true);
        exit_x = Width * 3 / 5;
        exit_y = basicUnit / 4;
        exit_width = exit.getWidth();
        exit_height = exit.getHeight();

        help = BitmapFactory.decodeResource(context.getResources(), R.drawable.help);
        help = Bitmap.createScaledBitmap(help, Width / 9, Width / 9, true);
        help_x = Width - help.getWidth() - basicUnit / 6;
        help_y = basicUnit / 4 + help.getWidth() + basicUnit / 8;

        button_width = help.getHeight();

        btnStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.btnstart);
        btnStart = Bitmap.createScaledBitmap(btnStart, Width / 4, Width / 13, true);
        btnStart_width = btnStart.getWidth();
        btnStart_height = btnStart.getHeight();

        btnStart_x = Width / 2 - btnStart_width / 2;
        btnStart_y = Height * 3 / 4;

        mountain = BitmapFactory.decodeResource(context.getResources(), R.drawable.mountain);
        mountain = Bitmap.createScaledBitmap(mountain, Width, Height / 2, true);

        timeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.timeimage);
        timeImage = Bitmap.createScaledBitmap(timeImage, Width / 7, basicUnit / 2, true);
        timeImage_w = timeImage.getWidth();

        runningBoy = new RunningBoy(context, Height);

        if (mThread == null) {
            mThread = new MyThread();
            mThread.start();
        }

        p1 = new Paint();
        p1.setColor(Color.WHITE); //하얀색으로 전체화면 채우기 용 paint

        paintRed.setColor(Color.RED);
        paintRed.setTextSize(30);

        pAlpha = new Paint();
        pAlpha.setAlpha(85);
    }

    void moveBall() {

        ball_y += Height / 70;
        if (ball_y > Height + 100) makeBall();

    }

    void makeBall() {

        Random r1 = new Random();
        int imsy = r1.nextInt(Width - Width / 8);
        ball_x = imsy;

        imsy = r1.nextInt(500);
        ball_y = -imsy;
    }


    void moveBus() {

        schoolBus_x -= RunningBoyView.Width / RunningBoyView.Height * 2;

        if (schoolBus_x <= -100) {
            Random a = new Random();
            int b = a.nextInt(RunningBoyView.Width / 2) + RunningBoyView.Width / 10;
            schoolBus_x = RunningBoyView.Width + b;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mThread.operation = false;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        moveBall();

        if (isCollision(runningBoy.x + runningBoy.w, runningBoy.y + runningBoy.h, runningBoy.w,
                runningBoy.h, ball_x + bWidth, ball_y + bWidth, bWidth, bWidth)) {
            player1Score += 10;
            moveBall();
        }

        if (whichScreen == 0) {
            canvas.drawBitmap(backGround, 0, 0, null);
            canvas.drawBitmap(mountain, 0, Height / 5, null);
            canvas.drawBitmap(schoolBus, schoolBus_x, schoolBus_y, null);
            canvas.drawBitmap(ball, ball_x, ball_y, null);

        }

        //처음화면 보여주기
        if (whichScreen == 1) {
            canvas.drawBitmap(backGround, 0, 0, null);
            canvas.drawText("Running Man Game", Width / 4, basicUnit, paintTitle);
            canvas.drawText("주인공을 화살표버튼을 터치해서 ", Width / 10 + Width / 25, Height / 2 - Height / 30, paintBS);
            canvas.drawText("움직입니다.", Width / 10 + Width / 25, Height / 2 + basicUnit / 3 + Height / 30, paintBS);
            canvas.drawBitmap(btnStart, btnStart_x, btnStart_y, null);
        }

        if (whichScreen == 0) {

            canvas.drawBitmap(runningBoy.image, runningBoy.x, runningBoy.y, null);
            canvas.drawBitmap(exit, exit_x, exit_y, pAlpha);
            canvas.drawBitmap(btnLeft.img, btnLeft.x, btnLeft.y, null);
            canvas.drawBitmap(btnRight.img, btnRight.x, btnRight.y, null);

            //시간 표시
            gameTime -= 1;
            canvas.drawBitmap(timeImage, basicUnit / 4, Height * 1 / 40, null);
            canvas.drawRect(basicUnit / 4 + timeImage_w + basicUnit / 10, Height * 1 / 40 + basicUnit / 10, basicUnit / 4 + timeImage_w + basicUnit / 10 + gameTime / 8, Height * 1 / 40 + basicUnit / 3 + basicUnit / 12, paintRed);

            if (gameTime / 4 <= 0) {
                whichScreen = 2;
            }
            canvas.drawText("점수: " + player1Score + "", 10, Height / 3, paintB);

        }          // 게임 진행중 화면 끝

        if (whichScreen == 2) {
            canvas.drawText("점수: " + player1Score + "", 10, Height / 2, paint);
            canvas.drawBitmap(btnStart, btnStart_x, btnStart_y, null);

        }

    }    //end of onDraw


    boolean isCollision(int boy_x, int boy_y, int mWidth, int mHeight, int ball_x, int ball_y,
                        int bWidth, int bHeight) {

        if ((mWidth + bWidth) > Math.abs(boy_x - ball_x) && (mHeight + bHeight) > Math.abs(boy_y - ball_y)) {
            makeBall();
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isTouch = false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isTouch = true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            isTouch = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP ) {
            isTouch = false;
        }

        // 터치 index, id
        int pIndex = MotionEventCompat.getActionIndex(event);
        int id = MotionEventCompat.getPointerId(event, pIndex);

        float x = (int) event.getX();
        float y = (int) event.getY();

        btnLeft.action(x, y, id, isTouch);
        btnRight.action(x, y, id, isTouch);

        if (whichScreen == 1 || whichScreen == 2) {
            if (x > btnStart_x && x < (btnStart_x + btnStart_width) && y > btnStart_y && y < (btnStart_y + btnStart_height)) {
                whichScreen = 0;
                gameTime = Width;
                player1Score = 0;
            }
        }

        if (whichScreen == 0) {
            if (x > exit_x && x < (exit_x + exit_width) && y > exit_y && y < (exit_y + exit_height)) {
                whichScreen = 1;
            }
        }

        return true;
    }

    class MyThread extends Thread {
        public boolean operation = true;

        MyThread() {

            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.create("", Typeface.BOLD));
            paint.setTextSize(Height / 6);

            paintQuestion.setColor(Color.BLUE);
            paintQuestion.setAntiAlias(true);
            paintQuestion.setTypeface(Typeface.create("", Typeface.BOLD));
            paintQuestion.setTextSize(Width / 20);

            paintB.setColor(Color.BLACK);
            paintB.setAntiAlias(true);
            paintB.setTypeface(Typeface.create("", Typeface.BOLD));
            paintB.setTextSize(Width / 22);

            paintBS.setColor(Color.BLACK);
            paintBS.setAntiAlias(true);
            paintBS.setTypeface(Typeface.create("", Typeface.BOLD));
            paintBS.setTextSize(Width / 26);

            paintTitle.setColor(Color.BLACK);
            paintTitle.setAntiAlias(true);
            paintTitle.setTypeface(Typeface.create("", Typeface.BOLD));
            paintTitle.setTextSize(Width / 18);

        }

        @Override
        public void run() {
            while (operation) {
                try {
                    runningBoy.moveBoy(btnLeft.isTouch, btnRight.isTouch);
                    runningBoy.moveBoy();
                    //화면 그리기
                    moveBus();
                    postInvalidate();
                    sleep(7);
                } catch (Exception e) {

                }
            }
        }
    } // Thread

}  //End of View