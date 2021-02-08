package com.sim.game_tp1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Obstacle  {

    Bitmap bitmap;
    Rect rect;
    public int x=0,y=0;

    public Obstacle(Resources  res, int id,Bitmap container) {
        bitmap=BitmapFactory.decodeResource(res,id);

        //bitmap=Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),false);

        this.y = container.getHeight() - bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }


    // Rest pour l'utiliser pour la collision et la mise en cadre
    public Rect getRect() {
        Rect rect=new Rect(x,y, x+bitmap.getWidth(),y+bitmap.getHeight());
        return rect;
    }


}
