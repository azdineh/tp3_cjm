package com.sim.game_tp3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.sim.game_tp1.R;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlay;
    private int screenX, screenY;
    private Paint paint,colispaint,scorepaint;


    // refX et refY: deux références pour déplacer finalBitmap dans le canvas
    int refX = 0, refY= 0;

    Bitmap[] tuiles;
    int[][] tilMap;

    // la classe Control représente les contrôle graphique, qui permettent de déplacer et faire sauter Mario.
    Control rightControl,leftControl,jumbControl;

    // la classe du Mario
    Mario mario;

    // contour pour animer Mario
    int cnt = 0;

    //step représente la vitesse de déplacement de finalBitmap dans le canvas
    int step=30;

    // le score
    private int score=0;




    // pour enlever toute confusion, j'ai utilisé deux variables pour bonne prise de dimensions :
    // tileH = tileW = hauteur de tileset
    int tileH, tileW;

    Bitmap finalBitmap;

    //X et Y de finalBitmap
    int finalBitmapX=0, finalBitmapY=0;

    Canvas canvas;

    //Des indicateurs booléens pour différencier entre les 3 états de Mario : Stop, marche, saut
    private boolean isMovingRight=false;
    private boolean isMovingLeft=false;

    //Pour gérer les collisions, on les regroupe dans un seul tableau.
    ArrayList<Obstacle> obstacles=new ArrayList<>();

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        this.screenX = screenX;
        this.screenY = screenY;

        //paint pour la mise en cadre des objets (pour simplifier les tests)
        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.RED);
        paint.setTextSize(50);

        colispaint=new Paint();
        colispaint.setTextSize(100);
        colispaint.setStyle(Paint.Style.FILL);
        colispaint.setColor(Color.BLACK);

        scorepaint=new Paint();
        scorepaint.setTextSize(60);
        scorepaint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(8);
        scorepaint.setColor(Color.YELLOW);

        Bitmap tileset = BitmapFactory.decodeResource(getResources(), R.drawable.tilest);
        tileH = tileset.getHeight();
        tileW = tileH;

        //initislisation des boutons de contrôles
        rightControl=new Control(getResources(),R.drawable.rightcontrol);
        leftControl=new Control(getResources(),R.drawable.leftcontrol);
        jumbControl=new Control(getResources(),R.drawable.jumpcontrol);

        //initialisation de Bitmap du Mario
        mario=new Mario(getResources());


        finalBitmap = Bitmap.createBitmap(tileW * 32, tileH * 13, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(finalBitmap);


        //tilMap=new int[13][32];
        tilMap = new int[][]{
          {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 10, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 3, 2, 3, 0, 0, 4, 5, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };


        //création des 11 tuiles
        tuiles=new Bitmap[11];
        for(int i=0;i<11;i++){
            tuiles[i] = Bitmap.createBitmap(tileset, tileW * i, 0, tileW, tileH);
        }

        //parcours de tileMap et dessin des tuiles dans les coordonnées exactes
        dessinerFinalbitmap();



    }

    public void dessinerFinalbitmap(){
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 32; j++) {
                canvas.drawBitmap(tuiles[tilMap[i][j]], tileW * j, tileH * i, null);

                // en créant des obstacles, qui sont les tuiles, à l'expection de tuile 0 (représente le vide),
                // et le tuile 1 qui représente le sol.
                // la classe Obstacle est basé sur l'objet Rect.
                Obstacle ob=new Obstacle();
                if(!(tilMap[i][j]==0 || tilMap[i][j]==1)){
                    ob.top=i*tileH;
                    ob.left=j*tileW;
                    ob.bottom=ob.top+tileH;
                    ob.right=ob.left+tileW;

                    //si le tuile est de numéro 2 (tuile jaune)
                    //on rend l'obstacle comme un point à gagner, en metant isToWin true;
                    if(tilMap[i][j]==2){
                        ob.isToWin=true;
                    }

                    //mémorise les coordonnées i et j pour une utilisation ulltérieur
                    ob.i=i;
                    ob.j=j;
                    obstacles.add(ob);
                }


            }
        }
    }

    @Override
    public void run() {

        while (isPlay) {
            update();
            draw();
            sleep();
        }
    }



    public void update() {
        cnt+=2;

        if(isMovingRight){
            if((finalBitmapX+finalBitmap.getWidth()>screenX+30)){
                refX--;
            }else{
                if(mario.x<screenX-250)
                mario.x=mario.x+step;
            }
            mario.changeStatStopAndMarch();
        }


        if(isMovingLeft){
            if((finalBitmapX<0)){
                refX++;
            }else{
                if(mario.x>100)
                    mario.x=mario.x-step;
            }
            mario.changeStatStopAndMarch();
        }



    }


    public void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            //initilise refY en vue pour affichier le partie basse de finalbitmap
            refY=-(finalBitmap.getHeight()-screenY);

            //finalBitmapX et finalBitmapY sont respectivement les X et Y de finalbitmap dans le canvas
            finalBitmapX=step*refX;
            finalBitmapY=refY;
            canvas.drawBitmap(finalBitmap,finalBitmapX,finalBitmapY,null);
            canvas.drawText("Score : " +score ,screenX-380,150,scorepaint);


            //draw les rectangle des obstacles (pour le test)
            for(int i=0;i<obstacles.size();i++){
                Obstacle b= obstacles.get(i);
                Obstacle ob=new Obstacle();

                //adjuster les coordonnées des obstacles(rectangles) par le même décalge de finalbitmap
                ob.top=b.top+refY;
                ob.bottom=b.bottom+refY;
                ob.right=b.right+finalBitmapX;
                ob.left=b.left+finalBitmapX;


                canvas.drawRect(ob.getRect(),paint);

                //si il y a une intersection entre Mario et obstacles, afficher "Collision"
                if(ob.getRect().intersect(mario.getRect()) && b.isToWin==false){
                    canvas.drawText("Collision",(screenX/2)-80,150,colispaint);

                    //ici, je vais ajouter le fonctionnement de game over,
                    // pour donner à l'utilisateur

                }

                //si il y a une intersection entre Mario et obstacles qui reprsénete un point à gagner, augmenter le score "Collision"
                if(ob.getRect().intersect(mario.getRect()) && b.isToWin==true){
                    score+=5;

                    //supprimer cette onbstacle
                    obstacles.remove(i);

                    //on fait disparaitre tuile2, et le remplace par tuile0
                    tilMap[b.i][b.j]=0;

                    //redessiner finalbitmap
                    dessinerFinalbitmap();

                }

            }

            //pour que Bitmap de Mario ne traverse pas le sol, on crée un retangle qui englobe les tuiles num 1
            Rect floor=new Rect();
            floor.set(finalBitmapX,finalBitmapY+(11*tileH),finalBitmapX+finalBitmap.getWidth(),finalBitmapY+finalBitmap.getHeight());

            //si il y a un contact entre mario et le sol, on s'arrête le mouvement vertical de mArio
            if(!mario.getRect().intersect(floor)){
                mario.y+=cnt;
            }

            //On dessine Mario dans canvas
            canvas.drawBitmap(mario.currentState,mario.x,mario.y,paint);

            //On dessine le rectangle correspondat au Mario (pour le test)
            canvas.drawRect(mario.getRect(),paint);

            //Positionnement des bouton de control
            int marge=40;
            rightControl.x=screenX - (rightControl.bitmap.getWidth()+marge);
            rightControl.y=screenY - (rightControl.bitmap.getHeight()+marge);

            leftControl.x=screenX - (leftControl.bitmap.getWidth()*2+marge);
            leftControl.y=screenY - (leftControl.bitmap.getHeight()+marge);

            jumbControl.x= marge;
            jumbControl.y=screenY - (jumbControl.bitmap.getHeight()+marge);

            //dessin de boutons de contrôls
            canvas.drawBitmap(rightControl.bitmap, rightControl.x,rightControl.y,null);
            canvas.drawBitmap(leftControl.bitmap, leftControl.x,leftControl.y,null);
            canvas.drawBitmap(jumbControl.bitmap, jumbControl.x,jumbControl.y,null);



            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void sleep() {
        try {
            Thread.sleep(80);
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

                int xevent= Math.round(event.getX());
                int yevent= Math.round(event.getY());

                //si, on clique sur le bouton droit
                if (rightControl.getRect().contains(xevent,yevent) ) {

                    //on change l'état de Mario à état de marche
                    mario.currentState=mario.mario_marche;
                    //on pivote verticalement le bitap du Mario vers le droit, si ce n'est pas le cas
                    if(!mario.isFlipedToRight){
                        mario.flipMario();
                        mario.isFlipedToRight=true;
                    }

                    isMovingRight=true;
                }

                //si, on clique sur le gauche droit
                if ( leftControl.getRect().contains(xevent,yevent) ) {
                    //on change l'état de Mario à état de marche
                    mario.currentState=mario.mario_marche;
                    //on pivote verticalement le bitmap du Mario vers la gauche, si ce n'est pas le cas
                    if(mario.isFlipedToRight){
                        mario.flipMario();
                        mario.isFlipedToRight=false;
                    }

                    isMovingLeft=true;
                }

                //si, on clique sur le bouton saut
                if(jumbControl.getRect().contains(xevent,yevent)){
                    //on change l'état de Mario à état du saut
                    mario.currentState=mario.mario_jump;
                    //on fait remonter Mario vers le haut
                    mario.y-=600;
                    //initiliser le conteur d'animation de Mario
                    cnt=0;

                }
                break;

            case MotionEvent.ACTION_UP:
                mario.currentState=mario.mario_stop;
                isMovingRight=false;
                isMovingLeft=false;

                break;
        }

        return  true;
    }


}
