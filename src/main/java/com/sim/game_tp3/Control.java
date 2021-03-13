package com.sim.game_tp3;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Control {

    //Bitmap du bouton de control
    Bitmap bitmap;

    //Le rectancle correspondant du bouton decontrol
    Rect rect;
    public int x=0,y=0;

    public Control(Resources res, int id) {
        bitmap= BitmapFactory.decodeResource(res,id);
        bitmap=Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,false);
    }


    // Rect pour l'utiliser pour la collision et la mise en cadre
    public Rect getRect() {
        rect=new Rect(x,y, x+bitmap.getWidth(),y+bitmap.getHeight());
        return rect;
    }
}
