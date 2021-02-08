package com.sim.game_tp1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Surprise {

    Bitmap bitmap;
    Rect rect;
    Random random;
    public int x = 0, y = 0;
    int duration = 10; // la durrée de la surprise, qui sera déterminée aléatoirement
    int delai = 10; // le delai à attendre pour afficher la surprise, qui sera aussi déterminée aléatoirement
    public int delaiCounter=0; // counter pour le delai
    public int durationCounter=0; // counter pour la duration
    public boolean isDisplayed = false;

    public Surprise(Resources res, int id, Bitmap container) {
        random = new Random();
        bitmap = BitmapFactory.decodeResource(res, id);

        initSurprise(container);
    }

    //init x, y, duration et delai de bitmap de la surprise
    public void initSurprise(Bitmap container){

        this.y = 100;

        //initiale x
        this.x =  container.getWidth()/2 + random.nextInt(400-100)+100;

        // duration de l'appaition de la surprise est aléatoire en 10 et 30 unité de temps
        this.duration = random.nextInt(200 - 100) + 100;

        // le delai à attendre avant l'apparition de la surprise
        this.delai = random.nextInt(40 - 10) + 10;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    // Rest pour l'utiliser pour la collision
    public Rect getRect() {
        Rect rect = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
        return rect;
    }


}
