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


    public HeartContainer(TextureAtlas heartAtlas,float x,float y){
        //Initialize the hearths
        hearts = new Heart[Constantes.NUMBER_OF_HEARTS];
        for(int i = 0;i<Constantes.NUMBER_OF_HEARTS;i++){
            hearts[i] = new Heart(heartAtlas,x+i*Constantes.HEART_WIDTH,y,Constantes.HEART_WIDTH,Constantes.HEART_HEIGHT);
        }
        heartCounter = Constantes.NUMBER_OF_HEARTS-1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(int i = 0;i<hearts.length;i++){
            hearts[i].draw(batch,parentAlpha);
        }
    }

    public void dispose(){
        for (Heart heart:hearts){
            heart.dispose();
        }
    }
    public void removePiece(){
        if(!heDead()) {
            hearts[heartCounter].eatPiece();
            if (!hearts[heartCounter].isActive()) {
                heartCounter -= 1;
            }
        }
    }

    public boolean heDead(){
        return heartCounter>=0;
    }

}
