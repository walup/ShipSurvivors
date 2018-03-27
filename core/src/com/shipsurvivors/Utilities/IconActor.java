package com.shipsurvivors.Utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by SEO on 26/03/2018.
 */
public class IconActor extends Actor {

        TextureRegion iconTexture;

        public IconActor(TextureRegion iconTexture, float width, float height){
            this.iconTexture = iconTexture;
            setSize(width,height);
            this.iconTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        public IconActor(Texture texture, float width, float height){
            iconTexture = new TextureRegion();
            iconTexture.setTexture(texture);
            iconTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            setSize(width,height);
        }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(iconTexture.getTexture(),getX(),getY(),getWidth(),getHeight());
    }
}
