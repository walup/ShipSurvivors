package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by SEO on 25/09/2017.
 */
public class Dock extends Actor {
    private TextureRegion dockTexture;
    private Attachable attachable;
    private Boolean available;
    private Boolean triggered;
    private float angle;


    /*Since i don't want to create a lot of bodies, to detect if a weapon is being attached we are going to use rectangles. */
    private Rectangle dockRect;


    public Dock(TextureRegion dockTexture, float x, float y , float width, float height, float angle){
        this.dockTexture = dockTexture;
        setBounds(x,y,width,height);
        this.angle = angle;
        available = true;
        triggered = false;
        dockRect = new Rectangle(x,y,width,height);
    }

    /*So all of our angles, will be handled in degrees, which means we have to do the proper conversions when calculating
    * the new position of them when the docks rotate*/

    public void rotate(float centerX, float centerY, float radius, float angleDelta){
        angle = (angle+angleDelta)%360;
        setPosition(centerX+radius*(float)Math.cos(Math.toRadians(angle)),centerY+radius*(float)Math.sin(Math.toRadians(angle)));
        updateDockRect();
        updateAttachablePosition();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(dockTexture,getX(),getY(),0,0,getWidth(),getHeight(),1,1,angle);
        if(!isAvailable()){
            attachable.draw(batch,parentAlpha);
        }
    }

    public void setAttachable(Attachable attachable){
        this.attachable = attachable;

    }

    public void updateAttachablePosition(){
        if(attachable!=null && !attachable.isShooting()){
            //We position the attachable in the middle.
            attachable.setPosition(getX()+(getWidth()-attachable.getWidth())/2,getY()+(getHeight()-attachable.getHeight())/2);
        }
    }

    public boolean isAvailable(){
        return available;
    }

    private void updateDockRect(){
        if(isAvailable()) {
            dockRect.set(getX(), getY(), getWidth(), getHeight());
        }
        else{
            dockRect.set(attachable.getX(),attachable.getY(),attachable.getWidth(),attachable.getHeight());
        }
    }

    public Rectangle getDockRect() {
        return dockRect;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void detach(){
        setAvailable(true);
        attachable.setActivated(false);
        attachable.setInContainer(false);
        attachable.setGrabbed(false);
        attachable.setAttached(false);
        attachable.setShooting(false);
        setTriggered(false);
        setAttachable(null);
    }

    public void trigger(){
        setTriggered(true);
        attachable.setActivated(true);
    }

    public void setTriggered(Boolean triggered) {
        this.triggered = triggered;
    }

    public Boolean isTriggered() {
        return triggered;
    }

    @Override
    public void act(float delta) {
        if(isTriggered()){
            if(!attachable.isActivated() && !attachable.isShooting()){
                attachable.detach();
                detach();
            }
            else{
                attachable.act(delta);
            }
        }
    }


    public boolean bulletStrike(Fixture fixture){
       if(!isAvailable() && isTriggered()){
           if(fixture.equals(attachable.getAccesoryFixture())){
               return true;
           }
           else{
               return false;
           }
       }
        return false;
    }

    public void moveDock(float velocityY, float delta){
        setPosition(getX(),getY()+velocityY*delta);
        updateDockRect();
        updateAttachablePosition();
    }

    public void dispose(){
        dockTexture.getTexture().dispose();
    }

}
