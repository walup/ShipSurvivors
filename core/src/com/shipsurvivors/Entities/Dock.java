package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by SEO on 25/09/2017.
 */
public class Dock extends Actor {
    private TextureRegion dockTexture;
    private Weapon weapon;
    private Boolean available;
    private float angle;

    public Dock(TextureRegion dockTexture,float x, float y , float width, float height, float angle){
        this.dockTexture = dockTexture;
        setBounds(x,y,width,height);
        available = true;
    }

    /*So all of our angles, will be handled in degrees, which means we have to do the proper conversions when calculating
    * the new position of them when the docks rotate*/
    public void rotate(float angleDelta,float radius){
        angle = angle+angleDelta;
        setPosition(radius*(float)Math.cos(Math.toRadians(angle)),radius*(float)Math.sin(Math.toRadians(angle)));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(dockTexture,getX(),getY(),0,0,getWidth(),getHeight(),1,1,angle);
    }

    public void addWeapon(){
        this.weapon = weapon;
        available = false;
    }

    public boolean isAvailable(){
        return available;
    }


}
