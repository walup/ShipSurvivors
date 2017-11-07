package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Icon;

/**
 * Created by SEO on 25/09/2017.
 */
public class Attachable extends Actor{
    /*This kind of object has three general states, attached in hand and grabbed. This are mutually exclusive booleans.
    * and there has to be some sort of hierarchy for overriding the states of this, for instance if it is attached, you
    * definitely can not put it in container.*/

    private boolean attached;
    private boolean inContainer;
    private boolean grabbed;
    private Icon card;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        /*If it is in the container or grabbed, we will draw the card. Else if it is attached we will call a method called
        * drawAttachable*/
        if(inContainer || grabbed){
            batch.draw(card.getIconTexture(),getX(),getY(),card.getWidth(),card.getHeight());
        }
        else if(attached){
            drawAttachable(batch,parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        if(isGrabbed()){
            //Then follow the mouse of the player.
        }
    }

    public Icon getCard() {
        return card;
    }

    public void setCard(Icon card) {
        this.card = card;
    }

    public boolean isAttached() {
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    public boolean isInContainer() {
        return inContainer;
    }

    public void setInContainer(boolean inHand) {
        this.inContainer = inHand;
    }

    public boolean isGrabbed() {
        return grabbed;
    }

    public boolean isFree(){
        return !isAttached() && !isGrabbed() && !isInContainer();
    }



    public void drawAttachable(Batch batch, float parentAlpha){
        /*This is a method that you should fill to draw your particular attachable. */

    }


}
