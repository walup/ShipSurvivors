package com.shipsurvivors.Utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by SEO on 25/09/2017.
 */

    /*This is basically a holder for an image, but it has the advantage that first you can size it, and also you it can be a
    * Texture Region or a Texture*/
    public class Icon {

        TextureRegion iconTexture;
        float width,height;

    public Icon(TextureRegion iconTexture, float width, float height){
            this.iconTexture = iconTexture;
            this.width = width;
            this.height = height;
            this.iconTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

    public Icon(Texture texture, float width, float height){
            iconTexture = new TextureRegion();
            iconTexture.setTexture(texture);
            iconTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            this.height = height;
            this.width = width;
        }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public Texture getIconTexture() {
        return iconTexture.getTexture();
    }
}


