package com.sim.game_tp1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.sim.game_tp1.GameView.screenRatioX;
import static com.sim.game_tp1.GameView.screenRatioY;

public class Jump {

    public boolean isGoingUp =false;
    int x,y,width,height, wingcounter=0;
    Bitmap jump1, jump2;

    Jump(int screenY, Resources res){
        jump1= BitmapFactory.decodeResource(res, R.drawable.jump1);
        jump2= BitmapFactory.decodeResource(res, R.drawable.jump2);

        width=jump1.getWidth();
        height=jump1.getHeight();

        jump1=Bitmap.createScaledBitmap(jump1,width,height,false);
        jump2=Bitmap.createScaledBitmap(jump2,width,height,false);

       y=screenY;
       x=70;

    }

    Bitmap getJump(){
        if(wingcounter==0){
            wingcounter++;
            return jump1;
        }
        wingcounter--;
        return jump2;
    }
}
