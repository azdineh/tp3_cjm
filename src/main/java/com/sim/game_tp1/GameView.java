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
    Jump jump;
    Obstacle obstacle1, obstacle2, obstacle3, obstacle4;
    Surprise surprise1;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        this.screenX = screenX;
        this.screenY = screenY;

        this.screenRatioX = 1920f / screenX;
        this.screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        jump = new Jump(screenY, getResources());
        obstacle1 = new Obstacle(getResources(), R.drawable.obstacle1, background1.background);
        obstacle2 = new Obstacle(getResources(), R.drawable.obstacle2, background1.background);
        obstacle3 = new Obstacle(getResources(), R.drawable.obstacle1, background1.background);
        obstacle4 = new Obstacle(getResources(), R.drawable.obstacle2, background1.background);

        surprise1=new Surprise(getResources(), R.drawable.surprise1, background1.background);


        background2.x = screenX;

        paint = new Paint();
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
        background1.x -= 10 ;
        background2.x -= 10 ;

        // faire bouger les obstacles
        obstacle1.x = background1.x + 600;
        obstacle2.x = obstacle1.x + obstacle1.bitmap.getWidth() + 640;
        obstacle3.x = background2.x + 700;
        obstacle4.x = obstacle3.x + obstacle3.bitmap.getWidth() + 500;



        if(surprise1.isDisplayed==true){
            // faire bouger le bitmap de la surprise
            surprise1.x-=10;
        }


        if(surprise1.isDisplayed==false){
            if(surprise1.delaiCounter==surprise1.delai){
                surprise1.isDisplayed=true;
                surprise1.delaiCounter=0;
            }
            else
                surprise1.delaiCounter++;
        }


        if(surprise1.isDisplayed==true){
            if(surprise1.durationCounter==surprise1.duration){
                surprise1.isDisplayed=false;
                surprise1.initSurprise(background1.background);
                surprise1.durationCounter=0;
            }
            else
                surprise1.durationCounter++;
        }



        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        if (jump.isGoingUp) {
            if (jump.y > screenY / 2 - 50) {
                jump.y -= 300;
            }
            cnt++;
            if (cnt == 14) jump.isGoingUp = false;

        } else {
            jump.y += 30;
            cnt = 0;

        }

        if (jump.y < 0) {
            jump.y = 0;
        }

        if (jump.y > screenY - jump.height) {
            jump.y = screenY - jump.height;
        }

    }


    public void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawBitmap(jump.getJump(), jump.x, jump.y, paint);

            canvas.drawBitmap(obstacle1.getBitmap(), obstacle1.x, obstacle1.y, paint);
            canvas.drawBitmap(obstacle2.getBitmap(), obstacle2.x, obstacle2.y, paint);
            canvas.drawBitmap(obstacle3.getBitmap(), obstacle3.x, obstacle3.y, paint);
            canvas.drawBitmap(obstacle4.getBitmap(), obstacle4.x, obstacle4.y, paint);

            if(surprise1.isDisplayed==true)
                canvas.drawBitmap(surprise1.getBitmap(), surprise1.x, surprise1.y, paint);




            canvas.drawRect(obstacle1.getRect(), paint);
            canvas.drawRect(obstacle2.getRect(), paint);
            canvas.drawRect(obstacle3.getRect(), paint);
            canvas.drawRect(obstacle4.getRect(), paint);




/*            float x1=228*screenX/600, x2=293*screenX/600;
            float y1=215*screenY/303, y2=303*screenY/303;

            Rect rect11=new Rect(background1.x+(int)+x1,background1.y+(int)y1, background1.x+(int)x2, background1.y+(int)y2);
            canvas.drawRect(rect11,paint);

            Rect rect21=new Rect(background2.x+(int)+x1,background2.y+(int)y1, background2.x+(int)x2, background2.y+(int)y2);
            canvas.drawRect(rect21,paint);

            float x3=464*screenX/600, x4=534*screenX/600;
            float y3=235*screenY/303, y4=303*screenY/303;

            Rect rect12=new Rect(background1.x+(int)+x3,background1.y+(int)y3, background1.x+(int)x4, background1.y+(int)y4);
            canvas.drawRect(rect12,paint);

            Rect rect22=new Rect(background2.x+(int)+x3,background2.y+(int)y3, background2.x+(int)x4, background2.y+(int)y4);
            canvas.drawRect(rect22,paint);*/


            Rect rect_sprit = new Rect(jump.x, jump.y, jump.x + jump.getJump().getWidth(), jump.y + jump.getJump().getHeight());
            canvas.drawRect(rect_sprit, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            paint.setColor(Color.RED);


            Bitmap collision = BitmapFactory.decodeResource(getResources(), R.drawable.collision);

            if (rect_sprit.intersect(obstacle1.getRect())
                    || rect_sprit.intersect(obstacle2.getRect())
                    || rect_sprit.intersect(obstacle3.getRect())
                    || rect_sprit.intersect(obstacle4.getRect()))
                canvas.drawBitmap(collision,screenX/2 - collision.getWidth()/2,50,paint);

 /*           if(rect_sprit.intersect(rect11) )
                canvas.drawBitmap(collision,screenX/2 - collision.getWidth()/2,50,paint);

            if(rect_sprit.intersect(rect12) )
                canvas.drawBitmap(collision,screenX/2 - collision.getWidth()/2,50,paint);
            if(rect_sprit.intersect(rect21) )
                canvas.drawBitmap(collision,screenX/2 - collision.getWidth()/2,50,paint);
            if(rect_sprit.intersect(rect22) )
                canvas.drawBitmap(collision,screenX/2 - collision.getWidth()/2,50,paint);
*/


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
                if (event.getX() < screenX / 2) {
                    jump.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                jump.isGoingUp = false;
                break;
        }
        return true;
    }
}
