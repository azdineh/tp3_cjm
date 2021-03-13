package com.sim.game_tp3;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Obstacle  {

    //Le rectangle correspondant à l'obstacle
    Rect rect;

    //si ce ce flag est true, obstacle devient un point à gagner
    //lors de la collision avec Mario, le score s'augmente de 5pt
    public Boolean isToWin=false;

    //les coordonnées de l'obstacle dans canvas
    public int top=0,left=0, right=0,bottom=0;

    //coordonnées de l'obstacle dans tileMap
    public int i=0,j=0;

    public Obstacle() {

    }

    // Rect pour l'utiliser pour la collision et la mise en cadre
    public Rect getRect() {
        rect=new Rect(left,top,right,bottom);
        return rect;
    }

}
