package com.shipsurvivors.Entities.HeartContainer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Constantes;

/**
 * Created by SEO on 25/01/2018.
 */
public class HeartContainer extends Actor {
    private Heart[] hearts;
    private int heartCounter;
    private boolean pieceRemoved;
    //This will be the time of immunity after a rick has hit the player
    private final float TILT_TIME = 5;
    private float chronometer = 0;


    public HeartContainer(TextureAtlas heartAtlas,float x,float y){
        //Initialize the hearths
        hearts = new Heart[Constantes.NUMBER_OF_HEARTS];
        for(int i = 0;i<Constantes.NUMBER_OF_HEARTS;i++){
            hearts[i] = new Heart(heartAtlas,x+i*Constantes.HEART_WIDTH,y,Constantes.HEART_WIDTH,Constantes.HEART_HEIGHT);
        }
        heartCounter = Constantes.NUMBER_OF_HEARTS-1;

        setPieceRemoved(false);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(int i = 0;i<hearts.length;i++){
            hearts[i].draw(batch,parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        if(isPieceRemoved()){
            chronometer+=delta;
            if(chronometer>TILT_TIME){
                setPieceRemoved(false);
                chronometer = 0;
            }
        }
    }

    public void dispose(){
        for (Heart heart:hearts){
            heart.dispose();
        }
    }
    public void removePiece(){
        if(!heDead() && !isPieceRemoved()) {
            hearts[heartCounter].eatPiece();
            if (!hearts[heartCounter].isActive()) {
                heartCounter -= 1;
            }
            setPieceRemoved(true);
            System.out.println(heartCounter);
        }
    }

    public boolean heDead(){
        return heartCounter<0;
    }

    public void setPieceRemoved(boolean pieceRemoved) {
        this.pieceRemoved = pieceRemoved;
    }

    public boolean isPieceRemoved() {
        return pieceRemoved;
    }
}
