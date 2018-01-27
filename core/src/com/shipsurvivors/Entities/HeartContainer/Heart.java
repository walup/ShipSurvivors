package com.shipsurvivors.Entities.HeartContainer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Constantes;

/**
 * Created by SEO on 25/01/2018.
 */
public class Heart extends Actor{
    private TextureAtlas atlas;
    private TextureRegion currentTexture;
    private int counter = 0;



    public Heart(TextureAtlas atlas,float x, float y, float width, float height){
        this.atlas = atlas;
        currentTexture = atlas.findRegions("heart").get(counter);
        currentTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        setBounds(x,y,width,height);
    }



    public boolean isActive(){
        return counter< Constantes.HEART_PIECES;

    }
    public void eatPiece(){
        if(isActive()){
            counter+=1;
            currentTexture = atlas.findRegions("heart").get(counter);
            currentTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentTexture,getX(),getY(),getWidth(),getHeight());
    }

    public void dispose(){
        atlas.dispose();
    }
}
