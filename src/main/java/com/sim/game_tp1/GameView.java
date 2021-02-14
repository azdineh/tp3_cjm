package com.sim.game_tp1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlay;
    private Background background1, background2;
    private int screenX, screenY;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    int refX = 0;
    int refY=0;

    Bitmap tuile0, tuile1, tuile2, tuile3, tuile4, tuile5, tuile6, tuile7, tuile8, tuile9, tuile10;

    int tileH, tileW;

    Bitmap finalBitmap;
    Canvas canvas;


    public GameView(Context context, int screenX, int screenY) {
        super(context);

        this.screenX = screenX;
        this.screenY = screenY;

        Bitmap tileset = BitmapFactory.decodeResource(getResources(), R.drawable.tilest);

        tileH = tileset.getHeight();
        tileW = tileH;

        finalBitmap = Bitmap.createBitmap(tileW * 32, tileH * 13, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(finalBitmap);


        tuile0 = Bitmap.createBitmap(tileset, tileW * 0, 0, tileW, tileH);
        tuile1 = Bitmap.createBitmap(tileset, tileW * 1, 0, tileW, tileH);
        tuile2 = Bitmap.createBitmap(tileset, tileW * 2, 0, tileW, tileH);
        tuile3 = Bitmap.createBitmap(tileset, tileW * 3, 0, tileW, tileH);
        tuile4 = Bitmap.createBitmap(tileset, tileW * 4, 0, tileW, tileH);
        tuile5 = Bitmap.createBitmap(tileset, tileW * 5, 0, tileW, tileH);
        tuile6 = Bitmap.createBitmap(tileset, tileW * 6, 0, tileW, tileH);
        tuile7 = Bitmap.createBitmap(tileset, tileW * 7, 0, tileW, tileH);
        tuile8 = Bitmap.createBitmap(tileset, tileW * 8, 0, tileW, tileH);
        tuile9 = Bitmap.createBitmap(tileset, tileW * 9, 0, tileW, tileH);
        tuile10 = Bitmap.createBitmap(tileset, tileW * 10, 0, tileW, tileH);

        //canvas.drawBitmap(tuile0,0,0,null);


    }

    @Override
    public void run() {

        while (isPlay) {
            update();
            draw();
            sleep();
        }
    }

    int cnt = 0;

    public void update() {


    }


    public void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            //canvas.setBitmap(finalBitmap);

            //print all tuile0
            for (int i = 0; i < 32; i++) {
                for (int j = 0; j < 12; j++) {
                    canvas.drawBitmap(tuile0, tileW * i, tileH * j, null);
                }
            }


            //built10
            for (int i = 0; i < 4; i++) {
                canvas.drawBitmap(tuile10, tileW * (7 - refX), tileH * i, null);
            }

            //built10
            for (int i = 0; i < 4; i++) {
                canvas.drawBitmap(tuile10, tileW * (27 - refX), tileH * i, null);
            }

            //tuile2
            canvas.drawBitmap(tuile2, tileW * (24 - refX), tileH * 4, null);


            //tuile1 bottom
            for (int i = 0; i < 32; i++) {
                for (int j = 10; j < 12; j++) {
                    canvas.drawBitmap(tuile1, tileW * i, tileH * j, null);
                }
            }


            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        thread = new Thread(this);
        thread.start(); // call run method of runnable
        isPlay = true;
    }

    public void pause() {
        try {
            thread.join(); // stop the thread
            isPlay = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // touch left of screen
                if (event.getX() < screenX / 2) {
                    refX++;
                }

                // touch right of screen
                if (event.getX() > screenX / 2) {
                    refX--;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
}
